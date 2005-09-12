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

/**
 * Request to change the source or target of a relationship element.
 * 
 * @author ldamus
 */
public class ReorientRelationshipRequest
	extends ReorientRequest {

	/**
	 * The relationship element.
	 */
	private final EObject relationship;

	/**
	 * Constructs a new request to change the source or target of a relationship
	 * element.
	 * 
	 * @param relationship
	 *            the relationship element
	 * @param newRelationshipEnd
	 *            the new source or target
	 * @param oldRelationshipEnd
	 *            the old source or target
	 * @param direction
	 *            Indicates whether or not the source of the target of the
	 *            relationship will be changed. One of {@link #REORIENT_SOURCE}
	 *            or {@link #REORIENT_TARGET}.
	 */
	public ReorientRelationshipRequest(EObject relationship,
			EObject newRelationshipEnd, EObject oldRelationshipEnd,
			int direction) {

		super(direction, newRelationshipEnd, oldRelationshipEnd);
		this.relationship = relationship;
	}

	/**
	 * Gets the relationship element.
	 * 
	 * @return the relationship element
	 */
	public EObject getRelationship() {
		return relationship;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest#getElementsToEdit()
	 */
	public List getElementsToEdit() {
		return Collections.singletonList(relationship);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest#getEditHelperContext()
	 */
	public Object getEditHelperContext() {
		return relationship;
	}

}