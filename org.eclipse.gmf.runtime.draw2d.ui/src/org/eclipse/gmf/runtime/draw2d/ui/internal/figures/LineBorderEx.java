/******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.internal.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.swt.graphics.Color;

/**
 * This subclass of the LineBorder is required to provide MapMode support.
 * Without this the line border around figures using Hi-Metrics is displayed
 * incorrectly.
 * 
 * @author jschofie
 */
public class LineBorderEx
	extends LineBorder {

	/**
	 * Constructs a default black LineBorder with a width of one pixel.
	 */
	public LineBorderEx() {
		super();
	}

	/**
	 * Constructs a black LineBorder with a specified width
	 * @param width width of the line border in pixels
	 */
	public LineBorderEx(int width) {
		super(width);
	}

	/**
	 * Constructs a LineBorder with a width of 1 pixel using the color specified
	 * @param color
	 */
	public LineBorderEx(Color color) {
		super(color);
	}

	/**
	 * Construct a LineBorder with a given color and width
	 * @param color
	 * @param width width of the line border in pixels
	 */
	public LineBorderEx(Color color, int width) {
		super(color, width);
	}

	@Override
	public Insets getInsets(IFigure figure) {
		int widthInLP = MapModeUtil.getMapMode(figure).DPtoLP(getWidth());
		return new Insets(widthInLP);
	}

	@Override
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		tempRect.setBounds(getPaintRectangle(figure, insets));

		int widthInLP = MapModeUtil.getMapMode(figure).DPtoLP(getWidth());
		int halfWidthInLP = MapModeUtil.getMapMode(figure).DPtoLP(
				getWidth() / 2);
		tempRect.x += halfWidthInLP;
		tempRect.y += halfWidthInLP;
		tempRect.width -= widthInLP;
		tempRect.height -= widthInLP;

		graphics.setLineWidth(getWidth());
		graphics.setLineStyle(getStyle());
		if (getColor() != null)
			graphics.setForegroundColor(getColor());

		graphics.drawRectangle(tempRect);
	}
}
