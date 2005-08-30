/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.swt.graphics.Color;

import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;

/**
 * code copied from real logic example in gef
 */
/*
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class CircuitFigure
	extends NodeFigure
	implements HandleBounds
{
	public static final Dimension SIZE = new Dimension(MapMode.DPtoLP(100), MapMode.DPtoLP(100)); 

	public CircuitFigure() {
		setBorder(new CircuitBorder());
		setOpaque(true);
	}

	public Dimension getPreferredSize(int w, int h) {
		Dimension newPrefSize = super.getPreferredSize(w, h);
		Dimension defaultSize = SIZE;
		newPrefSize.union(defaultSize);
		return newPrefSize;
	}

	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		Rectangle rect = getBounds().getCopy();
		graphics.setBackgroundColor(new Color(null, 255, 255, 255));
		graphics.fillRectangle(rect);
	}
}