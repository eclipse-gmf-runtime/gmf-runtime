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
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

/**
 * The operation used with the modeling assistant service that gets the type of
 * elements that can be used for the target end of a relationship.
 * 
 * @author cmahoney
 */
public class GetTypesForTargetOperation
	implements IModelingAssistantOperation {

	/** the source: adapts to editpart, view, element, etc. */
	private final IAdaptable source;

	/** the relationship type to be created */
	private final IElementType relationshipType;

	/**
	 * Creates a new <code>GetTypesForTargetOperation</code>.
	 * 
	 * @param source
	 *            the source: adapts to editpart, view, element, etc.
	 * @param relationshipType
	 *            the relationship type to be created
	 */
	protected GetTypesForTargetOperation(IAdaptable source,
			IElementType relationshipType) {

		this.source = source;
		this.relationshipType = relationshipType;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantOperation#getId()
	 */
	public String getId() {
		return GET_TYPES_FOR_TARGET_ID;
	}

	/**
	 * Returns the source. This is what will be used to assist in delayed
	 * loading of a modeling assistant provider.
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantOperation#getContext()
	 */
	public IAdaptable getContext() {
		return getSource();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		return ((IModelingAssistantProvider) provider).getTypesForTarget(
			getSource(), getRelationshipType());
	}

	/**
	 * Gets the relationshipType.
	 * 
	 * @return Returns the relationshipType.
	 */
	public IElementType getRelationshipType() {
		return relationshipType;
	}

	/**
	 * Gets the source.
	 * 
	 * @return Returns the source.
	 */
	public IAdaptable getSource() {
		return source;
	}
}