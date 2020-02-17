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

package org.eclipse.gmf.runtime.emf.ui.properties.descriptors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.PropertySource;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import org.eclipse.gmf.runtime.common.ui.services.properties.ICompositePropertySource;

/**
 * <code>IItemPropertySource</code> wrapper, is a linked list of composite 
 * property sources - may contain another composite property source wrapper 
 * inside. 
 * For example, a edit part will have a composite source, consisting of two - 
 * one (outer) for the shape itself and other (enclosed) - for the underlying 
 * UML element
 * 
 * @author nbalaba
 * 
 */
public class EMFCompositePropertySource
	extends PropertySource
	implements ICompositePropertySource {

	private ICompositePropertySource enclosed = null;

	// category of the source - each descriptor inherits that
	private String category = null;

	// a local cache of the local to this source object property descriptors vs their ids
	private Map descriptors;

	// another cache - Efeatures vs descriptors - so that clients can navigate
	// through the source using feature
	private Map features;

	/**
	 * Constructs an instance of <code>EMFCompositePropertySource</code>
	 * 
	 * @param object
	 * @param itemPropertySource
	 */
	protected EMFCompositePropertySource(Object object,
		IItemPropertySource itemPropertySource) {
		super(object, itemPropertySource);
	}

	/**
	 * Constructs an instance of <code>EMFCompositePropertySource</code>
	 * 
	 * @param object
	 * @param itemPropertySource
	 * @param category category of the source - each descriptor inherits that
	 */
	public EMFCompositePropertySource(Object object,
		IItemPropertySource itemPropertySource, String category) {
		this(object, itemPropertySource);
		this.category = category;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.properties.ICompositePropertySource#addPropertySource(org.eclipse.gmf.runtime.common.ui.services.properties.ICompositePropertySource)
	 */
	public void addPropertySource(ICompositePropertySource source) {
		assert this != source: "self is the same as source"; // self is not allowed - this will //$NON-NLS-1$
		// result in stack overflows

		if (this.enclosed == null)
			enclosed = source;

		else
			enclosed.addPropertySource(source);

	}

	/*
	 * Creates a cache of property descriptors confined to this property source object
	 * without the enclosed 
	 * @return
	 */
	protected Map getLocalDescriptors() {
		if (descriptors == null) {
			descriptors = new HashMap();
			features = new HashMap();
			super.getPropertyDescriptors();
		}
		return descriptors;
	}

	/**
	 * This delegates to This method can cache the (local, not children)
	 * descriptors so that their array will be modifiable (if there is a strong
	 * requirement to do so) We can cache them (as an optimization)
	 * {@link IItemPropertySource#getPropertyDescriptors IItemPropertySource.getPropertyDescriptors}.
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] local = (IPropertyDescriptor[]) getLocalDescriptors()
			.values().toArray(
				new IPropertyDescriptor[getLocalDescriptors().size()]);

		if (enclosed != null) {
			IPropertyDescriptor[] enclosedDecriptors = enclosed
				.getPropertyDescriptors();
			IPropertyDescriptor[] all = new IPropertyDescriptor[local.length
				+ enclosedDecriptors.length];
			System.arraycopy(enclosedDecriptors, 0, all, 0,
				enclosedDecriptors.length);
			System.arraycopy(local, 0, all, enclosedDecriptors.length,
				local.length);

			return all;

		}

		return local;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
	 */
	public Object getEditableValue() {
		Object aValue = super.getEditableValue();
		if (aValue == null && enclosed != null)
			return enclosed.getEditableValue();

		return aValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.edit.ui.provider.PropertySource#createPropertyDescriptor(org.eclipse.emf.edit.provider.IItemPropertyDescriptor)
	 */
	protected IPropertyDescriptor createPropertyDescriptor(
		IItemPropertyDescriptor itemPropertyDescriptor) {
		EMFCompositeSourcePropertyDescriptor d = (EMFCompositeSourcePropertyDescriptor) newPropertyDescriptor(itemPropertyDescriptor);
		cacheDescriptor(d);
		return d;
	}

	/*
	 * Create a new property descriptor wrapper around the EMF type
	 * @param itemPropertyDescriptor - the EMF based property descriptor
	 * @return - property descriptor that wraps around IItemPropertyDescriptor
	 */
	protected IPropertyDescriptor newPropertyDescriptor(
		IItemPropertyDescriptor itemPropertyDescriptor) {
		return new EMFCompositeSourcePropertyDescriptor(object,
			itemPropertyDescriptor, getCategory());

	}

	/*
	 * Cache descriptor into the local cache - id vs descriptors, and features vs descriptors
	 */
	protected void cacheDescriptor(EMFCompositeSourcePropertyDescriptor d) {
		if (d != null) {
			getLocalDescriptors().put(d.getId(), d);
			features.put(d.getFeature(), d);
		}
	}

	/**
	 * This delegates to
	 * {@link IItemPropertyDescriptor#getPropertyValue IItemPropertyDescriptor.getPropertyValue}.
	 */
	public Object getPropertyValue(Object propertyId) {
		EMFCompositeSourcePropertyDescriptor descriptor = (EMFCompositeSourcePropertyDescriptor) getLocalDescriptors()
			.get(propertyId);
		return descriptor != null ? descriptor.getPropertyValue()
			: ((enclosed != null) ? enclosed.getPropertyValue(propertyId)
				: null);
	}

	/**
	 * This delegates to
	 * {@link IItemPropertyDescriptor#isPropertySet IItemPropertyDescriptor.isPropertySet}.
	 */
	public boolean isPropertySet(Object propertyId) {
		IItemPropertyDescriptor descriptor = itemPropertySource
			.getPropertyDescriptor(object, propertyId);
		return descriptor != null ? descriptor.isPropertySet(object)
			: ((enclosed != null) ? enclosed.isPropertySet(propertyId)
				: false);
	}

	/**
	 * This delegates to
	 * {@link IItemPropertyDescriptor#resetPropertyValue IItemPropertyDescriptor.resetPropertyValue}.
	 */
	public void resetPropertyValue(Object propertyId) {
		final EMFCompositeSourcePropertyDescriptor descriptor = (EMFCompositeSourcePropertyDescriptor) getLocalDescriptors()
			.get(propertyId);

		if (descriptor != null) {
			descriptor.resetPropertyValue();
		} else if (enclosed != null)
			enclosed.resetPropertyValue(propertyId);
	}

	/**
	 * @return - the object for which the source is providing properties
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * This delegates to
	 * {@link IItemPropertyDescriptor#setPropertyValue IItemPropertyDescriptor.setPropertyValue}.
	 */
	public void setPropertyValue(Object propertyId, final Object value) {

		final EMFCompositeSourcePropertyDescriptor descriptor = (EMFCompositeSourcePropertyDescriptor) getLocalDescriptors()
			.get(propertyId);

		if (descriptor != null) {
			descriptor.setPropertyValue(value);
		} else if (enclosed != null)
			enclosed.setPropertyValue(propertyId, value);
	}

	/**
	 * @return Returns the category.
	 */
	protected String getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 * 
	 * @param category the new category value
	 */
	public void setCategory(String category) {
		this.category = category;
		Iterator i = getLocalDescriptors().values().iterator();
		while (i.hasNext()) {
			EMFCompositeSourcePropertyDescriptor d = (EMFCompositeSourcePropertyDescriptor) i
				.next();
			d.setCategory(getCategory());
		}
	}

}
