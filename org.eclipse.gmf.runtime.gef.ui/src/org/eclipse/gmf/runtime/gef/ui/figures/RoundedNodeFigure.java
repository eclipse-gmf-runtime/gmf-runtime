/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.gef.ui.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;


/**
 * Draws a rounded corner rectangle.
 * @author mhanner
 */
public class RoundedNodeFigure extends DefaultSizeNodeFigure {

	private static final int DEFAULT_ARC = 530;

	/** corner radius data. */
	private int _arcWidth, _arcHeight;

	/** Create an instance. */
	public RoundedNodeFigure() {
		this(DEFAULT_ARC, DEFAULT_ARC);
	}

	/** 
	 * Create an instance.
	 * <tt>arcWidth</tt> and <tt>arcHeight</tt> represent the horizontal and 
	 * vertical diameter of the corners.
	 * 
	 * @param arcWidth the arc width
	 * @param arcHeight the arc height
	 */
	public RoundedNodeFigure(int arcWidth, int arcHeight) {
		_arcWidth = arcWidth;
		_arcHeight = arcHeight;
	}

	/**
	 * Returns the arc height
	 * 
	 * @return the arc height of the rounded corners.
	 */
	public int getArcHeight() {
		return _arcHeight;
	}

	/**
	 * Sets the arc height of the rounded corners.
	 * 
	 * @param height the arc height of the rounded corners.
	 */
	public void setArcHeight(int height) {
		_arcHeight = height;
	}

	/**
	 * Returns the arc width
	 * 
	 * @return the arc width of the rounded corners.
	 */
	public int getArcWidth() {
		return _arcWidth;
	}

	/**
	 * Sets the arc width of the rounded corners.
	 * 
	 * @param width the arc width of the rounded corners.
	 */
	public void setArcWidth(int width) {
		_arcWidth = width;
	}

	/**
	 * paints the object: a rectangle with rounded corners.
	 */
	protected void paintFigure(Graphics graphics) {
		Rectangle r = getBounds().getCopy();
	
		graphics.fillRoundRectangle(r, getArcWidth(), getArcHeight());
		
		// Adjust for SWT bug?
		r.width -= 1;
		r.height -= 1;
		graphics.drawRoundRectangle(r, getArcWidth(), getArcHeight());
	}
}
