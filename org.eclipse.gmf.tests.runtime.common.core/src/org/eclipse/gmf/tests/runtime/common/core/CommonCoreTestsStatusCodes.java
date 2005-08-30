/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.common.core;

/**
 * A list of status codes for this plug-in.
 * 
 * @author khussey
 *
 */
public final class CommonCoreTestsStatusCodes {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 * 
	 */
	private CommonCoreTestsStatusCodes() {
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
	 * Status code indicating that an error occurred with internationalization.
	 */
	public static final int L10N_FAILURE = 3;

	/**
	 * Status code indicating that an error occurred with a command.
	 */
	public static final int COMMAND_FAILURE = 4;
	
	/**
	 * Status code indicating that an error occurred with a service.
	 */
	public static final int SERVICE_FAILURE = 5;
	
	/**
	 * Status code indicating that an operation was cancelled.
	 */
	public static final int CANCELLED = 6;
	
	/**
	 * Status code indicating that an operation was rolled back due to live
	 * validation errors.
	 */
	public static final int VALIDATION_FAILURE = 7;
	
}
