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
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;


/**
 * @author qili
 *
 * Node figure for Connection Points. Connection Points are small square items that can sit on the
 * border.
 */
public class OrGateTerminalFigure extends TerminalFigure {
	
	private static PointList points = new PointList();
	
	static {
		points.addPoint(2, 0);
		points.addPoint(2, 4);
	}
	
	/**
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		return new Dimension(prefSize);
	}
	
	/**
	 * Constructor
	 * @param edge
	 */
	public OrGateTerminalFigure(String ID, Dimension prefSize) {
		super(prefSize);
		setSize(prefSize);
		this.prefSize = new Dimension(prefSize);
		
		fixedAnchor = new FixedConnectionAnchor(this);
		fixedAnchor.offsetH = prefSize.width / 2;
		getConnectionAnchors().put(ID, fixedAnchor);
	}
		
	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 * 
	 * @param graphics
	 */
	protected void paintFigure(Graphics graphics) {	
		Rectangle r = getBounds().getCopy();
		
		graphics.translate(r.getLocation());
		PointList copy = points.getCopy();
		IMapMode mm = MapModeUtil.getMapMode(this);
		mm.DPtoLP(copy);
		graphics.drawPolygon(copy);	
	}

}
