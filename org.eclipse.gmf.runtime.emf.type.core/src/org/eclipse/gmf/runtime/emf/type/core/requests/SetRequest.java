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

package org.eclipse.gmf.runtime.emf.type.core.requests;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Request to set the value of a structural feature in a model element.
 * 
 * @author ldamus
 */
public class SetRequest
	extends AbstractEditCommandRequest {

	/**
	 * The structural feature whose value will be set.
	 */
	private final EStructuralFeature feature;

	/**
	 * The owner of the structural feature.
	 */
	private final EObject elementToEdit;

	/**
	 * The new value of the structural feature.
	 */
	private final Object value;

	/**
	 * Constructs a new request to set the value of a structural feature in a
	 * model element.
	 * 
	 * @param elementToEdit
	 *            the owner of the structural feature
	 * @param feature
	 *            the structural feature whose value is to be set
	 * @param value
	 *            the new value
	 */
	public SetRequest(EObject elementToEdit, EStructuralFeature feature,
			Object value) {

		super();
		this.elementToEdit = elementToEdit;
		this.feature = feature;
		this.value = value;
	}

	/**
	 * Gets the structural feature.
	 * 
	 * @return the structural feature
	 */
	public EStructuralFeature getFeature() {
		return feature;
	}

	/**
	 * Gets the new value.
	 * 
	 * @return the new value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Gets the owner of the structural feature.
	 * 
	 * @return the owner of the structural feature
	 */
	public EObject getElementToEdit() {
		return elementToEdit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest#getElementsToEdit()
	 */
	public List getElementsToEdit() {
		return Collections.singletonList(elementToEdit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest#getEditHelperContext()
	 */
	public Object getEditHelperContext() {
		return elementToEdit;
	}

}