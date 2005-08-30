/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.edithelpers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.emf.core.util.IProxyEObject;
import org.eclipse.gmf.runtime.emf.core.util.ProxyUtil;
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
			: ProxyUtil.getProxyClassID(getNewElement());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.core.internal.util.IProxyEObject#resolve()
	 */
	public EObject resolve() {
		return getNewElement() == null ? null
			: ProxyUtil.resolve(MEditingDomainGetter.getMEditingDomain(createElementRequest.getContainer()), getNewElement()); 
	}

}