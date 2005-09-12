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
