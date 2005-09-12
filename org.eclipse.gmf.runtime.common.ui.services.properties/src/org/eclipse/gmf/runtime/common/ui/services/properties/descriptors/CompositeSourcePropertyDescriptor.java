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

package org.eclipse.gmf.runtime.common.ui.services.properties.descriptors;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * A concrete implementation of the
 * <code>ICompositeSourcePropertyDescriptor</code>. A property descriptor
 * designed to work with the CompositePropertySource.
 */
public class CompositeSourcePropertyDescriptor extends PropertyDescriptor
        implements ICompositeSourcePropertyDescriptor {

    // target of the property
    private Object object = null;

    // readOnly flag
    private boolean readOnly = false;

    // custom cell editor - optional, may never be used, if sublcasses override
    // createPropertyEditor()
    private CellEditor propertyEditor = null;

    //property value cache - optional, may never be used. Subclasses should
    // override getEditableValue()
    // and setPropertyValue()
    private Object propertyValue = null;

    // a default value of a property - the one that the property will reset when
    // asked to reset to default
    private Object defaultValue = null;

    /**
     * Create an instance of the <code>CompositeSourePropertyDescriptor</code>
     * 
     * @param object 
     * @param id property id
     * @param displayName property display name
     */
    public CompositeSourcePropertyDescriptor(Object object, Object id,
            String displayName) {
        super(id, displayName);
        this.object = object;

    }

    /**
     * Returns the object target of the property
     * 
     * @return target of the property
     */
    protected Object getObject() {
        return object;
    }

    /**
     * Returns the propertyEditor.
     * 
     * @return the propertyEditor.
     */
    public CellEditor getPropertyEditor() {
        return propertyEditor;
    }

    /**
     * Returns the readOnly property
     * 
     * @return true if read only, false otherwise
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Add filter flag {@link org.eclipse.ui.views.properties.IPropertySheetEntry#FILTER_ID_EXPERT IPropertySheetEntry.FILTER_ID_EXPERT}
     * 
     * @param flag a filter flag
     */
    public void addFilterFlag(String flag) {
        assert null != flag : "flag cannot be null"; //$NON-NLS-1$

        List flags = Arrays.asList(getFilterFlags());
        if (!flags.contains(flag)) {
            int total = flags.size();
            String[] newFlags = new String[total];
            System.arraycopy(getFilterFlags(), 0, newFlags, 0, total);
            newFlags[total - 1] = flag;
            setFilterFlags(newFlags);
        }
    }

    /**
     * Returns the value of the property
     * 
     * @return the value of the property
     */
    public Object getPropertyValue() {

        Object aValue = getEditableValue();
        //		 see if we should convert the value to an editable value
        IPropertySource source = getPropertySource(aValue);
        if (source != null)
            aValue = source.getEditableValue();

        return aValue;
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.services.properties.descriptors.ICompositeSourcePropertyDescriptor#resetPropertyValue()
     */
    public void resetPropertyValue() {
        if (getDefaultValue() != null)
            setPropertyValue(getDefaultValue());
    }

    /**
     * Sets new value to the property
     * 
     * @param value the new value
     */
    public void setPropertyValue(final Object value) {

        if (value == null)
            return;

        Object oldValue = getEditableValue();

        if ((oldValue != null && oldValue.equals(value))
                || (oldValue == null && value == null))
            return;

        setValue(value);

    }

    /**
     * Utility method - all the checks and context set up are done in the
     * setPropertyValue. This just executes the set.
     * 
     * @param value the new value
     */
    protected void setValue(Object value) {
        this.propertyValue = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.IPropertyDescriptor#createPropertyEditor(org.eclipse.swt.widgets.Composite)
     */
    public CellEditor createPropertyEditor(Composite parent) {
        if (isReadOnly())
            return null;

        if (getPropertyEditor() != null) {
            if (getPropertyEditor().getControl() == null)
                getPropertyEditor().create(parent);
            return getPropertyEditor();
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.IPropertyDescriptor#isCompatibleWith(org.eclipse.ui.views.properties.IPropertyDescriptor)
     */
    public boolean isCompatibleWith(IPropertyDescriptor anotherProperty) {

        if (this == anotherProperty)
            return true;

        if (!(this.getClass().isInstance(anotherProperty)))
            return false;

        return (getCategory().equals(anotherProperty.getCategory()) && getId()
                .equals(anotherProperty.getId()));

    }

    /**
     * Parametrize cell editor creation - assign a custom cell editor to an instance variable
     *  
     * @param propertyEditor a custom cell editor
     */
    public void setPropertyEditor(CellEditor propertyEditor) {
        this.propertyEditor = propertyEditor;
    }

    /**
     * This is a utility method, which allows recursive display of the
     * properties. Should anyone desire to get rid of recursive display - this
     * should become getPropertyValue()
     * 
     * @return editable property value
     */
    protected Object getEditableValue() {
        return propertyValue;
    }

    /**
     * Returns an property source for the given value.
     * 
     * @param  value an object for which to obtain a property source or
     *         <code>null</code> if a property source is not available
     * @return an property source for the given object
     */
    protected IPropertySource getPropertySource(Object value) {

        return value == null ? null : (IPropertySource) Platform
                .getAdapterManager().getAdapter(value, IPropertySource.class);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.common.ui.services.properties.descriptors.ICompositeSourcePropertyDescriptor#setReadOnly(boolean)
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * Returns default value
     * 
     * @return the default value of the property, to which the reset to defaults
     *         will revert to. In case if this is null - reset will do nothing
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * Assigns a default value to this property
     * 
     * @param defaultValue assign a default value to this property
     */
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    /** 
     * @param value
     * @see org.eclipse.gmf.runtime.common.ui.services.properties.descriptors.ICompositeSourcePropertyDescriptor#resetPropertyValue()
     */
    public void resetPropertyValue(Object value) {
        setPropertyValue(getDefaultValue());
        
    }
}