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

package org.eclipse.gmf.runtime.draw2d.ui.figures;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * A set of methods that are useful when manipulating figures.  These are often
 * used in the paint routines or locators of IFigure implementors to do complex 
 * geometry calculations that may be common across different parts of a hierarchy.
 * 
 * @author sshaw
 * @canBeSeenBy %partners
 */
public class FigureUtilities extends org.eclipse.draw2d.FigureUtilities {

	/**
	 * Method colorToInteger.
	 * converts from a Color to an Integer representation
	 * @param c
	 * @return Integer
	 */
	public static Integer colorToInteger(Color c) {
		return new Integer(
			(c.getBlue() << 16) | (c.getGreen() << 8) | c.getRed());
	}

	/**
	 * Method integerToColor.
	 * converts from an Integer to a Color representation
	 * 
	 * Note: Normally, colors should be instantiated 
	 * using the AbstractResourceManager.
	 * 
	 * @param i
	 * @return Color
	 */
	public static Color integerToColor(Integer i) {
		if (i == null)
			return null;
		int color = i.intValue();
		return new Color(
			null,
			(color & 0x000000FF),
			(color & 0x0000FF00) >> 8,
			(color & 0x00FF0000) >> 16);
	}

	/**
	 * Method RGBToInteger
	 * converts from an RGB to an Integer representation
	 * @param rgb
	 * @return Integer
	 */
	public static Integer RGBToInteger(RGB rgb) {
		return new Integer((rgb.blue << 16) | (rgb.green << 8) | rgb.red);
	}


	/**
	 * Method integerToRGB
	 * converts from an Integer to an RGB representation
	 * @param color
	 * @return RGB
	 */
	public static RGB integerToRGB(Integer color) {
		int n = color.intValue();
		return new RGB(			
				(n & 0x000000FF),
				(n & 0x0000FF00) >> 8,
				(n & 0x00FF0000) >> 16);
	}

	/** 
	 * Return the location within the supplied constraint the supplied rectangle requires to be
	 * positioned according to the alignment parameter <i>pos</i>.  
	 * 
	 * <pre>
	 * NW-----N------NE
	 * |              |
	 * W    NS/EW     E
	 * |              |
	 * SW-----S------SE
	 * 
	 * </pre>
	 * <P>
	 * @param pos a geographic PositionConstant (N,E,W,S,NW,NW,NS,EW)
	 * @param topos the rectangle to be (re)positioned.
	 * @param constraint the containing bounds.
	 * @return a new location.
	 */
	public static Point getLocation(
		int pos,
		final Rectangle topos,
		final Rectangle constraint) {
		Rectangle b = constraint.getCopy();
		Point svb = new Point(b.x, b.y);

		switch (pos) {
			case PositionConstants.NORTH :
				svb.x += b.width / 2;
				break;

			case PositionConstants.SOUTH :
				svb.x += b.width / 2;
				svb.y += b.height;
				break;

			case PositionConstants.WEST :
				svb.y += b.height / 2 - topos.height / 2;
				break;

			case PositionConstants.EAST :
				svb.x += b.width;
				svb.y += b.height / 2 - topos.height / 2;
				break;

			case PositionConstants.NORTH_EAST : //top right
				svb.x += b.width - topos.width;
				break;

			case PositionConstants.SOUTH_EAST : //bottom right
				svb.x += b.width - topos.width;
				svb.y += b.height - topos.height;
				break;

			case PositionConstants.SOUTH_WEST : //bottom left
				svb.x += b.width;
				svb.y += topos.height;
				break;

			case PositionConstants.NORTH_SOUTH : // center of bounds area
			case PositionConstants.EAST_WEST :
				svb.x += b.width / 2 - topos.width / 2;
				svb.y += b.height / 2 - topos.height / 2;
				break;

			case PositionConstants.NORTH_WEST : //top left
			default :
				break;
		}
		return svb;
	}

	/**
	 * Return the location within the supplied constraint of an object of dimension  <i>dim</i> 
	 * according to the alignment parameter. 
	 * @see #getLocation(int, Rectangle, Rectangle)
	 * @param pos a geographic PositionConstant (N,E,W,S,NW,NW,NS,EW)
	 * @param dim some dimension.
	 * @param constraint the containing bounds.
	 * @return a new location.
	 */
	public static Point getLocation(
		int pos,
		final Dimension dim,
		final Rectangle constraint) {
		return getLocation(
			pos,
			new Rectangle(0, 0, dim.width, dim.height),
			constraint);
	}

}
