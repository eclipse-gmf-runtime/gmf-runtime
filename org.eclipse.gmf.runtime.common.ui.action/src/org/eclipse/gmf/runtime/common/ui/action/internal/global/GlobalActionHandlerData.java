/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.action.internal.global;

import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler;

/**
 * A tuple that contains the <code>IGlobalActionHandler</code> and its
 * associated <code>IGlobalActionContext</code>
 * 
 * @author Vishy Ramaswamy
 */
public final class GlobalActionHandlerData {

    /**
     * Attribute for the handler
     */
    final private IGlobalActionHandler handler;

    /**
     * Attribute for the context
     */
    final private IGlobalActionContext context;

    /**
     * Constructor for GlobalActionHandlerData.
     * 
     * @param handler attribute for the context
     * @param context attribute for the handler
     */
    public GlobalActionHandlerData(
        IGlobalActionHandler handler,
        IGlobalActionContext context) {
        super();

        assert null != handler;
        assert null != context;

        this.handler = handler;
        this.context = context;
    }

    /**
     * Returns the handler.
     * @return IGlobalActionHandler
     */
    public IGlobalActionHandler getHandler() {
        return handler;
    }

    /**
     * Returns the context.
     * @return IGlobalActionContext
     */
    public IGlobalActionContext getContext() {
        return context;
    }
}
