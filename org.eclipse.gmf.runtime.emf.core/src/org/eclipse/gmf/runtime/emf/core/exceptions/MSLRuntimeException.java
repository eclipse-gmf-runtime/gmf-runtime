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

package org.eclipse.gmf.runtime.emf.core.exceptions;

/**
 * All runtime exceptions in the MSL are specializations of this exception type.
 * 
 * @author rafikj
 */
public class MSLRuntimeException
	extends RuntimeException {

	/**
	 * Initializes me without any details.
	 */
	public MSLRuntimeException() {
		super();
	}

	/**
	 * Initializes me with a useful message.
	 * 
	 * @param message
	 *            a message
	 */
	public MSLRuntimeException(String message) {
		super(message);
	}

	/**
	 * Initializes me with a message and a cause.
	 * 
	 * @param message
	 *            a message
	 * @param cause
	 *            the cause of this exception
	 */
	public MSLRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Initializes me with a cause.
	 * 
	 * @param cause
	 *            the cause of this exception
	 */
	public MSLRuntimeException(Throwable cause) {
		this(null == cause ? null
			: cause.toString(), cause);
	}
}