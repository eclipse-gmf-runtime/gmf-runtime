/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.diagram.ui.requests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.Request;

import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;

/**
 * A Request to change the Z-Order
 */
/*
 * @canBeSeenBy %partners
 */
public class ZOrderRequest extends Request {
	
	/**
	 * the type for bring to front request 
	 */
	public static final String REQ_BRING_TO_FRONT = ActionIds.ACTION_BRING_TO_FRONT;
	/**
	 * the type for send to back request 
	 */
	public static final String 	REQ_SEND_TO_BACK = ActionIds.ACTION_SEND_TO_BACK;
	/**
	 * the type for bring forward request 
	 */
	public static final String 	REQ_BRING_FORWARD = ActionIds.ACTION_BRING_FORWARD;
	
	/**
	 * the type for send backward request 
	 */
	public static final String REQ_SEND_BACKWARD = ActionIds.ACTION_SEND_BACKWARD;

	/** List of <code>EditPart</code> objects to be ordered */
	protected List editParts = null;

	/**
	 * Creates a ZOrderRequest with a specified type.
	 * @param type 
	 * @todo Generated comment
	 */
	public ZOrderRequest(String type) {
		super(type);
	}
	
	/**
	 * Sets the editparts to order.
	 * @param theEditParts List of <code>EditPart</code> objects
	 */
	public void setPartsToOrder(List theEditParts) {
		editParts = new ArrayList(theEditParts);
	}

	/**
	 * Gets the editparts to order.
	 * @return List of <code>EditPart</code> objects;
	 * null if this was never set
	 */
	public List getPartsToOrder() {
		return editParts;
	}

}
