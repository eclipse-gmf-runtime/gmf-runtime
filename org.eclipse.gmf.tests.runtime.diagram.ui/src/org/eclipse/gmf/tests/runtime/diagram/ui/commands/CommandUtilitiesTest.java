/******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.diagram.ui.commands;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandUtilities;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;

/**
 * @author ldamus
 */
public class CommandUtilitiesTest extends TestCase {

	public CommandUtilitiesTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(CommandUtilitiesTest.class,
				"CommandUtilities tests"); //$NON-NLS-1$
	}

	/**
	 * Verifies that the utility that derives affected files from a GEF command
	 * produces an empty list when the input is <code>null</code>.
	 */
	public void test_getAffectedFiles_nullCommand_161668() {
		Collection affectedFiles = CommandUtilities.getAffectedFiles(null);
		assertTrue(affectedFiles.isEmpty());
	}

	/**
	 * Verifies that the utility that derives affected files from a GEF command
	 * returns the correct affected files when the input implements ICommand.
	 */
	public void test_getAffectedFiles_ICommand_161668() {
		List files = getFiles("CommandUtilitiesTest/test_getAffectedFiles_ICommand_161668"); //$NON-NLS-1$
		CommandImplementingICommand command = new CommandImplementingICommand(
				files);
		Collection affectedFiles = CommandUtilities.getAffectedFiles(command);
		assertEquals(files, affectedFiles);
	}

	/**
	 * Verifies that the utility that derives affected files from a GEF command
	 * returns the correct affected files of the delegate command when the input
	 * is an ICommandProxy.
	 */
	public void test_getAffectedFiles_ICommandProxy_161668() {
		List files = getFiles("CommandUtilitiesTest/test_getAffectedFiles_ICommandProxy_161668"); //$NON-NLS-1$
		ICommand iCommand = new AbstractCommand(
				"test_getAffectedFiles_ICommandProxy_161668()", files) { //$NON-NLS-1$

			protected CommandResult doExecuteWithResult(
					IProgressMonitor progressMonitor, IAdaptable info)
					throws ExecutionException {
				return CommandResult.newOKCommandResult();
			}

			protected CommandResult doRedoWithResult(
					IProgressMonitor progressMonitor, IAdaptable info)
					throws ExecutionException {
				return CommandResult.newOKCommandResult();
			}

			protected CommandResult doUndoWithResult(
					IProgressMonitor progressMonitor, IAdaptable info)
					throws ExecutionException {
				return CommandResult.newOKCommandResult();
			}
		};

		ICommandProxy commandProxy = new ICommandProxy(iCommand);

		Collection affectedFiles = CommandUtilities
				.getAffectedFiles(commandProxy);
		assertEquals(files, affectedFiles);
	}

	/**
	 * Verifies that the utility that derives affected files from a GEF command
	 * returns the correct affected files of the child commands when the input
	 * is a CompoundCommand.
	 */
	public void test_getAffectedFiles_CompoundCommand_161668() {

		// create an ICommand
		List iCommandFiles = getFiles("CommandUtilitiesTest/getAffectedFiles_CompoundCommand_161668_ICommand"); //$NON-NLS-1$
		CommandImplementingICommand iCommand = new CommandImplementingICommand(
				iCommandFiles);

		// create an ICommandProxy
		List iCommandProxyFiles = getFiles("CommandUtilitiesTest/getAffectedFiles_CompoundCommand_161668_ICommandProxy"); //$NON-NLS-1$
		ICommandProxy iCommandProxy = new ICommandProxy(
				new AbstractCommand(
						"test_getAffectedFiles_ICommandProxy_161668()", iCommandProxyFiles) { //$NON-NLS-1$

					protected CommandResult doExecuteWithResult(
							IProgressMonitor progressMonitor, IAdaptable info)
							throws ExecutionException {
						return CommandResult.newOKCommandResult();
					}

					protected CommandResult doRedoWithResult(
							IProgressMonitor progressMonitor, IAdaptable info)
							throws ExecutionException {
						return CommandResult.newOKCommandResult();
					}

					protected CommandResult doUndoWithResult(
							IProgressMonitor progressMonitor, IAdaptable info)
							throws ExecutionException {
						return CommandResult.newOKCommandResult();
					}
				});

		// put them both in a CompoundCommand
		CompoundCommand command = new CompoundCommand(
				"getAffectedFiles_CompoundCommand_161668"); //$NON-NLS-1$
		command.add(iCommand);
		command.add(iCommandProxy);

		// verify the affected files
		Collection affectedFiles = CommandUtilities.getAffectedFiles(command);
		assertEquals(iCommandFiles.size() + iCommandProxyFiles.size(),
				affectedFiles.size());
		assertTrue(affectedFiles.containsAll(iCommandFiles));
		assertTrue(affectedFiles.containsAll(iCommandProxyFiles));
	}

	private List getFiles(String path) {
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IFile file = workspaceRoot.getFile(new Path(path));
		return Collections.singletonList(file);
	}

	//
	// Test fixtures
	//

	private class CommandImplementingICommand extends Command implements
			ICommand {

		private List affectedFiles;

		private ICommand delegate;

		public CommandImplementingICommand(List affectedFiles) {
			this.affectedFiles = affectedFiles;
		}

		protected ICommand getDelegate() {
			if (delegate == null) {
				delegate = new AbstractCommand(
						"CommandImplementingICommand", affectedFiles) { //$NON-NLS-1$

					protected CommandResult doExecuteWithResult(
							IProgressMonitor progressMonitor, IAdaptable info)
							throws ExecutionException {
						return CommandResult.newOKCommandResult();
					}

					protected CommandResult doRedoWithResult(
							IProgressMonitor progressMonitor, IAdaptable info)
							throws ExecutionException {
						return CommandResult.newOKCommandResult();
					}

					protected CommandResult doUndoWithResult(
							IProgressMonitor progressMonitor, IAdaptable info)
							throws ExecutionException {
						return CommandResult.newOKCommandResult();
					}
				};
			}
			return delegate;
		};

		public ICommand compose(IUndoableOperation operation) {
			return getDelegate().compose(operation);
		}

		public List getAffectedFiles() {
			return getDelegate().getAffectedFiles();
		}

		public CommandResult getCommandResult() {
			return getDelegate().getCommandResult();
		}

		public ICommand reduce() {
			return getDelegate().reduce();
		}

		public void addContext(IUndoContext context) {
			getDelegate().addContext(context);
		}

		public boolean canRedo() {
			return getDelegate().canRedo();
		}

		public IStatus execute(IProgressMonitor monitor, IAdaptable info)
				throws ExecutionException {
			return getDelegate().execute(monitor, info);
		}

		public IUndoContext[] getContexts() {
			return getDelegate().getContexts();
		}

		public boolean hasContext(IUndoContext context) {
			return getDelegate().hasContext(context);
		}

		public IStatus redo(IProgressMonitor monitor, IAdaptable info)
				throws ExecutionException {
			return getDelegate().redo(monitor, info);
		}

		public void removeContext(IUndoContext context) {
			getDelegate().removeContext(context);
		}

		public IStatus undo(IProgressMonitor monitor, IAdaptable info)
				throws ExecutionException {
			return getDelegate().undo(monitor, info);
		}
	}
}
