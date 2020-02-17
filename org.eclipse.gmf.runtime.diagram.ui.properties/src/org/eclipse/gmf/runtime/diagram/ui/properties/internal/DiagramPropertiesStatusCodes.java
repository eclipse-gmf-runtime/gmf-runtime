/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.properties.internal;

/**
 * A list of status codes for this plug-in.
 * 
 * @author nbalaba
 */
public class DiagramPropertiesStatusCodes {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 * 
	 */
	private DiagramPropertiesStatusCodes() {
		// Private constructor.
	}

	/** Status code indicating that an operation was performed succesfully. */
	public static final int OK = 0;

	/** Status code indicating that an operation was cancelled. */
	public static final int CANCELLED = 6;

	/** Status code indicating that operation ignored exception. */
	public static final int IGNORED_EXCEPTION_WARNING = 10;
}
