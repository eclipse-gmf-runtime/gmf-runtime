/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.draw2d.ui.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.PointList;

/**
 * @author jschofie
 *
 * This interface is used by the Polygon Connection Anchor to obtain
 * the list of points that form a polygon.
 */
public interface IPolygonAnchorableFigure extends IFigure {

	/**
	 * This method allows the implementing class to return a
	 * list of points that represent the polygon to be connected
	 * to.
	 * @return a list of points forming the polygon
	 */
	public PointList getPolygonPoints();
}
