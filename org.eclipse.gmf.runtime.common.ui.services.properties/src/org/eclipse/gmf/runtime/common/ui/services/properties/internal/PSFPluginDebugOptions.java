/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004,2005.  All Rights Reserved.               |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.properties.internal;


/**
 * @canBeSeenBy org.eclipse.gmf.runtime.common.ui.services.properties.*
 * @author nbalaba
 */
public class PSFPluginDebugOptions {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 *  
	 */
	private PSFPluginDebugOptions() {
		/* private constructor */
	}

	/** Debug option. */
    public static final String DEBUG = PSFCommonUIPlugin.getPluginId() + "/debug"; //$NON-NLS-1$
	
    /** Debug option for tracing exception catching. */
	public static final String EXCEPTIONS_CATCHING = DEBUG
			+ "/exceptions/catching"; //$NON-NLS-1$
	
	/** Debug option for tracing exception throwing. */
	public static final String EXCEPTIONS_THROWING = DEBUG + "/exceptions/throwing"; //$NON-NLS-1$

}