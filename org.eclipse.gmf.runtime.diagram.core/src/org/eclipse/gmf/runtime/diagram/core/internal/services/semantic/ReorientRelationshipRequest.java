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
 * A request to reorient a relationship's source or target 
 * based on the request type
 * @author melaasar
 * 
 * @deprecated Use {@link org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest}
 *             to get reorient commands from the Element Type API
 *             {@link org.eclipse.gmf.runtime.emf.type.core.IElementType#getEditCommand(IEditCommandRequest)}.
 */
public class ReorientRelationshipRequest extends SemanticRequest {

	/** the affected relationship */
	private EObject _relationship;
	/** the new relationshipEnd */
	private EObject _relationshipEnd;
	/** the old relationshipEnd */
	private EObject _oldRelationshipEnd;

	/**
	 * Creates a new ReorientRelationshipRequest.
	 * @param requestType
	 * @param relationship
	 * @param relationshipEnd
	 */
	public ReorientRelationshipRequest( Object requestType, EObject relationship, EObject relationshipEnd,	EObject oldRelationshipEnd) {
		super(requestType);

		_relationship = relationship;
		_relationshipEnd = relationshipEnd;
		_oldRelationshipEnd = oldRelationshipEnd;
	}
	
	/**
	 * 
	 * @param requestType
	 */
	protected ReorientRelationshipRequest(Object requestType) {
		super(requestType);
	}

	/** Return the relationship. */
	public final EObject getRelationshipObject() {
		return _relationship;
	}

	/** Returns the relationshipEnd, which could be a source or a target based on the request type */
	public final EObject getRelationshipEndPoint() {
		return _relationshipEnd;
	}
	
	/** Returns the oldRelationshipEnd.  */
	public final EObject getOldRelationshipEndPoint() {
		return _oldRelationshipEnd;
	}
	
	/** Set the relationship. */
	public final void setRelationship(EObject element) {
		_relationship = element;
	}
	
	/** Set the new relationship endpoint. */
	public final void setRelationshipEnd(EObject element) {
		_relationshipEnd = element;
	}

	/** Set the old relationship endpoint. */
	public void setOldRelationshipEnd(EObject oldRelationshipEnd) {
		_oldRelationshipEnd = oldRelationshipEnd;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.SemanticRequest#getContext()
	 */
	public Object getContext() {
		return getRelationshipObject();
	}

	
}
