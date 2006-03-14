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

package org.eclipse.gmf.tests.runtime.common.ui.action.actions.global;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

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
import org.eclipse.gmf.runtime.common.ui.action.actions.global.GlobalRedoAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

public class GlobalRedoActionTest
    extends TestCase {

    private GlobalRedoAction redoAction;

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(GlobalRedoActionTest.class,
            "GlobalRedoAction Test Suite"); //$NON-NLS-1$
    }

    protected void setUp()
        throws Exception {
        IWorkbenchPart part = PlatformUI.getWorkbench()
            .getActiveWorkbenchWindow().getActivePage().getActivePart();

        IOperationHistory history = OperationHistoryFactory
            .getOperationHistory();
        IUndoContext undoContext = new UndoContext();

        redoAction = new GlobalRedoAction(part);
        redoAction.setUndoContext(undoContext);

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
            history.undo(undoContext, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
        }
    }

    /**
     * Tests that the delegate is disposed when the workbench part is set to
     * null.
     */
    public void test_dispose_() {
        assertTrue(redoAction.isEnabled());
        redoAction.dispose();
        assertFalse(redoAction.isEnabled());
    }

    /**
     * Tests that the delegate is disposed when the undo context is set to null.
     */
    public void test_nullUndoContext() {
        assertTrue(redoAction.isEnabled());
        redoAction.setUndoContext(null);
        assertFalse(redoAction.isEnabled());
    }
}
