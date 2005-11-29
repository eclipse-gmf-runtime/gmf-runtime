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
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.DrawableRenderedImage;
import org.eclipse.swt.graphics.Image;


/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 *
 * Subclass to allow implementation of the DrawableRenderedImage interface
 */
public class RenderedScaledGraphics extends org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScaledGraphics 
					implements DrawableRenderedImage {
	
	/**
	 * Constructs a new ScaledGraphics based on the given Graphics object.
	 * @param g the base graphics object
	 */
	public RenderedScaledGraphics(Graphics g) {
		super(g);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.internal.DrawableRenderedImage#drawRenderedImage(org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage, int, int, int, int)
	 */
	public RenderedImage drawRenderedImage(RenderedImage srcImage, int x, int y, int width, int height) {
		if (getGraphics() instanceof DrawableRenderedImage) {
			Rectangle r = zoomRect(x, y, width, height);
			return ((DrawableRenderedImage)getGraphics()).drawRenderedImage(srcImage, r.x, r.y, r.width, r.height);
		}
		else {
			Rectangle r = zoomRect(x, y, width, height);
			RenderInfo info = srcImage.getRenderInfo();
			info.setValues(r.width, r.height, 
							info.getFillColor(), info.getOutlineColor(), 
							info.shouldMaintainAspectRatio(), info.shouldAntiAlias());
			
			RenderedImage img = srcImage.getNewRenderedImage(info);
			
			Image swtImg = img.getSWTImage();
			if (swtImg!=null)
				getGraphics().drawImage(swtImg, r.x, r.y + r.height - swtImg.getBounds().height);
			return img;
		}
	}
}
