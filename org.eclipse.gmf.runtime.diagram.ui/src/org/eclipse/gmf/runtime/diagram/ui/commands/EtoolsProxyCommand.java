/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
