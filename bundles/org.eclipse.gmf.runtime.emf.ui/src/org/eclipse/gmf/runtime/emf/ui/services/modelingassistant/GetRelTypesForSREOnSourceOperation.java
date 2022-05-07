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

/**
 * The operation used with the modeling assistant service that gets the type of
 * relationships that can be used to show related elements from a given source
 * object.
 * 
 * @author cmahoney
 */
public class GetRelTypesForSREOnSourceOperation
	implements IModelingAssistantOperation {

	/** the source: adapts to EditPart, View, Element, etc. */
	private final IAdaptable source;

	/**
	 * Creates a new <code>GetRelTypesForSREOnSourceOperation</code>.
	 * 
	 * @param source
	 *            the source: adapts to EditPart, View, Element, etc.
	 */
	protected GetRelTypesForSREOnSourceOperation(IAdaptable source) {
		this.source = source;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantOperation#getId()
	 */
	public String getId() {
		return GET_REL_TYPES_FOR_SRE_ON_SOURCE_ID;
	}

	/**
	 * Returns the source. This is what will be used to assist in delayed
	 * loading of a modeling assistant provider.
	 */
	public IAdaptable getContext() {
		return getSource();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		return ((IModelingAssistantProvider) provider)
			.getRelTypesForSREOnSource(getSource());
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