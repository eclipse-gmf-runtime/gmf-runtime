/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.draw2d.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Extends {@link org.eclipse.draw2d.LineBorder} to support line style.
 * 
 * @author Anthony Hunter
 * @since 2.1
 */
public class GeoShapeLineStyleBorder extends LineBorder {

	private int style = Graphics.LINE_SOLID;

	/**
	 * Constructs a default black LineBorder with a width of one pixel.
	 * 
	 * @since 2.1
	 */
	public GeoShapeLineStyleBorder() {
		super();
	}

	/**
	 * Get the line style of this border.
	 * 
	 * @return The line style of this border.
	 */
	public int getStyle() {
		return style;
	}

	/**
	 * Set the line style of this border.
	 * 
	 * @param style
	 *            The line style of this border.
	 */
	public void setStyle(int style) {
		this.style = style;
	}

	/*
	 * @see org.eclipse.draw2d.LineBorder#paint(org.eclipse.draw2d.IFigure,
	 *      org.eclipse.draw2d.Graphics, org.eclipse.draw2d.geometry.Insets)
	 */
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		Rectangle bounds = getPaintRectangle(figure, insets).getCopy();
		bounds.x = bounds.x + getWidth() / 2;
		bounds.y = bounds.y + getWidth() / 2;
		bounds.width = bounds.width - Math.max(1, getWidth());
		bounds.height = bounds.height - Math.max(1, getWidth());
		graphics.setLineWidth(getWidth());
		graphics.setLineStyle(getStyle());
		if (getColor() != null)
			graphics.setForegroundColor(getColor());
		graphics.drawRectangle(bounds);
	}
}
