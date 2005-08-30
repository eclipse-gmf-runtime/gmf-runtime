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

import org.eclipse.core.runtime.IStatus;

/**
 * The result of a command execution. Command results have an associated status
 * and return value (if applicable). A command result is returned each time a
 * command is executed, undone, or redone by a command manager.
 * 
 * @author khussey
 * 
 * @see org.eclipse.core.runtime.IStatus
 * @canBeSeenBy %partners
 */
public final class CommandResult {

    /**
     * The return value for this command, if applicable.
     */
    private final Object returnValue;

    /**
     * The status of executing, undoing, or redoing this command.
     */
    private final IStatus status;

    /**
     * Constructs a new command result with the specified status and a default
     * return value.
     *
     * @param status The status for the new command result.
     */
    public CommandResult(IStatus status) {
        this(status, null);
    }

    /**
     * Constructs a new command result with the specified status and return
     * value.
     *
     * @param status The status for the new command result.
     * @param returnValue The return value for the new command result.
     */
    public CommandResult(IStatus status, Object returnValue) {
        super();

        assert null != status : "null status"; //$NON-NLS-1$

        this.status = status;
        this.returnValue = returnValue;
    }

    /**
     * Retrieves the return value of the command that is executed, undone or redone.
     * 
     * @return The return value.
     */
    public Object getReturnValue() {
        return returnValue;
    }

    /**
     * Retrieves the status of the command that is executed, undone or redone.
     * 
     * @return The status.
     */
    public IStatus getStatus() {
        return status;
    }

}
