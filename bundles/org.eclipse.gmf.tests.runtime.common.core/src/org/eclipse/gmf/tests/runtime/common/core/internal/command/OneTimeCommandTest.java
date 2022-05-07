/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.common.core.internal.command;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IOperationHistoryListener;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryEvent;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.commands.operations.UndoContext;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.OneTimeCommand;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class OneTimeCommandTest
    extends TestCase {

    private IOperationHistory history;

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(OneTimeCommandTest.class,
            "OneTimeCommand Test Suite"); //$NON-NLS-1$
    }

    public OneTimeCommandTest(String name) {
        super(name);
    }

    protected void setUp()
        throws Exception {
        super.setUp();
        history = OperationHistoryFactory.getOperationHistory();
    }

    /**
     * Verifies that subclasses of IsolatedCommand can be not undoable but also
     * not cause the undo history to be flushed when they are executed in a
     * linear undo model.
     */
    public void test_historyNotFlushed_132371()
        throws Exception {

        // create an undo context
        final IUndoContext context = new UndoContext();

        // add a listener to enforce a linear undo model for the undo context,
        // which flushes the undo history when the top-most operation is not
        // undoable
        IOperationHistoryListener historyListener = new IOperationHistoryListener() {

            public void historyNotification(OperationHistoryEvent event) {
                switch (event.getEventType()) {
                    case OperationHistoryEvent.OPERATION_ADDED:
                    case OperationHistoryEvent.OPERATION_REMOVED:
                    case OperationHistoryEvent.UNDONE:
                    case OperationHistoryEvent.REDONE:
                        if (!history.canUndo(context)) {
                            history.dispose(context, true, false, false);
                        }
                        break;
                }
            };
        };
        history.addOperationHistoryListener(historyListener);

        // put a marker operation on the history
        IUndoableOperation marker = new MarkerOperation();
        marker.addContext(context);
        history.execute(marker, new NullProgressMonitor(), null);
        assertTrue(
            "Marker operation must be undoable", history.canUndo(context)); //$NON-NLS-1$

        // create a command with no undo context
        OneTimeCommand command = new OneTimeCommand(
            "test_historyNotFlushed_132371") { //$NON-NLS-1$

            protected CommandResult doExecuteWithResult(
                    IProgressMonitor progressMonitor, IAdaptable info)
                throws ExecutionException {
                return CommandResult.newOKCommandResult();
            };
        };

        command.addContext(context);
        assertFalse("Unexpected undo context", command.hasContext(context)); //$NON-NLS-1$

        // execute the command
        history.execute(command, new NullProgressMonitor(), null);

        // verify that the marker operation is still undoable
        assertTrue(
            "Undo history should not have been flushed", history.canUndo(context)); //$NON-NLS-1$
        assertSame(
            "Marker operation should be on top of the undo history.", marker, history.getUndoOperation(context)); //$NON-NLS-1$
    }

    //
    // TEST FIXTURES
    //

    static class MarkerOperation
        extends AbstractOperation {

        boolean wasExecuted;

        boolean wasUndone;

        boolean wasRedone;

        MarkerOperation() {
            super("Marker operation"); //$NON-NLS-1$
        }

        void reset() {
            wasExecuted = false;
            wasUndone = false;
            wasRedone = false;
        }

        public IStatus execute(IProgressMonitor monitor, IAdaptable info)
            throws ExecutionException {
            wasExecuted = true;
            return Status.OK_STATUS;
        }

        public IStatus undo(IProgressMonitor monitor, IAdaptable info)
            throws ExecutionException {
            wasUndone = true;
            return Status.OK_STATUS;
        }

        public IStatus redo(IProgressMonitor monitor, IAdaptable info)
            throws ExecutionException {
            wasRedone = true;
            return Status.OK_STATUS;
        }
    }
}
