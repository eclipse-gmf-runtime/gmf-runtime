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

package org.eclipse.gmf.runtime.diagram.ui.dialogs.sortfilter;


/**
 * Wrapper class for CompartmentList item's.  Wraps the item along with a sort
 * ordinal and an visibility flag.
 * 
 * @author jcorchis
 */
public class SortFilterElement {
	
	// The visibility of the element
	private boolean visible = true;
	// The view reference
	private Object data = null;	
	

	
	/**
	 * Constructor for the 
	 * @param visible visible flag
	 * @param data the data associated with the filter element
	 */	
	public SortFilterElement(boolean visible, Object data) {
		this.data = data;	
	}
	
	
	/**
	 * Returns <tt>true</tt> if the compartment item is not filtered.
	 * @return <tt>true</tt> if the compartment item is not filtered.
	 */	
	public boolean isVisible() {
		return visible;
	}	
	
	/**
	 * Sets the visiblilty of this element
	 * @param visible
	 */	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	/**
	 * Returns the view associated with this list compartment item.
	 * @return the view associated with this list compartment item.
	 */
	public Object getData() {
		return data;
	} 
	
	/**
	 * check if this filter equals the passed filter
	 * @param other the other filter to check againest
	 * @return <tt>true</tt> if thery are equal other wise <tt>false</tt>
	 */
	public boolean equals(SortFilterElement other) {
		return data.equals(other.getData());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return data.hashCode();
	}
}
