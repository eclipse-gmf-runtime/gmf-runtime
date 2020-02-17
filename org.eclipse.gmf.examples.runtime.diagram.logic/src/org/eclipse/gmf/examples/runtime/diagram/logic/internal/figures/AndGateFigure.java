/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
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
public class AndGateFigure extends NodeFigure {
	
	private Dimension prefSize;
	
	/**
	 * Constructor for AndGateFigure.
	 */
	public AndGateFigure(Dimension prefSize) {
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

		//draw main area
		g.fillRectangle(r);
	
		//outline main area
		g.drawLine(r.x, r.y, r.right(), r.y);
		g.drawLine(r.right(), r.y, r.right(), r.bottom() - mm.DPtoLP(1));
		g.drawLine(r.x, r.y, r.x, r.bottom() - mm.DPtoLP(1));

		//draw and outline the arc
		r.height = mm.DPtoLP(9);
		r.y += mm.DPtoLP(4);
		g.fillArc(r, 180, 180);
		r.width--;
		r.height--;
		g.drawArc(r, 180, 190);
	}
}
