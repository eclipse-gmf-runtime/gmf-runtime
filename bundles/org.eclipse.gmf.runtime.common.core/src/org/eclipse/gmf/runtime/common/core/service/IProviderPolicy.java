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

package org.eclipse.gmf.runtime.common.core.service;

/**
 * The interface for all service providers lightweight's implementation. Specifies
 * a single part of the contract between a service and its providers:
 * <ul>
 * <li>A service can request a provider whether it wants to support a given
 * request
 * </ul>
 * <p>
 * Service implementers are not expected to implement this interface.
 * </p>
 * <p>
 * Service provider implementers should implement this interface directly in a
 * lightweight plug-in should they need to be more aggressive at preventing the
 * loading of the provider heavyweight plug-in. A provider need to expose the
 * class implementing this interface in the class attribute of a Policy
 * sub-element in their provider extension descriptor.
 * </p>
 * Provider's plugin.xml
 * <p>
 * 
 * <pre>
 * 
 *     &lt;extension
 *        id=&quot;...&quot;
 *        name=&quot;...&quot;
 *        point=&quot;...&quot;&gt;
 *        &lt;XXXProvider
 *           class=&quot;...&quot;&gt;
 *           &lt;Priority
 *              name=&quot;...&quot;&gt;
 *           &lt;/Priority&gt;
 *           &lt;Policy
 *              class=&quot;com.example.myClass&quot;
 *              plugin=&quot;com.example.myLightWeightPlugin&quot;/&gt;
 *        &lt;/XXXProvider&gt;
 *     &lt;/extension&gt;
 *  
 * </pre>
 * 
 * </p>
 */
public interface IProviderPolicy {

	/**
	 * Indicates whether this provider provides the specified operation.
	 * <p>
	 * <p>
	 * Providers generally cast the operation to a service specific
	 * {@link IOperation}-derived class in order to determine whether they
	 * support the request.
	 * 
	 * @return <code>true</code> if this provider provides the operation;
	 *         <code>false</code> otherwise.
	 * @param operation
	 *            The operation in question.
	 */
	public boolean provides(IOperation operation);

}