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

package org.eclipse.gmf.runtime.diagram.ui.properties.descriptors;

import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.gmf.runtime.emf.ui.properties.descriptors.EMFCompositePropertySource;
import org.eclipse.ui.views.properties.IPropertyDescriptor;


/**
 * <code>NotationPropertySource</code> a property source object wrapper around 
 * EMF based property source object for the notation elements - graphical edit parts,
 * views and styles.
 * 
 * @author nbalaba
 */
public class NotationPropertySource
	extends EMFCompositePropertySource {

	/**
	 * Create an instance of the <code>NotationPropertySource</code>
	 *  
	 * @param object - a notation element: graphical edit parts, views and styles.
	 * @param itemPropertySource - emf property source object
	 */
	public NotationPropertySource(Object object,
			IItemPropertySource itemPropertySource) {
		super(object, itemPropertySource);

	}

	/**
	 * Create an instance of the <code>NotationPropertySource</code>
	 *  
	 * @param object - a notation element: graphical edit parts, views and styles.
	 * @param itemPropertySource - emf property source object
	 * @param category - property category (usually a 'View')
	 */
	public NotationPropertySource(Object object,
			IItemPropertySource itemPropertySource, String category) {
		super(object, itemPropertySource, category);

	}	

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.ui.properties.descriptors.EMFCompositePropertySource#newPropertyDescriptor(org.eclipse.emf.edit.provider.IItemPropertyDescriptor)
	 */
	protected IPropertyDescriptor newPropertyDescriptor(
		IItemPropertyDescriptor itemPropertyDescriptor) {
		return new NotationPropertyDescriptor(object,
			itemPropertyDescriptor, getCategory());

	}	

}
