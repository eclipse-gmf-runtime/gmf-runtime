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
public class XOrGateFigure extends NodeFigure {

	private static final PointList outlinePoints = new PointList();
	private static final PointList topPoints = new PointList();

	static {
		//setup gate outline
		outlinePoints.addPoint(2, 10);
		outlinePoints.addPoint(2, 4);
		outlinePoints.addPoint(4, 6);
		outlinePoints.addPoint(6, 7);
		outlinePoints.addPoint(7, 7);
		outlinePoints.addPoint(8, 7);
		outlinePoints.addPoint(10, 6);
		outlinePoints.addPoint(12, 4);
		outlinePoints.addPoint(12, 10);

		//setup top curve of gate
		topPoints.addPoint(2, 2);
		topPoints.addPoint(4, 4);
		topPoints.addPoint(6, 5);
		topPoints.addPoint(7, 5);
		topPoints.addPoint(8, 5);
		topPoints.addPoint(10, 4);
		topPoints.addPoint(12, 2);
	}	

	 private Dimension prefSize;
	    
	/**
	 * Constructor for XOrGateFigure.
	 */
	public XOrGateFigure(Dimension prefSize) {
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

		//Draw an oval that represents the bottom arc
		r.y += mm.DPtoLP(4);
	
		/* 
		 * Draw the bottom gate arc.
		 * This is done with an oval. The oval overlaps the top
		 * arc of the gate, so this region is clipped.
		 */
		g.pushState();
		r.y++;
		g.clipRect(r);
		r.y--;
		
		r.width = r.width - mm.DPtoLP(1);
		g.fillOval(r);
		r.height--;
		g.drawOval(r);
		g.popState();

		//Draw the gate outline and top curve
		g.translate(getLocation());
		
		PointList topLP = topPoints.getCopy();
		mm.DPtoLP(topLP);
		g.drawPolyline(topLP);
		
		PointList outlineLP = outlinePoints.getCopy();
		mm.DPtoLP(outlineLP);
		g.fillPolygon(outlineLP);
		g.drawPolyline(outlineLP);
		g.translate(getLocation().negate());
	}
}