/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.properties.extended;

import org.eclipse.gmf.runtime.common.ui.services.properties.internal.l10n.CommonUIServicesPropertiesMessages;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;

/**
 * @author Tauseef A. Israr
 * Created on: Sep 9, 2002
 */
public class ExtendedBooleanPropertyDescriptor
	extends ExtendedComboboxPropertyDescriptor {

	/**
	 * Constructor for ExtendedBooleanPropertyDescriptor.
	 * 
	 * @param id
	 * @param displayName
	 */
	public ExtendedBooleanPropertyDescriptor(Object id, String displayName) {
		super(id, displayName, new String[] { 
			CommonUIServicesPropertiesMessages.ExtendedBooleanPropertyDescriptor_False, 
			CommonUIServicesPropertiesMessages.ExtendedBooleanPropertyDescriptor_True 
		});
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertyDescriptor#getLabelProvider()
	 */
	public ILabelProvider getLabelProvider() {
		return new LabelProvider() {
			public String getText(Object object) {
				if (object instanceof Integer) {
					if (((Integer) object).intValue() == 0)
						return CommonUIServicesPropertiesMessages.ExtendedBooleanPropertyDescriptor_False;
					else if (((Integer) object).intValue() == 1)
						return CommonUIServicesPropertiesMessages.ExtendedBooleanPropertyDescriptor_True;
				}
				return (String) getBlank();
			}
		};
	}

	/**
	 * Convert value to index.
	 * 
	 * @param b 
	 * @return an instance of <code>Integer</code>
	 */
	static public Integer valueToIndex(Boolean b) {
		return new Integer(b.booleanValue() ? 1 : 0);
	}

	/**
	 * Convert value to index.
	 * 
	 * @param i
	 * @return an instance of <code>Boolean</code>
	 */
	static public Boolean indexToValue(Integer i) {
		return Boolean.valueOf(i.intValue() != 0);
	}

}
