/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal;

/**
 * A list of status codes for this plug-in.
 * 
 * @author khussey
 *
 */
public final class GefStatusCodes {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 * 
	 */
	private GefStatusCodes() {
		// empty constructor
	}

	public static final int OK = 0;

    /**
     * Status code indicating that an error occurred with the drop action.
     */
    public static final int DROP_ACTION_FAILURE = 1;
    
    /**
     * Status code indicating that an error occurred while generating an
     * SVG document file
     */
    public static final int SVG_GENERATION_FAILURE = 2;
    
    /**
     * Status code warning that an exception was ignored.
     * Set to 9 to be consistent with CommonUIStatusCodes.
     */
    public static final int IGNORED_EXCEPTION_WARNING = 9;
}
