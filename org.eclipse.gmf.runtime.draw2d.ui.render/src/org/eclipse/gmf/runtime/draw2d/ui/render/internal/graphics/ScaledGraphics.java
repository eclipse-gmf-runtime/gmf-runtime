/****************************************************************************
Licensed Materials - Property of IBM
(C) Copyright IBM Corp. 2004. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
*****************************************************************************/

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
public class ScaledGraphics extends org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScaledGraphics 
					implements DrawableRenderedImage {
	
	private Graphics graphics;
	
	/**
	 * Constructs a new ScaledGraphics based on the given Graphics object.
	 * @param g the base graphics object
	 */
	public ScaledGraphics(Graphics g) {
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
