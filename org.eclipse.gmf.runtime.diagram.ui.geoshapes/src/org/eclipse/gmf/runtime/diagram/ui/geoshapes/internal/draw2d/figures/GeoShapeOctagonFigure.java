/******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and others.
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
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PrecisionPointList;

/**
 * @author jschofie
 * @author aboyko
 *
 * This Figure represents a Octagon Figure
 */
public class GeoShapeOctagonFigure extends GeoShapePolygonFigure {
	
	private static double factor = 1.0 / (2.0 + Math.sqrt(2.0));

	/**
	 * Constructor - Creates a octagon with a given Default size
	 * 
	 * @param width initial width of the figure
	 * @param height initial height of the figure
	 * @param spacing <code>int</code> that is the margin between children in logical units
	 */
	public GeoShapeOctagonFigure( int width, int height, int spacing ) {
		super(width, height, spacing);
	}
	
	/**
	 * Constructor - Creates a proper octagon with a given Default height
	 * 
	 * @param height initial height of the figure
	 * @param spacing <code>int</code> that is the margin between children in logical units
	 */
	public GeoShapeOctagonFigure(int size, int spacing) {
		super(size, size, spacing);
	}
		

	/**
	 * This method is used to compute the shapes polygon points.
	 * This is currently based on the shapes bounding box.
	 * 
	 * @param rect the rectangle that the shape will fit in
	 */
	protected PointList calculatePoints(Rectangle rect) {

		double xOffset = rect.preciseWidth() * factor;
		double yOffset = rect.preciseHeight() * factor;
		
		PointList points = new PrecisionPointList();

		Point p1 = new PrecisionPoint(rect.preciseX(), rect.preciseY() + yOffset);
		Point p2 = new PrecisionPoint(rect.preciseX() + xOffset, rect.preciseY());
		Point p3 = new PrecisionPoint(rect.preciseX() + rect.preciseWidth() - xOffset, rect.preciseY());
		Point p4 = new PrecisionPoint(rect.preciseX() + rect.preciseWidth() - 1, rect.preciseY() + yOffset);
		Point p5 = new PrecisionPoint(p4.preciseX(), rect.preciseY() + rect.preciseHeight() - yOffset);
		Point p6 = new PrecisionPoint(p3.preciseX(), rect.preciseY() + rect.preciseHeight() - 1);
		Point p7 = new PrecisionPoint(p2.preciseX(), p6.preciseY());
		Point p8 = new PrecisionPoint(rect.preciseX(), p5.preciseY());
	
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
