/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.properties;

import org.eclipse.gmf.runtime.common.core.service.IProvider;


/**
 * An interface to be implemented by the property modifier classes.
 * 
 * A property modifier is responsible for modifying properties, contributed by 
 * a property provider(s). A property modifier is attached to a property provider(s).
 * After the propety provider is done with property contribution, the modifier is
 * applied to the properties, in order to write-protect and/or set flags and/or override
 * display names.
 * 
 * If the modifier is to be applied to all providers - the XML declaration of the modifier
 * should specify the class of the provider as a '*'. 
 * 
 * @author nbalaba
 */
public interface IPropertiesModifier extends IProvider {
	
	/**
	 * Apply code <code>ICompositePropertySource</code>
	 * 
	 * @param propertySource <code>ICompositePropertySource</code> to be applied
	 */
	public void apply(ICompositePropertySource propertySource);

}
