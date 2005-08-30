/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.render.internal;

/**
 * A list of status codes for this plug-in.
 * 
 * @author cmahoney
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.render.*
 *  
 */
public final class DiagramUIRenderStatusCodes {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 *  
	 */
	private DiagramUIRenderStatusCodes() {
		// empty constructor
	}

	public static final int OK = 0;

	
	/**
	 * Status code indicating that an error occurred with a resource, such as
	 * loading an image file.
	 * Set to 5 to be consistent with CommonUIStatusCodes.
	 */
	public static final int RESOURCE_FAILURE = 5;

}