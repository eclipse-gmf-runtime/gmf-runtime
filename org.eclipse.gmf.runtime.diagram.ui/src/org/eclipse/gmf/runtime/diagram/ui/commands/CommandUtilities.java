/******************************************************************************
 * Copyright (c) 2006, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.commands;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
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

}
