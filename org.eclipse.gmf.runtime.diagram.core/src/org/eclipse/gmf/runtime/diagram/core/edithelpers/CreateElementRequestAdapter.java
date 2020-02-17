/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.edithelpers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.core.util.IProxyEObject;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;

/**
 * Descriptor for a create element request that can adapt to the request type,
 * the container element and the element type to be created.
 * 
 * @author ldamus
 */
public class CreateElementRequestAdapter
	implements IAdaptable, IProxyEObject {

	/**
	 * The request to create a new element.
	 */
	private final CreateElementRequest createElementRequest;

	/**
	 * Constructs a new adapter.
	 * 
	 * @param createElementRequest
	 *            the request to create a new element.
	 */
	public CreateElementRequestAdapter(CreateElementRequest createElementRequest) {

		assert null != createElementRequest : "Null createElementRequest not supported in CreateElementRequestAdapter";//$NON-NLS-1$

		this.createElementRequest = createElementRequest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter.isInstance(createElementRequest)) {
			return createElementRequest;
		}

		if (adapter.isInstance(getNewElement())) {
			return getNewElement();
		}
		if (adapter.isInstance(getElementType())) {
			return getElementType();
		}
		if (adapter.isInstance(this)) {
			return this;
		}
		
		return Platform.getAdapterManager().getAdapter(createElementRequest, adapter);
	}

	/**
	 * Gets the new element that has been created in response to this request.
	 * 
	 * @return the new element
	 */
	private EObject getNewElement() {
		return createElementRequest.getNewElement();
	}

	/**
	 * Gets the element type for the new element.
	 * 
	 * @return the element typeO
	 */
	private IElementType getElementType() {
		return createElementRequest.getElementType();
	}

	/**
	 * Sets the new element that has been created in response to this request.
	 * @param newElement the new element
	 */
	public void setNewElement(EObject newElement) {
		createElementRequest.setNewElement(newElement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.core.internal.util.IProxyEObject#getProxyClassID()
	 */
	public Object getProxyClassID() {
		return getNewElement() == null ? null
			: PackageUtil.getID(EMFCoreUtil.getProxyClass(getNewElement()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.core.internal.util.IProxyEObject#resolve()
	 */
	public EObject resolve() {
		return getNewElement() == null ? null
			: EMFCoreUtil.resolve(TransactionUtil.getEditingDomain(createElementRequest.getContainer()), getNewElement()); 
	}

}