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

package org.eclipse.gmf.runtime.common.ui.services.action.global;

import org.eclipse.gmf.runtime.common.core.command.ICommand;

/**
 * Interface that needs to implemented by the <code>IGlobalActionHandlerProvider</code>
 * 
 * @author Vishy Ramaswamy
 */
public interface IGlobalActionHandler {
    /**
     * Return true if the handler can handle the context
     *
     * @param context The context
     * @return boolean
     */
    public boolean canHandle(IGlobalActionContext context);

    /**
     * Return the ICommand for the context
     *
     * @param context The context
     * @return ICommand
     */
    public ICommand getCommand(IGlobalActionContext context);
    
    /**
     * Gets the label for this global action, given the context.
     * Returns <code>null</code> if the default global action label
     * should be used.
     * 
     * @param context the global action context
     * @return the global action label, or <code>null</code> if
     * 		   the default label should be used.
     */
    public String getLabel(IGlobalActionContext context);
}
