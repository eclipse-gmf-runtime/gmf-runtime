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

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.gmf.runtime.common.ui.services.properties.descriptors.ICompositeSourcePropertyDescriptor;

/**
 * @author Tauseef A. Israr
 * Created on: Sep 3, 2002
 * 
 */
public interface IExtendedPropertyDescriptor extends ICompositeSourcePropertyDescriptor {
	
	/**
	 * Retrieve <code>IPropertySource</code>
	 * 
	 * @return an instance of <code>IPropertySource</code>
	 */
    public IPropertySource getPropertySource();

    /**
     * Sets specified <code>IPropertySource</code>
     * 
     * @param propertySource an instance of <code>IPropertySource</code>
     */
    public void setPropertySource(IPropertySource propertySource);

    /**
     * Retrieve the boolean isDirty flag.
     * 
     * @return value of isDirty flag
     */
    public boolean isDirty();
   
    /**
     * @param dirtyFlag
     */
    public void setDirtyFlag(boolean dirtyFlag);

    /**
     * This method returns a blank value for a cell.
     * 
     * @return a blank value for a cell
     */
    public Object getBlank();



    /**
     * Sets the validator for this propertyDescriptor
     * @param validator The <code>ICellEditorValidator</code>
     */
    public void setValidator(ICellEditorValidator validator);

    /**
     * Sets the display name
     * @param displayName the display name
     */
    public void setDisplayName(String displayName);

}
