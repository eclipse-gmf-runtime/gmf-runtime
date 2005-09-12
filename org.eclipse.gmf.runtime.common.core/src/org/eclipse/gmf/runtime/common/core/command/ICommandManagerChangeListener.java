/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
