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

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * Request to change the source or target of a reference.
 * 
 * @author ldamus
 */
public class ReorientReferenceRelationshipRequest
	extends ReorientRequest {

	/**
	 * The owner of the reference.
	 */
	private final EObject referenceOwner;

	/**
	 * Constructs a new request to change the source or target of a reference.
	 * 
	 * @param referenceOwner
	 *            the owner of the reference
	 * @param newRelationshipEnd
	 *            the new source or target
	 * @param oldRelationshipEnd
	 *            the old source or target
	 * @param direction
	 *            Indicates whether or not the source of the target of the
	 *            relationship will be changed. One of {@link #REORIENT_SOURCE}
	 *            or {@link #REORIENT_TARGET}.
	 */
	public ReorientReferenceRelationshipRequest(EObject referenceOwner,
			EObject newRelationshipEnd, EObject oldRelationshipEnd,
			int direction) {

		super(direction, newRelationshipEnd, oldRelationshipEnd);
		this.referenceOwner = referenceOwner;
	}

	/**
	 * Get the owner of the reference.
	 * 
	 * @return the owner of the reference
	 */
	public EObject getReferenceOwner() {
		return referenceOwner;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest#getElementsToEdit()
	 */
	public List getElementsToEdit() {
		return Collections.singletonList(referenceOwner);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest#getEditHelperContext()
	 */
	public Object getEditHelperContext() {
		return referenceOwner;
	}

}