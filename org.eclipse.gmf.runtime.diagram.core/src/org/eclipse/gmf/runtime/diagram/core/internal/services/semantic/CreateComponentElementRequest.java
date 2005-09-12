/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
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

import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;


/**
 * @author melaasar
 *
 * A request to create a component model element in the context of another element
 * 
 * @deprecated Use {@link org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest}
 *             to get move commands from the Element Type API
 *             {@link org.eclipse.gmf.runtime.emf.type.core.IElementType#getEditCommand(IEditCommandRequest)}.
 */
public class CreateComponentElementRequest extends CreateElementRequest {

	/** the context element */
	private EObject _context;

	
	/**
	 * Creates a new component element request
	 * @param elementTypeInfo
	 */
	public CreateComponentElementRequest(IElementType elementType) {
		this(SemanticRequestTypes.SEMREQ_CREATE_COMPONENT_ELEMENT, elementType);
	}
	
	/**
	 * Creates a new component element request
	 * @param requestType
	 * @param elementTypeInfo
	 */
	public CreateComponentElementRequest( Object requestType, IElementType elementType) {
		super(requestType, elementType);
	}
	

	/**
	 * Creates a new component element request
	 * @param elementTypeInfo
	 * @param contextElement
	 */
	public CreateComponentElementRequest(IElementType elementType, EObject contextElement) {
		this(SemanticRequestTypes.SEMREQ_CREATE_COMPONENT_ELEMENT, elementType, contextElement);
	}
	
	
	/**
	 * Creates a new component element request
	 * @param requestType
	 * @param elementTypeInfo
	 * @param contextElement
	 */
	public CreateComponentElementRequest( Object requestType, IElementType createElementKind, EObject contextElement) {
		this(requestType, createElementKind);
		setContextElement(contextElement);
	}
		
	/**
	 * Returns the contextElement.
	 * @param contextElement element context
	 */
	public EObject getContextObject() {
		return _context;
	}

	/**
	 * Sets the contextElement.
	 * @param contextElement The contextElement to set
	 */
	public void setContextElement(EObject contextElement) {
		_context = contextElement;
	}
	
}
