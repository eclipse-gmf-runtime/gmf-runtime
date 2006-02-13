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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.EMFTypeCoreMessages;

/**
 * Abstract request for changing the source or target of a relationship.
 * 
 * @author ldamus
 */
public abstract class ReorientRequest extends AbstractEditCommandRequest {

	/**
	 * Indicates that the source of the relationship will be changed.
	 */
	public final static int REORIENT_SOURCE = 1;

	/**
	 * Indicates that the target of the relationship will be changed.
	 */
	public final static int REORIENT_TARGET = 2;

	/**
	 * Indicates whether or not the source of the target of the relationship
	 * will be changed. One of {@link #REORIENT_SOURCE}or
	 * {@link #REORIENT_TARGET}.
	 */
	private final int direction;

	/**
	 * The new source or target.
	 */
	private final EObject newRelationshipEnd;

	/**
	 * The old source or target.
	 */
	private final EObject oldRelationshipEnd;

	/**
	 * Constructs a new request tof changing the source or target of a
	 * relationship.
	 * 
	 * @param editingDomain
	 *            the editing domain in which I am requesting to make model
	 * @param direction
	 *            Indicates whether or not the source of the target of the
	 *            relationship will be changed. One of {@link #REORIENT_SOURCE}
	 *            or {@link #REORIENT_TARGET}.
	 * @param newRelationshipEnd
	 *            the new source or target
	 * @param oldRelationshipEnd
	 *            the old source or target
	 */
	protected ReorientRequest(TransactionalEditingDomain editingDomain, int direction,
			EObject newRelationshipEnd, EObject oldRelationshipEnd) {
		super(editingDomain);
		this.direction = direction;
		this.newRelationshipEnd = newRelationshipEnd;
		this.oldRelationshipEnd = oldRelationshipEnd;
	}

	/**
	 * Gets the value that indicates whether or not the source of the target of
	 * the relationship will be changed. One of {@link #REORIENT_SOURCE}or
	 * {@link #REORIENT_TARGET}.
	 * 
	 * @return one of {@link #REORIENT_SOURCE}or {@link #REORIENT_TARGET}.
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * O Gets the new source or target.
	 * 
	 * @return the new source or target
	 */
	public EObject getNewRelationshipEnd() {
		return newRelationshipEnd;
	}

	/**
	 * Gets the old source or target.
	 * 
	 * @return the old source or target
	 */
	public EObject getOldRelationshipEnd() {
		return oldRelationshipEnd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditCommandRequest#getDefaultLabel()
	 */
	protected String getDefaultLabel() {

		if (getDirection() == REORIENT_SOURCE) {
			return EMFTypeCoreMessages.Request_Label_ReorientSource;

		} else if (getDirection() == REORIENT_TARGET) {
			return EMFTypeCoreMessages.Request_Label_ReorientTarget;
		}
		return super.getDefaultLabel();
	}

}