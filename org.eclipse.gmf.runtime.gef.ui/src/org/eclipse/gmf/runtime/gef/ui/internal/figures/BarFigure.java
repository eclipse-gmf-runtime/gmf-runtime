/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;

import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;


/** 
 * Draws a vertical (or horizontal) bar.
 * @author mhanner
 * @canBeSeenBy org.eclipse.gmf.runtime.gef.ui.*
 */
public class BarFigure extends DefaultSizeNodeFigure {
	
	/** Constructor for StateLineFigure. */
	public BarFigure() {
		this(MapMode.DPtoLP(5), MapMode.DPtoLP(30));
	}

	/**
	 * Constructor for StateLineFigure.
	 * @param width figure width
	 * @param height figure height
	 */
	public BarFigure(int width, int height) {
		super( width, height );
	}
	
	/**
	 * Constructor for StateLineFigure.
	 * @param dim figure dimensions
	 */
	public BarFigure( Dimension dim ) {
		super(dim);
	}

	/** Paints the bar.  */
	protected void paintFigure(Graphics g) {
		g.drawRectangle(getBounds());
		g.fillRectangle(getBounds());
	}
		
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#getSlidableAnchorArea()
	 */
	protected double getSlidableAnchorArea() {
		return 1.0;
	}
	
}
