/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.emf.ui.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;

import junit.framework.TestCase;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.commands.operations.UndoContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.FileModificationValidator;
import org.eclipse.gmf.runtime.common.core.internal.command.BaseModificationValidator;
import org.eclipse.gmf.runtime.emf.ui.action.AbstractModelActionHandler;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.operations.UndoActionHandler;

/**
 * Tests the AbstractModelActionHandler.
 * 
 * @author ldamus
 */
public class AbstractModelActionHandlerTest extends TestCase {

	private IProject project;

	private IFile file;

	private IOperationHistory history;

	private TransactionalEditingDomain editingDomain;
	
	private IWorkbenchPart part;

	private IWorkbenchPartSite site;

	protected void setUp() throws Exception {

		super.setUp();

		// avoid UI prompting from validator
		FileModificationValidator
				.setModificationValidator(new BaseModificationValidator());

		part = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActivePart();

		site = part.getSite();

		editingDomain = TransactionalEditingDomain.Factory.INSTANCE
				.createEditingDomain();
		history = OperationHistoryFactory.getOperationHistory();

		try {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			project = root.getProject("FileModificationApproverTest"); //$NON-NLS-1$

			project.create(null);
			project.open(null);

			file = project.getFile("AbstractModelActionHandlerTest.txt"); //$NON-NLS-1$
			InputStream contents = new ByteArrayInputStream(new byte[0]);

			file.create(contents, false, new NullProgressMonitor());

		} catch (CoreException e) {
			fail(e);
		}
	}

	protected void tearDown() throws Exception {

		super.tearDown();

		file.delete(true, new NullProgressMonitor());
		project.close(new NullProgressMonitor());
		project.delete(true, true, new NullProgressMonitor());
		project = null;
		file = null;
		site = null;
	}

	private void fail(Exception e) {
		e.printStackTrace();
		fail("Should not have thrown: " + e.getLocalizedMessage()); //$NON-NLS-1$
	}

	private void setReadOnly(boolean b) {
		try {
			ResourceAttributes resourceAttributes = file
					.getResourceAttributes();
			resourceAttributes.setReadOnly(b);
			file.setResourceAttributes(resourceAttributes);

		} catch (CoreException e) {
			fail(e);
		}
	}

	/**
	 * Tests that validation is done for file modification when commands that
	 * change the model are executed through an AbstractModelActionHandler.
	 */
	public void test_fileModificationValidation_155418() {

		TestModelActionHandler action = new TestModelActionHandler(part);

		// create an undo action handler for the undo context so that we can
		// test that the history is flushed correctly
		UndoActionHandler undoAction = new UndoActionHandler(site, action
				.getUndoContext());
		undoAction.setPruneHistory(true);

		// execute fails when file is read-only
		setReadOnly(true);
		action.run(new NullProgressMonitor());
		assertFalse(action.getCommand().isExecuted());
		assertFalse(history.canUndo(action.getUndoContext()));

		// execute succeeds when file is read-write
		setReadOnly(false);
		action.run(new NullProgressMonitor());
		assertTrue(action.getCommandStatus().isOK());
		assertTrue(action.getCommand().isExecuted());
		assertTrue(history.canUndo(action.getUndoContext()));

		// undo fails and history flushed when file is read-only
		setReadOnly(true);
		try {
			history.undo(action.getUndoContext(), new NullProgressMonitor(),
					null);
		} catch (ExecutionException e) {
			fail(e);
		}
		assertFalse(action.getCommand().isUndone());

		// give the undo action a chance to run it's async exec to flush the
		// history
		Display display = PlatformUI.getWorkbench().getDisplay();
		while (display.readAndDispatch()) {
			// spin
		}

		assertFalse(history.canUndo(action.getUndoContext()));

		undoAction.dispose();
	}

	// test fixtures

	private class TestModelActionHandler extends AbstractModelActionHandler {

		private TestCommand command;

		private IUndoContext undoContext;

		public TestModelActionHandler(IWorkbenchPart part) {
			super(part);
			setText("TestModelActionHandler"); //$NON-NLS-1$
		}

		protected TransactionalEditingDomain getEditingDomain() {
			return editingDomain;
		}

		protected void doRun(IProgressMonitor progressMonitor) {
			execute(getCommand(), progressMonitor, null);
		}

		public TestCommand getCommand() {
			if (command == null) {
				command = new TestCommand();
				command.addContext(getUndoContext());
			}
			return command;
		}

		public IUndoContext getUndoContext() {
			if (undoContext == null) {
				undoContext = new UndoContext();
			}
			return undoContext;
		}

		public IStatus getCommandStatus() {
			return super.getStatus();
		}
		
		public void refresh() {
			// do nothing
		}
	}

	private class TestCommand extends AbstractCommand {

		private boolean executed = false;

		private boolean undone = false;

		private boolean redone = false;

		public TestCommand() {
			super("AbstractModelActionHandlerTest", //$NON-NLS-1$
					Collections.singletonList(file));
		}

		protected CommandResult doExecuteWithResult(
				IProgressMonitor progressMonitor, IAdaptable info)
				throws ExecutionException {
			this.executed = true;
			return CommandResult.newOKCommandResult();
		}

		protected CommandResult doRedoWithResult(
				IProgressMonitor progressMonitor, IAdaptable info)
				throws ExecutionException {
			this.undone = true;
			return CommandResult.newOKCommandResult();
		}

		protected CommandResult doUndoWithResult(
				IProgressMonitor progressMonitor, IAdaptable info)
				throws ExecutionException {
			this.redone = true;
			return CommandResult.newOKCommandResult();
		}

		public boolean isExecuted() {
			return executed;
		}

		public boolean isUndone() {
			return undone;
		}

		public boolean isRedone() {
			return redone;
		}
	}
}
