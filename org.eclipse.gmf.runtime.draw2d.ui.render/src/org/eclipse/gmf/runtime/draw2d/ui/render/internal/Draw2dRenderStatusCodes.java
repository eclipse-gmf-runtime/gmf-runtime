/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
