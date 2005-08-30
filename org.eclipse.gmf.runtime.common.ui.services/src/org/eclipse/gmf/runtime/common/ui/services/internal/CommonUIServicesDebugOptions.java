/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.internal;

/**
 * A list of debug options for this plug-in.
 * 
 * @author khussey
 *  
 */
public final class CommonUIServicesDebugOptions {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 *  
	 */
	private CommonUIServicesDebugOptions() {
		/* private constructor */
	}

	/**
	 * General debug string
	 */
	public static final String DEBUG = CommonUIServicesPlugin.getPluginId()
		+ "/debug"; //$NON-NLS-1$

	/**
	 * Debug option for exceptions being caught
	 */
	public static final String EXCEPTIONS_CATCHING = DEBUG
		+ "/exceptions/catching"; //$NON-NLS-1$

	/**
	 * Debug option for exceptions being thrown
	 */
	public static final String EXCEPTIONS_THROWING = DEBUG
		+ "/exceptions/throwing"; //$NON-NLS-1$
}