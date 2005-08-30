/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.requests;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.Request;

/**
 * @author sshaw
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
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
