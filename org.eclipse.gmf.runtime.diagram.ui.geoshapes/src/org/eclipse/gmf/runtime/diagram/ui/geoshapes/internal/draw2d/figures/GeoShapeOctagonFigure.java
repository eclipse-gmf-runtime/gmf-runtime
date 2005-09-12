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
 * This Figure represents a Octagon Figure
 */
public class GeoShapeOctagonFigure extends GeoShapePolygonFigure {

	/**
	 * Constructor - Creates a octagon with a given Default size
	 * 
	 * @param width initial width of the figure
	 * @param height initial height of the figure
	 */
	public GeoShapeOctagonFigure( int width, int height ) {
		super(width, height);
	}
		

	/**
	 * This method is used to compute the shapes polygon points.
	 * This is currently based on the shapes bounding box.
	 * 
	 * @param rect the rectangle that the shape will fit in
	 */
	protected PointList calculatePoints(Rectangle rect) {

		int xOffset = (int) (rect.width * 0.275);
		int yOffset = (int) (rect.height * 0.275);
		
		PointList points = new PointList();

		Point p1 = new Point( rect.x, rect.y + yOffset );
		Point p2 = new Point( rect.x + xOffset, rect.y );
		Point p3 = new Point( rect.x + rect.width - xOffset, rect.y );
		Point p4 = new Point( rect.x + rect.width - 1, rect.y + yOffset );
		Point p5 = new Point( rect.x + rect.width - 1, rect.y + rect.height - yOffset );
		Point p6 = new Point( rect.x + rect.width - xOffset, rect.y + rect.height - 1 );
		Point p7 = new Point( rect.x + xOffset, rect.y + rect.height -1 );
		Point p8 = new Point( rect.x, rect.y + rect.height - yOffset );
	
		points.addPoint( p1 );
		points.addPoint( p2 );
		points.addPoint( p3 );
		points.addPoint( p4 );
		points.addPoint( p5 );
		points.addPoint( p6 );
		points.addPoint( p7 );
		points.addPoint( p8 );
		points.addPoint( p1 );
			
		return points;
	}
}
