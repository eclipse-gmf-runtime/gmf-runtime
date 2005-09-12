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

package org.eclipse.gmf.runtime.common.ui.services.properties;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * Operation which is excuted in order to apply modifiers to a property source provided by a provider
 * 
 * @author nbalaba
 * @canBeSeenBy %partners 
 */
public class ApplyModifiersOperation
	implements IOperation {

	/** A property source that will be put through the modifiers */
	protected ICompositePropertySource propertySource;
	
	/**
	 * Create a new operation object
	 * 
	 * @param propertySource - a property source that will be put through the modifiers
	 */
	public ApplyModifiersOperation(ICompositePropertySource propertySource) {
		super();
		this.propertySource = propertySource;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		assert null != provider : "provider cannot be null"; //$NON-NLS-1$

		if (provider instanceof IPropertiesModifier)
			((IPropertiesModifier) provider).apply(getPropertySource());

		return getPropertySource();
	}

	/**
	 * @return - Returns the propertySource.
	 */
	public ICompositePropertySource getPropertySource() {
		return propertySource;
	}
}