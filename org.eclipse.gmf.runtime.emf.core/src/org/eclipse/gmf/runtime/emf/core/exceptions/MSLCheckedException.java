/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.exceptions;

/**
 * All checked exceptions in the MSL are specializations of this exception type.
 * 
 * @author Christian W. Damus (cdamus)
 */
public class MSLCheckedException
	extends Exception {

	/**
	 * Initializes me without any details.
	 */
	public MSLCheckedException() {
		super();
	}

	/**
	 * Initializes me with a useful message.
	 * 
	 * @param message a message
	 */
	public MSLCheckedException(String message) {
		super(message);
	}

	/**
	 * Initializes me with a message and a cause.
	 * 
	 * @param message a message
	 * @param cause the cause of this exception
	 */
	public MSLCheckedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Initializes me with a cause.
	 * 
	 * @param cause the cause of this exception
	 */
	public MSLCheckedException(Throwable cause) {
		this(null == cause ? null
			: cause.toString(), cause);
	}
}