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

import org.eclipse.gmf.runtime.common.core.service.IOperation;

/**
 * The abstract parent of all global action handler operations. This
 * class maintains a reference to the <code>IGlobalActionHandlerContext</code>.
 * 
 * @author Vishy Ramaswamy
 */
public abstract class GlobalActionHandlerOperation implements IOperation {
    /**
     * Attribute for the <code>IGlobalActionHandlerContext</code>.
     */
    private final IGlobalActionHandlerContext context;

    /**
     * Create a GlobalActionHandlerOperation.
     * 
     * @param context attribute for the <code>IGlobalActionHandlerContext</code>
     */
    public GlobalActionHandlerOperation(IGlobalActionHandlerContext context) {
        assert null != context : "context cannot be null"; //$NON-NLS-1$

        this.context = context;
    }

    /**
     * Returns the context.
     * @return The <code>context</code> instance variable
     */
    public final IGlobalActionHandlerContext getContext() {
        return context;
    }
}
