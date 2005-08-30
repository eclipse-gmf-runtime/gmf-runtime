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
 * A list of status codes for this plug-in.
 * 
 * @author khussey
 *  
 */
public final class CommonUIServicesDNDStatusCodes {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 *  
	 */
	private CommonUIServicesDNDStatusCodes() {
		/* private constructor */
	}

	/**
	 * Status code indicating that everything is OK.
	 */
	public static final int OK = 0;

	/**
	 * Status code indicating that an error occurred with a service.
	 */
	public static final int SERVICE_FAILURE = 1;

	/**
	 * Status code indicating that an error occurred with the drag action.
	 */
	public static final int DRAG_ACTION_FAILURE = 2;

	/**
	 * Status code indicating that an error occurred with the drop action.
	 */
	public static final int DROP_ACTION_FAILURE = 3;

	/**
	 * Status code warning that an exception was ignored
	 */
	public static final int IGNORED_EXCEPTION_WARNING = 4;
}