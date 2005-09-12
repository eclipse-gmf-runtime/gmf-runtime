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