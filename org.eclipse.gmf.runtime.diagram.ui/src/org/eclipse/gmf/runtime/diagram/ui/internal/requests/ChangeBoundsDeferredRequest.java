/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
