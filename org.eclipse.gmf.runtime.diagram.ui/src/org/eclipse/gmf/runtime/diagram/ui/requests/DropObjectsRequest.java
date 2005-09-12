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

package org.eclipse.gmf.runtime.diagram.ui.requests;

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.swt.dnd.DND;

/**
 * A Drop request that has a List of <code>Object</code>s 
 * to drop at a specific location on the target editpart
 * The request can also return an object representing the
 * result of the drop requiredDetail
 * 
 * @author melaasar
 */
public class DropObjectsRequest extends Request
	implements DropRequest {
		
	/** the drop location */
	private Point location; 
	/** the list of objects to drop */	
	private List objects = Collections.EMPTY_LIST;
	/** the result of satisfying the drop request */
	private Object result;
	/** the allowed requiredDetail as in the DND event */
	private int allowedDetail = DND.DROP_NONE;
	/** the required DND requiredDetail */
	private int requiredDetail = DND.DROP_NONE;
	 
	/**
	 * Method CreateViewRequest.
	 */
	public DropObjectsRequest() {
		super(RequestConstants.REQ_DROP_OBJECTS);
	}

	/**
	 * Returns the objects.
	 * @return List
	 */
	public final List getObjects() {
		return objects;
	}

	/**
	 * Returns the location.
	 * @return Point
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * Returns the result of the drop request
	 * @return any DROP_* field of the <code>DND</code> interface
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * gets the drag requiredDetail
	 * @return int
	 */
	public int getRequiredDetail() {
		return requiredDetail;
	}

	/**
	 * Gets the allowed requiredDetail as in the DND event
	 * @return the allowed requiredDetail as in the DND event
	 */
	public int getAllowedDetail() {
		return allowedDetail;
	}

	/**
	 * Sets the objects.
	 * @param objects The objects to set
	 */
	public final void setObjects(List objects) {
		if (objects == null)
			this.objects = Collections.EMPTY_LIST;
		else
			this.objects = objects;
	}

	/**
	 * Sets the location.
	 * @param location The location to set
	 */
	public void setLocation(Point location) {
		this.location = location;
	}

	/**
	 * Sets the result of the drop request
	 * Editpolicies can use this method to set the result of the request
	 * @param result The result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}

	/**
	 * Sets the drag requiredDetail
	 * @param operation can be any DROP_* field of the <code>DND</code> interface
	 */
	public void setRequiredDetail(int operation) {
		this.requiredDetail = operation;
	}

	/**
	 * Sets the allowed requiredDetail as in the DND event
	 * @param allowedDetail the allowed requiredDetail in the DND event
	 */
	public void setAllowedDetail(int allowedDetail) {
		this.allowedDetail = allowedDetail;
	}

}
