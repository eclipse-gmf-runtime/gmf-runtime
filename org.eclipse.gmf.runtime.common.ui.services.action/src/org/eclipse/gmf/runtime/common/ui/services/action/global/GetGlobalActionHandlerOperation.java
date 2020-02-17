/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
