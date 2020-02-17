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

package org.eclipse.gmf.runtime.emf.type.core.requests;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.emf.type.core.EditHelperContext;
import org.eclipse.gmf.runtime.emf.type.core.IClientContext;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

/**
 * Request to configure a new element with the attributes that it should have
 * according to its element type.
 * 
 * @author ldamus
 */
public class ConfigureRequest extends AbstractEditCommandRequest {

	/**
	 * The element to be configured.
	 */
	private final EObject elementToConfigure;

	/**
	 * The element type that defines the attributes that the new element should
	 * have.
	 */
	private final IElementType typeToConfigure;

	/**
	 * Constructs a new configure request.
	 * 
	 * @param editingDomain
	 *            the editing domain in which I am requesting to make model
	 * @param elementToConfigure
	 *            the new element to be configured
	 * @param typeToConfigure
	 *            the element type defining the attributes that the new element
	 *            should have
	 */
	public ConfigureRequest(TransactionalEditingDomain editingDomain,
			EObject elementToConfigure, IElementType typeToConfigure) {

		super(editingDomain);
		this.elementToConfigure = elementToConfigure;
		this.typeToConfigure = typeToConfigure;
	}
    
    /**
     * Constructs a new configure request. The editing domain will be derived
     * from <code>elementToConfigure</code>.
     * 
     * @param elementToConfigure
     *            the new element to be configured
     * @param typeToConfigure
     *            the element type defining the attributes that the new element
     *            should have
     */
    public ConfigureRequest(EObject elementToConfigure,
            IElementType typeToConfigure) {

        this(TransactionUtil.getEditingDomain(elementToConfigure),
            elementToConfigure, typeToConfigure);
    }

	/**
	 * Gets the element to be configured
	 * 
	 * @return the element to be configured
	 */
	public EObject getElementToConfigure() {
		return elementToConfigure;
	}

	/**
	 * Gets the element type that defines the attributes that the element should
	 * have
	 * 
	 * @return the element type
	 */
	public IElementType getTypeToConfigure() {
		return typeToConfigure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest#getElementsToEdit()
	 */
	public List getElementsToEdit() {
		if (getElementToConfigure() != null) {
			return Collections.singletonList(getElementToConfigure());
		}
		return super.getElementsToEdit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest#getEditHelperContext()
	 */
	public Object getEditHelperContext() {
		
		IClientContext context = getClientContext();
		
		if (context == null) {
			return getTypeToConfigure();
			
		} else {
			return new EditHelperContext(getTypeToConfigure(), context);
		}
	}
}