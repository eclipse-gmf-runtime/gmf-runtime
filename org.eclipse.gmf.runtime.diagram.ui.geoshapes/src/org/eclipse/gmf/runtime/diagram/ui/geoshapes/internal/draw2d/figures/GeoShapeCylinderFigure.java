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

/**
 * @author jschofie
 *
 * This Figure represents a Cylinder Figure
 */
public class GeoShapeCylinderFigure extends GeoShapeFigure {

	/**
	 * Constructor - Creates a cylinder with a given Default size
	 *
	 * @param width initial width of figure
	 * @param height initial height of the figure
	 */
	public GeoShapeCylinderFigure(int width, int height) {
		super(width, height);
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

}
