/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.resources;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;


/**
 * Mix-in interface to be implemented by {@link Resource.Factory} implementations
 * that provide extended object name/type/ID information in proxy URIs and/or
 * custom proxy resolution strategies.
 *
 * @author Christian W. Damus (cdamus)
 */
public interface IExtendedResourceFactory {
	/**
	 * Obtains the name of the specified <code>proxy</code> object from its
	 * proxy URI.
	 * 
	 * @param proxy a proxy object
	 * 
	 * @return its name, or <code>null</code> to defer to the default proxy name
	 *     algorithm
	 */
	String getProxyName(EObject proxy);

	/**
	 * Obtains the qualified name of the specified <code>proxy</code> object from its
	 * proxy URI.
	 * 
	 * @param proxy a proxy object
	 * 
	 * @return its qualified name, or <code>null</code> to defer to the default
	 *     proxy qualified name algorithm
	 */
	String getProxyQualifiedName(EObject proxy);

	/**
	 * Obtains the ID of the specified <code>proxy</code> object from its
	 * proxy URI.
	 * 
	 * @param proxy a proxy object
	 * 
	 * @return its ID, or <code>null</code> to defer to the default proxy ID
	 *     algorithm
	 */
	String getProxyID(EObject proxy);

	/**
	 * Obtains the ID of the specified <code>proxy</code> object's EClass from its
	 * proxy URI.
	 * 
	 * @param proxy a proxy object
	 * 
	 * @return its EClass ID, or <code>null</code> to defer to the default proxy
	 *     EClass ID algorithm
	 */
	String getProxyClassID(EObject proxy);

	/**
	 * Resolves the specified <code>proxy</code> object.
	 * 
	 * @param domain the editing domain in which to resolve the proxy (provides,
	 *     among other things, the resource set context)
	 * @param proxy a proxy object
	 * 
	 * @return the resolved object, or <code>null</code> if the proxy
	 *     cannot be resolved
	 */
	EObject resolve(TransactionalEditingDomain domain, EObject proxy);
}
