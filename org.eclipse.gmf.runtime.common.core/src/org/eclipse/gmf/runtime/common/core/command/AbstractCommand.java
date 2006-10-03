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

package org.eclipse.gmf.runtime.common.core.command;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IOperationApprover;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;

/**
 * An abstract superclass for GMF {@link IUndoableOperation}s that do not
 * modify EMF model resources.
 * <p>
 * The operation provides a list of {@link IFile}s that are expected to be modified when
 * the operation is executed, undone or redone. An {@link IOperationApprover} is
 * registered with the {@link OperationHistoryFactory#getOperationHistory()} to
 * validate the modification to these resources.
 * <p>
 * This class is meant to be extended by clients.
 * 
 * @author khussey
 * @author ldamus
 *
 * @see org.eclipse.gmf.runtime.common.core.command.ICommand
 * @canBeSeenBy %partners
 */
public abstract class AbstractCommand extends AbstractOperation
		implements ICommand {

	private final List affectedFiles;

	private CommandResult commandResult;
    
    /**
     * Initializes me with a label.
     * 
     * @param label
     *            the operation label
     */
    public AbstractCommand(String label) {
        this(label, null);
    }

	/**
	 * Initializes me with a label and a list of {@link IFile}s that anticipate modifying
	 * when I am executed, undone or redone.
	 * 
	 * @param label
	 *            the operation label
	 * @param affectedFiles
	 *            the list of affected {@link IFile}s; may be <code>null</code>
	 */
	public AbstractCommand(String label, List affectedFiles) {
		super(label);

		if (affectedFiles == null) {
			this.affectedFiles = new ArrayList(2);

		} else {
			this.affectedFiles = affectedFiles;
		}
	}

	/**
	 * Returns the {@link IFile}s that may be modified when the operation is
	 * executed, undone or redone.
	 */
	public List getAffectedFiles() {
		return affectedFiles;
	}

	// Documentation copied from the interface
	public CommandResult getCommandResult() {
		return commandResult;
	}

	/**
	 * Sets the command result.
	 * 
	 * @param result
	 *            the new result for this command.
	 */
	protected final void setResult(CommandResult result) {
		this.commandResult = result;
	}

	// Documentation copied from the interface
	public ICommand compose(IUndoableOperation operation) {

		if (operation != null) {

			return new CompositeCommand(getLabel()).compose(this)
					.compose(operation);
		}
		return this;
	}
    
	// Documentation copied from the interface
    public ICommand reduce() {
        return this;
    }

	/**
	 * Delegates to {@link #doExecuteWithResult(IProgressMonitor, IAdaptable)} and sets
	 * the command result.
	 */
	public IStatus execute(IProgressMonitor progressMonitor, IAdaptable info)
			throws ExecutionException {
		
		IProgressMonitor monitor = progressMonitor != null ? progressMonitor
				: new NullProgressMonitor();

		CommandResult result = doExecuteWithResult(monitor, info);
		setResult(result);
		return result != null ? result.getStatus()
	            : Status.OK_STATUS;
	}

	/**
	 * Performs the actual work of executing this command. Subclasses must
	 * implement this method to perform some operation.
	 * 
	 * @param progressMonitor
	 *            the progress monitor provided by the operation history. Must
	 *            never be <code>null</code>.
	 * @param info
	 *            the IAdaptable (or <code>null</code>) provided by the
	 *            caller in order to supply UI information for prompting the
	 *            user if necessary. When this parameter is not
	 *            <code>null</code>, it should minimally contain an adapter
	 *            for the org.eclipse.swt.widgets.Shell.class.
	 * 
	 * @return The result of executing this command. May be <code>null</code>
	 *         if the execution status is OK, but there is no meaningful result
	 *         to be returned.
	 * 
	 * @throws ExecutionException
	 *             if, for some reason, I fail to complete the operation
	 */
	protected abstract CommandResult doExecuteWithResult(
			IProgressMonitor progressMonitor, IAdaptable info)
			throws ExecutionException;

	/**
	 * Delegates to {@link #doRedoWithResult(IProgressMonitor, IAdaptable)} and sets the
	 * command result.
	 */
	public IStatus redo(IProgressMonitor progressMonitor, IAdaptable info)
			throws ExecutionException {

		IProgressMonitor monitor = progressMonitor != null ? progressMonitor
				: new NullProgressMonitor();
		
		CommandResult result = doRedoWithResult(monitor, info);
		setResult(result);
		return result != null ? result.getStatus()
	            : Status.OK_STATUS;
	}

	/**
	 * Performs the actual work of redoing this command. Subclasses must
	 * implement this method to perform the redo.
	 * 
	 * @param progressMonitor
	 *            the progress monitor provided by the operation history. Must
	 *            never be <code>null</code>.
	 * @param info
	 *            the IAdaptable (or <code>null</code>) provided by the
	 *            caller in order to supply UI information for prompting the
	 *            user if necessary. When this parameter is not
	 *            <code>null</code>, it should minimally contain an adapter
	 *            for the org.eclipse.swt.widgets.Shell.class.
	 * 
	 * @return The result of redoing this command. May be <code>null</code>
	 *         if the execution status is OK, but there is no meaningful result
	 *         to be returned.
	 * 
	 * @throws ExecutionException
	 *             on failure to redo
	 */
	protected abstract CommandResult doRedoWithResult(IProgressMonitor progressMonitor,
			IAdaptable info) throws ExecutionException;

	/**
	 * Delegates to {@link #doUndoWithResult(IProgressMonitor, IAdaptable)} and sets the
	 * command result.
	 */
	public IStatus undo(IProgressMonitor progressMonitor, IAdaptable info)
			throws ExecutionException {
		
		IProgressMonitor monitor = progressMonitor != null ? progressMonitor
				: new NullProgressMonitor();

		CommandResult result = doUndoWithResult(monitor, info);
		setResult(result);
		return result != null ? result.getStatus()
	            : Status.OK_STATUS;
	}

	/**
	 * Performs the actual work of undoing this command. Subclasses must
	 * implement this method to perform the undo.
	 * 
	 * @param progressMonitor
	 *            the progress monitor provided by the operation history. Must
	 *            never be <code>null</code>.
	 * @param info
	 *            the IAdaptable (or <code>null</code>) provided by the
	 *            caller in order to supply UI information for prompting the
	 *            user if necessary. When this parameter is not
	 *            <code>null</code>, it should minimally contain an adapter
	 *            for the org.eclipse.swt.widgets.Shell.class.
	 * 
	 * @return The result of undoing this command. May be <code>null</code>
	 *         if the execution status is OK, but there is no meaningful result
	 *         to be returned.
	 * 
	 * @throws ExecutionException
	 *             on failure to undo
	 */
	protected abstract CommandResult doUndoWithResult(IProgressMonitor progressMonitor,
			IAdaptable info) throws ExecutionException;

    public void dispose() {
        super.dispose();
        
        // clear my contexts
        IUndoContext[] contexts = getContexts();
        for (int i = 0; i < contexts.length; i++) {
            removeContext(contexts[i]);
        }
    }
}
