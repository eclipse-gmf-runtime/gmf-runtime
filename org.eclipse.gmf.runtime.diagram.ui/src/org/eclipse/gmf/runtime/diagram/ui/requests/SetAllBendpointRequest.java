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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.Request;

/**
 * @author sshaw
 */
public class SetAllBendpointRequest extends Request {
	private PointList points;
	private Point ptSourceRef = null;
	private Point ptTargetRef = null;
	
	/**
	 * Method SetAllBendPointsRequest.
	 * @param sz
	 * @param points
	 */
	public SetAllBendpointRequest(String sz, PointList points) {
		super(sz);
		this.points = points;
	}
	
	/**
	 * Method SetAllBendPointsRequest.
	 * @param sz
	 * @param points
	 * @param ptSourceRef
	 * @param ptTargetRef
	 */
	public SetAllBendpointRequest(String sz, PointList points, Point ptSourceRef, Point ptTargetRef) {
		super(sz);
		this.points = points;
		this.ptSourceRef = ptSourceRef;
		this.ptTargetRef = ptTargetRef;
	}
	
	/**
	 * Method setPoints.
	 * @param points
	 */
	public void setPoints(PointList points) {
		this.points = points;
	}
	
	/**
	 * Method getPoints.
	 * @return PointList
	 */
	public PointList getPoints() {
		return points;
	}
	
	/**
	 * @return Returns the ptRef1.
	 */
	public Point getSourceReference() {
		return ptSourceRef;
	}
	/**
	 * @return Returns the ptRef2.
	 */
	public Point getTargetReference() {
		return ptTargetRef;
	}
}
