/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.plugin;



/**
 * A list of debug options for this plug-in.
 * 
 * @author rafikj
 */
public final class MSLDebugOptions {

	private MSLDebugOptions() {
		// private
	}

	public static final String DEBUG = MSLPlugin.getPluginId() + "/debug"; //$NON-NLS-1$

	public static final String EXCEPTIONS_CATCHING = DEBUG
		+ "/exceptions/catching"; //$NON-NLS-1$

	public static final String EXCEPTIONS_THROWING = DEBUG
		+ "/exceptions/throwing"; //$NON-NLS-1$

	public static final String METHODS_ENTERING = DEBUG + "/methods/entering"; //$NON-NLS-1$

	public static final String METHODS_EXITING = DEBUG + "/methods/exiting"; //$NON-NLS-1$
	
	public static final String MODEL_OPERATIONS = DEBUG + "/model/operations"; //$NON-NLS-1$

	public static final String EVENTS = DEBUG + "/events/tracing"; //$NON-NLS-1$	
	
	public static final String RESOURCES = DEBUG + "/resources"; //$NON-NLS-1$
}