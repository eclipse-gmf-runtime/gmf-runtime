/******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
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

import org.eclipse.draw2d.Graphics;

/**
 * @author jschofie
 *
 * This Figure represents a Rectangle Figure
 */
public class GeoShapeRectangleFigure extends GeoShapeFigure {

	/**
	 * Constructor - Creates a rectangle with a given Default size
	 * 
	 * @param width inital width of the figure
	 * @param height initial height of the figure
	 * @param spacing <code>int</code> that is the margin between children in logical units
	 */
	public GeoShapeRectangleFigure( int width, int height, int spacing ) {
		super(width, height, spacing);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 * @since 1.2
	 */
	protected void paintFigure(Graphics g) {
		applyTransparency(g);
		if (!isUsingGradient()) {		
			g.fillRectangle(getClientArea());
		} else {
			fillGradient(g);
		}		
    }
	

}
