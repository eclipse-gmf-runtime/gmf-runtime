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

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
/**
 * @author jschofie
 *
 * This Figure represents a Rounded Rectangle Figure
 */
public class GeoShapeRoundedRectangleFigure extends GeoShapeFigure {

	/** corner radius. */
	private static final int RADIUS = MapMode.DPtoLP(20);
	
	/**
	 * Constructor - Creates a rounded rectangle with a given Default size
	 * 
	 * @param width inital width of the figure
	 * @param height initial height of the figure
	 */
	public GeoShapeRoundedRectangleFigure( int width, int height ) {
		super(width, height);
	}

	/** Return the corner radius. */
	protected int getCornerRadius() {
		return RADIUS;
	}
	

	/**
	 * Draw the state object.
	 * @see org.eclipse.draw2d.Figure#paintBorder(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics g) {
	
		Rectangle r = getBounds();
		
		Rectangle insideRect = new Rectangle( r.x, r.y, r.width - 1, r.height - 1 );
		
		int cornerRadius = getCornerRadius();

		// Draw the rectangle with the fill color
		g.fillRoundRectangle( r, cornerRadius, cornerRadius );
			
		// Draw the rectangle outline
		g.drawRoundRectangle( insideRect, cornerRadius, cornerRadius );
	}

}
