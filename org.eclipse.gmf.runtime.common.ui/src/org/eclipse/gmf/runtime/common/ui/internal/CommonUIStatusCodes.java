/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.internal;

/**
 * A list of status codes for this plug-in.
 * 
 * @author khussey
 *
 */
public final class CommonUIStatusCodes {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 * 
	 */
	private CommonUIStatusCodes() {
		/* private constructor */
	}

	/**
	 * Status code indicating that everything is OK.
	 */
	public static final int OK = 0;

	/**
	 * Status code indicating that an error occurred during plug-in start-up.
	 */
	public static final int PLUGIN_STARTUP_FAILURE = 1;

	/**
	 * Status code indicating that an error occurred during plug-in shut-down.
	 */
	public static final int PLUGIN_SHUTDOWN_FAILURE = 2;

	/**
	 * Status code indicating that an error occurred with an action.
	 */
	public static final int ACTION_FAILURE = 3;

	/**
	 * Status code indicating that an error occurred with a service.
	 */
	public static final int SERVICE_FAILURE = 4;
    
    /**
     * Status code indicating that an error occurred with a resource, such
     * as loading an image file.
     */
    public static final int RESOURCE_FAILURE = 5;
    
    /**
     * Status code indicating that a UI error occurred.
     */
    public static final int GENERAL_UI_FAILURE = 6;

	/**
	 * Status code warning that an exception was ignored
	 */
	public static final int IGNORED_EXCEPTION_WARNING = 7;
}
