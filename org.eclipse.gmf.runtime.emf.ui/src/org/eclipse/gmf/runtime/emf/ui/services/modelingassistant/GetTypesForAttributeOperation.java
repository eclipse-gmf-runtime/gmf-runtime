/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              	   |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.ui.services.modelingassistant;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * The operation used with the modeling assistant service that gets the type of
 * elements that can be created or used when qualifying an attribute's type.
 * 
 * @author cmahoney
 */
public class GetTypesForAttributeOperation
	implements IModelingAssistantOperation {

	/** the attribute: adapts to editpart, view, element, etc. */
	private final IAdaptable attribute;

	/**
	 * Creates a new <code>GetTypesForAttributeOperation</code>.
	 * 
	 * @param attribute
	 *            the attribute: adapts to editpart, view, element, etc.
	 */
	protected GetTypesForAttributeOperation(IAdaptable attribute) {
		this.attribute = attribute;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantOperation#getId()
	 */
	public String getId() {
		return GET_TYPES_FOR_ATTRIBUTE_ID;
	}

	/**
	 * Returns the attribute. This is what will be used to assist in delayed
	 * loading of a modeling assistant provider.
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantOperation#getContext()
	 */
	public IAdaptable getContext() {
		return getAttribute();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		return ((IModelingAssistantProvider) provider)
			.getTypesForAttribute(getAttribute());
	}

	/**
	 * @return Returns the attribute that is to have its type qualified.
	 */
	public final IAdaptable getAttribute() {
		return attribute;
	}

}