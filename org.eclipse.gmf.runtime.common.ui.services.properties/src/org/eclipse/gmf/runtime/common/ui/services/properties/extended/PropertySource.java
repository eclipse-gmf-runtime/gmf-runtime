/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.properties.extended;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import org.eclipse.gmf.runtime.common.ui.services.properties.ICompositePropertySource;
import org.eclipse.gmf.runtime.common.ui.services.properties.internal.l10n.CommonUIServicesPropertiesMessages;

/**
 * @author Tauseef A. Israr Created on: Aug 27, 2002
 *  
 */
public class PropertySource implements IExtendedPropertySource {

    /**
     * refernce to the model element to whose properties are represented by this
     * propertysource instance
     */
    private Object element;

    /**
     * map containing properties key: id Value: property
     */
    protected Map properties;

    /**
     * map containing propertyDescriptors key: id value: propertydescriptor
     */
    protected Map propertyDescriptors;



    /**
     * A flag which is set when we want to show an editable value other than the
     * real editable value incase of mulitple select unequality.
     */
    private boolean flag = false;

    /** <code>ICompositePropertySource</code> */
    protected ICompositePropertySource enclosed = null;

    /**
     * basic constructor
     * 
     */
    public PropertySource() {
        propertyDescriptors = new HashMap();
        properties = new HashMap();
    }

    /**
     * Add enclosed property source object (a child) - this creates a linked
     * list of property sources
     * 
     * @param source -
     *            an eclosed (a child) property source object
     */
    public void addPropertySource(ICompositePropertySource source) {
        assert (this != source); // self is not allowed - this will
        // result in stack overflows

        if (this.enclosed == null)
            enclosed = source;

        else
            enclosed.addPropertySource(source);

    }

    /**
     * The constructor which accepts an element object as an argument.
     * 
     * @param element
     *            The element which is in the selection
     */
    public PropertySource(Object element) {
        this();
        this.element = element;
    }

    /**
     * 
     * getter for the editable value
     * 
     * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
     */
    public Object getEditableValue() {
        if (isDirty()) {
            setDirty(false);
            return CommonUIServicesPropertiesMessages.ExtendedPropertyDescriptor_blank;
        }
        if (getElement() == null && enclosed != null)
            return enclosed.getEditableValue();

        return getElement();
    }

    /**
     * Setter of the editor value
     * 
     * @param object
     * @deprecated
     */
    public void setEditableValue(Object object) {
    	assert (false) : "This method is obsolete and should not be used"; //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
     */
    public IPropertyDescriptor[] getPropertyDescriptors() {
        List list = new ArrayList(propertyDescriptors.values());
        IPropertyDescriptor[] descriptors = new IPropertyDescriptor[list.size()];
        System.arraycopy(list.toArray(), 0, descriptors, 0, list.size());

        if (enclosed != null) {
            IPropertyDescriptor[] enclosedDecriptors = enclosed
                    .getPropertyDescriptors();
            IPropertyDescriptor[] all = new IPropertyDescriptor[descriptors.length
                    + enclosedDecriptors.length];
            System.arraycopy(enclosedDecriptors, 0, all, 0,
                    enclosedDecriptors.length);
            System.arraycopy(descriptors, 0, all, enclosedDecriptors.length,
                    descriptors.length);

            return all;

        }

        return descriptors;
    }

    /**
     * Adds propertyDescriptor to this property source. Also sets back link from
     * property descriptor to this property source.
     * 
     * @param propertyDescriptor IExtendedPropertyDescriptor descriptor to be added
     */
    public void addPropertyDescriptor(
            IExtendedPropertyDescriptor propertyDescriptor) {
        assert null != propertyDescriptor;

        propertyDescriptors.put(propertyDescriptor.getId(), propertyDescriptor);
        propertyDescriptor.setPropertySource(this);

    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(Object)
     */
    public Object getPropertyValue(Object id) {

        assert null != id;

        if (properties.containsKey(id)) {
            IExtendedPropertyDescriptor propertyDescriptor = (IExtendedPropertyDescriptor) propertyDescriptors
                    .get(id);

            Object[] args = new Object[1];
            args[0] = id;
            String message = MessageFormat.format(CommonUIServicesPropertiesMessages.PropertySource__ERROR__descriptorError,
            	args);

            assert null != propertyDescriptor : message;
            if (propertyDescriptor.isDirty()) {
                propertyDescriptor.setDirtyFlag(false);
                if (properties.get(id) instanceof IExtendedPropertySource) {
                    ((IExtendedPropertySource) properties.get(id))
                            .setDirty(true);
                    return properties.get(id);
                }
                return propertyDescriptor.getBlank();
            } else
                return properties.get(id);
        }

        if (enclosed != null)
            return enclosed.getPropertyValue(id);

        return null;
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource#isPropertySet(Object)
     */
    public boolean isPropertySet(Object id) {

        assert null != id;
        
        if (propertyDescriptors.containsKey(id))
            return true;

        if (enclosed != null)
            return enclosed.isPropertySet(id);

        return false;
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(Object)
     */
    public void resetPropertyValue(Object id) {
        /* method not implemented */
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(Object,
     *      Object)
     */
    public void setPropertyValue(Object id, Object value) {
        if (propertyDescriptors.containsKey(id)) {

            if (isValueOkay(id, value)) {
                // necessary to avoid infinite loops
                Object oldValue = getPropertyValue(id);
                if ((oldValue != null && oldValue.equals(value))
                        || (oldValue == null && value == null)) {
                    return;
                }
                boolean failed = true;
                addProperty(id, value);

                try {
                    getPropertyDescriptor(id).setPropertyValue(value);
                    failed = false;
                } finally {
                    if (failed) {
                        addProperty(id, oldValue);
                    }
                }
            }
        } else if (enclosed != null)
            enclosed.setPropertyValue(id, value);

    }

    /**
     * Returns the element.
     * 
     * @return Object
     */
    public Object getElement() {
        return element;
    }

    /**
     * Sets the element.
     * 
     * @param element
     *            The modelElement to set
     */
    public void setElement(Object element) {
        assert null != element;
        this.element = element;
    }

    /**
     * add properties to the properties map
     * 
     * @param id Object
     * @param property Object
     */
    public void addProperty(Object id, Object property) {
        properties.put(id, property);
    }

    /**
     * removes properties from the properties map
     * 
     * @param id Object
     */
    public void removeProperty(Object id) {
        assert null != id;
        propertyDescriptors.remove(id);
        properties.remove(id);
    }

    /**
     * Gets <code>IExtendedPropertyDescriptor</code>
     * 
     * @param id
     *            The id of the property descriptor.
     * @return the <code>IExtendedPropertyDescriptor</code>
     */
    public IExtendedPropertyDescriptor getPropertyDescriptor(Object id) {
        assert null != id;

        return (IExtendedPropertyDescriptor) propertyDescriptors.get(id);
    }

    /**
     * @see org.eclipse.gmf.runtime.common.ui.services.properties.extended.IExtendedPropertySource#isDirty()
     */
    public boolean isDirty() {
        return flag;
    }

    /**
     * @see org.eclipse.gmf.runtime.common.ui.services.properties.extended.IExtendedPropertySource#setDirty(boolean)
     */
    public void setDirty(boolean flag) {
        this.flag = flag;
    }

    /**
     * Returns the properties.
     * 
     * @return Map
     */
    public Map getProperties() {
        return properties;
    }

    /**
     * Sets the properties.
     * 
     * @param properties
     *            The properties to set
     */
    public void setProperties(Map properties) {
        this.properties = properties;
    }

    /**
     * Sets the propertyDescriptors.
     * 
     * @param propertyDescriptors
     *            The propertyDescriptors to set
     */
    public void setPropertyDescriptors(Map propertyDescriptors) {
        this.propertyDescriptors = propertyDescriptors;
    }

    /**
     * @see org.eclipse.gmf.runtime.common.ui.services.properties.extended.IExtendedPropertySource#refresh()
     */
    public void refresh() {
        /* method not implemented */
    }

    /**
     * checks to see if the value entered is the same as the BLANK value
     * displayed in multiple select. If it is the same as BLANK value, return
     * false; else return true
     */
    public boolean isValueOkay(Object id, Object value) {
        if (value == null)
            return true;
        if (value instanceof String) {
            if (((String) value).equals(CommonUIServicesPropertiesMessages.ExtendedPropertyDescriptor_blank))
                return false;
        }
        return true;
    }

}