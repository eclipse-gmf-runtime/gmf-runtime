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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.diagram.ui.util.DrawConstant;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;

/**
 * Node figure for Connection Points. Connection Points are small square items that can sit on the
 * border.
 * 
 * @author qili
 *  
 */
public class BottomTerminalFigure extends TerminalFigure {
	
	protected static PointList bottomConnector = new PointList();
	
	static {
		bottomConnector.addPoint(MapMode.DPtoLP(-3), MapMode.DPtoLP(0));
		bottomConnector.addPoint(MapMode.DPtoLP(2), MapMode.DPtoLP(0));
		bottomConnector.addPoint(MapMode.DPtoLP(3), MapMode.DPtoLP(-2));
		bottomConnector.addPoint(MapMode.DPtoLP(3), MapMode.DPtoLP(-7));
		bottomConnector.addPoint(MapMode.DPtoLP(-2), MapMode.DPtoLP(-7));
		bottomConnector.addPoint(MapMode.DPtoLP(-2), MapMode.DPtoLP(-2));
	}
	
	/**
	 * Constructor
	 * @param edge
	 */
	public BottomTerminalFigure(DrawConstant side, String ID) {
		super(side);
		Dimension preferredSize = new Dimension(MapMode.DPtoLP(6), MapMode.DPtoLP(7));
		setOpaque(true);
		setSize(preferredSize);
		
		fixedAnchor = new FixedConnectionAnchor(this);
		//c.offsetH = MapMode.DPtoLP(1);
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
		r.translate(0, MapMode.DPtoLP(4));
		
		graphics.translate(r.getLocation());
//		 Draw the gaps for the connectors
		graphics.setForegroundColor(ColorConstants.white);
		
		PointList copy = bottomConnector.getCopy();
		copy.translate(copy.getBounds().width / 2, copy.getBounds().height / 2);
		graphics.fillPolygon(copy);
		graphics.drawPolygon(copy);	
	}

}

