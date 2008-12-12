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

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.draw2d.ui.figures.IOvalAnchorableFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.SlidableOvalAnchor;

/**
 * This Figure represents a Ellipse Figure
 * 
 * @author jschofie
 */
public class GeoShapeEllipseFigure extends GeoShapeFigure implements
		IOvalAnchorableFigure {

	/**
	 * Constructor - Creates a ellipse with a given Default size
	 * 
	 * @param width
	 *            initial width of the figure
	 * @param height
	 *            initial height of the figure
	 * @param spacing
	 *            <code>int</code> that is the margin between children in
	 *            logical units
	 */
	public GeoShapeEllipseFigure(int width, int height, int spacing) {
		super(width, height, spacing);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics g) {

		Rectangle r = getOvalBounds().getCopy();

		// Draw the ellipse with the fill color
		g.fillOval(r);

		// set the line type and line width
		g.setLineStyle(getLineStyle());
		g.setLineWidth(getLineWidth());

		// Draw the ellipse outline
		r.width--;
		r.height--;
		if (getLineWidth() > 1) {
			r.shrink((getLineWidth() - 1) / 2, (getLineWidth() - 1) / 2);
		}
		g.drawOval(r);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.figures.IOvalAnchorableFigure#getOvalBounds()
	 */
	public Rectangle getOvalBounds() {
		Rectangle r = getBounds();

		Rectangle ovalRect = new Rectangle(r);

		// not using the full bounds of the rectangle to draw
		// the oval in as it results in the top and the left
		// edge of the oval being chopped off.  That is why 
		// we are indenting by 1
		ovalRect.setSize(r.width-1, r.height-1);
		
		return ovalRect;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#createAnchor(org.eclipse.draw2d.geometry.PrecisionPoint)
	 */
	protected ConnectionAnchor createAnchor(PrecisionPoint p) {
		if (p == null)
			return createDefaultAnchor();
		return new SlidableOvalAnchor(this, p);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#createDefaultAnchor()
	 */
	protected ConnectionAnchor createDefaultAnchor() {
		return new SlidableOvalAnchor(this);
	}
}
