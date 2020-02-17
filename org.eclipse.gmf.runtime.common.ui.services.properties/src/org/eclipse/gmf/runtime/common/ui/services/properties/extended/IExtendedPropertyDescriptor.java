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
