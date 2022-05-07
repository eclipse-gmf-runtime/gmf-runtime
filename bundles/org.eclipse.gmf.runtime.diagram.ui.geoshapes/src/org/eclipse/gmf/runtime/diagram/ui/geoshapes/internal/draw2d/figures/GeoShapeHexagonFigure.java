/******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
	 * Constructor - Creates a proper hexagon with a given default height
	 * @param height initial height of the figure
	 * @param spacing <code>int</code> that is the margin between children in logical units
	 */
	public GeoShapeHexagonFigure(int height, int spacing) {
		super((int) Math.round(2 * height / Math.sqrt(3)), height, spacing);
	}
	
	/**
	 * This method is used to compute the shapes polygon points.
	 * This is currently based on the shapes bounding box.
	 * 
	 * @param rect the rectangle that the shape will fit in
	 */
	protected PointList calculatePoints(Rectangle rect) {

		PointList points = new PrecisionPointList();
		
		Point p1 = new PrecisionPoint( rect.preciseX() + rect.preciseWidth() - 1, rect.preciseY() + rect.preciseHeight() / 2 );
		Point p2 = new PrecisionPoint( rect.preciseX() + 0.75 * rect.preciseWidth() , rect.preciseY() );
		Point p3 = new PrecisionPoint( rect.preciseX() + rect.preciseWidth() / 4, rect.preciseY() );
		Point p4 = new PrecisionPoint( rect.preciseX(), rect.preciseY() + rect.preciseHeight() / 2 );
		Point p5 = new PrecisionPoint( p3.preciseX(), rect.preciseY() + rect.preciseHeight() - 1 );
		Point p6 = new PrecisionPoint( p2.preciseX(), p5.preciseY() );

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
