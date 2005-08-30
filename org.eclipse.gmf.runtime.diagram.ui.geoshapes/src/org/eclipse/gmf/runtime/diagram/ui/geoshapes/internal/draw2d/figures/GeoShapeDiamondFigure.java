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
 * This Figure represents a Diamond Figure
 */
public class GeoShapeDiamondFigure extends GeoShapePolygonFigure { 

	/**
	 * Constructor - Creates a diamond with a given Default size
	 * 
	 * @param width initial width of the figure
	 * @param height initial height of the figure
	 */
	public GeoShapeDiamondFigure(int width, int height) {
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
		
		int halfX = rect.x + (rect.width / 2);
		int halfY = rect.y + (rect.height / 2);
		
		Point p1 = new Point( halfX, rect.y );
		Point p2 = new Point( rect.x + rect.width - 1, halfY );
		Point p3 = new Point( halfX, rect.y + rect.height - 1 );
		Point p4 = new Point( rect.x, halfY );
		
		points.addPoint( p1 );
		points.addPoint( p2 );
		points.addPoint( p3 );
		points.addPoint( p4 );
		points.addPoint( p1 );	
		
		return points;
	}
	
}
