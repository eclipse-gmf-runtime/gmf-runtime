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

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.figures.IPolygonAnchorableFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;
/**
 * @author jschofie
 *
 * This Figure represents a Rounded Rectangle Figure
 */
public class GeoShapeRoundedRectangleFigure extends GeoShapeFigure implements IPolygonAnchorableFigure {

	private int radius = 0;
    
    // cache the anchor border point list since the calculation is costly.
    private PointList anchorBorderPointList;
    
	/**
	 * Constructor - Creates a rounded rectangle with a given Default size
	 * 
	 * @param width inital width of the figure
	 * @param height initial height of the figure
	 * @param spacing <code>int<code> the spacing between children in logical units
	 * @param radius <code>int</code> the radius size of the corner roundness in logical units
	 */
	public GeoShapeRoundedRectangleFigure( int width, int height, int spacing, int radius ) {
		super(width, height, spacing);
		this.radius = radius; 
	}

	/** Return the corner radius. */
	protected int getCornerRadius() {
		return radius;
	}
	
	/**
	 * Draw the state object.
	 * @see org.eclipse.draw2d.Figure#paintBorder(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics g) {
	    Rectangle r = getBounds().getCopy();
		int cornerRadius = getCornerRadius();

		// Draw the rectangle with the fill color
		g.fillRoundRectangle( r, cornerRadius, cornerRadius );
			
		// set the line type and line width
		g.setLineStyle(getLineStyle());
		g.setLineWidth(getLineWidth());
		
		// Draw the rectangle outline
		g.drawRoundRectangle( r, cornerRadius, cornerRadius );
   	}
      
     /**
     * Estimate the anchor intersection points by using a polyline smoothed
     * with bezier curves for the rounded top and bottom arcs.
     * 
     * @return PointList of the border of the shape
     */
    public PointList getAnchorBorderPointList() {
        int arcSize = getCornerRadius();

        PrecisionRectangle rBounds = new PrecisionRectangle(getBounds());

        // working our way counter-clockwise find key points.
        Point keyPoint1 = new PrecisionPoint(rBounds.getTopLeft().x + arcSize/2, rBounds.getTopLeft().y);
        Point keyPoint2 = new PrecisionPoint(rBounds.getTopLeft().x, rBounds.getTopLeft().y  + arcSize/2);
        Point keyPoint3 = new PrecisionPoint(rBounds.getBottomLeft().x, rBounds .getBottomLeft().y - arcSize/2);
        Point keyPoint4 = new PrecisionPoint(rBounds.getBottomLeft().x + arcSize/2, rBounds.getBottomLeft().y);
        Point keyPoint5 = new PrecisionPoint(rBounds.getBottomRight().x - arcSize/2, rBounds.getBottomRight().y);
        Point keyPoint6 = new PrecisionPoint(rBounds.getBottomRight().x, rBounds.getBottomRight().y - arcSize/2);
        Point keyPoint7 = new PrecisionPoint(rBounds.getTopRight().x, rBounds.getTopRight().y  + arcSize/2);
        Point keyPoint8 = new PrecisionPoint(rBounds.getTopRight().x - arcSize/2, rBounds.getTopRight().y);
        
        // create point lists for the corners
        PointList upperLeftPointList = new PointList();
        upperLeftPointList.addPoint(keyPoint1); 
        upperLeftPointList.addPoint(rBounds.getTopLeft().x + arcSize/4, rBounds.getTopLeft().y + arcSize/16);
        upperLeftPointList.addPoint(rBounds.getTopLeft().x + arcSize/16, rBounds.getTopLeft().y + arcSize/4);
        upperLeftPointList.addPoint(keyPoint2); 
       
        PointList lowerLeftPointList = new PointList();
        lowerLeftPointList.addPoint(keyPoint3);
        lowerLeftPointList.addPoint(rBounds.getBottomLeft().x + arcSize/16, rBounds.getBottomLeft().y - arcSize/4);
        lowerLeftPointList.addPoint(rBounds.getBottomLeft().x + arcSize/4, rBounds.getBottomLeft().y - arcSize/16);
        lowerLeftPointList.addPoint(keyPoint4);
        
        PointList lowerRightPointList = new PointList();
        lowerRightPointList.addPoint(keyPoint5);
        lowerRightPointList.addPoint(rBounds.getBottomRight().x - arcSize/4, rBounds.getBottomRight().y - arcSize/16);
        lowerRightPointList.addPoint(rBounds.getBottomRight().x - arcSize/16, rBounds.getBottomRight().y - arcSize/4);
        lowerRightPointList.addPoint(keyPoint6);
       
        PointList upperRightPointList = new PointList();
        upperRightPointList.addPoint(keyPoint7);
        upperRightPointList.addPoint(rBounds.getTopRight().x - arcSize/16, rBounds.getTopRight().y + arcSize/4);
        upperRightPointList.addPoint(rBounds.getTopRight().x - arcSize/4, rBounds.getTopRight().y + arcSize/16);
        upperRightPointList.addPoint(keyPoint8);
        
        // combine all the points and smooth out the corners with bezier curves.
        PointList combinedPointList = new PointList();
        combinedPointList.addPoint(keyPoint1);
        combinedPointList.addAll(PointListUtilities.calcSmoothPolyline(
            upperLeftPointList, PolylineConnectionEx.SMOOTH_MORE,
            PointListUtilities.DEFAULT_BEZIERLINES));
        combinedPointList.addPoint(keyPoint2);
        combinedPointList.addPoint(keyPoint3);
        combinedPointList.addAll(PointListUtilities.calcSmoothPolyline(
            lowerLeftPointList, PolylineConnectionEx.SMOOTH_MORE,
            PointListUtilities.DEFAULT_BEZIERLINES));
        combinedPointList.addPoint(keyPoint4);
        combinedPointList.addPoint(keyPoint5);
        combinedPointList.addAll(PointListUtilities.calcSmoothPolyline(
            lowerRightPointList, PolylineConnectionEx.SMOOTH_MORE,
            PointListUtilities.DEFAULT_BEZIERLINES));
        combinedPointList.addPoint(keyPoint6);
        combinedPointList.addPoint(keyPoint7);
        combinedPointList.addAll(PointListUtilities.calcSmoothPolyline(
            upperRightPointList, PolylineConnectionEx.SMOOTH_MORE,
            PointListUtilities.DEFAULT_BEZIERLINES));
        combinedPointList.addPoint(keyPoint8);
        combinedPointList.addPoint(keyPoint1); 

        PointListUtilities.normalizeSegments(combinedPointList);

        return combinedPointList;
    }
    
    /*
     * (non-Javadoc)
     * @see org.eclipse.draw2d.Figure#setBounds(org.eclipse.draw2d.geometry.Rectangle)
     */
    public void setBounds(Rectangle rect) {
        super.setBounds(rect);
        anchorBorderPointList = null;
    }
    
    /*
     * @see org.eclipse.gmf.runtime.draw2d.ui.figures.IPolygonAnchorableFigure#getPolygonPoints()
     */
    public PointList getPolygonPoints() {
        if (anchorBorderPointList == null) {
            anchorBorderPointList = getAnchorBorderPointList();
        }
        return anchorBorderPointList.getCopy();
    }
         
    
}
