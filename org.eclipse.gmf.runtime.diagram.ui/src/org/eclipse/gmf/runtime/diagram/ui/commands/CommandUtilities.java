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

import java.util.Iterator;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;

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

}
