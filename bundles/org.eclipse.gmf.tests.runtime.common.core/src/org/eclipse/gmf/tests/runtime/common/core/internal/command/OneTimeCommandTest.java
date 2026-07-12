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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OneTimeCommandTest {

	private IOperationHistory history;

	@BeforeEach
	public void setUp() throws Exception {
		history = OperationHistoryFactory.getOperationHistory();
	}

	/**
	 * Verifies that subclasses of IsolatedCommand can be not undoable but also not
	 * cause the undo history to be flushed when they are executed in a linear undo
	 * model.
	 */
	@Test
	public void test_historyNotFlushed_132371() throws Exception {

		// create an undo context
		final IUndoContext context = new UndoContext();

		// add a listener to enforce a linear undo model for the undo context,
		// which flushes the undo history when the top-most operation is not
		// undoable
		IOperationHistoryListener historyListener = new IOperationHistoryListener() {

			@Override
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
			}
		};
		history.addOperationHistoryListener(historyListener);

		// put a marker operation on the history
		IUndoableOperation marker = new MarkerOperation();
		marker.addContext(context);
		history.execute(marker, new NullProgressMonitor(), null);
		assertTrue(history.canUndo(context), "Marker operation must be undoable"); //$NON-NLS-1$

		// create a command with no undo context
		OneTimeCommand command = new OneTimeCommand("test_historyNotFlushed_132371") { //$NON-NLS-1$

			@Override
			protected CommandResult doExecuteWithResult(IProgressMonitor progressMonitor, IAdaptable info)
					throws ExecutionException {
				return CommandResult.newOKCommandResult();
			}
		};

		command.addContext(context);
		assertFalse(command.hasContext(context), "Unexpected undo context"); //$NON-NLS-1$

		// execute the command
		history.execute(command, new NullProgressMonitor(), null);

		// verify that the marker operation is still undoable
		assertTrue(history.canUndo(context), "Undo history should not have been flushed"); //$NON-NLS-1$
		assertSame(marker, history.getUndoOperation(context), "Marker operation should be on top of the undo history."); //$NON-NLS-1$
	}

	//
	// TEST FIXTURES
	//

	static class MarkerOperation extends AbstractOperation {

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

		@Override
		public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
			wasExecuted = true;
			return Status.OK_STATUS;
		}

		@Override
		public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
			wasUndone = true;
			return Status.OK_STATUS;
		}

		@Override
		public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
			wasRedone = true;
			return Status.OK_STATUS;
		}
	}
}
