/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.internal.services.semantic;

import org.eclipse.emf.ecore.EObject;


/**
 * A request to destroy a model element
 * @author melaasar
 * 
 * @deprecated Use {@link org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest}
 *             to get move commands from the Element Type API
 *             {@link org.eclipse.gmf.runtime.emf.type.core.IElementType#getEditCommand(IEditCommandRequest)}.
 */
public class DestroyElementRequest extends SemanticRequest {

	/** the element to be destroyed */
	private EObject _element;	
	
	// 
	// Support for deleting references
	//
	private EObject _referenceOwner;
	private EObject _reference;

	/**
	 * Creates a new DestroyElementRequest with no element yet
	 */
	public DestroyElementRequest() {
		super(SemanticRequestTypes.SEMREQ_DESTROY_ELEMENT);
	}
	
	/**
	 * Creates a new DestroyElementRequest.
	 * @param element to be destroyed
	 */
	public DestroyElementRequest(EObject element) {
		this(SemanticRequestTypes.SEMREQ_DESTROY_ELEMENT, element);
	}
	
	/**
	 * Creates a new DestroyElementRequest.
	 * @param requestType
	 * @param element to be destroyed
	 */
	public DestroyElementRequest(Object requestType, EObject element) {
		super(requestType);
		setElement(element);
	}
	
	/** Returns the reference owner. */
	public final EObject getReferenceObjectOwner() {
		return _referenceOwner;
	}
	
	/** Returns the reference to be removed. */
	public final EObject getReferenceObject() {
		return _reference;
	}
	
	/** Sets the reference owner. */
	public final void setReferenceOwner(EObject referenceOwner) {
		_referenceOwner = referenceOwner;
	}
	
	/** Sets the reference to be removed. */
	public void setReference(EObject reference) {
		_reference = reference;
	}
	
	//
	// End support for destroying references
	//

	/**  Return the element to be destroyed	 */
	public final EObject getObject() {
		return _element;
	}
	
	public final void setElement( EObject element ) {
		_element = element;
	}
	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.SemanticRequest#getContext()
	 */
	public Object getContext() {
		if (getObject() == null && getReferenceObjectOwner() != null)
			return getReferenceObjectOwner();
		else
			return getObject();
	}		
	

}
