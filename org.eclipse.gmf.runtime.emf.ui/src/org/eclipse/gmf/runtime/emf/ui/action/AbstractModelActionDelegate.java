/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
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
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.action.AbstractActionDelegate;
import org.eclipse.gmf.runtime.common.ui.action.IActionWithProgress;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.ui.internal.MslUIDebugOptions;
import org.eclipse.gmf.runtime.emf.ui.internal.MslUIPlugin;
import org.eclipse.gmf.runtime.emf.ui.internal.MslUIStatusCodes;

/**
 * The abstract parent of all concrete action delegates that execute model
 * commands. Concrete subclasses must provide a definition of the
 * <code>doRun()</code> method to gather any required input and execute a
 * model command.
 * <P>
 * Subclasses that execute a command must return <code>false</code>from
 * {@link #isReadOnly()}. The superclass performs the
 * {@link #doRun(IProgressMonitor)} method in an EMF operation executed on the
 * operation history. Subclasses should set the action delegate status using
 * {@link #setStatus(IStatus)} to their command execution status.
 * <P>
 * Subclasses that only want to read from the model must return
 * <code>true</code> from {@link #isReadOnly()}.
 * 
 * @author khussey
 */
public abstract class AbstractModelActionDelegate
	extends AbstractActionDelegate {
    
    private IStatus status;
    
    /**
     * Intializes me with the singleton editing domain.
     */
    protected AbstractModelActionDelegate() {
        super();
    }

	/**
     * Runs this model action delegate as a read action.
     * 
     * @see IActionWithProgress#run(IProgressMonitor)
     */
    public final void run(final IProgressMonitor progressMonitor) {

        if (isReadOnly()) {
            // run exclusive so that subclasses can only read from the model
            try {
                getEditingDomain().runExclusive(new Runnable() {

                    public void run() {
                        AbstractModelActionDelegate.super.run(progressMonitor);
                    }
                });
            } catch (InterruptedException e) {
                Trace
                    .catching(MslUIPlugin.getDefault(),
                        MslUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
                        "run", e); //$NON-NLS-1$
            }

        } else {
            // Run in a composite transactional operation so that subclasses can
			// both read and write to the model. Commands executed by subclasses
			// to modify the model will be appended to this composite
			// transactional operation.
        	WriteCommand write = new WriteCommand(getEditingDomain(),
					getLabel(), getActionManager().getOperationHistory(),
					new Runnable() {
						public void run() {
							AbstractModelActionDelegate.super
									.run(progressMonitor);
						}
					});

            try {
                getActionManager().getOperationHistory().execute(write,
                    new NullProgressMonitor(), null);

            } catch (ExecutionException e) {
                Trace
                    .catching(MslUIPlugin.getDefault(),
                        MslUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
                        "run", e); //$NON-NLS-1$
                Log.error(MslUIPlugin.getDefault(),
                    MslUIStatusCodes.IGNORED_EXCEPTION_WARNING, e
                        .getLocalizedMessage(), e);
            }
        }
    }
    
    /**
     * Gets my editing domain. Can be <code>null</code> if I don't modify
     * model resources.
     * 
     * @return my editing domain.
     */
    protected abstract TransactionalEditingDomain getEditingDomain();
    
    /**
     * Answers whether or not I am a read-only action. Returns
     * <code>false</code> by default. Subclasses may override.
     * 
     * @return <code>false</code>
     */
    protected boolean isReadOnly() {
        return false;
    }
    
    /**
     * Gets the status of running this action delegate.
     * 
     * @return my status
     */
    protected IStatus getStatus() {
        if (status == null) {
            return Status.OK_STATUS;
        }
        return status;
    }
    
    /**
     * Sets the action delegate status. Subclasses should set the status when they
     * execute a command.
     * 
     * @param status
     *            the new status
     */
    protected void setStatus(IStatus status) {
        this.status = status;
    }
    
    /**
	 * Convenience method for subclasses to execute an undoable operation on the
	 * action manager's operation history. Sets my status to the status of the
	 * operation execution, and returns that status.
	 * 
	 * @param operation
	 *            the operation to be executed
	 * @param progressMonitor
	 *            the progress monitor
	 * @param info
	 *            the adaptable info, may be <code>null</code>
	 * @return the status of the operation execution.
	 */
	protected IStatus execute(IUndoableOperation operation,
			IProgressMonitor progressMonitor, IAdaptable info) {

		try {
			setStatus(getActionManager().getOperationHistory().execute(
					operation, progressMonitor, info));

		} catch (ExecutionException e) {
			setStatus(new Status(Status.ERROR, MslUIPlugin.getPluginId(),
					MslUIStatusCodes.IGNORED_EXCEPTION_WARNING, e
							.getLocalizedMessage(), e));
			Trace
					.catching(MslUIPlugin.getDefault(),
							MslUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
							"run", e); //$NON-NLS-1$
			Log.error(MslUIPlugin.getDefault(),
					MslUIStatusCodes.IGNORED_EXCEPTION_WARNING, e
							.getLocalizedMessage(), e);
		}
		return getStatus();
	}
	
	/**
	 * Command used to allow subclasses of the
	 * {@link AbstractModelActionDelegate} to read and write to the model from
	 * their #doRun implementation.
	 * <P>
	 * This is a kind of {@link ICompositeOperation} that opens itself on the
	 * operation history. It runs a {@link Runnable} supplied at the time of
	 * instantiation and any operations executed through the history in that
	 * runnable are considered to be part of the composite, and can be undone
	 * and redone in a single step. This allows file modification validation to
	 * be done on each command executed by a subclass of
	 * {@link AbstractModelActionDelegate}.
	 * 
	 * @author ldamus
	 */
	private class WriteCommand extends AbstractTransactionalCommand implements
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
					history.closeOperation(status.isOK(), false,
							IOperationHistory.EXECUTE);

				} catch (RuntimeException e) {
					history.closeOperation(false, false,
							IOperationHistory.EXECUTE);
					throw e;
				}
			}
			
			// have to compute the result because we didn't actually execute the
			// compositeDelegate
			return computeResult();
		}

		/**
		 * Redoes the commands that were accumulated when the runnable was
		 * run.
		 */
		protected CommandResult doRedoWithResult(
				IProgressMonitor progressMonitor, IAdaptable info)
				throws ExecutionException {

			compositeDelegate.redo(progressMonitor, info);
			return compositeDelegate.getCommandResult();
		}

		/**
		 * Undoes the commands that were accumulated when the runnable was
		 * run.
		 */
		protected CommandResult doUndoWithResult(
				IProgressMonitor progressMonitor, IAdaptable info)
				throws ExecutionException {

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

			IStatus status = new MultiStatus(worst.getPlugin(),
					worst.getCode(), (IStatus[]) statuses
							.toArray(new IStatus[statuses.size()]), worst
							.getMessage(), null);

			return new CommandResult(status, returnValues);
		}
	}

}
