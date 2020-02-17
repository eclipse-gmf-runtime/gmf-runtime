/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core;

import org.eclipse.emf.ecore.EObject;

/**
 * The concrete implementation of the context of an editing gesture. It
 * represents the <code>EObject</code> or <code>IElementType</code> that is
 * the subject of the editing action, and the <code>IClientContext</code> in
 * which the editing will be performed.
 * <P>
 * This class may be instantiated by clients.
 * 
 * @author ldamus
 */
public class EditHelperContext implements IEditHelperContext {

	private IClientContext clientContext;

	private EObject eObject;

	private IElementType elementType;

	/**
	 * Constructs a new context object.
	 */
	public EditHelperContext() {
		super();
	}

	/**
	 * Constructs a new context with an <code>eObject</code> and a
	 * <code>clientContext</code> which describe the context in which an
	 * editing gesture will be made.
	 * 
	 * @param eObject
	 *            the <code>EObject</code> that is the subject of the editing
	 *            gesture
	 * @param clientContext
	 *            the client context
	 */
	public EditHelperContext(EObject eObject, IClientContext clientContext) {
		this();
		this.eObject = eObject;
		this.clientContext = clientContext;
	}

	/**
	 * Constructs a new context with an <code>elementType</code> and a
	 * <code>clientContext</code> which describe the context in which an
	 * editing gesture will be made.
	 * 
	 * @param elementType
	 *            the <code>IElementType</code> that is the subject of the
	 *            editing gesture
	 * @param clientContext
	 *            the client context
	 */
	public EditHelperContext(IElementType elementType,
			IClientContext clientContext) {
		this();
		this.elementType = elementType;
		this.clientContext = clientContext;
	}

	// documentation copied from the interface
	public IClientContext getClientContext() {
		return clientContext;
	}

	/**
	 * Sets the client context in which the editing gesture will be performed.
	 * 
	 * @param clientContext
	 *            the client context
	 */
	public void setClientContext(IClientContext clientContext) {
		this.clientContext = clientContext;
	}

	// documentation copied from the interface
	public EObject getEObject() {
		return eObject;
	}

	/**
	 * Sets the <code>EObject</code> that is the subject of the editing
	 * gesture.
	 * 
	 * @param eObject
	 *            the eObject
	 */
	public void setEObject(EObject eObject) {
		this.eObject = eObject;
	}

	// documentation copied from the interface
	public IElementType getElementType() {
		return elementType;
	}

	/**
	 * Sets the <code>EObject</code> that is the subject of the editing
	 * gesture.
	 * 
	 * @param elementType
	 *            the element type
	 */
	public void setElementType(IElementType elementType) {
		this.elementType = elementType;
	}

}
