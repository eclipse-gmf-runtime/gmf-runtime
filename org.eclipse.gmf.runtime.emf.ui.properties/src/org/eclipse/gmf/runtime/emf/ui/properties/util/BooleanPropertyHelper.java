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

package org.eclipse.gmf.runtime.emf.ui.properties.util;

import org.eclipse.emf.ecore.EObject;

/**
 * Helper class used by boolean-based property descriptors and cell editors. It
 * provides for conversion from/to objects used by cell editors. It also
 * provides abstract methods to set/get the actual property.
 * 
 * @author dmisic
 */
public abstract class BooleanPropertyHelper {

	/**
	 * Integer constant representing the boolean 'false'
	 */
	private final static Integer INT_FALSE = new Integer(0);

	/**
	 * Integer constant representing the boolean 'true'
	 */
	private final static Integer INT_TRUE = new Integer(1);

	/**
	 * The element that owns the property
	 */
	private EObject element;

	/**
	 * Constructor
	 * 
	 * @param element The element that owns the property
	 */
	public BooleanPropertyHelper(EObject element) {
		super();
		this.element = element;
	}

	/**
	 * Checks if the provided object is compatible with the boolean based
	 * property. This implementation requires the value to be of type Integer.
	 * This method may be overwritten but only in conjunction with setValue().
	 * 
	 * @param obj Object to be checked
	 * @return 'true' if the object is compatible
	 */
	public boolean isCompatible(Object obj) {
		return (obj != null && obj instanceof Integer);
	}

	/**
	 * Sets the value to the boolean property. If the object (value) is not
	 * compatible, throws the IllegalArgumentException exception. This
	 * implementation requires the value to be of type Integer. This method may
	 * be overwritten but only in conjunction with isCompatible().
	 * 
	 * @param value Value to be set
	 */
	public void setValue(Object value) {
		if (!isCompatible(value)) {
			throw new IllegalArgumentException();
		}

		boolean boolValue = false;
		if (((Integer) value).intValue() == 1) {
			boolValue = true;
		}
		setBooleanValue(boolValue);
	}

	/**
	 * Clients should overwrite to set the boolean value to the property.
	 * 
	 * @param value Boolean value to be set
	 */
	abstract protected void setBooleanValue(boolean value);

	/**
	 * Gets the Integer representation of the boolean property.
	 * 
	 * @return Integer representation of the boolean property
	 */
	public Integer getValue() {
		if (getBooleanValue()) {
			return INT_TRUE;
		}
		return INT_FALSE;
	}

	/**
	 * Clients should overwrite to get the boolean value from the property.
	 * 
	 * @return Boolean value from the property
	 */
	abstract protected boolean getBooleanValue();

	/**
	 * @return Returns the element that owns the property.
	 */
	public EObject getElement() {
		return element;
	}
}
