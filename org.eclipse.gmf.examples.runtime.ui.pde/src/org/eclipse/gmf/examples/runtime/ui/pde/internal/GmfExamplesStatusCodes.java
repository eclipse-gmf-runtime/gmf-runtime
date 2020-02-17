/******************************************************************************
 * Copyright (c) 2002, 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.ui.pde.internal;

/**
 * A list of status codes for this plug-in.
 * 
 * @author khussey
 *  
 */
public final class GmfExamplesStatusCodes {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 *  
	 */
	private GmfExamplesStatusCodes() {
		//No-op
	}

	/**
	 * Status code indicating that an error occurred with internationalization.
	 */
	public static final int L10N_FAILURE = 3;
	
	/**
	 * Status code indicating that an error occurred with a service.
	 */
	public static final int SERVICE_FAILURE = 5;

	/**
	 * Error caused by an exception
	 */
	public static final int EXCEPTION_OCCURED = 10;

}
