/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.         	       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.properties.internal;

/**
 * A list of debug options for this plug-in.
 *  
 * @author nbalaba
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.properties.*
 */
public class PresentationPropertiesDebugOptions {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 * 
	 */
	private PresentationPropertiesDebugOptions() {
		// Private constructor.
	}

	/** Debug option. */
	public static final String DEBUG = PresentationPropertiesPlugin
		.getPluginId()
		+ "/debug"; //$NON-NLS-1$

	/** Debug option to trace exception catching. */
	public static final String EXCEPTIONS_CATCHING = DEBUG
		+ "/exceptions/catching"; //$NON-NLS-1$

	/** Debug option to trace exception throwing. */
	public static final String EXCEPTIONS_THROWING = DEBUG
		+ "/exceptions/throwing"; //$NON-NLS-1$

	/** Debug option to trace method entering. */
	public static final String METHODS_ENTERING = DEBUG + "/methods/entering"; //$NON-NLS-1$

	/** Debug option to trace method exiting. */
	public static final String METHODS_EXITING = DEBUG + "/methods/exiting"; //$NON-NLS-1$

}