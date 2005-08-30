/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.printing.internal;

/**
 * A list of debug options for this plug-in.
 * 
 * @author wdiu, mainly copied from khussey's CommonCoreDebugOptions
 *
 */
public final class CommonPrintingDebugOptions {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 * 
	 */
	private CommonPrintingDebugOptions() {
		/* private constructor */
	}

	/** Debug option. */
	public static final String DEBUG = PrintingPlugin.getPluginId() + "/debug"; //$NON-NLS-1$

	/** Debug option to trace exception catching. */
	public static final String EXCEPTIONS_CATCHING = DEBUG + "/exceptions/catching"; //$NON-NLS-1$
	
	/** Debug option to trace exception throwing. */
	public static final String EXCEPTIONS_THROWING = DEBUG + "/exceptions/throwing"; //$NON-NLS-1$

	/** Debug option to trace method entering. */
	public static final String METHODS_ENTERING = DEBUG + "/methods/entering"; //$NON-NLS-1$
	
	/** Debug option to trace method exiting. */
	public static final String METHODS_EXITING = DEBUG + "/methods/exiting"; //$NON-NLS-1$
}
