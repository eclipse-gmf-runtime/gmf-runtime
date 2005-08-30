/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.render.internal;

/**
 * A list of status codes for this plug-in.
 * 
 * @author khussey
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 *
 */
public final class Draw2dRenderStatusCodes {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 * 
	 */
	private Draw2dRenderStatusCodes() {
		// empty constructor
	}

	public static final int OK = 0;

    /**
     * Status code indicating that an error occurred while generating an
     * SVG document file
     */
    public static final int SVG_GENERATION_FAILURE = 2;
}
