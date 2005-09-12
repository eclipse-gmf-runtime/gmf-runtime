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

package org.eclipse.gmf.runtime.emf.core.internal.util;

import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.emf.core.IValidationStatus;
import org.eclipse.emf.validation.model.IConstraintStatus;


/**
 * An extremely straightforward adapter of the {@link IConstraintStatus}
 * interface to the {@link IValidationStatus} interface.
 *
 * @author Christian W. Damus (cdamus)
 */
public final class ConstraintStatusAdapter implements IValidationStatus {
	private final IConstraintStatus adaptee;
	
	ConstraintStatusAdapter(IConstraintStatus status) {
		adaptee = status;
	}
	
	/**
	 * Delegates to the adapted status.
	 */
	public IStatus[] getChildren() {
		return adaptee.getChildren();
	}

	/**
	 * Delegates to the adapted status.
	 */
	public int getCode() {
		return adaptee.getCode();
	}

	/**
	 * Delegates to the adapted status.
	 */
	public Throwable getException() {
		return adaptee.getException();
	}

	/**
	 * Delegates to the adapted status.
	 */
	public String getMessage() {
		return adaptee.getMessage();
	}

	/**
	 * Delegates to the adapted status.
	 */
	public String getPlugin() {
		return adaptee.getPlugin();
	}

	/**
	 * Delegates to the adapted status.
	 */
	public Set getRelatedObjects() {
		return adaptee.getResultLocus();
	}

	/**
	 * Delegates to the adapted status.
	 */
	public int getSeverity() {
		return adaptee.getSeverity();
	}

	/**
	 * Delegates to the adapted status.
	 */
	public EObject getTarget() {
		return adaptee.getTarget();
	}

	/**
	 * Delegates to the adapted status.
	 */
	public boolean isMultiStatus() {
		return adaptee.isMultiStatus();
	}

	/**
	 * Delegates to the adapted status.
	 */
	public boolean isOK() {
		return adaptee.isOK();
	}

	/**
	 * Delegates to the adapted status.
	 */
	public boolean matches(int severityMask) {
		return adaptee.matches(severityMask);
	}

	/**
	 * Delegates to the adapted status.
	 */
	public String toString() {
		return adaptee.toString();
	}
	
	/**
	 * The rule property defined in the marker resolution extension
	 * 
	 * @return The id for the marker resolution extension
	 */
	public String getRuleID() {
		return adaptee.getConstraint().getDescriptor().getId();
	}

	
	/**
	 * Wraps a model validation service status result in the skin of an
	 * MSL-style status.
	 * 
	 * @param status the model validation service status
	 * @return the MSL validation status
	 */
	public static IStatus wrapStatus(IStatus status) {
		if (status instanceof IConstraintStatus) {
			IConstraintStatus constraintStatus = (IConstraintStatus)status;
			
			return new ConstraintStatusAdapter(constraintStatus);
		} else if (!status.isMultiStatus()) {
			// if it's not a multi-status and it's not a constraint status, then
			//   it's a plain Eclipse Status object.  No conversion necessary
			return status;
		}
		
		IStatus[] children = status.getChildren();
		IStatus[] converted = new IStatus[children.length];
		
		for (int i = 0; i < children.length; i++) {
			converted[i] = wrapStatus(children[i]);
		}
		
		return new MultiStatus(
				status.getPlugin(),
				status.getCode(),
				converted,
				status.getMessage(),
				status.getException());
	}
}
