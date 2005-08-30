/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.core.internal;

/**
 * A list of debug options for this plug-in.
 * 
 * @author khussey
 *
 */
public final class CommonCoreDebugOptions {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 * 
	 */
	private CommonCoreDebugOptions() {
		/* private constructor */
	}

	/** Debug option. */
	public static final String DEBUG = CommonCorePlugin.getPluginId() + "/debug"; //$NON-NLS-1$

	/** Debug option for tracing exception catching. */
	public static final String EXCEPTIONS_CATCHING = DEBUG + "/exceptions/catching"; //$NON-NLS-1$
	
	/** Debug option for tracing exception throwing. */
	public static final String EXCEPTIONS_THROWING = DEBUG + "/exceptions/throwing"; //$NON-NLS-1$

	/** Debug option for tracing method entering. */
	public static final String METHODS_ENTERING = DEBUG + "/methods/entering"; //$NON-NLS-1$
	
	/** Debug option for tracing method exiting. */
	public static final String METHODS_EXITING = DEBUG + "/methods/exiting"; //$NON-NLS-1$

	/** Debug option for tracing command admin. */
	public static final String COMMANDS_ADMIN = DEBUG + "/commands/admin"; //$NON-NLS-1$
	
	/** Debug option for trasing command execution. */
	public static final String COMMANDS_EXECUTE = DEBUG + "/commands/execute"; //$NON-NLS-1$
	
	/** Debug option for tracing command undo. */
	public static final String COMMANDS_UNDO = DEBUG + "/commands/undo"; //$NON-NLS-1$
	
	/** Debug option for tracing command redo. */
	public static final String COMMANDS_REDO = DEBUG + "/commands/redo"; //$NON-NLS-1$

	/** Debug option for tracing service configuration. */
	public static final String SERVICES_CONFIG = DEBUG + "/services/config"; //$NON-NLS-1$
	
	/** Debug option for tracing service activation. */
	public static final String SERVICES_ACTIVATE = DEBUG + "/services/activate"; //$NON-NLS-1$
	
	/** Debug option for tracing service execution. */
	public static final String SERVICES_EXECUTE = DEBUG + "/services/execute"; //$NON-NLS-1$
	
	/** Debug option for tracing on demand loading. */
    public static final String ONDEMANEDLOADING = DEBUG + "/plugin/ondemand/loading";//$NON-NLS-1$

}
