/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.commands.core.command;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.core.edit.MUndoInterval;

/**
 * A command which has a corresponding undo interval.
 * @author Michael Yee
 */
public interface IUndoIntervalCommand extends ICommand {

	/**
	 * Retrieves the command's undo interval.
	 * @return the command's undo interval.
	 */
	public MUndoInterval getUndoInterval();
}
