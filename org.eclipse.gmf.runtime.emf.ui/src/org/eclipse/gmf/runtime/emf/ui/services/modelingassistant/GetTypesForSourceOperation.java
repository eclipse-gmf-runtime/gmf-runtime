/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.ui.services.modelingassistant;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

/**
 * The operation used with the modeling assistant service that gets the type of
 * elements that can be used for the source end of a relationship.
 * 
 * @author cmahoney
 */
public class GetTypesForSourceOperation
	implements IModelingAssistantOperation {

	/** the target: adapts to EditPart, View, Element, etc. */
	private final IAdaptable target;

	/** the relationship type to be created */
	private final IElementType relationshipType;

	/**
	 * Creates a new <code>GetTypesForSourceOperation</code>.
	 * 
	 * @param target
	 *            the target: adapts to EditPart, View, Element, etc.
	 * @param relationshipType
	 *            the relationship type to be created
	 *  
	 */
	protected GetTypesForSourceOperation(IAdaptable target,
			IElementType relationshipType) {

		this.target = target;
		this.relationshipType = relationshipType;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantOperation#getId()
	 */
	public String getId() {
		return GET_TYPES_FOR_SOURCE_ID;
	}

	/**
	 * Returns the target. This is what will be used to assist in delayed
	 * loading of a modeling assistant provider.
	 */
	public IAdaptable getContext() {
		return getTarget();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		return ((IModelingAssistantProvider) provider).getTypesForSource(
			getTarget(), getRelationshipType());
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
	 * Gets the target.
	 * 
	 * @return Returns the target.
	 */
	public IAdaptable getTarget() {
		return target;
	}
}