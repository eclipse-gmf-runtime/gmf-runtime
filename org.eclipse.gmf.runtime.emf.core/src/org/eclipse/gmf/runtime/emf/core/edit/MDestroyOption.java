/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.edit;

/**
 * Enumeration that describes options that can be passed to destroy.
 * 
 * @author rafikj
 */
public final class MDestroyOption {

	/**
	 * No events.
	 */
	public static final int NO_EVENTS = 1;

	/**
	 * Keep incoming and outgoing references.
	 */
	public static final int KEEP_REFERENCES = 2;

	/**
	 * Make destroyed object a proxy object.
	 */
	public static final int MAKE_PROXY = 4;

	private MDestroyOption() {
		// private
	}
}