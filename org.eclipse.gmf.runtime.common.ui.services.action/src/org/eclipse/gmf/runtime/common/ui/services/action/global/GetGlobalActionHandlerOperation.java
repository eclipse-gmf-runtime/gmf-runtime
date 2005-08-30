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

import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * An operation that gets the <code>IGlobalActionHandler</code> associated
 * with the <code>IGlobalActionHandlerContext</code> from the
 * <code>IGlobalActionHandlerProvider</code>
 * 
 * @author Vishy Ramaswamy
 */
public final class GetGlobalActionHandlerOperation
    extends GlobalActionHandlerOperation {
    /**
     * Constructor for GetGlobalActionHandlerOperation.
     * @param context The global action handler context
     */
    public GetGlobalActionHandlerOperation(IGlobalActionHandlerContext context) {
        super(context);
    }

    /**
     * Executes this operation on the <code>IGlobalActionHandlerProvider</code>
     * provider.
     * 
     * @param provider The provider on which to execute this operation.
     * 
     * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(IProvider)
     */
    public Object execute(IProvider provider) {

        if (provider instanceof IGlobalActionHandlerProvider) {
            /* get the provider */
            IGlobalActionHandlerProvider prov =
                (IGlobalActionHandlerProvider)provider;

            /* get the handler */
            IGlobalActionHandler handler =
                prov.getGlobalActionHandler(getContext());

            /* return the handler */
            return handler;
        }

        return null;
    }
}
