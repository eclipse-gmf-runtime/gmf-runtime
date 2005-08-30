/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.type.core.requests;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.gmf.runtime.emf.type.core.IElementType;

/**
 * Request to create a new relationship element.
 * 
 * @author ldamus
 */
public class CreateRelationshipRequest
	extends CreateElementRequest {

	/**
	 * The parameter name for the relationship source.
	 */
	public static final String SOURCE = "CreateRelationshipRequest.source"; //$NON-NLS-1$

	/**
	 * The parameter name for the relationship target.
	 */
	public static final String TARGET = "CreateRelationshipRequest.target"; //$NON-NLS-1$

	/**
	 * The source of the relationship.
	 */
	private EObject source;

	/**
	 * The target of the relationship.
	 */
	private EObject target;

	/**
	 * <code>true</code> if the user should be prompted to create any parts
	 * necessary to the creation of the new relationship, <code>false</code>
	 * otherwise.
	 */
	private boolean prompt;

	/**
	 * Constructs a new request to create a new relationship.
	 * 
	 * @param container
	 *            the container for the new relationship
	 * @param source
	 *            the source of the new relationship
	 * @param target
	 *            the target of the new relationship
	 * @param elementType
	 *            the element type of the new relationship
	 * @param containmentFeature
	 *            the feature that will contain the new relationship
	 */
	public CreateRelationshipRequest(EObject container, EObject source,
			EObject target, IElementType elementType,
			EReference containmentFeature) {

		super(container, elementType, containmentFeature);

		this.source = source;
		this.target = target;
	}

	/**
	 * Constructs a new request to create a new relationship.
	 * 
	 * @param container
	 *            the container for the new relationship
	 * @param source
	 *            the source of the new relationship
	 * @param target
	 *            the target of the new relationship
	 * @param elementType
	 *            the element type of the new relationship
	 */
	public CreateRelationshipRequest(EObject container, EObject source,
			EObject target, IElementType elementType) {

		this(container, source, target, elementType, null);
	}

	/**
	 * Constructs a new request to create a new relationship.
	 * 
	 * @param source
	 *            the source of the new relationship
	 * @param target
	 *            the target of the new relationship
	 * @param elementType
	 *            the element type of the new relationship
	 */
	public CreateRelationshipRequest(EObject source, EObject target,
			IElementType elementType) {

		this(null, source, target, elementType, null);
	}

	/**
	 * Constructs a new request to create a new relationship.
	 * 
	 * @param elementType
	 *            the element type of the new relationship
	 */
	public CreateRelationshipRequest(IElementType elementType) {

		this(null, null, null, elementType, null);
	}

	/**
	 * Gets the source of the new relationship.
	 * 
	 * @return the source of the new relationship
	 */
	public EObject getSource() {
		return source;
	}

	/**
	 * Sets the relationship source.
	 * <p>
	 * Does nothing of the source has not changed. Othewise, invalidates the
	 * edit helper context and containment feature.
	 * 
	 * @param source
	 *            the relationship source
	 */
	public void setSource(EObject source) {

		if (this.source != source) {

			this.source = source;
			invalidateEditHelperContext();

			if (super.getContainer() == null) {
				invalidateContainmentFeature();
			}
		}
	}

	/**
	 * Gets the target of the new relationship.
	 * 
	 * @return the target of the new relationship
	 */
	public EObject getTarget() {
		return target;
	}

	/**
	 * Sets the relationship target.
	 * <p>
	 * Does nothing of the target has not changed. Otherwise, invalidates the
	 * edit helper context.
	 * 
	 * @param target
	 *            the relationship target
	 */
	public void setTarget(EObject target) {

		if (this.target != target) {
			this.target = target;
			invalidateEditHelperContext();
		}
	}

	/**
	 * Gets the container for the new element.
	 * 
	 * @return the container for the new element.
	 */
	public EObject getContainer() {

		EObject eObject = super.getContainer();

		if (eObject == null) {
			return getSource();
		}
		return eObject;
	}

	/**
	 * Gets the value of the prompt flag.
	 * 
	 * @return <code>true</code> if the user should be prompted to create any
	 *         parts necessary to the creation of the new relationship,
	 *         <code>false</code> if defaults should be used.
	 */
	public boolean isPrompt() {
		return prompt;
	}

	/**
	 * Sets the value of the prompt flag.
	 * 
	 * @param prompt
	 *            <code>true</code> if the user should be prompted to create
	 *            any parts necessary to the creation of the new relationship,
	 *            <code>false</code> if defaults should be used.
	 */
	public void setPrompt(boolean prompt) {
		this.prompt = prompt;

	}
}