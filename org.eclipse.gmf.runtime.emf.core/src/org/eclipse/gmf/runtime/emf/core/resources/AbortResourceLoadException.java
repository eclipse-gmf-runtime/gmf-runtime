/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.resources;

/**
 * This exception is intended to be thrown during Resource load when an
 * error occurs. This RuntimeException wrapper is required in order to
 * abort loading at any time.
 * 
 * @author Christian Vogt (cvogt)
 */
public class AbortResourceLoadException extends RuntimeException {

	private static final long serialVersionUID = -5621491416138595586L;

	/**
	 * Initializes me without any details.
	 */
	public AbortResourceLoadException() {
		super();
	}

	/**
	 * Initializes me with a useful message.
	 * 
	 * @param message
	 *            a message
	 */
	public AbortResourceLoadException(String message) {
		super(message);
	}

	/**
	 * Initializes me with a cause.
	 * 
	 * @param cause
	 *            the cause of this exception.
	 */
	public AbortResourceLoadException(Throwable cause) {
		super(null == cause ? null
			: cause.toString(), cause);
	}
}
