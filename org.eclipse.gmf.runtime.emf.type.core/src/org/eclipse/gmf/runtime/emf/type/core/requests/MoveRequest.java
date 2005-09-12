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
import org.eclipse.emf.ecore.EReference;

import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.ResourceManager;

/**
 * Request to move a model element from one container to another or from one
 * reference feature to another.
 * 
 * @author ldamus
 */
public class MoveRequest
	extends AbstractEditCommandRequest {

	/**
	 * The element to be moved.
	 */
	private EObject elementToMove;

	/**
	 * The new container for the element to be moved.
	 */
	private EObject targetContainer;

	/**
	 * The new reference feature into which the element should be moved.
	 */
	private EReference targetFeature;

	/**
	 * Constructs a new request to move a model element from one container to
	 * another.
	 * 
	 * @param targetContainer
	 *            the target container
	 * @param targetFeature
	 *            the target feature
	 * @param elementToMove
	 *            the element to be moved
	 */
	public MoveRequest(EObject targetContainer, EReference targetFeature,
			EObject elementToMove) {

		super();
		this.elementToMove = elementToMove;
		this.targetContainer = targetContainer;
		this.targetFeature = targetFeature;
	}

	/**
	 * Constructs a new request to move a model element from one container to
	 * another.
	 * 
	 * @param targetContainer
	 *            the target container
	 * @param elementToMove
	 *            the element to be moved.
	 */
	public MoveRequest(EObject targetContainer, EObject elementToMove) {

		this(targetContainer, null, elementToMove);
	}

	/**
	 * Gets the element to be moved.
	 * 
	 * @return the element to be moved
	 */
	public EObject getElementToMove() {
		return elementToMove;
	}

	/**
	 * Sets the container into which the element will be moved.
	 * 
	 * @param targetContainer
	 *            the target container
	 */
	public void setTargetContainer(EObject targetContainer) {
		this.targetContainer = targetContainer;
	}

	/**
	 * Gets the container into which the element will be moved.
	 * 
	 * @return the container into which the element will be moved
	 */
	public EObject getTargetContainer() {
		return targetContainer;
	}

	/**
	 * Sets the reference feature into which the element should be moved.
	 * 
	 * @param targetFeature
	 *            the target feature
	 */
	public void setTargetFeature(EReference targetFeature) {
		this.targetFeature = targetFeature;
	}

	/**
	 * Gets the reference feature into which the element should be moved.
	 * 
	 * @return the target feature
	 */
	public EReference getTargetFeature() {
		return targetFeature;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest#getElementsToEdit()
	 */
	public List getElementsToEdit() {

		if (targetContainer != null) {
			return Collections.singletonList(targetContainer);
		}

		return super.getElementsToEdit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditCommandRequest#getDefaultLabel()
	 */
	protected String getDefaultLabel() {
		return ResourceManager.getInstance().getString("Request.Label.Move"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest#getEditHelperContext()
	 */
	public Object getEditHelperContext() {
		return targetContainer;
	}

}