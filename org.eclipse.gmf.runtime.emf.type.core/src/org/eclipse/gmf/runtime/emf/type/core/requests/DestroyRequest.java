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

import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.ResourceManager;

/**
 * Abstract superclass for reuqests to destroy a model element.
 * 
 * @author ldamus
 */
public abstract class DestroyRequest
	extends AbstractEditCommandRequest {

	/**
	 * Flag to indicate whether or not confirmation is required when destoying a
	 * model element.
	 */
	private boolean confirmationRequired;

	/**
	 * Constructs a new request to destroy a model element.
	 * 
	 * @param confirmationRequired
	 *            <code>true</code> if the user should be prompted to confirm
	 *            the element deletion, <code>false</code> otherwise.
	 */
	public DestroyRequest(boolean confirmationRequired) {
		super();
		this.confirmationRequired = confirmationRequired;
	}

	/**
	 * Answers whether or not the user should be prompted to confirm the element
	 * deletion.
	 * 
	 * @return <code>true</code> if the user should be prompted to confirm the
	 *         element deletion, <code>false</code> otherwise
	 */
	public boolean isConfirmationRequired() {
		return confirmationRequired;
	}

	/**
	 * Sets the flag that indicates whether or not confirmation of the deletion is required.
	 * @param confirmationRequired <code>true</code> if the user should be prompted to confirm the
	 *         element deletion, <code>false</code> otherwise
	 */
	public void setConfirm(boolean confirmationRequired) {
		this.confirmationRequired = confirmationRequired;
	}

	/**
	 * Gets the container of the element to be destroyed.
	 * @return the container of the element to be destroyed
	 */
	public abstract EObject getContainer();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest#getElementsToEdit()
	 */
	public List getElementsToEdit() {
		if (getContainer() != null) {
			return Collections.singletonList(getContainer());
		}
		return super.getElementsToEdit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditCommandRequest#getDefaultLabel()
	 */
	protected String getDefaultLabel() {
		return ResourceManager.getInstance().getString("Request.Label.Destroy"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest#getEditHelperContext()
	 */
	public Object getEditHelperContext() {
		return getContainer();
	}
}