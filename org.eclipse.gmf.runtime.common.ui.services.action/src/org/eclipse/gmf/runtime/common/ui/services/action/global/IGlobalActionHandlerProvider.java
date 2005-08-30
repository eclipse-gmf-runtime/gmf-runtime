/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
