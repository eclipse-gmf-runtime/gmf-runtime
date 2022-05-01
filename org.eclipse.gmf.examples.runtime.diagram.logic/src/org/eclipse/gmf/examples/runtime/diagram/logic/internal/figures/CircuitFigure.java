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
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.swt.graphics.Color;

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
	private Dimension prefSize;
	
	public CircuitFigure(Dimension prefSize) {
		setBorder(new CircuitBorder());
		setOpaque(true);
		this.prefSize = prefSize;
	}

	public Dimension getPreferredSize(int w, int h) {
		Dimension newPrefSize = super.getPreferredSize(w, h);
		Dimension defaultSize = prefSize;
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