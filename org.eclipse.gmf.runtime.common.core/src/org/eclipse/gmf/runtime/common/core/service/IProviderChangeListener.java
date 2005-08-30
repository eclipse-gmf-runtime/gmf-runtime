/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.core.service;

import java.util.EventListener;

/**
 * An interface for types that listen to service provider changes.
 * <p>
 * Service implementers are expected to implement this interface within their
 * service should they need to know about state change in their providers.
 * </p>
 * <p>
 * Service provider implementers never need to implement this interface.
 * </p>
 * 
 * @see IProvider#addProviderChangeListener
 * @see IProvider#removeProviderChangeListener
 */
public interface IProviderChangeListener
	extends EventListener {

	/**
	 * Handles an event indicating that a provider has changed.
	 * 
	 * @param event
	 *            The provider change event to be handled.
	 */
	public void providerChanged(ProviderChangeEvent event);

}