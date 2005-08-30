/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.properties.descriptors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import org.eclipse.gmf.runtime.common.ui.services.properties.ICompositePropertySource;

/**
 * A concrete implementation of the <code>ICompositePropertySource</code>
 * interface. A composite property source - is a <code>IPropertySource</code>
 * object, which allows link <code>ICompositePropertySource</code> object into
 * a linked list structure. The underlaying linked list is transparent to a
 * client - from outside it is a <code>IPropertySource</code> object.
 */
public class CompositePropertySource implements ICompositePropertySource {

    // the source of the properties
    private Object object = null;

    // child source
    private ICompositePropertySource enclosed = null;

    // category of the source - each descriptor inherits that
    private String category = null;

    // a local cache of the local to this source object property descriptors vs
    // their ids
    private Map propertyDescriptors = new HashMap();

    /**
     * Create an instance of the <code>ICompositePropertySource</code> for the
     * given object. This creates a single link object, ready to be appended to
     * an existing linked list, or become a head of a new one.
     * 
     * @param object -
     *            the target of the properties
     */
    public CompositePropertySource(Object object) {
        super();
        this.object = object;
    }

    /**
     * Create an instance of the <code>ICompositePropertySource</code> for the
     * given object. This creates a single link object, ready to be appended to
     * an existing linked list, or become a head of a new one.
     * 
     * The descriptors in the scope of this link by default will be assigned the
     * given category
     * 
     * @param object -
     *            the target of the properties
     * @param category -
     *            the property category to be assigned to all descrtiptors in
     *            the scope of this link
     */
    public CompositePropertySource(Object object, String category) {
        this(object);
        this.category = category;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.common.ui.services.properties.ICompositePropertySource#addPropertySource(org.eclipse.gmf.runtime.common.ui.services.properties.ICompositePropertySource)
     */
    public void addPropertySource(ICompositePropertySource source) {
        assert this != source : "self is not allowed - this will result in stack overflows"; //$NON-NLS-1$

        if (this.enclosed == null)
            enclosed = source;

        else
            enclosed.addPropertySource(source);

    }

    /**
     * Adds a new property descriptor to the property source. If there is
     * a property with that id already, nothing will happen
     * 
     * @param descriptor -
     *            a new property descriptor to add to the property source
     */
    public void addPropertyDescriptor(
            ICompositeSourcePropertyDescriptor descriptor) {

        if (!isPropertySet(descriptor.getId())) {
            if (descriptor.getCategory() == null)
                descriptor.setCategory(getCategory());

            getLocalDescriptors().put(descriptor.getId(), descriptor);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
     */
    public Object getEditableValue() {
        if (getObject() == null && enclosed != null)
            return enclosed.getEditableValue();

        return getObject();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
     */
    public IPropertyDescriptor[] getPropertyDescriptors() {
        List list = new ArrayList(getLocalDescriptors().values());
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

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
     */
    public Object getPropertyValue(Object id) {
        ICompositeSourcePropertyDescriptor descriptor = (ICompositeSourcePropertyDescriptor) getLocalDescriptors()
                .get(id);
        return descriptor != null ? descriptor.getPropertyValue()
                : ((enclosed != null) ? enclosed.getPropertyValue(id) : null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.IPropertySource#isPropertySet(java.lang.Object)
     */
    public boolean isPropertySet(Object id) {

        return getLocalDescriptors().containsKey(id) ? true
                : (enclosed != null ? enclosed.isPropertySet(id) : false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java.lang.Object)
     */
    public void resetPropertyValue(Object id) {

        ICompositeSourcePropertyDescriptor descriptor = (ICompositeSourcePropertyDescriptor) getLocalDescriptors()
                .get(id);

        if (descriptor != null) {
            descriptor.resetPropertyValue();
        } else if (enclosed != null)
            enclosed.resetPropertyValue(id);
    }

    /**
     * This delegates to IItemPropertyDescriptor.setPropertyValue().
     */
    public void setPropertyValue(Object propertyId, Object value) {

        CompositeSourcePropertyDescriptor descriptor = (CompositeSourcePropertyDescriptor) getLocalDescriptors()
                .get(propertyId);

        if (descriptor != null) {
            descriptor.setPropertyValue(value);
        } else if (enclosed != null)
            enclosed.setPropertyValue(propertyId, value);
    }

    /**
     * Returns the category, if one is set for this link.
     * @return - the category, if one is set for this link. 
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the target of the properties.
     * @return - the target of the properties. 
     */
    public Object getObject() {
        return object;
    }

    /**
     * Returns the descriptors local to this link.
     * @return - the descriptors local to this link.
     */
    protected Map getLocalDescriptors() {
        return propertyDescriptors;
    }

    /**
     * Returns the next link on the linked list.
     * @return - the next link on the linked list.
     */
    protected ICompositePropertySource getEnclosed() {
        return enclosed;
    }

}