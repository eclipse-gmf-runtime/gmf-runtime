/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.emf.core.resources;

import org.eclipse.gmf.runtime.emf.core.exceptions.MSLCheckedException;


/**
 * Exception indicating failure to refactor a model element into its parent
 * resource in a logical resource.
 *
 * @author Christian W. Damus (cdamus)
 */
public class CannotAbsorbException
	extends MSLCheckedException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Initializes me with a localized, user-friendly message.
	 * 
	 * @param message the message
	 */
	public CannotAbsorbException(String message) {
		super(message);
	}

	/**
	 * Initializes me with a localized, user-friendly message and an
	 * exception that is the cause of the failure to absorb.
	 * 
	 * @param message the message
	 * @param cause the causing exception
	 */
	public CannotAbsorbException(String message, Throwable cause) {
		super(message, cause);
	}

}
