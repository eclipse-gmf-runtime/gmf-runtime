/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.emf.core.resources;

import org.eclipse.gmf.runtime.emf.core.exceptions.MSLCheckedException;


/**
 * Exception indicating failure to refactor a model element into its parent
 * resource in a logical resource.
 *
 * @author Christian W. Damus (cdamus)
 * 
 * @deprecated Use the cross-resource containment support provided by EMF,
 *     instead, by defining containment features that are capable of storing
 *     proxies.
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
