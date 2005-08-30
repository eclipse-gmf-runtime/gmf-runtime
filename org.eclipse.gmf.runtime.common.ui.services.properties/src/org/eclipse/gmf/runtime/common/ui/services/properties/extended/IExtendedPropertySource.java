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

import java.util.Map;

import org.eclipse.gmf.runtime.common.ui.services.properties.ICompositePropertySource;

/**
 * @author Tauseef A. Israr
 *
 * This interface extends <code>IPropertySource</code> interface.
 * 
 * 
 */
public interface IExtendedPropertySource extends ICompositePropertySource {

    /**
     * Returns the element.
     * @return Object
     */
    public Object getElement();

    /**
     * add properties to the properties map
     * @param id Object
     * @param property Object 
     */
    public void addProperty(Object id, Object property);

    /**
     * removes properties from the properties map
     * @param id Object
     */
    public void removeProperty(Object id);

    /**
    * Adds propertyDescriptor to this property source.
    * @param propertyDescriptor IExtendedPropertyDescriptor that is to be added
    */
    public void addPropertyDescriptor(IExtendedPropertyDescriptor propertyDescriptor);
    
    /**
     * Set the element of which the properties belong to.
     * 
     * @param element Object The element to which the properties belong to.
     */
    public void setElement(Object element);
    
    /**
     * Set the dirty flag to boolean.  This functionality is used when the 
     * property source itself is a value of a property and we need to show 
     * some value other than the editable value in case of multiple select 
     * unequal value.
     * @param flag : Flag = true means dirty, Flag = false means not dirty.
     */
    public void setDirty(boolean flag);
    
    /**
     * Return if the dirty flag.
     * @return flag: True means dirty and viceversa.
     */
    public boolean isDirty();
    
    /**
     * Refresh properties
     */
    public void refresh();
    
    
    /**
     * Returns the properties.
     * @return Map
     */
    public Map getProperties();
    
    /**
     * Returns true if the property value is not equal to BLANK as displayed
     * in multiple select unequal values.
     * 
     * @param id property id
     * @param value value to verify
     * @return <code>true</code> if value is ok, <code>false</code> otherwise
     */
    public boolean isValueOkay(Object id, Object value);
    
}
