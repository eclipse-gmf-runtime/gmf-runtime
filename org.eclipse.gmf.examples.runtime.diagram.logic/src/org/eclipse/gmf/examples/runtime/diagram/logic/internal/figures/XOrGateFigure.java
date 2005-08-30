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
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;

/**
 * code copied from real logic example in gef
 */
/*
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class XOrGateFigure extends NodeFigure {

	protected static final Dimension SIZE = new Dimension(MapMode.DPtoLP(15), MapMode.DPtoLP(17));	
	protected static final PointList GATE_OUTLINE = new PointList();
	protected static final PointList GATE_TOP = new PointList();

	static {
		//setup gate outline
		GATE_OUTLINE.addPoint(MapMode.DPtoLP(2), MapMode.DPtoLP(10));
		GATE_OUTLINE.addPoint(MapMode.DPtoLP(2), MapMode.DPtoLP(4));
		GATE_OUTLINE.addPoint(MapMode.DPtoLP(4), MapMode.DPtoLP(6));
		GATE_OUTLINE.addPoint(MapMode.DPtoLP(6), MapMode.DPtoLP(7));
		GATE_OUTLINE.addPoint(MapMode.DPtoLP(7), MapMode.DPtoLP(7));
		GATE_OUTLINE.addPoint(MapMode.DPtoLP(8), MapMode.DPtoLP(7));
		GATE_OUTLINE.addPoint(MapMode.DPtoLP(10), MapMode.DPtoLP(6));
		GATE_OUTLINE.addPoint(MapMode.DPtoLP(12), MapMode.DPtoLP(4));
		GATE_OUTLINE.addPoint(MapMode.DPtoLP(12), MapMode.DPtoLP(10));

		//setup top curve of gate
		GATE_TOP.addPoint(MapMode.DPtoLP(2), MapMode.DPtoLP(2));
		GATE_TOP.addPoint(MapMode.DPtoLP(4), MapMode.DPtoLP(4));
		GATE_TOP.addPoint(MapMode.DPtoLP(6), MapMode.DPtoLP(5));
		GATE_TOP.addPoint(MapMode.DPtoLP(7), MapMode.DPtoLP(5));
		GATE_TOP.addPoint(MapMode.DPtoLP(8), MapMode.DPtoLP(5));
		GATE_TOP.addPoint(MapMode.DPtoLP(10), MapMode.DPtoLP(4));
		GATE_TOP.addPoint(MapMode.DPtoLP(12), MapMode.DPtoLP(2));
	}	

	/**
	 * Constructor for XOrGateFigure.
	 */
	public XOrGateFigure() {
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

		//Draw an oval that represents the bottom arc
		r.y += MapMode.DPtoLP(4);
	
		/* 
		 * Draw the bottom gate arc.
		 * This is done with an oval. The oval overlaps the top
		 * arc of the gate, so this region is clipped.
		 */
		g.pushState();
		r.y++;
		g.clipRect(r);
		r.y--;
		
		r.width = r.width - MapMode.DPtoLP(1);
		g.fillOval(r);
		r.height--;
		g.drawOval(r);
		g.popState();

		//Draw the gate outline and top curve
		g.translate(getLocation());
		g.drawPolyline(GATE_TOP);
		g.fillPolygon(GATE_OUTLINE);
		g.drawPolyline(GATE_OUTLINE);
		g.translate(getLocation().negate());
	}
}