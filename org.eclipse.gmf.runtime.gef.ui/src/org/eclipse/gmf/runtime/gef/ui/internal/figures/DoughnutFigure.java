/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
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
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * Draws doughnut style figure.
 * @author mhanner
 * @canBeSeenBy org.eclipse.gmf.runtime.gef.ui.*
 */

public class DoughnutFigure extends CircleFigure {

	/** inner circle's scale value. */
	private float _scaleFactor = 0f;

	/** Create an instance. */
	public DoughnutFigure() {
		super();
	}

	/**
	 * Creates a <i>donought</i> type figure. <BR>
	 * @param scaleFactor the scale factor used to calculate the <i>inner circles</i> diameter.
	 * @param d circle's diameter position where the figure should be drawn.
	 * @see #setScaleFactor(float)
	 */
	public DoughnutFigure(int d, float sf) {
		super(d,d);
		setScaleFactor(sf);
	}

	/** 
	 * set the inner circles scale factor.
	 * @param factor 
	 * @throws IllegalArgumentException if the supplied scaleFactor is outside the range [0.0,1.0].
	 */
	public void setScaleFactor(float factor) {
		if (factor < 0f || factor > 1f) {
			throw new IllegalArgumentException("" + factor); //$NON-NLS-1$
		}
		_scaleFactor = factor;
	}

	/** Return the scale factor. */
	protected float getScaleFactor() {
		return _scaleFactor;
	}

	/**
	 * Draw the state object.
	 * @see org.eclipse.draw2d.Figure#paintBorder(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics g) {
		super.paintFigure(g);

		final Rectangle r = getBounds();
		//
		// draw the inner circle (if any)
		float sf = getScaleFactor();
		if (sf != 0f) {

			// draw the smaller (inner) circle
			Rectangle r2 = r.getCopy();

			int h = r.height - (int) (r.height * sf);
			int v = r.width - (int) (r.width * sf);
			r2.shrink(h, v); //keep centers constant

			if (!r2.equals(r)) {
				Color fg = getForegroundColor();
				Color bg = getBackgroundColor();
				g.setBackgroundColor(fg);
				g.fillOval(r2);
				g.setForegroundColor(fg);
				g.setBackgroundColor(bg);
			}
		}

	}
}
