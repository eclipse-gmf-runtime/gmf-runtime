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

import java.util.List;

import org.eclipse.core.commands.operations.IOperationApprover;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;

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
}
