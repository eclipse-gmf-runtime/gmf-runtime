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

package org.eclipse.gmf.runtime.emf.core;

import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;

/**
 * An extension of the Eclipse core status interface that provides additional
 * information about the violation of a constraint during the validation of a model.
 * <p>
 * In general, validation status objects will not indicate an
 * {@link IStatus#isOK() OK}status, because they describe constraint
 * violations. Moreover, validation status objects are never multi-statuses.
 * </p>
 * <p>
 * API clients should <b>not </b> implement this interface.
 * </p>
 */
public interface IValidationStatus
	extends IStatus {

	/**
	 * Obtains the model element on which a constraint was evaluated, that was
	 * violated (or not) according to my {@link IStatus#getSeverity() severity}.
	 * In the case of a constraint violation, my
	 * {@link IStatus#getMessage() message}indicates the nature of the problem.
	 * 
	 * @return the target of a constraint evaluation
	 */
	EObject getTarget();

	/**
	 * In the case of a constraint violation, obtains the set of related
	 * elements that contributed to the problem. This set always includes,
	 * minimally, the {@link #getTarget() target}of the constraint evaluation.
	 * 
	 * @return the set of elements that caused the problem, or an empty set in
	 *         case of a successful constraint evaluation
	 */
	Set getRelatedObjects();

	/**
	 * The <code>IValidationStatus</code> interface redefines this method to
	 * always return <code>false</code>.
	 * 
	 * @return <code>false</code>
	 */
	boolean isMultiStatus();

	/**
	 * The <code>IValidationStatus</code> interface redefines this method to
	 * always return an empty array of child statuses.
	 * 
	 * @return an empty array of statuses
	 */
	IStatus[] getChildren();

	/**
	 * The rule property defined in the marker resolution extension
	 * 
	 * @return The id for the marker resolution extension
	 */
	String getRuleID();
}