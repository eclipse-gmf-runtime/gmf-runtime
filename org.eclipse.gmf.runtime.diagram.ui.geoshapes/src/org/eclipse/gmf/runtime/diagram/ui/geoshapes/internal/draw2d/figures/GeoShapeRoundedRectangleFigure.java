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
 * This Figure represents a Rounded Rectangle Figure
 */
public class GeoShapeRoundedRectangleFigure extends GeoShapeFigure {

	private int radius = 0;
	
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
	
		Rectangle r = getBounds();
		
		Rectangle insideRect = new Rectangle( r.x, r.y, r.width - 1, r.height - 1 );
		
		int cornerRadius = getCornerRadius();

		// Draw the rectangle with the fill color
		g.fillRoundRectangle( r, cornerRadius, cornerRadius );
			
		// Draw the rectangle outline
		g.drawRoundRectangle( insideRect, cornerRadius, cornerRadius );
	}

}
