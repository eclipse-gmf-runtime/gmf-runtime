/******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.draw2d.ui.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;

/**
 * Extends {@link org.eclipse.draw2d.LineBorder} to replace rectangular border with
 * rounded rectangle.
 * 
 * @author lgrahek
 * @since 1.2
 *
 */
public class RoundedRectangleBorder extends LineBorder {
	
	/**
	 * Width of the corner arc in logic points
	 */
	int arcWidth;

	/**
	 * Height of the corner arc in logic points
	 */	
	int arcHeight;	

	/**
	 * Creates an instance of this class with the given arc width and arc height in pixels
	 * @param arcWidth The width of the corner arc
	 * @param arcHeight The height of the corner arc
	 */
	public RoundedRectangleBorder(int arcWidth, int arcHeight) {
		super();
		this.arcWidth = arcWidth;
		this.arcHeight = arcHeight;		
	}	
	
	/**
	 * Insets are defined by the line width.
	 * @see org.eclipse.draw2d.LineBorder#getInsets(org.eclipse.draw2d.IFigure)
	 */
	public Insets getInsets(IFigure figure) {
		return new Insets(getWidth());
	}	

	/**
	 * Gets the arc width.
	 * @return arc width
	 */
	public int getArcWidth() {
		return arcWidth;
	}

	/**
	 * Sets the arc width
	 * @param arcWidth 
	 */
	public void setArcWidth(int arcWidth) {
		this.arcWidth = arcWidth;
	}

	/**
	 * Gets the arc height.
	 * @return arc height
	 */	
	public int getArcHeight() {
		return arcHeight;
	}

	/**
	 * Sets the arc height
	 * @param arcHeight 
	 */
	public void setArcHeight(int arcHeight) {
		this.arcHeight = arcHeight;
	}

	/**
	 * Paints rounded rectangular border taking into account arcWidth, arcHeight, line width and line style
	 * 
	 * @see org.eclipse.draw2d.LineBorder#paint(org.eclipse.draw2d.IFigure, org.eclipse.draw2d.Graphics, org.eclipse.draw2d.geometry.Insets)
	 */
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		tempRect.setBounds(getPaintRectangle(figure, insets));
		// Shrink to accommodate for the line width
		tempRect.shrink(getWidth() / 2, getWidth() / 2);

		graphics.setLineWidth(getWidth());
		graphics.setLineStyle(getStyle());
		// If the color for this border is specified, then use it. 
		// Otherwise, use the figure's foreground color
		if (getColor() != null) {
			graphics.setForegroundColor(getColor());
		} else {
			graphics.setForegroundColor(figure.getForegroundColor());
		}

		graphics.drawRoundRectangle(tempRect, arcWidth, arcHeight);
	}	
}
