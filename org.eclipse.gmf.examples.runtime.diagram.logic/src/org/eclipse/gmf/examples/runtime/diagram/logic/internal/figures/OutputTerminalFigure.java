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
public class OutputTerminalFigure extends TerminalFigure {
	
	private static PointList points = new PointList();
	
    static {
		points.addPoint(2, 0);
		points.addPoint(2, 3);
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
	public OutputTerminalFigure(String ID, Dimension prefSize) {
		super(prefSize);
		setSize(prefSize);
		
		fixedAnchor = new FixedConnectionAnchor(this);
		fixedAnchor.offsetH = prefSize.height / 2;
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
		
		graphics.translate(r.getLocation());
		
		PointList copy = points.getCopy();
		IMapMode mm = MapModeUtil.getMapMode(this);
		mm.DPtoLP(copy);
		graphics.drawPolygon(copy);	
	}

}
