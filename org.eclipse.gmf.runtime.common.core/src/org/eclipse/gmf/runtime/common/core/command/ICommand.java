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

import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.operations.IOperationApprover;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * A self-composing undoable operation that has a {@link CommandResult} and a
 * list of affected {@link IFile}s.
 * <P>
 * Executing, undoing or redoing a command can have a result which clients can
 * obtain by using the {@link #getCommandResult()} method. For example,
 * executing a command that create a new entity may wish to make the new entity
 * accessible to clients through the {@link #getCommandResult()} method.
 * <P>
 * The command provides a list of {@link IFile}s that are expected to be
 * modified when the it is executed, undone or redone. An
 * {@link IOperationApprover} is registered with the
 * {@link OperationHistoryFactory#getOperationHistory()} to validate the
 * modification to these resources.
 * <P>
 * If an error occurs, or the progress monitor is canceled during execute, undo
 * or redo, the command should make every effort to roll back the changes it has
 * made up to that point.
 * 
 * @author khussey
 * @author ldamus
 * 
 * @canBeSeenBy %partners
 */
public interface ICommand extends IUndoableOperation {

	/**
	 * Retrieves the result of executing, undoing, or redoing this command,
	 * depending on which of these operations was last performed. This value can
	 * be <code>null</code> if the operation has no meaningful result.
	 * <P>
	 * The value of this result is undefined if the command has not yet been
	 * executed, undone or redone.
	 * 
	 * @return The result of executing, undoing or redoing this command.
	 */
	public abstract CommandResult getCommandResult();

	/**
	 * Returns the list of {@link IFile}s that are expected to be modified by
	 * this command.
	 * 
	 * @return the list of {@link IFile}s that will be modified
	 */
	public abstract List getAffectedFiles();

	/**
	 * Returns a new command object that represents a composition of this
	 * command with the specified <code>command</code> parameter.
	 * 
	 * @param operation
	 *            The operation that is to be composed with this command.
	 * @return A command that represents a composition of this command with the
	 *         specified command.
	 */
	public abstract ICommand compose(IUndoableOperation operation);
    
    /**
     * Returns the simplest form of this command that is equivalent. Use this
     * method to remove unnecessary nesting of commands.
     * 
     * @return the simplest form of this command that is equivalent
     */
    public abstract ICommand reduce();

    
    /**
     * Retrieves the collection of objects that would be affected if this
     * command were executed, undone, or redone.
     * 
     * @return The collection of objects affected by this command.
     * 
     * @deprecated Commands that will modify resources and wish to have these
     *             resources validated should implement the
     *             {@link #getAffectedFiles()} interface.
     */
    public Collection getAffectedObjects();

    /**
     * Indicates whether non workspace files are involved in executing, undoing
     * or redoing this command.
     * 
     * @return boolean
     * 
     * @deprecated No replacement. File validation is now done through a
     *             {@link IOperationApprover} registered with with the
     *             {@link OperationHistoryFactory#getOperationHistory()}.
     */
    public boolean involvesReadOnlyNonWorkSpaceFiles();

    /**
     * Return a validator which can be used to check whether the units being
     * modified by a command are writable.
     * 
     * @return the validator
     * 
     * @deprecated No replacement. File validation is now done through a
     *             {@link IOperationApprover} registered with with the
     *             {@link OperationHistoryFactory#getOperationHistory()}.
     */
    public CMValidator getValidator();

    /**
     * Answers whether this command can be executed.
     * 
     * @return <code>true</code> if the command can be executed;
     *         <code>false</code> otherwise.
     * 
     * @deprecated Use {@link IUndoableOperation#canExecute()} instead.
     */
    public boolean isExecutable();

    /**
     * Answers whether this command can be redone.
     * 
     * @return <code>true</code> if the command can be redone;
     *         <code>false</code> otherwise.
     * 
     * @deprecated Use {@link IUndoableOperation#canRedo()} instead.
     */
    public boolean isRedoable();

    /**
     * Answers whether this command can be undone.
     * 
     * @return <code>true</code> if the command can be undone;
     *         <code>false</code> otherwise.
     * 
     * @deprecated Use {@link IUndoableOperation#canUndo()()} instead.
     */
    public boolean isUndoable();

    /**
     * Executes this command. A progress monitor is supplied so that the
     * progress of executing the command may be tracked.
     * 
     * @param progressMonitor
     *            The object that monitors the progress of this command
     *            execution. May be
     *            {@link org.eclipse.core.runtime.NullProgressMonitor}if the
     *            command should be executed without monitoring its progress.
     * 
     * @deprecated Use
     *             {@link IUndoableOperation#execute(IProgressMonitor, org.eclipse.core.runtime.IAdaptable)}
     *             instead.
     */
    public void execute(IProgressMonitor progressMonitor);

    /**
     * Redoes this command.
     * 
     * @deprecated Use
     *             {@link IUndoableOperation#redo(IProgressMonitor, org.eclipse.core.runtime.IAdaptable)}
     *             instead.
     */
    public void redo();

    /**
     * Undoes this command.
     * 
     * @deprecated Use
     *             {@link IUndoableOperation#undo(IProgressMonitor, org.eclipse.core.runtime.IAdaptable)}
     *             instead.
     */
    public void undo();

}
