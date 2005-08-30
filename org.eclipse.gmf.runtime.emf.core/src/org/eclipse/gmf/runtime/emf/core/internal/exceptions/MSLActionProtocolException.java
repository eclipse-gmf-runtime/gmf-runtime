/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.exceptions;

import org.eclipse.gmf.runtime.emf.core.exceptions.MSLRuntimeException;

/**
 * Exception thrown when the action protocol is violated.
 * 
 * @author rafikj
 */
public class MSLActionProtocolException
	extends MSLRuntimeException {

	/**
	 * Initializes me without any details.
	 */
	public MSLActionProtocolException() {
		super();
	}

	/**
	 * Initializes me with a useful message.
	 * 
	 * @param message
	 *            a message
	 */
	public MSLActionProtocolException(String message) {
		super(message);
	}

	/**
	 * Initializes me with a cause.
	 * 
	 * @param cause
	 *            the cause of this exception.
	 */
	public MSLActionProtocolException(Throwable cause) {
		super(null == cause ? null
			: cause.toString(), cause);
	}
}