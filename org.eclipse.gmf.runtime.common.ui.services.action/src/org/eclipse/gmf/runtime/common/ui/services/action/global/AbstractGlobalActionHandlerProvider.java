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
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;

/**
 * The abstract parent of all concrete global action handler providers.
 * A concrete provider needs to override the <code>getGlobalActionHandler()</code>
 * method only. The <code>provides()</code> method has a default implementation.
 * The <code>provides()</code> method is already handled by the proxy for
 * the provider (<code>GlobalActionHandlerService.ProviderDescriptor</code>).
 * The proxy contains all the information necessary to decide handle the
 * <code>provides()</code> method.
 * 
 * @author Vishy Ramaswamy
 */
public abstract class AbstractGlobalActionHandlerProvider
    extends AbstractProvider
    implements IGlobalActionHandlerProvider {
    /**
     * Constructor for AbstractGlobalActionHandlerProvider.
     */
    public AbstractGlobalActionHandlerProvider() {
        super();
    }

    /**
     * Returns a <code>IGlobalActionHandler</code> for the given
     * <code>IGlobalActionHandlerContext</code>
     * 
     * @param context The context information
     * 
     * @return The global action handler associated with the context
     */
    public IGlobalActionHandler getGlobalActionHandler(IGlobalActionHandlerContext context) {
        return new AbstractGlobalActionHandler() {

            public boolean canHandle(IGlobalActionContext cntxt) {
                return false;
            }

            public ICommand getCommand(IGlobalActionContext cntxt) {
                return null;
            }
        };
    }

    /**
     * Returns <code>true</code> if the provider can handle the
     * <code>GlobalActionHandlerOperation</code> operation
     * 
     * @param operation An operation
     * 
     * @return Returns <code>true</code> if the provider can handle the
     *          <code>GlobalActionHandlerOperation</code> operation. Otherwise
     *          returns <code>false</code>
     */
    public final boolean provides(IOperation operation) {

        if (operation instanceof GlobalActionHandlerOperation) {
            return true;
        }

        return false;
    }
}
