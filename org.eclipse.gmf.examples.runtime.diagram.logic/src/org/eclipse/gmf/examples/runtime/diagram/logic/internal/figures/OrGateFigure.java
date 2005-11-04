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
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;

/**
 * code copied from real logic example in gef
 */
/*
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class OrGateFigure extends NodeFigure {
	private static final PointList points = new PointList();

	static {
		points.addPoint(2, 10);
		points.addPoint(2, 2);
		points.addPoint(4, 4);
		points.addPoint(6, 5);
		points.addPoint(7, 5);
		points.addPoint(8, 5);
		points.addPoint(10, 4);
		points.addPoint(12, 2);
		points.addPoint(12, 10);
	}	

	private Dimension prefSize;
	
	/**
	 * Creates a new OrGateFigure
	 */
	public OrGateFigure(Dimension prefSize) {
		getBounds().width = prefSize.width; 
		getBounds().height = prefSize.height;
		this.prefSize = new Dimension(prefSize);
	}

	/**
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		return new Dimension(prefSize);
	}

	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 */
	protected void paintFigure(Graphics g) {
		Rectangle r = getBounds().getCopy();
		
		IMapMode mm = MapModeUtil.getMapMode(this);
		r.translate(mm.DPtoLP(2), mm.DPtoLP(2));
		r.setSize(mm.DPtoLP(11), mm.DPtoLP(9));
	
		//Draw the bottom arc of the gate
		r.y += mm.DPtoLP(4);
		r.width = r.width - mm.DPtoLP(1);
		g.fillOval(r);
		r.height--;
		g.drawOval(r);
	
		//draw gate
		g.translate(getLocation());
		PointList outline = points.getCopy();
		mm.DPtoLP(outline);
		g.fillPolygon(outline);
		g.drawPolyline(outline);
		g.translate(getLocation().getNegated());
	}
}