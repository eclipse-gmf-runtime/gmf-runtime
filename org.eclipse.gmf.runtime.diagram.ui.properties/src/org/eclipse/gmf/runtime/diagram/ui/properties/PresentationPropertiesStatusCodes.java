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
