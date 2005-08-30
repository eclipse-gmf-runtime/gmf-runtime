/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.properties;

/**
 * A list of status codes for this plug-in.
 * 
 * @author nbalaba
 */
public class PresentationPropertiesStatusCodes {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 * 
	 */
	private PresentationPropertiesStatusCodes() {
		// Private constructor.
	}

	/** Status code indicating that an operation was performed succesfully. */
	public static final int OK = 0;

	/** Status code indicating that an operation was cancelled. */
	public static final int CANCELLED = 6;
	
	/** Status code indicating that operation ignored exception. */
	public static final int IGNORED_EXCEPTION_WARNING = 10;
}
