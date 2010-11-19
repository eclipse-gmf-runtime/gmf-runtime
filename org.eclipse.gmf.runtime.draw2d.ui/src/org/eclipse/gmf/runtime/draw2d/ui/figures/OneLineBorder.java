/******************************************************************************
 * Copyright (c) 2002, 2010 IBM Corporation and others.
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
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.swt.graphics.Color;

/**
 * This is a border that only draws a line at one position around the shape based on a position 
 * constant.  This is useful for cases where it may be necessary to draw a separator between shapes
 * without instantiating a new figure.
 * 
 * @author sshaw
 */
public class OneLineBorder extends LineBorder {

	private int position = PositionConstants.TOP;

	/**
	 * 
	 */
	public OneLineBorder() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param width the width of the line in relative coordinates
	 * @param position the value should be one of <code>PositionConstants.TOP</code>, <code>PositionConstants.BOTTOM</code>,
	 * <code>PositionConstants.LEFT</code>, <code>PositionConstants.RIGHT</code>
	 */
	public OneLineBorder(int width, int position) {
		super(width);
		this.position = position;
	}

	/**
	 * Constructor
	 * 
	 * @param color The color of the border.
	 */
	public OneLineBorder(Color color) {
		super(color);
	}

	/**
	 * Constructor
	 * 
	 * @param color The color of the border.
	 * @param width the width of the line in relative coordinates
	 * @param position the value should be one of <code>PositionConstants.TOP</code>, <code>PositionConstants.BOTTOM</code>,
	 * <code>PositionConstants.LEFT</code>, <code>PositionConstants.RIGHT</code>
	 */
	public OneLineBorder(Color color, int width, int position) {
		super(color, width);
		this.position = position;
	}

	/**
	 * @return Returns the position.
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position The position to set.
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.draw2d.Border#getInsets(org.eclipse.draw2d.IFigure)
	 */
	public Insets getInsets(IFigure figure) {
		switch (position) {
			case PositionConstants.TOP :
				return new Insets(getWidth(), 0, 0, 0);
			case PositionConstants.LEFT :
				return new Insets(0, getWidth(), 0, 0);
			case PositionConstants.BOTTOM :
				return new Insets(0, 0, getWidth(), 0);
			case PositionConstants.RIGHT :
				return new Insets(0, 0, 0, getWidth());
			default :
				return IFigure.NO_INSETS;
		}
	}

	/**
	 * Paints the border based on the inputs given. 
	 *
	 * @param figure  <code>IFigure</code> for which this is the border.
	 * @param graphics  <code>Graphics</code> handle for drawing the border.
	 * @param insets  Space to be taken up by this border.
	 */
	@Override
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		tempRect.setBounds(getPaintRectangle(figure, insets));

		int one = MapModeUtil.getMapMode(figure).DPtoLP(1);
		int widthInDP = getWidth() / one;
		
		int halfWidthInLP = MapModeUtil.getMapMode(figure).DPtoLP(widthInDP / 2);
		
		graphics.setLineWidth(getWidth());
		graphics.setLineStyle(getStyle());
		if (getColor() != null) {
			graphics.setForegroundColor(getColor());
		}
		
		switch (position) {
			case PositionConstants.TOP :
				tempRect.y += halfWidthInLP;
				tempRect.height -= getWidth();
				graphics.drawLine(tempRect.getTopLeft(), tempRect.getTopRight());				
				break;
			case PositionConstants.BOTTOM :	
				tempRect.y += halfWidthInLP;
				tempRect.height -= getWidth();
				graphics.drawLine(tempRect.getBottomLeft(), tempRect.getBottomRight());
				break;
			case PositionConstants.LEFT :
				tempRect.x += halfWidthInLP;
				tempRect.width -= getWidth();
				graphics.drawLine(tempRect.getTopLeft(), tempRect.getBottomLeft());
				break;
			case PositionConstants.RIGHT :
				tempRect.x += halfWidthInLP;
				tempRect.width -= getWidth();
				graphics.drawLine(tempRect.getTopRight(), tempRect.getBottomRight());
				break;
		}
	}

}
