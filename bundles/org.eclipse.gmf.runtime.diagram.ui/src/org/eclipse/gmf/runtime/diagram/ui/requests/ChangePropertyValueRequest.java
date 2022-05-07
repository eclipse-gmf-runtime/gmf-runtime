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

package org.eclipse.gmf.runtime.diagram.ui.requests;

import org.eclipse.gef.Request;

/**
 * @author chmahone
 *
 * This class can be used for any request that simply needs
 * to save a new value.  For example, the type could be the
 * action id and the value would be some new value that the
 * receiver of this request knows how to handle.
 */
public class ChangePropertyValueRequest extends Request {
	private Object value;
	private String propertyID;
	final private String propertyName;

	/**
	 * Constructor for ChangePropertyValueRequest
	 * @param propertyName The name of the property
	 * @param propertyID String value representing the property ID to change
	 */
	public ChangePropertyValueRequest(String propertyName, String propertyID) {
		super(RequestConstants.REQ_PROPERTY_CHANGE);
		this.propertyName = propertyName;
		this.propertyID = propertyID;
	}
	
	/**
	 * Constructor for ChangePropertyValueRequest
	 * @param propertyName The name of the property
	 * @param propertyID String value representing the property ID to change
	 * @param value Object which is to be the new value of the property
	 */
	public ChangePropertyValueRequest(String propertyName, String propertyID, Object value) {
		super(RequestConstants.REQ_PROPERTY_CHANGE);
		this.propertyName = propertyName;
		this.propertyID = propertyID;
		this.value = value;
	}
	
	/**
	 * Gets the property name
     * @return the property name
     */
    public String getPropertyName() {
		return propertyName;
	}
	
	/**
	 * Gets the property ID
     * @return the property ID
     */
    public String getPropertyID() {
		return propertyID;
	}
	
	/**
	 * Returns the value.
	 * @return Object the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 * @param value The value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

}
