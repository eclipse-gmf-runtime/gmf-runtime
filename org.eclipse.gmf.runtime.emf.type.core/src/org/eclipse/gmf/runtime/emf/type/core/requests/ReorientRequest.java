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

import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.ResourceManager;

/**
 * Abstract request for changing the source or target of a relationship.
 * 
 * @author ldamus
 */
public abstract class ReorientRequest
	extends AbstractEditCommandRequest {

	/**
	 * Indicates that the source of the relationship will be changed.
	 */
	public final static int REORIENT_SOURCE = 1; //$NON-NLS-1$

	/**
	 * Indicates that the target of the relationship will be changed.
	 */
	public final static int REORIENT_TARGET = 2; //$NON-NLS-1$

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
	 * @param direction
	 *            Indicates whether or not the source of the target of the
	 *            relationship will be changed. One of {@link #REORIENT_SOURCE}
	 *            or {@link #REORIENT_TARGET}.
	 * @param newRelationshipEnd
	 *            the new source or target
	 * @param oldRelationshipEnd
	 *            the old source or target
	 */
	protected ReorientRequest(int direction, EObject newRelationshipEnd,
			EObject oldRelationshipEnd) {
		super();
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
			return ResourceManager.getInstance().getString(
				"Request.Label.ReorientSource"); //$NON-NLS-1$

		} else if (getDirection() == REORIENT_TARGET) {
			return ResourceManager.getInstance().getString(
				"Request.Label.ReorientTarget"); //$NON-NLS-1$
		}
		return super.getDefaultLabel();
	}

}