/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
