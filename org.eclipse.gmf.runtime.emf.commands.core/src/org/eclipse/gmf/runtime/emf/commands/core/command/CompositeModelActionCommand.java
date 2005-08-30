/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.commands.core.command;

import java.util.ArrayList;
import java.util.List;

/**
 * A composite model command, i.e. a model command that is composed of other
 * commands. All composite model commands have an associated undo interval,
 * through which they can be undone or redone.
 * 
 * @deprecated Use
 *             {@link org.eclipse.gmf.runtime.emf.commands.core.command.CompositeModelCommand}
 *             instead. It has been enhanced so that all of the commands in the
 *             CompositeModelCommand execute in a single write action, which is
 *             the same behaviour as this class.
 * 
 * @author tisrar
 */
public class CompositeModelActionCommand
	extends CompositeModelCommand {

	/**
	 * Constructs a new composite model command with the specified label and
	 * model operation context, and a default undo interval title and key.
	 * 
	 * @param label
	 *            The label for the new composite model command.
	 *  
	 */
	public CompositeModelActionCommand(String label) {
		this(label, label);
	}

	/**
	 * Constructs a new composite model command with the specified label, model
	 * operation context, undo interval title, and undo interval key.
	 * 
	 * @param label
	 *            The label for the new composite model command.
	 * @param undoIntervalTitle
	 *            The undo interval title for the new composite model command.
	 *  
	 */
	public CompositeModelActionCommand(String label, String undoIntervalTitle) {
		this(label, undoIntervalTitle, new ArrayList());
	}

	/**
	 * Constructs a new composite model command with the specified label, model
	 * operation context, and a list of commands
	 * 
	 * @param label
	 *            The label for the new composite model command.
	 * @param commands
	 *            The initial list of commands
	 *  
	 */
	public CompositeModelActionCommand(String label, List commands) {
		this(label, label, commands);
	}

	/**
	 * Constructs a new composite model command with the specified label, model
	 * operation context, undo interval title, undo interval key and list of
	 * commands.
	 * 
	 * @param label
	 *            The label for the new composite model command.
	 * @param undoIntervalTitle
	 *            The undo interval title for the new composite model command.
	 * @param commands
	 *            The initial list of commands
	 *  
	 */
	public CompositeModelActionCommand(String label, String undoIntervalTitle,
			List commands) {
		super(label, undoIntervalTitle, commands);
	}

}