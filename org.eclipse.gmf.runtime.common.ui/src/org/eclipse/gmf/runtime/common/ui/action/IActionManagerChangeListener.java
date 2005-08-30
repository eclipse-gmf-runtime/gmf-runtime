/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.action;

import java.util.EventListener;

/**
 * The interface for all objects that are interested in action manager change
 * events. To be such a listener, a class has to implement this interface and
 * register itself as a listener on an action manager by calling
 * <code>addActionManagerChangeListener()</code>. When no longer interested in
 * receiving event notifications, it can deregister itself as a listener by
 * calling <code>removeActionManagerChangeListener()</code> on the action
 * manager.
 * 
 * @author khussey
 * 
 * @see org.eclipse.gmf.runtime.common.ui.action.ActionManager
 * @see org.eclipse.gmf.runtime.common.ui.action.ActionManagerChangeEvent
 */
public interface IActionManagerChangeListener extends EventListener {

    /**
     * Handles an event indicating that an action manager has changed.
     * 
     * @param event The action manager change event to be handled.
     */
    public void actionManagerChanged(ActionManagerChangeEvent event);

}
