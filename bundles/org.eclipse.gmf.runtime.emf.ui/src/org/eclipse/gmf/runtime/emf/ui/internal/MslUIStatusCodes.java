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

package org.eclipse.gmf.runtime.emf.ui.internal;

/**
 * A list of status codes for this plug-in.
 * 
 * @author khussey
 *
 */
public final class MslUIStatusCodes {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 * 
	 */
	private MslUIStatusCodes() {
		// Private constructor.
	}

	public static final int OK = 0;

	public static final int IGNORED_EXCEPTION_WARNING = 10;
}
