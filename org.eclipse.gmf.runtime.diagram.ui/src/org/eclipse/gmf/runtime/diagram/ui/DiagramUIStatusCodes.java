/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui;

/**
 * A list of status codes for the Diagram UI plugin
 * 
 * @author khussey
 *
 */
public final class DiagramUIStatusCodes {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 * 
	 */
	private DiagramUIStatusCodes() {
		//static class: prevent instatiation
	}

	/**
	 * Status code indicating that no errors occurred
	 */
	public static final int OK = 0;

    /**
     * Status code indicating that an error occurred with the drop action.
     */
    public static final int DROP_ACTION_FAILURE = 1;    

    /**
	 * Status code indicating that an error occurred with a command.
	 */
	public static final int COMMAND_FAILURE = 4;
	
	/**
	 * Status code indicating that an error occurred with a resource, such as
	 * loading an image file.
	 * Set to 5 to be consistent with CommonUIStatusCodes.
	 */
	public static final int RESOURCE_FAILURE = 5;

	/**
	 * Status code indicating that a UI error occurred.
	 * Set to 6 to be consistent with CommonUIStatusCodes.
	 */
	public static final int GENERAL_UI_FAILURE = 6;
	
	/**
	 * Status code warning that an exception was ignored.
	 * Set to 9 to be consistent with CommonUIStatusCodes.
	 */
	public static final int SERVICE_FAILURE = 7;

	/**
	 * Status code warning that an exception was ignored.
	 * Set to 9 to be consistent with CommonUIStatusCodes.
	 */
	public static final int IGNORED_EXCEPTION_WARNING = 9;
	


}
