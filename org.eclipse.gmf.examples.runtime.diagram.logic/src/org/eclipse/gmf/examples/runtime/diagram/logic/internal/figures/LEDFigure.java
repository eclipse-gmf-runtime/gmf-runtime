/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramColorRegistry;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

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
	
	/**
	 * Color of the shadow around the LEDFigure's display
	 */
	public static final Color DISPLAY_SHADOW = new Color(null, 57, 117, 90); 

	/**
	 * Color of the LEDFigure's displayed value
	 */
	public static final Color DISPLAY_TEXT = new Color(null, 255, 199, 16);

	protected String value;
	
	private Dimension prefSize;
	/**
	 * Creates a new LEDFigure
	 */
	public LEDFigure(Dimension prefSize) {
		getBounds().width = prefSize.width;
		getBounds().height = prefSize.height;
		this.prefSize = new Dimension(prefSize);
	}
	
	/**
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		return new Dimension(prefSize);
	}

	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 */
	protected void paintFigure(Graphics g) {
		IMapMode mm = MapModeUtil.getMapMode(this);
		Rectangle displayRectangle = new Rectangle(mm.DPtoLP(15), mm.DPtoLP(11), mm.DPtoLP(31), mm.DPtoLP(25));
		Rectangle displayShadow = new Rectangle(mm.DPtoLP(14), mm.DPtoLP(10), mm.DPtoLP(32), mm.DPtoLP(26));
		Rectangle displayHighlight = new Rectangle(mm.DPtoLP(15), mm.DPtoLP(11), mm.DPtoLP(32), mm.DPtoLP(26));
		Point valuePoint = new Point(mm.DPtoLP(16), mm.DPtoLP(10));

		int Y1 = mm.DPtoLP(0);
		int Y2 = mm.DPtoLP(44);
		
		Rectangle r = getBounds().getCopy();
		
		g.translate(r.getLocation());
		g.fillRectangle(mm.DPtoLP(0), mm.DPtoLP(0), r.width, r.height /*- MapMode.DPtoLP(4)*/);	
		int right = r.width - 1;
		g.drawLine(mm.DPtoLP(0), Y1, right, Y1);
		g.drawLine(mm.DPtoLP(0), Y1, mm.DPtoLP(0), Y2);
	
		g.drawLine(mm.DPtoLP(0), Y2, right, Y2);
		g.drawLine(right, Y1, right, Y2);

		// Draw the display
		RGB whiteColor = ColorConstants.white.getRGB();
		RGB backgroundColor = getBackgroundColor().getRGB();
		RGB newHightlightRGB = new RGB((whiteColor.red + backgroundColor.red)/2 , (whiteColor.green + backgroundColor.green)/2 , (whiteColor.blue + backgroundColor.blue)/2 );
		g.setBackgroundColor( DiagramColorRegistry.getInstance()
			.getColor(newHightlightRGB));
		g.fillRectangle(displayHighlight);
		RGB blackColor = ColorConstants.black.getRGB();
		RGB newShadowRGB = new RGB((blackColor.red + backgroundColor.red)/2 , (blackColor.green + backgroundColor.green)/2 , (blackColor.blue + backgroundColor.blue)/2 );
		g.setBackgroundColor( DiagramColorRegistry.getInstance()
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
