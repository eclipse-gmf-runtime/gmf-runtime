/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.render.internal;

/**
 * A list of status codes for this plug-in.
 * 
 * @author cmahoney
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
	
	/**
	 * Status code warning that an exception was ignored.
	 * Set to 9 to be consistent with CommonUIStatusCodes.
	 */
	public static final int IGNORED_EXCEPTION_WARNING = 9;

}
