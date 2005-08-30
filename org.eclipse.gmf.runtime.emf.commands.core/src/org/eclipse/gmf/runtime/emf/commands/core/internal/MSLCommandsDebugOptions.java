/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.commands.core.internal;


/**
 * A list of debug options for this plug-in.
 * 
 * @author Christian W. Damus
 */
public final class MSLCommandsDebugOptions {

	private MSLCommandsDebugOptions() {
		// private
	}

	/** Debug option. */
	public static final String DEBUG = MSLCommandsPlugin.getPluginId() + "/debug"; //$NON-NLS-1$

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
	
	/** Debug option to trace execution of model operations. */
	public static final String MODEL_OPERATIONS = DEBUG + "/model/operations"; //$NON-NLS-1$
}