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

import java.util.EventListener;

/**
 * The interface for all objects that are interested in command manager change
 * events. To be such a listener, a class would have to implement this interface
 * and register itself as a listener on a command manager by calling
 * {@link org.eclipse.gmf.runtime.common.core.command.CommandManager#addCommandManagerChangeListener(ICommandManagerChangeListener)}.
 * When no longer interested in receiving event notifications, it can deregister
 * itself as a listener by calling
 * {@link org.eclipse.gmf.runtime.common.core.command.CommandManager#removeCommandManagerChangeListener(ICommandManagerChangeListener)}
 * on the command manager.
 * 
 * @author khussey
 * 
 * @see org.eclipse.gmf.runtime.common.core.command.CommandManager
 * @see org.eclipse.gmf.runtime.common.core.command.CommandManagerChangeEvent
 * @canBeSeenBy %partners
 */
public interface ICommandManagerChangeListener extends EventListener {

    /**
     * Handles an event indicating that a command manager has changed.
     * 
     * @param event The command manager change event to be handled.
     */
    public void commandManagerChanged(CommandManagerChangeEvent event);

}
