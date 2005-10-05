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


package org.eclipse.gmf.runtime.draw2d.ui.internal.graphics;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import org.eclipse.swt.graphics.Image;


/**
 * The MapModeGraphics class is used to translate the various map modes.
 *
 * @author sshaw
 */
public class MapModeGraphics extends ScaledGraphics {

	/**
	 * Constructs a new ScaledGraphics based on the given Graphics object.
	 * @param g the base graphics object
	 */
	public MapModeGraphics(Graphics g) {
		super(g);
		setScale(MapMode.getScale());
	}

	/** @see Graphics#drawImage(Image, int, int) */
	public void drawImage(Image srcImage, int x, int y) {
		org.eclipse.swt.graphics.Rectangle size = srcImage.getBounds();
		drawImage(srcImage, 0, 0, size.width, size.height, x, y, size.width, size.height);
	}

	/** @see Graphics#drawImage(Image, int, int, int, int, int, int, int, int) */
	public void drawImage(Image srcImage, int sx, int sy, int sw, int sh,
											int tx, int ty, int tw, int th) {
		super.drawImage(srcImage, sx, sy, sw, sh, tx, ty, MapMode.DPtoLP(tw), MapMode.DPtoLP(th));
	}
	
	/**
	 * Override to prevent zooming of the font height.
	 */
	int zoomFontHeight(int height) {
		return height;
	}

	Point zoomTextPoint(int x, int y) {
		return zoomRect(x, y, 0, 0).getTopLeft();
	}
}
