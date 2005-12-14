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


/**
 * @author mmostafa
 *
 * This class can be used for any request that simply needs change the value 
 * of notation View's child property.
 * The Notation View type and the property Id will give the receiver of the 
 * request a chance to locate the View's child and get the property to change
 * on it. 
 * The request hold the Property Id to indicate which property to 
 * change and a Notation View type which will be used to find the child that
 * will be changed.
 */
package org.eclipse.gmf.runtime.diagram.ui.requests;


public class ChangeChildPropertyValueRequest
	extends ChangePropertyValueRequest {
	
	// variable to hold the notation View type
	private String notationViewType;
	
	
	/**
	 * Constructor for ChangeChildPropertyValueRequest
	 * @param propertyName The name of the property
	 * @param propertyID String value representing the property ID to change
	 * @param the Notation view type
	 */
	public ChangeChildPropertyValueRequest(String propertyName, String propertyID, String chldSemanticHint) {
		super(propertyName, propertyID);
		setType(RequestConstants.REQ_CHILD_PROPERTY_CHANGE);
		notationViewType = chldSemanticHint;
	}
	
	/**
	 * getter for the notation view type
	 * @return the notation view type associated with this request
	 */
	public String getNotationViewType(){
		return notationViewType;
	}
	
	/**
	 * Constructor for ChangeChildPropertyValueRequest
	 * @param propertyName The name of the property
	 * @param propertyID String value representing the property ID to change
	 * @param value Object which is to be the new value of the property
	 * @param the Notation view type 
	 */
	public ChangeChildPropertyValueRequest(String propertyName, String propertyID, Object value, String chldSemanticHint) {
		super(propertyName,propertyID,value);
		setType(RequestConstants.REQ_CHILD_PROPERTY_CHANGE);
		notationViewType = chldSemanticHint;
	}

}
