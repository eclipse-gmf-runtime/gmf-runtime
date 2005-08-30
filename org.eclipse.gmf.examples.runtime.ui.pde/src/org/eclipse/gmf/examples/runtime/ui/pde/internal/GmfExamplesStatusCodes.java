/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
