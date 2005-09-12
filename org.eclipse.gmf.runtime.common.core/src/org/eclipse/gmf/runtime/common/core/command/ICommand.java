/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
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

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * A unit of work that can potentially be undone and redone. {@link ICommand}s
 * can be executed by the
 * {@link org.eclipse.gmf.runtime.common.core.command.CommandManager}, which
 * will maintain a history of the commands that it has executed that are
 * eligible to be undone and redone.
 * <P>
 * Commands have a label that can be used to provide information to a client.
 * For example, the command label could be used as the text in an undo or redo
 * menu item.
 * <P>
 * Progress through the work done by a command is monitored by an
 * {@link org.eclipse.core.runtime.IProgressMonitor} with which the command is
 * executed.
 * <P>
 * Executing, undoing or redoing a command can have a result which clients can
 * obtain by using the {@link #getCommandResult()} method. For example,
 * executing a command that create a new entity may wish to make the new entity
 * accessible to clients through the {@link #getCommandResult()} method.
 * 
 * @author khussey
 * @canBeSeenBy %partners
 */
public interface ICommand {

	/**
	 * Retrieves the label for this command. The label is typically a very brief
	 * description (suitable for display in a menu item) of what this command
	 * does when it is executed.
	 * 
	 * @return The label for this command.
	 */
	public String getLabel();

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
	public CommandResult getCommandResult();

	/**
	 * Retrieves the collection of objects that would be affected if this
	 * command were executed, undone, or redone.
	 * 
	 * @return The collection of objects affected by this command.
	 */
	public Collection getAffectedObjects();

	/**
	 * Indicates whether non workspace files are involved in executing, undoing
	 * or redoing this command.
	 * 
	 * @return boolean
	 */	
	public boolean involvesReadOnlyNonWorkSpaceFiles();

	/**
	 * Return a validator which can be used to check whether the units being
	 * modified by a command are writable.
	 * 
	 * @return the validator
	 */		
	public CMValidator getValidator();
	
	/**
	 * Returns a new command object that represents a composition of this
	 * command with the specified <code>command</code> parameter.
	 * 
	 * @param command
	 *            The command that is to be composed with this command.
	 * @return A command that represents a composition of this command with the
	 *         specified command.
	 */
	ICommand compose(ICommand command);

	/**
	 * Answers whether this command can be executed.
	 * 
	 * @return <code>true</code> if the command can be executed;
	 *         <code>false</code> otherwise.
	 */
	public boolean isExecutable();

	/**
	 * Answers whether this command can be redone.
	 * 
	 * @return <code>true</code> if the command can be redone;
	 *         <code>false</code> otherwise.
	 */
	public boolean isRedoable();

	/**
	 * Answers whether this command can be undone.
	 * 
	 * @return <code>true</code> if the command can be undone;
	 *         <code>false</code> otherwise.
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
	 */
	public void execute(IProgressMonitor progressMonitor);

	/**
	 * Redoes this command.
	 */
	public void redo();

	/**
	 * Undoes this command.
	 */
	public void undo();

}
