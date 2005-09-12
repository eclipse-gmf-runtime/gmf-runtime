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
