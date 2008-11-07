/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.emf.core.resources;

import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.DefaultOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.emf.workspace.ResourceUndoContext;
import org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResourceModificationManager;
import org.eclipse.gmf.tests.runtime.emf.core.BaseTests;

/**
 * Tests the management of the modified state of GMF resources as operations are
 * executed, undone and redone on the operation history. The modified state of
 * these resources is managed by the {@link GMFResourceModificationManager}.
 * 
 * @author ldamus
 */
public class GMFResourceModificationManagerTests extends BaseTests {

	private DefaultOperationHistory history;
	
	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(GMFResourceModificationManagerTests.class,
				"GMFResourceModificationManagerTests Test Suite"); //$NON-NLS-1$
	}

	@Override
	protected void setUp() throws Exception {
		history = new DefaultOperationHistory();
		super.setUp();
		testEcoreResource.setTrackingModification(true);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		history = null;
	}

	@Override
	protected TransactionalEditingDomain createEditingDomain() {
		return GMFEditingDomainFactory.getInstance().createEditingDomain(
				history);
	}

	protected IUndoableOperation createOperation(String name) {
		return new AbstractEMFOperation(domain, name) {
			@Override
			protected IStatus doExecute(IProgressMonitor monitor,
					IAdaptable info) throws ExecutionException {

				ecoreRoot.getEClassifiers().add(
						EcoreFactory.eINSTANCE.createEClass());

				return Status.OK_STATUS;
			}
		};
	}

	/**
	 * Tests that the resource reports that it is not modified when the history
	 * is undone back to a given save point.
	 */
	public void test_execute_save_execute_undo() {
		IUndoContext context = new ResourceUndoContext(domain, testEcoreResource);

		IUndoableOperation op1 = createOperation("operation1"); //$NON-NLS-1$
		IUndoableOperation op2 = createOperation("operation2"); //$NON-NLS-1$

		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());

		// execute --> modified
		try {
			history.execute(op1, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertTrue("Resource should be modified", testEcoreResource.isModified());

		// save --> not modified
		try {
			testEcoreResource.save(null);
		} catch (IOException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());

		// execute --> modified
		try {
			history.execute(op2, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertTrue("Resource should be modified", testEcoreResource.isModified());

		// undo --> not modified
		try {
			history.undo(context, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());
	}

	/**
	 * Tests that undo will return the resource to a modified state after a
	 * save, and that a subsequent redo will return the resource back to its
	 * unmodified state.
	 */
	public void test_execute_save_undo_redo() {
		IUndoContext context = new ResourceUndoContext(domain, testEcoreResource);
		IUndoableOperation op = createOperation("op"); //$NON-NLS-1$

		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());

		// execute --> modified
		try {
			history.execute(op, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertTrue("Resource should be modified", testEcoreResource.isModified());

		// save --> not modified
		try {
			testEcoreResource.save(null);
		} catch (IOException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());

		// undo --> modified
		try {
			history.undo(context, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertTrue("Resource should be modified", testEcoreResource.isModified());

		// redo --> not modified
		try {
			history.redo(context, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());
	}

	/**
	 * Tests that the resource is modified after save followed by undo, but is
	 * not modified after save followed by execute, followed by undo.
	 */
	public void test_execute_save_undo_save_execute_undo() {
		IUndoContext context = new ResourceUndoContext(domain, testEcoreResource);

		IUndoableOperation op1 = createOperation("operation1"); //$NON-NLS-1$
		IUndoableOperation op2 = createOperation("operation2"); //$NON-NLS-1$

		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());

		// execute --> modified
		try {
			history.execute(op1, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertTrue("Resource should be modified", testEcoreResource.isModified());

		// save --> not modified
		try {
			testEcoreResource.save(null);
		} catch (IOException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());

		// undo --> modified
		try {
			history.undo(context, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertTrue("Resource should be modified", testEcoreResource.isModified());

		// save --> not modified
		try {
			testEcoreResource.save(null);
		} catch (IOException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());

		// execute --> modified
		try {
			history.execute(op2, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertTrue("Resource should be modified", testEcoreResource.isModified());

		// undo --> not modified
		try {
			history.undo(context, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());
	}

	/**
	 * Tests that the resource is modified after undo when there is more than
	 * one operation on the history. Also, tests that the resource is modified
	 * after save followed by redo.
	 */
	public void test_execute_execute_undo_save_redo() {
		IUndoContext context = new ResourceUndoContext(domain, testEcoreResource);

		IUndoableOperation op1 = createOperation("operation1"); //$NON-NLS-1$
		IUndoableOperation op2 = createOperation("operation2"); //$NON-NLS-1$

		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());

		// execute --> modified
		try {
			history.execute(op1, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertTrue("Resource should be modified", testEcoreResource.isModified());

		// execute --> modified
		try {
			history.execute(op2, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertTrue("Resource should be modified", testEcoreResource.isModified());

		// undo --> modified
		try {
			history.undo(context, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertTrue("Resource should be modified", testEcoreResource.isModified());

		// save --> not modified
		try {
			testEcoreResource.save(null);
		} catch (IOException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());

		// redo --> modified
		try {
			history.undo(context, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertTrue("Resource should not be modified", testEcoreResource.isModified());
	}

	/**
	 * Tests that after the flush limit is exceeded for operations with a given
	 * resource context, the resource continues to report that it is modified
	 * even when all of the operations on the history have been undone.
	 */
	public void test_execute_flushLimit_undo() {
		IUndoContext context = new ResourceUndoContext(domain, testEcoreResource);
		history.setLimit(context, 1);
		IUndoableOperation op1 = createOperation("op1"); //$NON-NLS-1$
		IUndoableOperation op2 = createOperation("op2"); //$NON-NLS-1$

		// execute --> modified
		try {
			history.execute(op1, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertTrue("Resource should be modified", testEcoreResource.isModified());

		// execute --> modified
		try {
			history.execute(op2, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertTrue("Resource should be modified", testEcoreResource.isModified());
		assertSame("Operation should have been flushed", history
				.getUndoHistory(context).length, 1);

		// undo --> modified
		try {
			history.undo(context, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertTrue("Resource should be modified", testEcoreResource.isModified());

		// undo --> modified
		try {
			history.undo(context, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertTrue("Resource should be modified", testEcoreResource.isModified());

	}

	/**
	 * Tests that if the isModified state of the resource is set to
	 * <code>false</code> during the execution of an operation, subsequent
	 * execute followed by undo will return the resource back to its unmodified
	 * state.
	 */
	public void test_executeModifiedFalse_execute_undo() {
		IUndoContext context = new ResourceUndoContext(domain, testEcoreResource);
		IUndoableOperation notModified = new AbstractEMFOperation(domain,
				"notModified") { //$NON-NLS-1$
			@Override
			protected IStatus doExecute(IProgressMonitor monitor,
					IAdaptable info) throws ExecutionException {

				ecoreRoot.getEClassifiers().add(
						EcoreFactory.eINSTANCE.createEClass());
				ecoreRoot.eResource().setModified(false);

				return Status.OK_STATUS;
			}

			@Override
			public boolean canUndo() {
				return false;
			}

			@Override
			public boolean canRedo() {
				return false;
			}
		};

		IUndoableOperation op = createOperation("operation"); //$NON-NLS-1$

		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());

		// execute --> not modified
		try {
			history.execute(notModified, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());

		// execute --> modified
		try {
			history.execute(op, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertTrue("Resource should be modified", testEcoreResource.isModified());

		// undo --> not modified
		try {
			history.undo(context, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());
	}

	/**
	 * Tests that if the isModified state of the resource is set to
	 * <code>false</code> during the undo of an operation, subsequent
	 * execute followed by undo will return the resource back to its unmodified
	 * state.
	 */
	public void test_execute_undoModifiedFalse_execute_undo() {
		IUndoContext context = new ResourceUndoContext(domain, testEcoreResource);
		IUndoableOperation notModified = new AbstractEMFOperation(domain,
				"notModified") { //$NON-NLS-1$
			@Override
			protected IStatus doExecute(IProgressMonitor monitor,
					IAdaptable info) throws ExecutionException {

				ecoreRoot.getEClassifiers().add(
						EcoreFactory.eINSTANCE.createEClass());
				ecoreRoot.eResource().setModified(false);

				return Status.OK_STATUS;
			}
			@Override
			protected IStatus doUndo(IProgressMonitor monitor,
					IAdaptable info) throws ExecutionException {
				IStatus status = super.doUndo(monitor, info);
				ecoreRoot.eResource().setModified(false);
				return status;
			}
		};

		IUndoableOperation op = createOperation("operation"); //$NON-NLS-1$

		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());

		// execute --> not modified
		try {
			history.execute(notModified, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());

		// undo --> not modified
		try {
			history.undo(context, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());
		
		// execute --> modified
		try {
			history.execute(op, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertTrue("Resource should be modified", testEcoreResource.isModified());

		// undo --> not modified
		try {
			history.undo(context, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());
	}

	/**
	 * Tests that if the isModified state of the resource is set to
	 * <code>false</code> during the redo of an operation, subsequent
	 * execute followed by undo will return the resource back to its unmodified
	 * state.
	 */
	public void test_execute_undo_redoModifiedFalse_execute_undo() {
		IUndoContext context = new ResourceUndoContext(domain, testEcoreResource);
		IUndoableOperation notModified = new AbstractEMFOperation(domain,
				"notModified") { //$NON-NLS-1$
			@Override
			protected IStatus doExecute(IProgressMonitor monitor,
					IAdaptable info) throws ExecutionException {

				ecoreRoot.getEClassifiers().add(
						EcoreFactory.eINSTANCE.createEClass());
				ecoreRoot.eResource().setModified(false);

				return Status.OK_STATUS;
			}
			@Override
			protected IStatus doUndo(IProgressMonitor monitor,
					IAdaptable info) throws ExecutionException {
				IStatus status = super.doUndo(monitor, info);
				ecoreRoot.eResource().setModified(false);
				return status;
			}

			@Override
			protected IStatus doRedo(IProgressMonitor monitor,
					IAdaptable info) throws ExecutionException {
				IStatus status = super.doRedo(monitor, info);
				ecoreRoot.eResource().setModified(false);
				return status;
			}
		};

		IUndoableOperation op = createOperation("operation"); //$NON-NLS-1$

		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());

		// execute --> not modified
		try {
			history.execute(notModified, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());

		// undo --> not modified
		try {
			history.undo(context, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());

		// redo --> not modified
		try {
			history.redo(context, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());
		
		// execute --> modified
		try {
			history.execute(op, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertTrue("Resource should be modified", testEcoreResource.isModified());

		// undo --> not modified
		try {
			history.undo(context, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());
	}
	
	/**
	 * Tests that if only the isModified state of the resource is set to <true> then
	 * <code>false</code> during the execution of an operation (no other change
	 * to the resource), subsequent execute followed by undo will return the
	 * resource back to its unmodified state.
	 */
	public void test_modifiedTrueFalse_execute_undo() {
		IUndoContext context = new ResourceUndoContext(domain, testEcoreResource);
		IUndoableOperation notModified = new AbstractEMFOperation(domain,
				"notModified") { //$NON-NLS-1$
			@Override
			protected IStatus doExecute(IProgressMonitor monitor,
					IAdaptable info) throws ExecutionException {
				ecoreRoot.eResource().setModified(true);
				ecoreRoot.eResource().setModified(false);

				return Status.OK_STATUS;
			}

			@Override
			public boolean canUndo() {
				return false;
			}

			@Override
			public boolean canRedo() {
				return false;
			}
		};

		IUndoableOperation op = createOperation("operation"); //$NON-NLS-1$

		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());

		// execute --> not modified
		try {
			history.execute(notModified, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());

		// execute --> modified
		try {
			history.execute(op, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertTrue("Resource should be modified", testEcoreResource.isModified());

		// undo --> not modified
		try {
			history.undo(context, null, null);
		} catch (ExecutionException e) {
			fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}
		assertFalse("Resource should not be modified", testEcoreResource
				.isModified());
	}
}
