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
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;

/**
 * Draws a circle and inserts text inside of it.
 * @author mhanner
 * @canBeSeenBy org.eclipse.gmf.runtime.gef.ui.*
 */
public class TextCircleFigure extends CircleFigure {


	/** text to be displayed within this figure. */
	private String _txt;

	/** text alignment. */
	private int _alignment = PositionConstants.NORTH_SOUTH;


	/** Create an instance. */
	public TextCircleFigure() {
		super();
	}

	/**
	 * Create an instance.
	 * @param d
	 */
	public TextCircleFigure(int d, String txt) {
		super(d);
		setText(txt);
	}
	/** 
	 * Set the text to be displayed within the state vertex figure. 
	 * Same as calling <code>setText(text, PositionConstants.NORTH_SOUTH);
	 * @see FigureUtilities#getLocation(int, Dimension, Rectangle)
	 * @see PositionConstants
	 * @param text the text to be displayed
	 */
	public void setText(String text) {
		setText(text, PositionConstants.NORTH_SOUTH);
	}

	/** 
	 * set the text to be displaued within the state vertex figure. 
	 * @param text the text to be displayed
	 * @param alignment <tt>text<tt> position within the state vertex figure. 
	 */
	public void setText(String text, int alignment) {
		_txt = text;
		setTextAlignment(alignment);
	}

	/** Return the text to be displayed within the state vertex figure. */
	protected final String getText() {
		return _txt;
	}

	/** 
	 * set the text alignment within the state vertex figure. 
	 * @param alignment <tt>text<tt> position within the state vertex figure. 
	 * @see FigureUtilities#getLocation(int, Dimension, Rectangle)
	 */
	public void setTextAlignment(int alignment) {
		_alignment = alignment;
	}

	/** return the text alignment within the state vertex figure. */
	protected final int getTextAlignment() {
		return _alignment;
	}

	/**
	 * Draws the text inside the circle.
	 * @see org.eclipse.draw2d.Figure#paintBorder(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics g) {
		super.paintFigure(g);
		final Rectangle r = getBounds();
		// draw the text (if any)
		String txt = getText();
		if (txt != null && txt.length() > 0) {
			Dimension td = FigureUtilities.getTextExtents(txt, g.getFont());
			MapMode.translateToLP(td);
			Point p = FigureUtilities.getLocation(getTextAlignment(), td, r);
			g.drawString( _txt, p );
		}
	}

}
