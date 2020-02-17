/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;

/**
 * Node figure for Connection Points. Connection Points are small square items that can sit on the
 * border.
 * 
 * @author qili
 *  
 */
public class BottomTerminalFigure extends TerminalFigure {
	
	private static PointList points = new PointList();
	
	static {
		points.addPoint(-3, 0);
		points.addPoint(2, 0);
		points.addPoint(3, -2);
		points.addPoint(3, -7);
		points.addPoint(-2, -7);
		points.addPoint(-2, -2);
	}
	
	/**
	 * Constructor
	 * @param edge
	 */
	public BottomTerminalFigure(String ID, Dimension prefSize) {
		super(prefSize);
		setOpaque(true);
		setSize(prefSize);
		
		fixedAnchor = new FixedConnectionAnchor(this);
		fixedAnchor.topDown = false;
		getConnectionAnchors().put(ID, fixedAnchor);
	}

	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 * 
	 * @param graphics
	 */
	protected void paintFigure(Graphics graphics) {			
		Rectangle r = getBounds().getCopy();
		
		IMapMode mm = MapModeUtil.getMapMode(this);
		r.translate(0, mm.DPtoLP(4));
		
		graphics.translate(r.getLocation());
//		 Draw the gaps for the connectors
		graphics.setForegroundColor(ColorConstants.white);
		
		PointList copy = points.getCopy();
		mm.DPtoLP(copy);
		copy.translate(copy.getBounds().width / 2, copy.getBounds().height / 2);
		graphics.fillPolygon(copy);
		graphics.drawPolygon(copy);	
	}

}

