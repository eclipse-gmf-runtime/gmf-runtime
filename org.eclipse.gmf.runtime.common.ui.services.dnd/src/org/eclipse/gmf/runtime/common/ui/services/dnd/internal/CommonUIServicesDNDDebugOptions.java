/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.dnd.internal;

/**
 * A list of debug options for this plug-in.
 * 
 * @author khussey
 *  
 */
public final class CommonUIServicesDNDDebugOptions {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 *  
	 */
	private CommonUIServicesDNDDebugOptions() {
		/* private constructor */
	}

	/**
	 * General debug string
	 */
	public static final String DEBUG = CommonUIServicesDNDPlugin.getPluginId()
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

	/**
	 * Debug option for dnd
	 */
	public static final String DND = DEBUG + "/dnd/tracing"; //$NON-NLS-1$	

	/**
	 * Service configuration errors, such as invalid xml element in xml config
	 * file.
	 */
	public static final String SERVICES_CONFIG = DEBUG + "/services/config"; //$NON-NLS-1$
}