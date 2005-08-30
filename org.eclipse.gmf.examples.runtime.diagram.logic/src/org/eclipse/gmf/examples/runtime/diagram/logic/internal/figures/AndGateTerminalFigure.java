/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
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

import org.eclipse.gmf.runtime.diagram.ui.util.DrawConstant;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;


/**
 * @author qili
 *
 * Node figure for Connection Points. Connection Points are small square items that can sit on the
 * border.
 */
public class AndGateTerminalFigure extends TerminalFigure {
	
	protected static PointList connector = new PointList();
	
	static {
		connector.addPoint(MapMode.DPtoLP(2), MapMode.DPtoLP(0));
		connector.addPoint(MapMode.DPtoLP(2), MapMode.DPtoLP(2));
	}
	
	/**
	 * Constructor
	 * @param edge
	 */
	public AndGateTerminalFigure(DrawConstant side, String ID) {
		super(side);
		Dimension preferredSize = new Dimension(MapMode.DPtoLP(4), MapMode.DPtoLP(2));
		setSize(preferredSize);
		
		fixedAnchor = new FixedConnectionAnchor(this);
		fixedAnchor.offsetH = MapMode.DPtoLP(2);
		fixedAnchor.offsetV = MapMode.DPtoLP(0);
		
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
