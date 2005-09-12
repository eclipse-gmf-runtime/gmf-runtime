/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.ui.services.modelingassistant;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * The operation used with the modeling assistant service that gets the type of
 * relationships that can be used to show related elements from a given target
 * element.
 * 
 * @author cmahoney
 */
public class GetRelTypesForSREOnTargetOperation
	implements IModelingAssistantOperation {

	/** the target: adapts to editpart, view, element, etc. */
	private final IAdaptable target;

	/**
	 * Creates a new <code>GetRelTypesForSREOnTargetOperation</code>.
	 * 
	 * @param target
	 *            the target: adapts to editpart, view, element, etc.
	 */
	protected GetRelTypesForSREOnTargetOperation(IAdaptable target) {
		this.target = target;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantOperation#getId()
	 */
	public String getId() {
		return GET_REL_TYPES_FOR_SRE_ON_TARGET_ID;
	}

	/**
	 * Returns the target. This is what will be used to assist in delayed
	 * loading of a modeling assistant provider.
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantOperation#getContext()
	 */
	public IAdaptable getContext() {
		return getTarget();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		return ((IModelingAssistantProvider) provider)
			.getRelTypesForSREOnTarget(getTarget());
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