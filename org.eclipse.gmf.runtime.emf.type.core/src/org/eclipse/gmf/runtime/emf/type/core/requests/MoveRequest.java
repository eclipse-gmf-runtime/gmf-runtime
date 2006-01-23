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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.EMFTypeCoreMessages;

/**
 * Request to move a collections of model elements from one location to another.
 * The request can specify the target features that should be used to contain
 * each of the elements being moved.
 * <P>
 * If the target feature is not specified for a given element being moved, then
 * a default feature is found in the target according to the following rules:
 * <UL>
 * <LI>If the feature fomerly containing the moved element exists in the target
 * element, it will be used.</LI>
 * <LI>Otherwise, the first feature in the target that can contain the moved
 * element will be used.</LI>
 * </UL>
 * 
 * @author ldamus
 */
public class MoveRequest extends AbstractEditCommandRequest {

	/**
	 * The map of <code>EObject</code>s to be moved. Keyed on
	 * <code>EObject</code>. Each value is the <code>EReference</code>
	 * feature in the target element into which the element should be moved.
	 * <P>
	 * If the feature is not specified for a given element, then a default
	 * feature is found in the target.
	 */
	private final Map elementsToMove;

	/**
	 * The new container for the element to be moved.
	 */
	private EObject targetContainer;

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
		this.targetContainer = targetContainer;
		this.elementsToMove = new HashMap();
		elementsToMove.put(elementToMove, targetFeature);
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
	 * Constructs a new request to move a collection of model element into a new
	 * container. The features in the target used to contain the moved elements
	 * will be derived as follows:
	 * <UL>
	 * <LI>If the feature fomerly containing the moved element exists in the
	 * target element, it will be used.</LI>
	 * <LI>Otherwise, the first feature in the target that can contain the
	 * moved element will be used.</LI>
	 * </UL>
	 * 
	 * @param targetContainer
	 *            the target container
	 * @param elementsToMove
	 *            the list of <code>EObjects</code> to be moved.
	 */
	public MoveRequest(EObject targetContainer, List elementsToMove) {

		super();
		this.targetContainer = targetContainer;
		this.elementsToMove = new HashMap();

		for (Iterator i = elementsToMove.iterator(); i.hasNext();) {
			this.elementsToMove.put(i.next(), null);
		}
	}

	/**
	 * Constructs a new request to move a collection of model element into
	 * specific features of a new container.
	 * 
	 * @param targetContainer
	 *            the target container
	 * @param elementsToMove
	 *            the map of <code>EObjects</code> to <code>EReference</code>
	 *            features to be moved.
	 */
	public MoveRequest(EObject targetContainer, Map elementsToMove) {

		super();
		this.targetContainer = targetContainer;
		this.elementsToMove = elementsToMove;
	}

	/**
	 * Gets the map of elements to be moved. Each entry in the map consists of
	 * an <code>EObject</code> key, which is the element to be moved to the
	 * new target, and an <code>EReference</code> value, which is the feature
	 * in the new target that should contain the moved element.
	 * 
	 * @return the map of elements to be moved
	 */
	public Map getElementsToMove() {
		return elementsToMove;
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
	 * Sets the reference feature into which an element should be moved.
	 * 
	 * @param element
	 *            the element to be moved
	 * @param targetFeature
	 *            the target feature
	 */
	public void setTargetFeature(EObject element, EReference targetFeature) {
		getElementsToMove().put(element, targetFeature);
	}

	/**
	 * Gets the feature in the target element that should contain
	 * <code>element</code> after it is moved.
	 * 
	 * @param element
	 *            the element to be moved
	 * @return the feature that will contain the element in the target
	 */
	public EReference getTargetFeature(EObject element) {
		return (EReference) getElementsToMove().get(element);
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
		return EMFTypeCoreMessages.Request_Label_Move;
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