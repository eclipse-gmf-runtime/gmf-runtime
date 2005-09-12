/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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

import org.eclipse.gmf.runtime.diagram.ui.util.DrawConstant;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;


/**
 * @author qili
 *
 * Node figure for Connection Points. Connection Points are small square items that can sit on the
 * border.
 */
public class OrGateTerminalFigure extends TerminalFigure {
	
	protected static PointList connector = new PointList();
	
	static {
		connector.addPoint(MapMode.DPtoLP(2), MapMode.DPtoLP(0));
		connector.addPoint(MapMode.DPtoLP(2), MapMode.DPtoLP(4));
	}
	
	/**
	 * Constructor
	 * @param edge
	 */
	public OrGateTerminalFigure(DrawConstant side, String ID) {
		super(side);
		Dimension preferredSize = new Dimension(MapMode.DPtoLP(4), MapMode.DPtoLP(4));
		setSize(preferredSize);
		
		fixedAnchor = new FixedConnectionAnchor(this);
		fixedAnchor.offsetH = MapMode.DPtoLP(2);
		getConnectionAnchors().put(ID, fixedAnchor);
	}
		
	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 * 
	 * @param graphics
	 */
	protected void paintFigure(Graphics graphics) {	
		Rectangle r = getBounds().getCopy();
		r.translate(MapMode.DPtoLP(0), MapMode.DPtoLP(0));
		
		graphics.translate(r.getLocation());
		PointList copy = connector.getCopy();
		graphics.drawPolygon(copy);	
	}

}
