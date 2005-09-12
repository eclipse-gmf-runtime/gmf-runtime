/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.internal.edithelpers;

import org.eclipse.core.runtime.IAdapterFactory;

import org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.CreateElementRequest;
import org.eclipse.gmf.runtime.diagram.core.internal.util.SemanticRequestTranslator;

/**
 * Adapts the an edit command request into a semantic request to support clients
 * still using the semantic request types.
 * 
 * @author ldamus
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.core.*
 * 
 * @deprecated Only used to support clients adapting to the deprecated semantic
 *             request types.
 */
public class CreateElementRequestAdapterFactory
	implements IAdapterFactory {

	/**
	 * My adapter list.
	 */
	private final Class[] adapterList = new Class[] {CreateElementRequest.class};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object,
	 *      java.lang.Class)
	 */
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		CreateElementRequest translatedRequest = SemanticRequestTranslator
			.translate((org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest) adaptableObject);

		return translatedRequest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	public Class[] getAdapterList() {
		return adapterList;
	}

}