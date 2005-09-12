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

package org.eclipse.gmf.runtime.emf.ui.properties.descriptors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import org.eclipse.gmf.runtime.common.ui.services.properties.PropertiesServiceAdapterFactory;

/**
 * A property source object that wraps around a containment list, presenting each item on the list
 * as if it was a property. 
 * 
 * @author nbalaba
 */
public class EObjectContainmentListPropertyValue
	implements IPropertySource {

	// properties provider to obtain properties of the objects on the list
	protected static final PropertiesServiceAdapterFactory propertiesProvider = new PropertiesServiceAdapterFactory();

	// the containment list itself
	protected EObjectContainmentEList target; 

	// local descritpors cache, where key is the property id and a value is a descritor for the property
	protected Map descriptors;

	// a label provider
	protected ILabelProvider labelProvider;

	/*
	 * @return - a default label provider
	 */
	private static ILabelProvider getDefaultLabelProvider() {
		return new LabelProvider() {

			public String getText(Object anObject) {
				return ""; //$NON-NLS-1$				
			}

			public Image getImage(Object anObject) {
				return null;
			}
		};
	}

	/**
	 *  Create an instance of the <code>EObjectContainmentListPropertyValue</code>
	 * 
	 * @param target - contaiment list 
	 * @param labelProvider  - label provider 
	 */
	public EObjectContainmentListPropertyValue(EObjectContainmentEList target,
			ILabelProvider labelProvider) {
		super();

		this.target = target;
		this.descriptors = new HashMap(target.size());
		this.labelProvider = labelProvider;

		for (Iterator e = target.iterator(); e.hasNext();) {
			EObject object = (EObject) e.next();
			createDescriptor(object);
		}

	}

	/**
	 * Create an instance of the <code>EObjectContainmentListPropertyValue</code>
	 * 
	 * @param target - contaiment list 
	 */
	public EObjectContainmentListPropertyValue(EObjectContainmentEList target) {
		this(target, getDefaultLabelProvider());
	}

	/**
	 * Create and cache property descriptor for the object on the list
	 * 
	 * @param object - object on the containment list
	 */
	protected void createDescriptor(EObject object) {
		PropertyDescriptor descriptor = new PropertyDescriptor(object, object
			.eClass().getName());
		descriptor.setLabelProvider(labelProvider);
		descriptors.put(object, descriptor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
	 */
	public Object getEditableValue() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List list = new ArrayList(descriptors.values());
		IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[list
			.size()];
		System
			.arraycopy(list.toArray(), 0, propertyDescriptors, 0, list.size());

		return propertyDescriptors;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id) {

		return propertiesProvider.getPropertySource(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#isPropertySet(java.lang.Object)
	 */
	public boolean isPropertySet(Object id) {
		return descriptors.containsKey(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java.lang.Object)
	 */
	public void resetPropertyValue(Object id) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object,
	 *      java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		// do notning - this a read-only property source

	}

	/**
	 * @return Returns the labelProvider.
	 */
	public ILabelProvider getLabelProvider() {
		return labelProvider;
	}

	/**
	 * @param labelProvider
	 *            The labelProvider to set.
	 */
	public void setLabelProvider(ILabelProvider labelProvider) {
		this.labelProvider = labelProvider;
	}

	/**
	 * @return Returns the target.
	 */
	public EObjectContainmentEList getTarget() {
		return target;
	}
}