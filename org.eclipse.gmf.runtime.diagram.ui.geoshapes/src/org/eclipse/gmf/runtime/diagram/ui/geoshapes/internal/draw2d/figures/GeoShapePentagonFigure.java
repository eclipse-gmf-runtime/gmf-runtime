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
 * This Figure represents a Pentagon Figure
 */
public class GeoShapePentagonFigure extends GeoShapePolygonFigure {
	
	private final static double WIDTH_FACTOR = 2 * Math.sin(0.4 * Math.PI) / (1 + Math.cos(0.2 * Math.PI));
	private final static double BOTTOM_OFFSET_FACTOR = 1 / (4 * Math.cos(0.2 * Math.PI));
	private final static double SIDE_OFFSET_FACTOR = (1 - Math.cos(0.4 * Math.PI)) / (1 + Math.cos(0.2 * Math.PI));

	/**
	 * Constructor - Creates a pentagon with a given Default size
	 * 
	 * @param width inital width of the figure
	 * @param height initial height of the figure
	 * @param spacing <code>int</code> that is the margin between children in logical units
	 */
	public GeoShapePentagonFigure( int width, int height, int spacing ) {
		super(width, height, spacing);
	}
	
	/**
	 * Constructor - Creates a proper pentagon with a given height
	 * 
	 * @param height initial height of the figure
	 * @param spacing <code>int</code> that is the margin between children in logical units
	 */
	public GeoShapePentagonFigure(int height, int spacing) {
		this((int) Math.round(WIDTH_FACTOR * height), height, spacing);
	}
			

	/**
	 * This method is used to compute the shapes polygon points.
	 * This is currently based on the shapes bounding box.
	 * 
	 * @param rect the rectangle that the shape will fit in
	 */
	protected PointList calculatePoints(Rectangle rect) {

		PointList points = new PrecisionPointList();
		
		Point p1 = new PrecisionPoint(rect.preciseX() + rect.preciseWidth() / 2, rect.preciseY());
		Point p2 = new PrecisionPoint(rect.preciseX() + rect.preciseWidth() - 1, rect.preciseY() + rect.preciseHeight() * SIDE_OFFSET_FACTOR);
		Point p3 = new PrecisionPoint(rect.preciseX() + rect.preciseWidth() * (0.5 + BOTTOM_OFFSET_FACTOR), rect.preciseY() + rect.preciseHeight() - 1);
		Point p4 = new PrecisionPoint(rect.preciseX() + rect.preciseWidth() * (0.5 - BOTTOM_OFFSET_FACTOR), p3.preciseY());
		Point p5 = new PrecisionPoint(rect.preciseX(), p2.preciseY());
		
		points.addPoint( p1 );
		points.addPoint( p2 );
		points.addPoint( p3 );
		points.addPoint( p4 );
		points.addPoint( p5 );
		points.addPoint( p1 );

		return points;
	}

}
