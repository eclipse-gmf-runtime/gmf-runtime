/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.draw2d.figures;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author jschofie
 *
 * This Figure represents a Triangle Figure
 */
public class GeoShapeTriangleFigure extends GeoShapePolygonFigure {

	/**
	 * Constructor - Creates a Triangle with a given Default size
	 * 
	 * @param base inital length of the squares base
	 */
	public GeoShapeTriangleFigure( int base ) {
		super(base, base);
	}
	
	/**
	 * This method is used to compute the shapes polygon points.
	 * This is currently based on the shapes bounding box.
	 * 
	 * @param rect the rectangle that the shape will fit in
	 */
	protected PointList calculatePoints(Rectangle rect) {

		PointList points = new PointList();
		
		Point p1 = new Point( rect.x + ( rect.width / 2 ), rect.y );
		Point p2 = new Point( rect.x, rect.y + rect.height - 1 );
		Point p3 = new Point( rect.x + rect.width - 1, rect.y + rect.height - 1 );

		points.addPoint( p1 );
		points.addPoint( p2 );
		points.addPoint( p3 );
		points.addPoint( p1 );
		
		return points;
	}

}
