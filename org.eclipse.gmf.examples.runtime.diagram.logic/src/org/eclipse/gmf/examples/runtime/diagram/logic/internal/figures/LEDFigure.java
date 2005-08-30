/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.internal.themes.ColorUtils;

import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;

/**
 * code copied from real logic example in gef
 */
/*
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class LEDFigure
	extends NodeFigure
	implements HandleBounds
{
	private Color fontColor;
	
	public static final Dimension SIZE = new Dimension(MapMode.DPtoLP(61), MapMode.DPtoLP(44)); 

	/**
	 * Color of the shadow around the LEDFigure's display
	 */
	public static final Color DISPLAY_SHADOW = new Color(null, 57, 117, 90); 

	/**
	 * Color of the LEDFigure's displayed value
	 */
	public static final Color DISPLAY_TEXT = new Color(null, 255, 199, 16);

	protected static Rectangle displayRectangle = new Rectangle(MapMode.DPtoLP(15), MapMode.DPtoLP(11), MapMode.DPtoLP(31), MapMode.DPtoLP(25));
	protected static Rectangle displayShadow = new Rectangle(MapMode.DPtoLP(14), MapMode.DPtoLP(10), MapMode.DPtoLP(32), MapMode.DPtoLP(26));
	protected static Rectangle displayHighlight = new Rectangle(MapMode.DPtoLP(15), MapMode.DPtoLP(11), MapMode.DPtoLP(32), MapMode.DPtoLP(26));
	protected static Point valuePoint = new Point(MapMode.DPtoLP(16), MapMode.DPtoLP(10));

	protected static final int Y1 = MapMode.DPtoLP(0);
	protected static final int Y2 = MapMode.DPtoLP(44);

	protected String value;
	
	/**
	 * Creates a new LEDFigure
	 */
	public LEDFigure() {
		getBounds().width = SIZE.width;
		getBounds().height = SIZE.height;
	}
	
	/**
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		return SIZE;
	}

	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 */
	protected void paintFigure(Graphics g) {
		Rectangle r = getBounds().getCopy();
		
		g.translate(r.getLocation());
		g.fillRectangle(MapMode.DPtoLP(0), MapMode.DPtoLP(0), r.width, r.height /*- MapMode.DPtoLP(4)*/);	
		int right = r.width - 1;
		g.drawLine(MapMode.DPtoLP(0), Y1, right, Y1);
		g.drawLine(MapMode.DPtoLP(0), Y1, MapMode.DPtoLP(0), Y2);
	
		g.drawLine(MapMode.DPtoLP(0), Y2, right, Y2);
		g.drawLine(right, Y1, right, Y2);

		// Draw the display
		RGB whiteColor = ColorConstants.white.getRGB();
		RGB backgroundColor = getBackgroundColor().getRGB();
		RGB newHightlightRGB = new RGB((whiteColor.red + backgroundColor.red)/2 , (whiteColor.green + backgroundColor.green)/2 , (whiteColor.blue + backgroundColor.blue)/2 );
		g.setBackgroundColor( PresentationResourceManager.getInstance()
			.getColor(newHightlightRGB));
		g.fillRectangle(displayHighlight);
		RGB blackColor = ColorConstants.black.getRGB();
		RGB newShadowRGB = new RGB((blackColor.red + backgroundColor.red)/2 , (blackColor.green + backgroundColor.green)/2 , (blackColor.blue + backgroundColor.blue)/2 );
		g.setBackgroundColor( PresentationResourceManager.getInstance()
			.getColor(newShadowRGB));
		g.fillRectangle(displayShadow);
		
		g.setBackgroundColor(ColorConstants.black);
		g.fillRectangle(displayRectangle);
	
		// Draw the value
		g.setForegroundColor(getFontColor());
		g.drawText(value, valuePoint);
	}
	
	/**
	 * @return font color 
	 */
	public Color getFontColor() {
		return fontColor;
	}
	
	/**
	 * @param c set the font color
	 */
	public void setFontColor(Color c) {
		fontColor = c;
		revalidate();
	}

	/**
	 * Sets the value of the LEDFigure to val.
	 * 
	 * @param val The value to set on this LEDFigure
	 */
	public void setValue(int val) {
		value = String.valueOf(val);
		if (val < 10)
			value = "0" + value;	//$NON-NLS-1$
		repaint();
	}

	/**
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "LEDFigure"; //$NON-NLS-1$
	}
}
