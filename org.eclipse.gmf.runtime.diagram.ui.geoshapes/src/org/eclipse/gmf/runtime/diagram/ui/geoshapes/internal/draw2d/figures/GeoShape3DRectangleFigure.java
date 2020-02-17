/******************************************************************************
 * Copyright (c) 2003, 2009 IBM Corporation and others.
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.figures.IPolygonAnchorableFigure;
import org.eclipse.swt.graphics.Path;

/**
 * @author jschofie
 *
 * This Figure represents a 3D Rectangle Figure
 */
public class GeoShape3DRectangleFigure extends GeoShapeFigure 
	implements IPolygonAnchorableFigure {

	/**
	 * Constructor - Creates a 3D Rectangle with a Default size
	 * 
	 * @param width initial or preferred width of the figure
	 * @param height initial or preferred height of the figure
	 * @param spacing <code>int</code> that is the margin between children in logical units
	 */
	public GeoShape3DRectangleFigure(int width, int height, int spacing) {
		super(width, height, spacing);
	}
				
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics g) {

		List<Point> points = computePoints( getBounds().getCopy().shrink(getLineWidth() / 2, getLineWidth() / 2) );

		PointList outline = new PointList();
		for( int index = 0; index < 6; index++ ) {
			outline.addPoint( (Point)points.get( index ) );
		}

		g.pushState();
		// don't apply transparency to the outline
		applyTransparency(g);
		if (!isUsingGradient()) {
			// Fill the shape with the fill color
			g.fillPolygon(outline);
		} else {
			fillGradient(g, getPath(points));
		}
		g.popState();

		// set the line type and line width
		g.setLineStyle(getLineStyle());
		g.setLineWidth(getLineWidth());
		
		// Draw the shape's outline
		g.drawPolygon(outline);

		// Draw the remaining lines
		Point p2 = (Point) points.get(1);
		Point p4 = (Point) points.get(3);
		Point p6 = (Point) points.get(5);
		Point p7 = (Point) points.get(6);
		g.drawLine(p6, p7);
		g.drawLine(p7, p4);
		g.drawLine(p7, p2);
	}

	private List<Point> computePoints( Rectangle rect ) {
		List<Point> toReturn = new ArrayList<Point>();
		
		int scaleWidth  = (int) (rect.width * 0.25);
		int scaleHeight = (int) (rect.height * 0.25);
		
		Rectangle r1 = new Rectangle( rect.x, rect.y, rect.width - scaleWidth, rect.height - scaleHeight );
		Rectangle r2 = new Rectangle( r1.x + scaleWidth, r1.y + scaleHeight, r1.width, r1.height );
				
		Point p1 = new Point( r1.x, r1.y );
		Point p2 = new Point( r1.x + r1.width, r1.y );
		Point p3 = new Point( r2.x + r2.width - 1, r2.y );
		Point p4 = new Point( p3.x, r2.y + r2.height - 1 );
		Point p5 = new Point( r2.x, p4.y );
		Point p6 = new Point( r1.x, r1.y + r1.height );
		Point p7 = new Point( p2.x, p6.y );

		toReturn.add( p1 );
		toReturn.add( p2 );
		toReturn.add( p3 );
		toReturn.add( p4 );
		toReturn.add( p5 );
		toReturn.add( p6 );
		toReturn.add( p7 );

		return toReturn;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.draw2d.figures.IPolygonAnchorableFigure#getPolygonPoints()
	 */
	public PointList getPolygonPoints() {
		
		List<Point> points = computePoints( getBounds() );

		PointList outline = new PointList();
		for( int index = 0; index < 6; index++ ) {
			outline.addPoint( (Point)points.get( index ) );
		}

		// Close the polygon
		outline.addPoint( (Point)points.get( 0 ) );

		return outline;
	}
	
	/**
	 * Creates the path corresponding to this figure based on given points.
	 * 
	 * @param points
	 * @return created path
	 * @since 1.2
	 */
	protected Path getPath(List<Point> points) {
		Path path = new Path(null);
		Point pt = (Point)points.get(0);
		path.moveTo(pt.x, pt.y);
		for( int index = 1; index < 6; index++ ) {
			pt = (Point)points.get(index);
			path.lineTo(pt.x, pt.y);
		}
		path.close();
		return path;
	}

}
