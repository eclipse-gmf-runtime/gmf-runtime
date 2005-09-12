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

/**
 * Helper class used by dialog cell editors in the Collection Editor.
 * 
 * There is a deficiency in Eclipse Table Viewers - they only support one 
 * cell editor per column.  
 * 
 * @author Michael Yee
 */
public class ElementValue {
    /** the element */
    final private Object element;

    /** the element's value */
    private Object value;

    /**
     * Constructor for ElementValue.
     * @param element the element
     * @param value the element's value
     */
    public ElementValue(Object element, Object value) {
        this.element = element;
        this.value = value;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return value.toString();
    }

    /**
     * Returns the element.
     * @return Object
     */
    public Object getElement() {
        return element;
    }

    /**
     * Returns the element's value.
     * @return Object
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the element's value
     * @param value the value
     */
    public void setValue(Object value) {
        this.value = value;
    }
}
