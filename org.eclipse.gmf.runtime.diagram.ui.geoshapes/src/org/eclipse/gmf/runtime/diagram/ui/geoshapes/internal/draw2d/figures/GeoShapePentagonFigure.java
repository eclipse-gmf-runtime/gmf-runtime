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
 * This Figure represents a Pentagon Figure
 */
public class GeoShapePentagonFigure extends GeoShapePolygonFigure {

	/**
	 * Constructor - Creates a pentagon with a given Default size
	 * 
	 * @param width inital width of the figure
	 * @param height initial height of the figure
	 */
	public GeoShapePentagonFigure( int width, int height ) {
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
		
		int xOffset = (int)(rect.width * 0.20);
		int yOffset = (int)(rect.height * 0.40);
		
		Point p1 = new Point( rect.x + (rect.width / 2), rect.y );
		Point p2 = new Point( rect.x + rect.width - 1, rect.y + yOffset );
		Point p3 = new Point( rect.x + rect.width - xOffset, rect.y + rect.height - 1 );
		Point p4 = new Point( rect.x + xOffset, rect.y + rect.height - 1 );
		Point p5 = new Point( rect.x, rect.y + yOffset );
		
		points.addPoint( p1 );
		points.addPoint( p2 );
		points.addPoint( p3 );
		points.addPoint( p4 );
		points.addPoint( p5 );
		points.addPoint( p1 );

		return points;
	}

}
