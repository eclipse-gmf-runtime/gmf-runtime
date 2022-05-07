/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * A set of methods that are useful when manipulating figures.  These are often
 * used in the paint routines or locators of IFigure implementors to do complex 
 * geometry calculations that may be common across different parts of a hierarchy.
 * 
 * @author sshaw
 */
public class FigureUtilities extends org.eclipse.draw2d.FigureUtilities {

	/**
	 * Method colorToInteger.
	 * converts from a Color to an Integer representation
	 * @param c
	 * @return Integer
	 */
	public static Integer colorToInteger(Color c) {
		return Integer.valueOf(
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
		return Integer.valueOf((rgb.blue << 16) | (rgb.green << 8) | rgb.red);
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

	
	/**
	 * Helper method to paint a grid.  Painting is optimized as it is restricted to the
	 * Graphics' clip.
	 * 
	 * @param	g			The Graphics object to be used for painting
	 * @param	f			The figure in which the grid is to be painted
	 * @param	origin		Any point where the grid lines are expected to intersect
	 * @param	distanceX	Distance between vertical grid lines; if 0 or less, vertical grid
	 * 						lines will not be drawn
	 * @param	distanceY	Distance between horizontal grid lines; if 0 or less, horizontal
	 * 						grid lines will not be drawn
	 * @param	lineStyle   Line style to be used for painting the grid lines
	 * @param   dashes      Dash pattern to be used for the grid line (ignored if lineStyle != LINE_CUSTOM)
	 * 
	 */
	public static void paintGridWithStyle(Graphics g, IFigure f, 
			org.eclipse.draw2d.geometry.Point origin, int distanceX, int distanceY, int lineStyle, int[] dashes) {
		Rectangle clip = g.getClip(Rectangle.SINGLETON);
		
		int origLineStyle = g.getLineStyle();		
		
		if (distanceX > 0) {
			if (origin.x >= clip.x)
				while (origin.x - distanceX >= clip.x)
					origin.x -= distanceX;
			else
				while (origin.x < clip.x)
					origin.x += distanceX;
			for (int i = origin.x; i < clip.x + clip.width; i += distanceX) {
				g.setLineStyle(lineStyle);
				if ((dashes != null) && (lineStyle == SWT.LINE_CUSTOM)) g.setLineDash(dashes);
				g.drawLine(i, clip.y, i, clip.y + clip.height);
				g.setLineStyle(origLineStyle);
			}
		}
		
		if (distanceY > 0) {
			if (origin.y >= clip.y)
				while (origin.y - distanceY >= clip.y)
					origin.y -= distanceY;
			else
				while (origin.y < clip.y)
					origin.y += distanceY;
			for (int i = origin.y; i < clip.y + clip.height; i += distanceY) {
				g.setLineStyle(lineStyle);
				if ((dashes != null) && (lineStyle == SWT.LINE_CUSTOM)) g.setLineDash(dashes);
				g.drawLine(clip.x, i, clip.x + clip.width, i);
				g.setLineStyle(origLineStyle);
			}
		}
	}

	/**
	 * Calculates the anchorable figure bounds. There could be cases that a figure
	 * implements both <code>IOvalAnchorableFigure</code> and <code>IPolygonAnchorableFigure</code>
	 * interfaces. The latter interface is more popular because any figure can be approximated
	 * with line segments including ellipse. Therefore, we need to check first if it's an
	 * ellipse like figure and then if it's some kind of a polygon.
	 * 
	 * @param figure the figure
	 * @return the bounding <code>Rectangle</code> of the anchorable part of the figure
	 */
	public static Rectangle getAnchorableFigureBounds(IFigure figure) {
		if (figure instanceof IOvalAnchorableFigure) {
			return ((IOvalAnchorableFigure)figure).getOvalBounds().getCopy();
		} else if (figure instanceof IPolygonAnchorableFigure) {
			return ((IPolygonAnchorableFigure)figure).getPolygonPoints().getBounds();
		} else if (figure instanceof IFigure) {
			return figure.getBounds().getCopy();
		}
		return null;
	}
	
}
