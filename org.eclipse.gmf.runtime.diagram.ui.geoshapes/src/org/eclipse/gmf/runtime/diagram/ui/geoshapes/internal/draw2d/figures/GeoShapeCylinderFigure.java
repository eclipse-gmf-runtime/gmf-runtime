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

}
