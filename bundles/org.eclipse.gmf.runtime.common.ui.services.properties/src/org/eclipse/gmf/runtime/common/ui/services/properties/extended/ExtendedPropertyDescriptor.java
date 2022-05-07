/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.properties.extended;

import org.eclipse.gmf.runtime.common.ui.services.properties.internal.l10n.CommonUIServicesPropertiesMessages;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * @author Tauseef A. Israr Created on: Sep 9, 2002
 */
public class ExtendedPropertyDescriptor
	extends PropertyDescriptor
	implements IExtendedPropertyDescriptor {

	private IPropertySource propertySource;

	private boolean dirty = false;

	private boolean readOnly = false;

	/**
	 * The property display name
	 */
	private String displayName;

	/**
	 * @param id
	 * @param displayName
	 */
	public ExtendedPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
		this.displayName = displayName;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.services.properties.extended.IExtendedPropertyDescriptor#getPropertySource()
	 */
	public IPropertySource getPropertySource() {
		return propertySource;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.services.properties.extended.IExtendedPropertyDescriptor#isDirty()
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.services.properties.extended.IExtendedPropertyDescriptor#setDirtyFlag(boolean)
	 */
	public void setDirtyFlag(boolean dirty) {
		this.dirty = dirty;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.services.properties.extended.IExtendedPropertyDescriptor#setPropertySource(org.eclipse.ui.views.properties.IPropertySource)
	 */
	public void setPropertySource(IPropertySource propertySource) {
		this.propertySource = propertySource;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.services.properties.extended.IExtendedPropertyDescriptor#getBlank()
	 */
	public Object getBlank() {
		return CommonUIServicesPropertiesMessages.ExtendedPropertyDescriptor_blank;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.services.properties.descriptors.ICompositeSourcePropertyDescriptor#isReadOnly()
	 */
	public boolean isReadOnly() {
		return this.readOnly;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.services.properties.descriptors.ICompositeSourcePropertyDescriptor#setReadOnly(boolean)
	 */
	public void setReadOnly(boolean read) {
		this.readOnly = read;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.services.properties.descriptors.ICompositeSourcePropertyDescriptor#setPropertyValue(java.lang.Object)
	 */
	public void setPropertyValue(Object value) {
		/* method not implemented */
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertyDescriptor#isCompatibleWith(org.eclipse.ui.views.properties.IPropertyDescriptor)
	 */
	public boolean isCompatibleWith(IPropertyDescriptor anotherProperty) {
		assert null != anotherProperty;

		if (!(anotherProperty instanceof ExtendedPropertyDescriptor))
			return false;

		IExtendedPropertyDescriptor xdePropertyDescriptor = (IExtendedPropertyDescriptor) anotherProperty;

		if ((getPropertySource() == null)
			|| (xdePropertyDescriptor.getPropertySource() == null))
			return false;

		IPropertySource propertySource1 = getPropertySource();
		IPropertySource propertySource2 = xdePropertyDescriptor
			.getPropertySource();

		Object value1 = propertySource1.getPropertyValue(getId());
		Object value2 = propertySource2.getPropertyValue(getId());

		if ((value1 == null) && (value2 == null))
			return true;

		if ((value1 != null) && (value2 != null)) {
			if (!value1.equals(value2)) {
				setDirtyFlag(true);
			}
		} else {
			setDirtyFlag(true);
		}
		return true;
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertyDescriptor#getDisplayName()
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.services.properties.extended.IExtendedPropertyDescriptor#setDisplayName(java.lang.String)
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.services.properties.descriptors.ICompositeSourcePropertyDescriptor#getPropertyValue()
	 */
	public Object getPropertyValue() {
		return getPropertySource().getPropertyValue(getId());
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.services.properties.descriptors.ICompositeSourcePropertyDescriptor#resetPropertyValue()
	 */
	public void resetPropertyValue() {
		// do nothing by default

	}

}