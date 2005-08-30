/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.core.service;

/**
 * The interface for all service provider operations.
 * <p>
 * Service implementers are expected to expose service specific classes
 * implementing this interface.
 * </p>
 * <p>
 * Service provider implementers never need to implement this interface, they
 * instead use the service specific implementation in their provider
 * implementation.
 * </p>
 * 
 * @see IProvider#provides
 */
public interface IOperation {

	/**
	 * Executes this operation on the specified provider.
	 * <p>
	 * Service implementers generally implement this method by first casting the
	 * provider to their service specific {@link IProvider}-derived class and
	 * then by delegating it the execution. Delegation is accomplished through
	 * the service specific provider derived class API.
	 * </p>
	 * 
	 * @param provider
	 *            The provider on which to execute the operation.
	 * @return The result of executing this operation.
	 */
	public Object execute(IProvider provider);

}