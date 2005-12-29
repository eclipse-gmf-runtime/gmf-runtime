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

import org.eclipse.core.runtime.IStatus;

/**
 * A checked exception indicating that a model write action has been abandoned
 * because of the violation of one or more live constraints.
 *
 * @author Christian W. Damus (cdamus)
 * 
 * @see org.eclipse.gmf.runtime.emf.core.IValidationStatus
 */
public class MSLActionAbandonedException
	extends MSLCheckedException {

	private static final long serialVersionUID = 6054386644313187147L;
	
	private final IStatus status;

	/**
	 * Initializes me with the status object indicating the reason(s) for
	 * abandoning the model write action.
	 * 
	 * @param status the status that caused the action abandonment.  If this
	 *     is a multi-status, then its children are {@link org.eclipse.gmf.runtime.emf.core.IValidationStatus}es
	 *     having details of the particular constraints that failed.  Otherwise,
	 *     it may itself be an {@link org.eclipse.gmf.runtime.emf.core.IValidationStatus}
	 */
	public MSLActionAbandonedException(IStatus status) {
		this(status.getMessage(), status);
	}

	/**
	 * Initializes me with the status object indicating the reason(s) for
	 * abandoning the model write action, in addition to a message.
	 * 
	 * @param message a descriptive message
	 * @param status the status that caused the action abandonment
	 */
	public MSLActionAbandonedException(String message, IStatus status) {
		super(message);
		this.status = status;
	}

	/**
	 * Obtains the status object describing the reason(s) for the abandonment
	 * of the action.  The status is a multi-status, in which each child
	 * describes the violation of a live constraint.
	 * 
	 * @return the status object, which commonly is a multi-status whose
	 *        children are {@link org.eclipse.gmf.runtime.emf.core.IValidationStatus}es.  Otherwise,
	 *        it may itself be an {@link org.eclipse.gmf.runtime.emf.core.IValidationStatus}
	 */
	public final IStatus getStatus() {
		return status;
	}
}