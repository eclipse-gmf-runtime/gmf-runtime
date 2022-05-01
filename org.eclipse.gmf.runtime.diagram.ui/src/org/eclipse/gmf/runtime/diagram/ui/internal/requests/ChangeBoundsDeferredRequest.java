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

package org.eclipse.gmf.runtime.diagram.ui.internal.requests;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;

/**
 * @author sshaw
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
