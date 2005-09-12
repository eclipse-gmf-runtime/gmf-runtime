/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.action.global;

import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * Interface that needs to be implemented by the client who provides
 * a <code>IGlobalActionHandler</code>
 * 
 * @author Vishy Ramaswamy
 */
public interface IGlobalActionHandlerProvider extends IProvider {
	
	/**
	 * Describes the element type when no element type is associated with a
	 * global action handler provider.
	 */
	public static final class NullElementType {
		// No definition required.
	}
	
    /**
     * Returns the <code>IGlobalActionHandler</code> for the given
     * <code>IGlobalActionHandlerContext</code>
     * 
     * @param context The context for the provider
     * @return Returns an <code>IGlobalActionHandler</code>
     */
    public IGlobalActionHandler getGlobalActionHandler(IGlobalActionHandlerContext context);
}
