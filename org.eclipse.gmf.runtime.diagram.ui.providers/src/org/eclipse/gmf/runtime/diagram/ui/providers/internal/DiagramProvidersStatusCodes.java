/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

/**
 * A list of status codes for this plug-in.
 * 
 *  @author Yasser Lulu
 *
 */
public final class DiagramProvidersStatusCodes {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 * 
	 */
	private DiagramProvidersStatusCodes() {
		//static class: prevent instatiation
	}

	public static final int OK = 0;	
	
	/**
	 * Status code indicating that an error occurred with internationalization.
	 */
	public static final int L10N_FAILURE = 3;
	
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
