/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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
import org.eclipse.swt.graphics.Image;

import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.MapModeGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.DrawableRenderedImage;


/**
 * Overridden to implement DrawableRenderedImage interface
 *
 * @author sshaw
 */
public class RenderedMapModeGraphics
	extends MapModeGraphics implements DrawableRenderedImage {

	/**
	 * Constructor
	 * @param g <code>Graphics</code> element to delegate render to
	 * @param mm <code>IMapMode</code> to retrieve the scale factor from.
	 */
	public RenderedMapModeGraphics(Graphics g, IMapMode mm) {
		super(g, mm);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.internal.DrawableRenderedImage#drawRenderedImage(org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage, int, int, int, int)
	 */
	public void drawRenderedImage(RenderedImage srcImage, int x, int y, int width, int height) {
		if (getGraphics() instanceof DrawableRenderedImage) {
			Rectangle r = zoomRect(x, y, width, height);
			((DrawableRenderedImage)getGraphics()).drawRenderedImage(srcImage, r.x, r.y, r.width, r.height);
		}
		else {
			Rectangle r = zoomRect(x, y, width, height);
			RenderInfo info = srcImage.getRenderInfo();
			info.setValues(r.width, r.height, 
							info.getFillColor(), info.getOutlineColor(), 
							info.shouldMaintainAspectRatio(), info.shouldAntiAlias());
			
			RenderedImage img = srcImage.getNewRenderedImage(info);
			
			Image swtImg = img.getSWTImage();
			getGraphics().drawImage(swtImg, r.x, r.y + r.height - swtImg.getBounds().height);
		}
	}
}
