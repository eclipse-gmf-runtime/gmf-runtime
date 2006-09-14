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

package org.eclipse.gmf.runtime.emf.ui.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.ICompositeOperation;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;

/**
 * Command used to allow subclasses of the {@link AbstractModelActionDelegate}
 * to read and write to the model from their #doRun implementation.
 * <P>
 * This is a kind of {@link ICompositeOperation} that opens itself on the
 * operation history. It runs a {@link Runnable} supplied at the time of
 * instantiation and any operations executed through the history in that
 * runnable are considered to be part of the composite, and can be undone and
 * redone in a single step. This allows file modification validation to be done
 * on each command executed by a subclass of {@link AbstractModelActionDelegate}.
 * 
 * @author ldamus
 */
abstract class WriteCommand extends AbstractTransactionalCommand implements
		ICompositeOperation {

	private final CompositeTransactionalCommand compositeDelegate;

	private final Runnable runnable;

	private final IOperationHistory history;

	/**
	 * Initializes me.
	 * 
	 * @param domain
	 *            my editing domain
	 * @param label
	 *            my user-readable label
	 * @param history
	 *            the operation history
	 * @param runnable
	 *            the runnable to run when I execute
	 */
	public WriteCommand(TransactionalEditingDomain domain, String label,
			IOperationHistory history, Runnable runnable) {

		super(domain, label, null);
		this.compositeDelegate = new CompositeTransactionalCommand(domain,
				label);
		this.runnable = runnable;
		this.history = history;
	}

	/**
	 * Opens a composite operation on the operation history and runs the
	 * runnable.
	 */
	protected CommandResult doExecuteWithResult(
			IProgressMonitor progressMonitor, IAdaptable info)
			throws ExecutionException {

		if (runnable != null) {
			history.openOperation(this, IOperationHistory.EXECUTE);

			try {
				runnable.run();
				history.closeOperation(getStatus().isOK(), false,
						IOperationHistory.EXECUTE);

			} catch (RuntimeException e) {
				history.closeOperation(false, false, IOperationHistory.EXECUTE);
				throw e;
			}
		}

		// have to compute the result because we didn't actually execute the
		// compositeDelegate
		return computeResult();
	}

	/**
	 * Redoes the commands that were accumulated when the runnable was run.
	 */
	protected CommandResult doRedoWithResult(IProgressMonitor progressMonitor,
			IAdaptable info) throws ExecutionException {

		compositeDelegate.redo(progressMonitor, info);
		return compositeDelegate.getCommandResult();
	}

	/**
	 * Undoes the commands that were accumulated when the runnable was run.
	 */
	protected CommandResult doUndoWithResult(IProgressMonitor progressMonitor,
			IAdaptable info) throws ExecutionException {

		compositeDelegate.undo(progressMonitor, info);
		return compositeDelegate.getCommandResult();
	}

	/**
	 * Adds to my {@link CompositeTransactionalCommand} delegate.
	 */
	public void add(IUndoableOperation operation) {

		compositeDelegate.add(operation);
		refreshContexts();
	}

	/**
	 * Removes from my {@link CompositeTransactionalCommand} delegate.
	 */
	public void remove(IUndoableOperation operation) {
		compositeDelegate.remove(operation);
		refreshContexts();
	}

	private void refreshContexts() {

		IUndoContext[] currentContexts = getContexts();

		for (int i = 0; i < currentContexts.length; i++) {
			if (!compositeDelegate.hasContext(currentContexts[i])) {
				removeContext(currentContexts[i]);
			}
		}

		IUndoContext[] newContexts = compositeDelegate.getContexts();

		for (int i = 0; i < newContexts.length; i++) {
			if (!hasContext(newContexts[i])) {
				addContext(newContexts[i]);
			}
		}
	}

	/**
	 * Gets the affected files from my {@link CompositeTransactionalCommand}
	 * delegate.
	 */
	public List getAffectedFiles() {
		return compositeDelegate.getAffectedFiles();
	}

	/**
	 * Computes a command result based on the contents of my
	 * {@link CompositeTransactionalCommand} delegate.
	 * 
	 * @return the command result
	 */
	private CommandResult computeResult() {

		if (compositeDelegate.size() == 0) {
			return null;

		} else if (compositeDelegate.size() == 1) {
			IUndoableOperation operation = (IUndoableOperation) compositeDelegate
					.iterator().next();

			if (operation instanceof ICommand) {
				return ((ICommand) operation).getCommandResult();
			}
			return new CommandResult(Status.OK_STATUS);
		}

		IStatus worst = Status.OK_STATUS;
		List statuses = new ArrayList();
		List returnValues = new ArrayList();

		for (Iterator i = compositeDelegate.iterator(); i.hasNext();) {
			IUndoableOperation operation = (IUndoableOperation) i.next();

			if (operation instanceof ICommand) {
				ICommand command = (ICommand) operation;
				CommandResult result = command.getCommandResult();

				if (result != null) {
					IStatus nextStatus = result.getStatus();

					if (nextStatus.getSeverity() > worst.getSeverity()) {
						worst = nextStatus;
					}
					statuses.add(nextStatus);

					Object nextValue = result.getReturnValue();

					if (nextValue != null) {

						if (getClass().isInstance(command)) {
							// unwrap the values from other composites
							if (nextValue != null
									&& nextValue instanceof Collection) {
								returnValues.addAll((Collection) nextValue);

							} else {
								returnValues.add(nextValue);
							}

						} else {
							returnValues.add(nextValue);
						}
					}
				}
			}
		}

		IStatus status = new MultiStatus(worst.getPlugin(), worst.getCode(),
				(IStatus[]) statuses.toArray(new IStatus[statuses.size()]),
				worst.getMessage(), null);

		return new CommandResult(status, returnValues);
	}
	
	public abstract IStatus getStatus();
}