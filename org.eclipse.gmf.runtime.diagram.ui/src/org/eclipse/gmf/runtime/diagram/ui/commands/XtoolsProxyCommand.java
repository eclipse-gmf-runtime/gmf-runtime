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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;

/**
 * @author sshaw
 *
 * An Xtools Command Wrapper for an Etools Command
 */
public class XtoolsProxyCommand extends AbstractCommand {

	/** The wrapped command */
	private Command command;
	
	/**
	 * Method XtoolsProxyCommand.
	 * @param command
	 */
	public XtoolsProxyCommand(Command command) {
		super(command.getLabel());
		Assert.isNotNull(command);
		this.command = command;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		command.execute();
		return newOKCommandResult();
	}
	 
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doRedo()
	 */
	protected CommandResult doRedo() {
		command.redo();
		return newOKCommandResult();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doUndo()
	 */
	protected CommandResult doUndo() {
		command.undo();
		return newOKCommandResult();
	}

	/**
	 * Returns the wrapped command.
	 * @return Command
	 */
	public Command getCommand() {
		return command;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isUndoable()
	 */
	public boolean isUndoable() {
		return command.canUndo();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isRedoable()
	 */
	public boolean isRedoable() {
		return command.canUndo();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isExecutable()
	 */
	public boolean isExecutable() {
		return command.canExecute();
	}

}
