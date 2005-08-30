/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal.services.semantic;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;


/**
 * A request to move a model element into a new context
 * @author melaasar
 * 
 * @deprecated Use {@link org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest}
 *             to get move commands from the Element Type API
 *             {@link org.eclipse.gmf.runtime.emf.type.core.IElementType#getEditCommand(IEditCommandRequest)}.
 */
public class MoveElementRequest extends SemanticRequest {

	/** the element */
	private EObject _element;
	/** the new context element */
	private EObject _newContextElement;

	/**
	 * Method GetReparentSemanticElementCommandOperation.
	 * @param requestType
	 * @param element the element to be reparented
	 * @param elementContext the new element context
	 */
	public MoveElementRequest( Object requestType, EObject element,	EObject newContextElement) {
		super(requestType);		
		assert null != element : "Null element in MoveElementRequest";//$NON-NLS-1$		
		assert null != newContextElement : "Null newContextElement in MoveElementRequest";//$NON-NLS-1$
		_element = element;
		_newContextElement = newContextElement;
	}

	/**
	 * Method GetReparentSemanticElementCommandOperation.
	 * @param element the element to be reparented
	 * @param elementContext the new element context
	 */
	public MoveElementRequest(EObject element, EObject newContextElement) {
		this( SemanticRequestTypes.SEMREQ_MOVE_ELEMENT, element, newContextElement);
	}

	/** Return the element being moved. */
	public final EObject getMoveElement() {
		return _element;
	}
	
	/** Return the element new container. */
	public final EObject getNewContainerElement() {
		return _newContextElement;
	}

	/** Set the element being moved. */
	public final void setElement(EObject element) {
		_element = element;
	}

	/** Set the element new container element. */
	public void setNewContextElement( EObject element ) {
		_newContextElement = element;
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.SemanticRequest#getContext()
	 */
	public Object getContext() {
		return getMoveElement();
	}

}
