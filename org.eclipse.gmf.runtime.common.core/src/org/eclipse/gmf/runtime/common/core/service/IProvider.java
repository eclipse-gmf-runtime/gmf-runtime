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

/**
 * The interface for all service providers. Specifies a part of the contract
 * between a service and its providers:
 * <ul>
 * <li>A service can listen to its provider
 * <li>A service can request a provider whether it wants to support a given
 * request
 * </ul>
 * <p>
 * Service implementers are expected to expose service specific classes
 * implementing this interface.
 * </p>
 * <p>
 * Service provider implementers never need to implement this interface
 * directly, they instead derive their provider implementation from the service
 * specific implementation of this interface.
 * </p>
 */
public interface IProvider {

	/**
	 * Adds the specified listener to the list of provider change listeners for
	 * this provider.
	 * 
	 * @param listener
	 *            The listener to be added.
	 */
	public void addProviderChangeListener(IProviderChangeListener listener);

	/**
	 * Indicates whether this provider provides the specified operation.
	 * <p>
	 * </p>
	 * <p>
	 * </p>
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

	/**
	 * Removes the specified listener from the list of provider change listeners
	 * for this provider.
	 * 
	 * @param listener
	 *            The listener to be removed.
	 */
	public void removeProviderChangeListener(IProviderChangeListener listener);

}