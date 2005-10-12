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


package org.eclipse.gmf.runtime.draw2d.ui.render.internal.graphics;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.DrawableRenderedImage;


/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 *
 * Subclass to allow implementation of the DrawableRenderedImage interface
 */
public class RenderedScaledGraphics extends org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScaledGraphics 
					implements DrawableRenderedImage {
	
	private Graphics graphics;
	
	/**
	 * Constructs a new ScaledGraphics based on the given Graphics object.
	 * @param g the base graphics object
	 */
	public RenderedScaledGraphics(Graphics g) {
		super(g);
		graphics = g;
	}
	
	private double scale = 1.0;
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Graphics#scale(double)
	 */
	public void scale(double amount) {
		scale = amount;
		super.scale(amount);
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.internal.DrawableRenderedImage#drawRenderedImage(RenderedImage, int, int, int)
	 */
	public void drawRenderedImage(RenderedImage srcImage, int x, int y, int width, int height) {
		if (graphics instanceof DrawableRenderedImage) {
			Point tr = new Point((int)(Math.round(x * scale)), (int)(Math.round(y * scale)));
			Dimension dim = new Dimension((int)(Math.round(((x + width) * scale))) - tr.x,
				(int)(Math.round(((y + height) * scale))) - tr.y);
			
			((DrawableRenderedImage)graphics).drawRenderedImage(srcImage, tr.x, tr.y, dim.width, dim.height);
		}
		else {
			int nNewWidth = (int)Math.round(srcImage.getRenderInfo().getWidth() * scale);
			int nNewHeight = (int)Math.round(srcImage.getRenderInfo().getHeight() * scale);
				
			RenderInfo info = srcImage.getRenderInfo();
			info.setValues(nNewWidth, nNewHeight, 
							info.getFillColor(), info.getOutlineColor(), 
							info.shouldMaintainAspectRatio(), info.shouldAntiAlias());
				
			RenderedImage img = srcImage.getNewRenderedImage(info);
				
			drawImage(img.getSWTImage(), 0, 0, nNewWidth, nNewHeight, 
					x, y, srcImage.getRenderInfo().getWidth(), srcImage.getRenderInfo().getHeight());
		}
	}
}
