/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.properties.extended;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import org.eclipse.gmf.runtime.common.ui.services.properties.internal.l10n.PSFResourceManager;

/**
 * @author Tauseef A. Israr Created on: Sep 9, 2002
 */
public class ExtendedPropertyDescriptor
	extends PropertyDescriptor
	implements IExtendedPropertyDescriptor {

	/** Blank extended property descriptor. */
	public static final String BLANK = PSFResourceManager
		.getI18NString("ExtendedPropertyDescriptor.blank"); //$NON-NLS-1$

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
		return BLANK;
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