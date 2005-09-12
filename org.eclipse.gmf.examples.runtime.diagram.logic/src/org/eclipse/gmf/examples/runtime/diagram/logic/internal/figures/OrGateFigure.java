/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
public class OrGateFigure extends NodeFigure {
	protected static final Dimension SIZE = new Dimension(MapMode.DPtoLP(15), MapMode.DPtoLP(17));
	protected static final PointList GATE_OUTLINE = new PointList();

	static {
		GATE_OUTLINE.addPoint(MapMode.DPtoLP(2), MapMode.DPtoLP(10));
		GATE_OUTLINE.addPoint(MapMode.DPtoLP(2), MapMode.DPtoLP(2));
		GATE_OUTLINE.addPoint(MapMode.DPtoLP(4), MapMode.DPtoLP(4));
		GATE_OUTLINE.addPoint(MapMode.DPtoLP(6), MapMode.DPtoLP(5));
		GATE_OUTLINE.addPoint(MapMode.DPtoLP(7), MapMode.DPtoLP(5));
		GATE_OUTLINE.addPoint(MapMode.DPtoLP(8), MapMode.DPtoLP(5));
		GATE_OUTLINE.addPoint(MapMode.DPtoLP(10), MapMode.DPtoLP(4));
		GATE_OUTLINE.addPoint(MapMode.DPtoLP(12), MapMode.DPtoLP(2));
		GATE_OUTLINE.addPoint(MapMode.DPtoLP(12), MapMode.DPtoLP(10));
	}	

	/**
	 * Creates a new OrGateFigure
	 */
	public OrGateFigure() {
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
	
		//Draw the bottom arc of the gate
		r.y += MapMode.DPtoLP(4);
		r.width = r.width - MapMode.DPtoLP(1);
		g.fillOval(r);
		r.height--;
		g.drawOval(r);
	
		//draw gate
		g.translate(getLocation());
		g.fillPolygon(GATE_OUTLINE);
		g.drawPolyline(GATE_OUTLINE);
		g.translate(getLocation().getNegated());
	}
}