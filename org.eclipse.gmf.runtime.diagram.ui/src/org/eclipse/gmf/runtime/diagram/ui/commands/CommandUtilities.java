/******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;

/**
 * Class containing commands utility methods.
 * 
 * @author aboyko
 *
 */
public class CommandUtilities {
	
	/**
	 * This utility function determines whether the command is redoable.
	 * Since GEF commands API doesn't support for canRedo method, this
	 * utility will help to travel through the contents of GEF wrapper
	 * commands and determine redoability of the GEF command based on
	 * the redoability of the commands it contains.
	 * 
	 * @param command The command to be examined
	 * @return <code>true</code> if the passed command is redoable
	 */
	public static boolean canRedo(Command command)
	{
		if (command == null)
			return false;
		
		if (command instanceof IUndoableOperation)
		{
			return ((IUndoableOperation) command).canRedo();
		}
		else if (command instanceof CompoundCommand)
		{
			for ( Iterator iter = ((CompoundCommand)command).getCommands().iterator(); iter.hasNext(); )
			{
				try
				{
					if (!canRedo((Command)iter.next()))
						return false;
				}
				catch (ClassCastException e)
				{
					return false;
				}
			}
			return true;
		}
		else if (command instanceof ICommandProxy)
		{
			return ((ICommandProxy)command).getICommand().canRedo();
		}
		return command.canUndo();
	}
	
	/**
	 * Determines the files affected by <code>command</code>. Since GEF
	 * command API has no support for #getAffectedFiles, this utility will
	 * traverse the contents of GEF wrapper commands and determine the files
	 * affected by the <code>ICommands</code> it contains.
	 * 
	 * @param command
	 *            the command
	 * @return the affected files
	 */
	public static Collection getAffectedFiles(Command command)
	{
		if (command == null)
			return Collections.EMPTY_LIST;
		
		if (command instanceof ICommand) {
			return ((ICommand) command).getAffectedFiles();
			
		} else if (command instanceof ICommandProxy) {
			return ((ICommandProxy)command).getICommand().getAffectedFiles();
			
		} else if (command instanceof CompoundCommand) {
			LinkedHashSet result = new LinkedHashSet();
			
			for (Iterator iter = ((CompoundCommand)command).getCommands().iterator(); iter.hasNext();) {
				result.addAll(getAffectedFiles((Command) iter.next()));
			}
			return result;
		}
		return Collections.EMPTY_LIST;
	}
	
	/**
	 * A helper that inspects the command for the most severe command result.
	 * 
	 * @param command
	 * @return IStatus in the command or null if no result can be obtained from the command.
	 * @since 1.2
	 */
	public static IStatus getMostSevereStatus(Command command) {
		IStatus status = null;

		ICommand iCommand = null;

		if (command instanceof CompoundCommand) {

			List<IStatus> statusList = new ArrayList<IStatus>(
					((CompoundCommand) command).size());

			Iterator<?> iter = ((CompoundCommand) command).getCommands()
					.iterator();
			while (iter.hasNext()) {
				Command nextCommand = (Command) iter.next();
				status = getMostSevereStatus(nextCommand);
				if (status != null) {
					statusList.add(status);
				}
			}
			return aggregateStatuses(statusList);
			
		} else if (command instanceof ICommand) {
			iCommand = (ICommand) command;
			
		} else if (command instanceof ICommandProxy) {
			iCommand = ((ICommandProxy) command).getICommand();
		}

		if (iCommand != null) {
			CommandResult commandResult = iCommand.getCommandResult();
			if (commandResult != null) {
				status = commandResult.getStatus();
			}
		}
		return status;
	}

	/**
	 * Creates a suitable aggregate from these statuses. If there are no
	 * statuses to aggregate, then an null status is returned. If there is a
	 * single status to aggregate, then it is returned. Otherwise, a
	 * multi-status is returned with the provided statuses as children.
	 * 
	 * @param statuses
	 *            the statuses to aggregate. May have zero, one, or more
	 *            elements (all must be {@link IStatus}es)
	 * 
	 * @return the multi-status or null
	 * @since 1.2
	 */
	protected static IStatus aggregateStatuses(List<IStatus> statuses) {
		final IStatus result;

		if (statuses.isEmpty()) {
			result = null;
		} else if (statuses.size() == 1) {
			result = ((IStatus) statuses.get(0));
		} else {
			// find the most severe status, to use its plug-in, code, and
			// message
			IStatus[] statusArray = (IStatus[]) statuses
					.toArray(new IStatus[statuses.size()]);

			IStatus worst = statusArray[0];
			for (int i = 1; i < statusArray.length; i++) {
				if (statusArray[i].getSeverity() > worst.getSeverity()) {
					worst = statusArray[i];
				}
			}
			result = new MultiStatus(worst.getPlugin(), worst.getCode(),
					statusArray, worst.getMessage(), null);
		}
		return result;
	}

}
