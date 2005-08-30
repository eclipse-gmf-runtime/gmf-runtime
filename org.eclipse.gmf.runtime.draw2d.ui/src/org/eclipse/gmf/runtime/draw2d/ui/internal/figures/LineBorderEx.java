/***************************************************************************
 Licensed Materials - Property of IBM
 (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.

 US Government Users Restricted Rights - Use, duplication or disclosure
 restricted by GSA ADP Schedule Contract with IBM Corp.
***************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.internal.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Color;

import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;



/**
 * This subclass of the LineBorder is required to provide MapMode support.
 * Without this the line border around figures using Hi-Metrics is displayed
 * incorrectly.
 * 
 * @author jschofie
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.*
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
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.LineBorder#paint(org.eclipse.draw2d.IFigure, org.eclipse.draw2d.Graphics, org.eclipse.draw2d.geometry.Insets)
	 */
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		tempRect.setBounds(getPaintRectangle(figure, insets));
		if (getWidth() % 2 == 1) {
			tempRect.width -= MapMode.DPtoLP(1);
			tempRect.height -= MapMode.DPtoLP(1);
		}
		int shrinkWidth = MapMode.DPtoLP( getWidth() / 2 );
		tempRect.shrink(shrinkWidth, shrinkWidth);

		graphics.setLineWidth(getWidth());
		if (getColor() != null)
			graphics.setForegroundColor(getColor());

		graphics.drawRectangle(tempRect);
	}
}
