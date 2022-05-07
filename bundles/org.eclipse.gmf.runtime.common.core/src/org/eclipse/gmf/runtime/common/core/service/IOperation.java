/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
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