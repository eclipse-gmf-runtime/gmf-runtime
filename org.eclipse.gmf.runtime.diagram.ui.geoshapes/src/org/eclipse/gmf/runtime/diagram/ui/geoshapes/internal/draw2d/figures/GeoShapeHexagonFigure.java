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
 * This Figure represents a Hexagon Figure
 */
public class GeoShapeHexagonFigure extends GeoShapePolygonFigure {

	/**
	 * Constructor - Creates a hexagon with a given Default size
	 * @param width initial width of the figure
	 * @param height initial height of the figure
	 */
	public GeoShapeHexagonFigure( int width, int height ) {
		super(width, height);
	}
	
	/**
	 * This method is used to compute the shapes polygon points.
	 * This is currently based on the shapes bounding box.
	 * 
	 * @param rect the rectangle that the shape will fit in
	 */
	protected PointList calculatePoints(Rectangle rect) {

		PointList points = new PointList();
		
		Point p1 = new Point( rect.x + rect.width - 1, rect.y + ( rect.height / 2 ) );
		Point p2 = new Point( rect.x + rect.width - (rect.width * 0.225 ), rect.y );
		Point p3 = new Point( rect.x + (rect.width * 0.225 ), rect.y );
		Point p4 = new Point( rect.x, rect.y + (rect.height / 2 ) );
		Point p5 = new Point( p3.x, rect.y + rect.height - 1 );
		Point p6 = new Point( p2.x, p5.y );

		points.addPoint( p1 );
		points.addPoint( p2 );
		points.addPoint( p3 );
		points.addPoint( p4 );
		points.addPoint( p5 );
		points.addPoint( p6 );
		points.addPoint( p1 );

		return points;
	}
}
