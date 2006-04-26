/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.emf.type.core.EditHelperContext;
import org.eclipse.gmf.runtime.emf.type.core.IClientContext;

/**
 * Request to change the source or target of a reference.
 * 
 * @author ldamus
 */
public class ReorientReferenceRelationshipRequest extends ReorientRequest {

	/**
	 * The owner of the reference.
	 */
	private final EObject referenceOwner;

	/**
	 * Constructs a new request to change the source or target of a reference.
	 * 
	 * @param editingDomain
	 *            the editing domain in which I am requesting to make model
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
	public ReorientReferenceRelationshipRequest(TransactionalEditingDomain editingDomain,
			EObject referenceOwner, EObject newRelationshipEnd,
			EObject oldRelationshipEnd, int direction) {

		super(editingDomain, direction, newRelationshipEnd, oldRelationshipEnd);
		this.referenceOwner = referenceOwner;
	}
    
    /**
     * Constructs a new request to change the source or target of a reference. The
     * editing domain will be derived from the <code>referenceOwner</code>.
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

        this(TransactionUtil.getEditingDomain(referenceOwner), referenceOwner,
                newRelationshipEnd, oldRelationshipEnd, direction);
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
		IClientContext context = getClientContext();
		
		if (context == null) {
			return referenceOwner;
		} else {
			return new EditHelperContext(referenceOwner, context);
		}
	}

}