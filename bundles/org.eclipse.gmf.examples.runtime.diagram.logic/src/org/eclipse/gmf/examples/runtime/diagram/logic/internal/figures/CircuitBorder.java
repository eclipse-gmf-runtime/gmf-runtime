/******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and others.
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

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;

/**
 * code copied from real logic example in gef
 */
/*
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class CircuitBorder  
	extends AbstractBorder
{
	protected Insets insets = new Insets(6, 6, 6, 6);

	public Insets getInsets(IFigure figure) {
		return insets;
	}

	public void paint(IFigure figure, Graphics g, Insets in) {
		Rectangle r = figure.getBounds().getShrinked(in);
		IMapMode mm = MapModeUtil.getMapMode(figure);
		
		//Draw the sides of the border
		g.fillRectangle(r.x, r.y, r.width, mm.DPtoLP(6));
		g.fillRectangle(r.x, r.bottom() - mm.DPtoLP(6), r.width, mm.DPtoLP(6));
		g.fillRectangle(r.x, r.y + mm.DPtoLP(2), mm.DPtoLP(6), r.height - mm.DPtoLP(4));
		g.fillRectangle(r.right() - mm.DPtoLP(6), r.y + mm.DPtoLP(2), mm.DPtoLP(6), r.height - mm.DPtoLP(4));

		//Outline the border
		g.drawLine(r.x, r.y + 8, r.right() - 1, r.y + 8);
		g.drawLine(r.x, r.bottom() - 8, r.right() - 1, r.bottom() - 8);
		g.drawLine(r.x, r.y + 2, r.x, r.bottom() - 3);
		g.drawLine(r.right() - 1, r.bottom() - 3, r.right() - 1, r.y + 2);
		
		r.shrink(new Insets(1, 1, 0, 0));
		r.expand(1, 1);
		r.shrink(getInsets(figure));
	}
}