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
 * This Figure represents a Triangle Figure
 */
public class GeoShapeTriangleFigure extends GeoShapePolygonFigure {

	/**
	 * Constructor - Creates a Triangle with a given Default size
	 * 
	 * @param height inital height of the bounding square
	 * @param spacing <code>int</code> that is the margin between children in logical units
	 */
	public GeoShapeTriangleFigure(int height, int spacing) {
		super((int)Math.round(2 * height / Math.sqrt(3)), height, spacing);
	}
	
	/**
	 * This method is used to compute the shapes polygon points.
	 * This is currently based on the shapes bounding box.
	 * 
	 * @param rect the rectangle that the shape will fit in
	 */
	protected PointList calculatePoints(Rectangle rect) {

		PointList points = new PrecisionPointList();
		
		Point p1 = new PrecisionPoint( rect.preciseX() + rect.preciseWidth() / 2, rect.y );
		Point p2 = new PrecisionPoint( rect.preciseX(), rect.preciseY() + rect.preciseHeight() - 1 );
		Point p3 = new PrecisionPoint( rect.preciseX() + rect.preciseWidth() - 1, rect.preciseY() + rect.preciseHeight() - 1 );

		points.addPoint( p1 );
		points.addPoint( p2 );
		points.addPoint( p3 );
		points.addPoint( p1 );
		
		return points;
	}

}
