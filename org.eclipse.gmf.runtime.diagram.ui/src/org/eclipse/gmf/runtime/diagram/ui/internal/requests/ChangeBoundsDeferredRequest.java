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

package org.eclipse.gmf.runtime.diagram.ui.internal.requests;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.Request;

import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;

/**
 * @author sshaw
 * @canBeSeenBy %level1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ChangeBoundsDeferredRequest extends Request {

	IAdaptable adapter;
	
	/**
	 * Constructor for ChangePropertyValueRequest
	 * @param propertyID String value representing the property ID to change
	 */
	public ChangeBoundsDeferredRequest(IAdaptable adapter) {
		super(RequestConstants.REQ_MOVE_DEFERRED);
		this.adapter = adapter;
	}
	
	/**
	 * Method getLocationAdapter.
	 * @return IAdaptable
	 */
	public IAdaptable getLocationAdapter() {
		return adapter;
	}
}
