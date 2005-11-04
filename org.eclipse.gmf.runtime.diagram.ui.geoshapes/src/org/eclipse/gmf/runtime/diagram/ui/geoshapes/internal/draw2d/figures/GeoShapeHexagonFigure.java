/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


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
	 * @param spacing <code>int</code> that is the margin between children in logical units
	 */
	public GeoShapeHexagonFigure( int width, int height, int spacing ) {
		super(width, height, spacing);
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
