/******************************************************************************
 * Copyright (c) 2006, 2014 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.common.ui.action.actions.global;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.commands.operations.UndoContext;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.gmf.runtime.common.ui.action.actions.global.GlobalUndoAction;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class GlobalUndoActionTest
    extends TestCase {

    private GlobalUndoAction undoAction;
    private IViewPart part;

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(GlobalUndoActionTest.class,
            "GlobalUndoAction Test Suite"); //$NON-NLS-1$
    }

    protected void setUp()
        throws Exception {
        if (part == null) {
    		// There is an issue when running the tests, run no tests for now.
        	return;
        }
    	if(part == null){
            part = (IViewPart) PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage().getActivePart();
        	} else {
        		part.setFocus();
        	}

        IOperationHistory history = OperationHistoryFactory
            .getOperationHistory();
        IUndoContext undoContext = new UndoContext();

        undoAction = new GlobalUndoAction(part);
        undoAction.setUndoContext(undoContext);

        IUndoableOperation operation = new AbstractOperation(
            "test_nullWorkbenchPart") { //$NON-NLS-1$

            public IStatus execute(IProgressMonitor monitor, IAdaptable info)
                throws ExecutionException {
                return Status.OK_STATUS;
            }

            public IStatus undo(IProgressMonitor monitor, IAdaptable info)
                throws ExecutionException {
                return Status.OK_STATUS;
            }

            public IStatus redo(IProgressMonitor monitor, IAdaptable info)
                throws ExecutionException {
                return Status.OK_STATUS;
            }
        };

        try {
            operation.addContext(undoContext);
            history.execute(operation, new NullProgressMonitor(), null);
            history.execute(operation, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
        }
    }

    /**
     * Tests that the action is not enabled when it's part is closed.
     */
    public void ignore_dispose_131781()  throws PartInitException{
        
        // Enables testing that closing the view doesn't cause exceptions to be
        // reported to the user
        SafeRunnable.setIgnoreErrors(false);
        
        // Re-set the undo context to ensure that the UndoActionHandler's part
        // listener is registered AFTER the GlobalUndoAction. We can then test
        // that closing the part doesn't cause the UndoActionHandler's part
        // listener to throw an NPE.
        undoAction.setUndoContext(undoAction.getUndoContext());
        
        try {
            OperationHistoryFactory.getOperationHistory().undo(
               undoAction.getUndoContext(), new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
        }  
        
        assertTrue(undoAction.isEnabled());
        
        // Close the view
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
            .hideView(part);
        
        assertFalse(undoAction.isEnabled());
        
        SafeRunnable.setIgnoreErrors(true);
        
        // Close the view
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
            .showView(part.getViewSite().getId());
    }

    /**
     * Tests that the delegate is disposed when the undo context is set to null.
     */
    public void ignore_nullUndoContext() {
        assertTrue(undoAction.isEnabled());
        undoAction.setUndoContext(null);
        assertFalse(undoAction.isEnabled());
    }

	public void test_testNothing() {
		// There is an issue when running the tests, run no tests for now.
	}
}
