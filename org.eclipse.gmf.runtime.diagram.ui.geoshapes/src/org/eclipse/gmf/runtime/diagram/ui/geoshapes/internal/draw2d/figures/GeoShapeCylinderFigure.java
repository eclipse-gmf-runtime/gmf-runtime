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

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.figures.IPolygonAnchorableFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;

/**
 * @author jschofie
 *
 * This Figure represents a Cylinder Figure
 */
public class GeoShapeCylinderFigure extends GeoShapeFigure implements IPolygonAnchorableFigure {

    // cache the anchor border point list since the calculation is costly.
    private PointList anchorBorderPointList;
    
	/**
	 * Constructor - Creates a cylinder with a given Default size
	 *
	 * @param width initial width of figure
	 * @param height initial height of the figure
	 * @param spacing <code>int</code> that is the margin between children in logical units
	 */
	public GeoShapeCylinderFigure(int width, int height, int spacing) {
		super(width, height, spacing);
	}
			
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics g) {
		
		Rectangle r = getBounds();

		int height = (int) (r.height * 0.25);
		
		Rectangle ellipse = new Rectangle( r.x, r.y, r.width, height );
		Rectangle middle = new Rectangle( r.x, r.y + ( height / 2 ), r.width, r.height - height + 1 ); 
		Rectangle lowerArc = new Rectangle( r.x, r.y + r.height - height - 1, r.width, height );

		// Draw the ellipse with the fill color
		g.fillOval( ellipse );
		
		// Draw the middle section with the fill color
		g.fillRectangle( middle );
		
		// Draw the lower arc with the fill color
		g.fillArc( lowerArc, 180, 180 );
	
		// Draw the ellipse outline
		g.drawOval( ellipse.x, ellipse.y, ellipse.width - 1, ellipse.height - 1 );
		
		// Draw the middle section
		g.drawLine( middle.x, middle.y, middle.x, middle.y + middle.height );
		g.drawLine( middle.x + middle.width - 1, middle.y, middle.x + middle.width - 1, middle.y + middle.height );
		
		// Draw the lower arc outline
		g.drawArc( lowerArc, 180, 180 );
    }
        
    /**
     * Estimate the anchor intersection points by using a polyline smoothed
     * with bezier curves for the rounded top and bottom arcs. 
     * 
     * @return PointList of the border of the cylinder shape
     */
    protected PointList getAnchorBorderPointList() {
        Rectangle rBounds = getBounds();
        
        // similar calculations as those made in paintFigure()
        int height = (int) (rBounds.height * 0.25);
        Rectangle rUpperEllipse = new Rectangle( rBounds.x,rBounds.y, rBounds.width, height );
        Rectangle rMiddle = new Rectangle( rBounds.x, rBounds.y + height/2, rBounds.width, rBounds.height - height + 1 ); 
        Rectangle rLowerEllipse = new Rectangle( rBounds.x, rBounds.y + rBounds.height - height - 1, rBounds.width, height );   
        
        // working our way counter-clockwise, find key points.
        Point keyPoint1 = new PrecisionPoint(rMiddle.getTopLeft().x, rMiddle.getTopLeft().y);
        Point keyPoint2 = new PrecisionPoint(rMiddle.getBottomLeft().x, rMiddle.getBottomLeft().y);
        Point keyPoint3 = new PrecisionPoint(rMiddle.getBottomRight().x, rMiddle.getBottomRight().y);
        Point keyPoint4 = new PrecisionPoint(rMiddle.getTopRight().x, rMiddle.getTopRight().y);
               
        // build point list for upper ellipse
        PointList upperPointList = new PointList();
        upperPointList.addPoint(keyPoint4); // top-right
        
        // Intermediate segments to estimate the top arc
        LineSeg lineSeg = new LineSeg(new PrecisionPoint(rUpperEllipse.getTopRight().x - rUpperEllipse.width/32, rUpperEllipse.getTopRight().y ), 
            new PrecisionPoint(rUpperEllipse.getBottomRight().x - rUpperEllipse.width/32, rUpperEllipse.getCenter().y) );
        upperPointList.addPoint(lineSeg.getLineIntersectionsWithEllipse(rUpperEllipse).getLastPoint());
        lineSeg = new LineSeg(new PrecisionPoint(rUpperEllipse.getTopRight().x - rUpperEllipse.width/12, rUpperEllipse.getTopRight().y ), 
            new PrecisionPoint(rUpperEllipse.getBottomRight().x - rUpperEllipse.width/12, rUpperEllipse.getCenter().y) );
        upperPointList.addPoint(lineSeg.getLineIntersectionsWithEllipse(rUpperEllipse).getLastPoint());
        lineSeg = new LineSeg(new PrecisionPoint(rUpperEllipse.getTopRight().x - rUpperEllipse.width/8, rUpperEllipse.getTopRight().y ), 
            new PrecisionPoint(rUpperEllipse.getBottomRight().x - rUpperEllipse.width/8, rUpperEllipse.getCenter().y) );
        upperPointList.addPoint(lineSeg.getLineIntersectionsWithEllipse(rUpperEllipse).getLastPoint());
        lineSeg = new LineSeg(new PrecisionPoint(rUpperEllipse.getTopRight().x - rUpperEllipse.width/4, rUpperEllipse.getTopRight().y ), 
            new PrecisionPoint(rUpperEllipse.getBottomRight().x - rUpperEllipse.width/4, rUpperEllipse.getCenter().y) );
        upperPointList.addPoint(lineSeg.getLineIntersectionsWithEllipse(rUpperEllipse).getLastPoint());
                    
        upperPointList.addPoint(rUpperEllipse.getCenter().x , rUpperEllipse.getCenter().y - rUpperEllipse.height/2); // center.
                
        lineSeg = new LineSeg(new Point(rUpperEllipse.getTopLeft().x + rUpperEllipse.width/4, rUpperEllipse.getTopRight().y ), 
            new PrecisionPoint(rUpperEllipse.getTopLeft().x + rUpperEllipse.width/4, rUpperEllipse.getCenter().y) );
        upperPointList.addPoint(lineSeg.getLineIntersectionsWithEllipse(rUpperEllipse).getLastPoint());
        lineSeg = new LineSeg(new PrecisionPoint(rUpperEllipse.getTopLeft().x + rUpperEllipse.width/8, rUpperEllipse.getTopRight().y ), 
            new PrecisionPoint(rUpperEllipse.getTopLeft().x + rUpperEllipse.width/8, rUpperEllipse.getCenter().y) );
        upperPointList.addPoint(lineSeg.getLineIntersectionsWithEllipse(rUpperEllipse).getLastPoint());
        lineSeg = new LineSeg(new PrecisionPoint(rUpperEllipse.getTopLeft().x + rUpperEllipse.width/12, rUpperEllipse.getTopRight().y ), 
            new PrecisionPoint(rUpperEllipse.getTopLeft().x + rUpperEllipse.width/12, rUpperEllipse.getCenter().y) );
        upperPointList.addPoint(lineSeg.getLineIntersectionsWithEllipse(rUpperEllipse).getLastPoint());
        lineSeg = new LineSeg(new PrecisionPoint(rUpperEllipse.getTopLeft().x + rUpperEllipse.width/32, rUpperEllipse.getTopRight().y ), 
            new PrecisionPoint(rUpperEllipse.getTopLeft().x + rUpperEllipse.width/32, rUpperEllipse.getCenter().y) );
        upperPointList.addPoint(lineSeg.getLineIntersectionsWithEllipse(rUpperEllipse).getLastPoint());
                
        upperPointList.addPoint(keyPoint1); // top-left
                       
        // build point list for lower ellipse
        PointList lowerPointList = new PointList();
        lowerPointList.addPoint(keyPoint2); // bottom-left
       
        // intermediate segments to estimate the bottom arc
        lineSeg = new LineSeg(new PrecisionPoint(rUpperEllipse.getBottomLeft().x + rUpperEllipse.width/32, rLowerEllipse.getCenter().y ), 
            new PrecisionPoint(rUpperEllipse.getBottomLeft().x + rUpperEllipse.width/32, rLowerEllipse.getBottomLeft().y) );
        lowerPointList.addPoint(lineSeg.getLineIntersectionsWithEllipse(rLowerEllipse).getFirstPoint());
        lineSeg = new LineSeg(new PrecisionPoint(rUpperEllipse.getBottomLeft().x + rUpperEllipse.width/12, rLowerEllipse.getCenter().y ), 
            new PrecisionPoint(rUpperEllipse.getBottomLeft().x + rUpperEllipse.width/12, rLowerEllipse.getBottomLeft().y) );
        lowerPointList.addPoint(lineSeg.getLineIntersectionsWithEllipse(rLowerEllipse).getFirstPoint());
        lineSeg = new LineSeg(new PrecisionPoint(rUpperEllipse.getBottomLeft().x + rUpperEllipse.width/8, rLowerEllipse.getCenter().y ), 
            new PrecisionPoint(rUpperEllipse.getBottomLeft().x + rUpperEllipse.width/8, rLowerEllipse.getBottomLeft().y) );
        lowerPointList.addPoint(lineSeg.getLineIntersectionsWithEllipse(rLowerEllipse).getFirstPoint());
        lineSeg = new LineSeg(new PrecisionPoint(rUpperEllipse.getBottomLeft().x + rUpperEllipse.width/4, rLowerEllipse.getCenter().y ), 
            new PrecisionPoint(rUpperEllipse.getBottomLeft().x + rUpperEllipse.width/4, rLowerEllipse.getBottomLeft().y) );
        lowerPointList.addPoint(lineSeg.getLineIntersectionsWithEllipse(rLowerEllipse).getFirstPoint());
        
        lowerPointList.addPoint(rLowerEllipse.getCenter().x , rLowerEllipse.getCenter().y + rLowerEllipse.height/2); // center.
        
        lineSeg = new LineSeg(new PrecisionPoint(rUpperEllipse.getBottomRight().x - rUpperEllipse.width/4, rLowerEllipse.getCenter().y ), 
            new PrecisionPoint(rUpperEllipse.getBottomRight().x - rUpperEllipse.width/4, rLowerEllipse.getBottomRight().y) );
        lowerPointList.addPoint(lineSeg.getLineIntersectionsWithEllipse(rLowerEllipse).getFirstPoint());
        lineSeg = new LineSeg(new PrecisionPoint(rUpperEllipse.getBottomRight().x - rUpperEllipse.width/8, rLowerEllipse.getCenter().y ), 
            new PrecisionPoint(rUpperEllipse.getBottomRight().x - rUpperEllipse.width/8, rLowerEllipse.getBottomRight().y) );
        lowerPointList.addPoint(lineSeg.getLineIntersectionsWithEllipse(rLowerEllipse).getFirstPoint());
        lineSeg = new LineSeg(new PrecisionPoint(rUpperEllipse.getBottomRight().x - rUpperEllipse.width/12, rLowerEllipse.getCenter().y ), 
            new PrecisionPoint(rUpperEllipse.getBottomRight().x - rUpperEllipse.width/12, rLowerEllipse.getBottomRight().y) );
        lowerPointList.addPoint(lineSeg.getLineIntersectionsWithEllipse(rLowerEllipse).getFirstPoint());
        lineSeg = new LineSeg(new PrecisionPoint(rUpperEllipse.getBottomRight().x - rUpperEllipse.width/32, rLowerEllipse.getCenter().y ), 
            new PrecisionPoint(rUpperEllipse.getBottomRight().x - rUpperEllipse.width/32, rLowerEllipse.getBottomRight().y) );
        lowerPointList.addPoint(lineSeg.getLineIntersectionsWithEllipse(rLowerEllipse).getFirstPoint());
                
        lowerPointList.addPoint(keyPoint3); // bottom-right
                       
        // combine all the points and close the polyline moving counter clockwise
        PointList combinedPointList = new PointList();
        combinedPointList.addPoint(keyPoint1);
        combinedPointList.addPoint(keyPoint2);
        combinedPointList.addAll(PointListUtilities.calcSmoothPolyline(
            lowerPointList, PolylineConnectionEx.SMOOTH_MORE,
            PointListUtilities.DEFAULT_BEZIERLINES));
        combinedPointList.addPoint(keyPoint3);
        combinedPointList.addPoint(keyPoint4);
        combinedPointList.addAll(PointListUtilities.calcSmoothPolyline(
            upperPointList, PolylineConnectionEx.SMOOTH_MORE,
            PointListUtilities.DEFAULT_BEZIERLINES));
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
