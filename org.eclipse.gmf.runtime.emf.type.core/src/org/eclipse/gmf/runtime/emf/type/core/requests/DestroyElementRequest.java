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

import org.eclipse.emf.ecore.EObject;

/**
 * Request to destroy a model element.
 * 
 * @author ldamus
 */
public class DestroyElementRequest
	extends DestroyRequest {

	/**
	 * The element to destroy.
	 */
	private EObject elementToDestroy;

	/**
	 * Constructs a new request to destroy a model element.
	 * 
	 * @param confirmationRequired
	 *            <code>true</code> if the user should be prompted to confirm
	 *            the element deletion, <code>false</code> otherwise.
	 */
	public DestroyElementRequest(boolean confirmationRequired) {

		this(null, confirmationRequired);
	}

	/**
	 * Constructs a new request to destroy a model element.
	 * 
	 * @param elementToDestroy
	 *            the element to be destroyed
	 * @param confirmationRequired
	 *            <code>true</code> if the user should be prompted to confirm
	 *            the element deletion, <code>false</code> otherwise.
	 */
	public DestroyElementRequest(EObject elementToDestroy,
			boolean confirmationRequired) {

		super(confirmationRequired);
		this.elementToDestroy = elementToDestroy;
	}

	/**
	 * Gets the element to be destroyed.
	 * 
	 * @return the element to be destroyed
	 */
	public EObject getElementToDestroy() {
		return elementToDestroy;
	}

	/**
	 * Sets the element to be destroyed.
	 * 
	 * @param elementToDestroy
	 *            the element to be destroyed
	 */
	public void setElementToDestroy(EObject elementToDestroy) {
		this.elementToDestroy = elementToDestroy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.DestroyRequest#getContainer()
	 */
	public EObject getContainer() {
		if (getElementToDestroy() != null) {
			return getElementToDestroy().eContainer();
		}
		return null;
	}

}