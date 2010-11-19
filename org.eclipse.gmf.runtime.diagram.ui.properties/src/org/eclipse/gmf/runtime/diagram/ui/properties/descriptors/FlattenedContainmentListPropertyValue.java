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

package org.eclipse.gmf.runtime.diagram.ui.properties.descriptors;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.gmf.runtime.emf.ui.properties.descriptors.EObjectContainmentListPropertyValue;

/**
 * Property source object for containment lists, which flattens the presentation of the propeties.
 * It does not display each individual object as a property, but rather takes properties of all
 * the objects on the list and displays them all as if they were properties of one object (the list).
 * 
 * Use for Styles containment list.
 * 
 * @author nbalaba
 */
public class FlattenedContainmentListPropertyValue
	extends EObjectContainmentListPropertyValue {
	
	// local cache of property id vs property source object it belongs to
	private Map sources; 

	/**
	 * Create an instance of the <code>FlattenedContainmentListPropertyValue</code>
	 * 
	 * @param target - contaiment list 
	 */

	public FlattenedContainmentListPropertyValue(
			EObjectContainmentEList target) {
		super(target);

	}
	
	/*
	 * @return - Returns local cache of property id vs property source object it belongs to
	 */
	private Map getSources() {
		if(sources == null)
			sources = new HashMap();
		return sources;
	}

	/**
	 * Create and cache property descriptors for the object on the list
	 * 
	 * @param object - object on the containment list
	 */
	protected void createDescriptor(EObject object) {
		IPropertySource propertySource = propertiesProvider
			.getPropertySource(object);

		IPropertyDescriptor[] object_descriptors = propertySource
			.getPropertyDescriptors();
		for (int i = 0; i < object_descriptors.length; i++) {
			descriptors.put(object_descriptors[i].getId(), object_descriptors[i]);
			getSources().put(object_descriptors[i].getId(), propertySource);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id) {

		IPropertySource propertySource = (IPropertySource) sources.get(id);
		return propertySource.getPropertyValue(id);
	}	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object,
	 *      java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		IPropertySource propertySource = (IPropertySource) sources.get(id);
		propertySource.setPropertyValue(id,value);

	}
}
