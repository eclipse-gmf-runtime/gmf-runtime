/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.core.command;

import java.util.EventObject;

/**
 * Represents an event that is fired when a command manager changes. Instances
 * of this class have an associated command manager (the source of the event)
 * and command (the command that was executed, undone, or redone).
 * 
 * @author khussey
 * 
 * @see org.eclipse.gmf.runtime.common.core.command.ICommandManagerChangeListener
 * @canBeSeenBy %partners
 */
public class CommandManagerChangeEvent extends EventObject {

    /**
     * The command that was executed, undone, or redone.
     */
    private final ICommand command;

    /**
     * Constructs a new command manager change event for the specified command
     * manager.
     * 
     * @param source The command manager that changed.
     */
    public CommandManagerChangeEvent(CommandManager source) {
        this(source, null);
    }

    /**
     * Constructs a new command manager change event for the specified command
     * manager and command.
     * 
     * @param source The command manager that changed.
     * @param command The command that has been executed, undone, or redone.
     */
    public CommandManagerChangeEvent(CommandManager source, ICommand command) {
        super(source);

        this.command = command;
    }

    /**
     * Retrieves the command that was executed, undone or redone.
     * 
     * @return the command that was executed, undone or redone.
     */
    public ICommand getCommand() {
        return command;
    }

    /**
	 * Sets the command manager that changed and that is the source of this
	 * event.
	 * 
	 * @param source
	 *            The command manager.
	 */
    protected void setSource(CommandManager source) {
        assert null != source : "null source"; //$NON-NLS-1$

        this.source = source;
    }

}
