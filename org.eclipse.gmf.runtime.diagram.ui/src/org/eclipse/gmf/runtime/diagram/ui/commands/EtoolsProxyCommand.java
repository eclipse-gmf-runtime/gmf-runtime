/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.commands;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.command.ICommand;

/**
 * @author melaasar
 *
 * An Etools Command Wrapper for an Xtools Command
 */
public class EtoolsProxyCommand extends Command {

	/** The wrapped command */
	private ICommand iCommand;
		
	/**
	 * Constructor
	 * @param iCommand the command to wrap
	 */
	public EtoolsProxyCommand(ICommand iCommand) {
		super(iCommand.getLabel());
		Assert.isNotNull(iCommand);
		this.iCommand = iCommand;
	}

	/**
	 * gets the warapped <code>ICommand</code>
	 * @return the wrapped command
	 */
	public ICommand getICommand() {
		return iCommand;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		return getICommand().isExecutable();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	public boolean canUndo() {
		return getICommand().isUndoable();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		getICommand().execute(new NullProgressMonitor());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo() {
		getICommand().redo();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		getICommand().undo();
	}

}
