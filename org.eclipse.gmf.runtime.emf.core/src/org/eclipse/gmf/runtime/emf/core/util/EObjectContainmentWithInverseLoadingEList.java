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


package org.eclipse.gmf.runtime.emf.core.util;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;


/**
 * Specialized containment reference list (with inverse) that automatically
 * loads sub-unit objects when they are retrieved from the list.
 *
 * @author Christian W. Damus (cdamus)
 * 
 * @deprecated Use the cross-resource containment support provided by EMF,
 *     instead, by defining containment features that are capable of storing
 *     proxies.
 */
public class EObjectContainmentWithInverseLoadingEList
	extends EObjectContainmentWithInverseEList {

	private static final long serialVersionUID = 1L;

	/**
	 * Initializes me with my contained type, owning object, feature ID, and
	 * opposite feature ID.
	 * 
	 * @param dataClass my contained type
	 * @param owner the object that owns me
	 * @param featureID the ID of the feature that I implement
	 * @param inverseFeatureID the ID of my opposite
	 */
	public EObjectContainmentWithInverseLoadingEList(Class dataClass,
			InternalEObject owner, int featureID, int inverseFeatureID) {
		super(dataClass, owner, featureID, inverseFeatureID);
	}
}
