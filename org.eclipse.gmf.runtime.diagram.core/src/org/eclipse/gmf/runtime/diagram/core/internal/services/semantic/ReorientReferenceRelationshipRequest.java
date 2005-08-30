/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.       		       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal.services.semantic;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;


/**
 * Request to reorient a reference relationship. Since this relationship is not
 * represented by a relationship element, different values are needed to manipulate
 * the relationship.
 * 
 * @author jcorchis
 * 
 * @deprecated Use {@link org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest}
 *             to get reorient reference commands from the Element Type API
 *             {@link org.eclipse.gmf.runtime.emf.type.core.IElementType#getEditCommand(IEditCommandRequest)}.
 */
public class ReorientReferenceRelationshipRequest
	extends ReorientRelationshipRequest {

	/** request elements */
	private EObject _referenceOwner;
	private EObject _oldTarget;
	private EObject _newTarget;

	/**
	 * Constructor
	 * @param requestType
	 * @param referenceOwner reference owner for the original relationship
	 * @param oldElement orignal target of the relationship
	 * @param newElement the new source or target
	 */
	public ReorientReferenceRelationshipRequest(
		Object requestType,
		EObject referenceOwner,
		EObject oldElement,
		EObject newElement) {

		super(requestType);
		_referenceOwner = referenceOwner;
		_oldTarget = oldElement;
		_newTarget = newElement;

	}
	
	/** Return the reference onwer. */
	public final EObject getReferenceObjectOwner() {
		return _referenceOwner;
	}


	/** Return the orignal target of the relationship. */
	public final EObject getOldTarget() {
		return _oldTarget;
	}


	/** Return the relationship target. */
	public final EObject getNewTarget() {
		return _newTarget;
	}
	/**
	 * Returns a context of a constant string for this request.
	 * Currently, the context is ReorientReferenceRelationshipRequest.CONTEXT
	 * @return the context
	 */
	public Object getContext() {
		return getReferenceObjectOwner();
	}

}
