/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;

/**
 * code copied from real logic example in gef
 */
/*
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class AndGateFigure extends NodeFigure {
	
	private static final Dimension SIZE = new Dimension(MapMode.DPtoLP(15), MapMode.DPtoLP(17));

	/**
	 * Constructor for AndGateFigure.
	 */
	public AndGateFigure() {
		getBounds().width = SIZE.width;
		getBounds().height = SIZE.height; 
	}

	/**
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		return SIZE;
	}

	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 */
	protected void paintFigure(Graphics g) {
		Rectangle r = getBounds().getCopy();
		r.translate(MapMode.DPtoLP(2), MapMode.DPtoLP(2));
		r.setSize(MapMode.DPtoLP(11), MapMode.DPtoLP(9));

		//draw main area
		g.fillRectangle(r);
	
		//outline main area
		g.drawLine(r.x, r.y, r.right(), r.y);
		g.drawLine(r.right(), r.y, r.right(), r.bottom() - MapMode.DPtoLP(1));
		g.drawLine(r.x, r.y, r.x, r.bottom() - MapMode.DPtoLP(1));

		//draw and outline the arc
		r.height = MapMode.DPtoLP(9);
		r.y += MapMode.DPtoLP(4);
		g.fillArc(r, 180, 180);
		r.width--;
		r.height--;
		g.drawArc(r, 180, 190);
	}
}
