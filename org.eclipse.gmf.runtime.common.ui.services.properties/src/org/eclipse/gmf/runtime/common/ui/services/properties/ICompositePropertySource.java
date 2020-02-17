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

package org.eclipse.gmf.runtime.common.ui.services.properties;

import org.eclipse.ui.views.properties.IPropertySource;

/**
 * The composite property source is a linked list of composite property sources. 
 * This allows the property service to chain property sources contributed by the different 
 * providers
 */
public interface ICompositePropertySource
	extends IPropertySource {

	/**
	 * Add another composite property source to the linked list
	 * 
	 * @param source - a composite property source to be added to the linked list
	 */
	public void addPropertySource(ICompositePropertySource source);

}
