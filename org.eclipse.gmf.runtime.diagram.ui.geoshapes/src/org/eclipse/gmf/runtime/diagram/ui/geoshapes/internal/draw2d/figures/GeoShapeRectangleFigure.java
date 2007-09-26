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
	 */
	protected void paintFigure(Graphics g) {
       g.fillRectangle(getBounds().getCopy());
    }
}
