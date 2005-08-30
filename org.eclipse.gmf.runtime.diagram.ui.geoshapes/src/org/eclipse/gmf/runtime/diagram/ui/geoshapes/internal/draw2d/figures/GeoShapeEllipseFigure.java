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

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.draw2d.ui.figures.IOvalAnchorableFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.SlidableOvalAnchor;

/**
 * @author jschofie
 *
 * This Figure represents a Ellipse Figure
 */
public class GeoShapeEllipseFigure extends GeoShapeFigure
	implements IOvalAnchorableFigure {

	/**
	 * Constructor - Creates a ellipse with a given Default size
	 * @param width initial width of the figure
	 * @param height initial height of the figure
	 */	
	public GeoShapeEllipseFigure(int width, int height ) {
		super(width, height);
	}
		
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics g) {
	
		Rectangle r = getBounds();

		// Draw the ellipse with the fill color
		g.fillOval(r);
		
		// Draw the ellipse outline
		g.drawOval(r.x, r.y, r.width - 1, r.height - 1);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.figures.IOvalAnchorableFigure#getOvalBounds()
	 */
	public Rectangle getOvalBounds() {
		return getBounds();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#createAnchor(org.eclipse.draw2d.geometry.PrecisionPoint)
	 */
	protected ConnectionAnchor createAnchor(PrecisionPoint p) {
		if (p==null)
			return createDefaultAnchor();
		return new SlidableOvalAnchor(this, p);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#createDefaultAnchor()
	 */
	protected ConnectionAnchor createDefaultAnchor() {
		return new SlidableOvalAnchor(this);
	}
}
