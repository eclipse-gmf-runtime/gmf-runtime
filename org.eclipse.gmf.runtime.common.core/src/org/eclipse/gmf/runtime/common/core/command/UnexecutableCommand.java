/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */package org.eclipse.gmf.runtime.common.core.command;

import org.eclipse.core.runtime.IProgressMonitor;


/**
 * A command that cannot be executed. This is an implementation of the Null
 * Object pattern that can be used to provide a command object that can't be
 * executed to clients who require an
 * {@link org.eclipse.gmf.runtime.common.core.command.ICommand}.
 * 
 * @author melaasar
 * @canBeSeenBy %partners
 */
public class UnexecutableCommand extends AbstractCommand {

	/**
	 * The singleton instance.
	 */
	public static final UnexecutableCommand INSTANCE = new UnexecutableCommand();
	
	/**
	 * Creates an instance of an unexecutable command.
	 */
	protected UnexecutableCommand() {
		super(null);
	}
	
	/**
	 * Does nothing.
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		return null;
	}
	
	/**
	 * @return false.
	 */
	public boolean isExecutable() {
		return false;
	}
	
	/**
	 * @return false.
	 */
	public boolean isRedoable() {
		return false;
	}
	
	/**
	 * @return false.
	 */
	public boolean isUndoable() {
		return false;
	}
}
