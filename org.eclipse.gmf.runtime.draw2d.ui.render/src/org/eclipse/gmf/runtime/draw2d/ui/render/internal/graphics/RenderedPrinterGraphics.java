/******************************************************************************
 * Copyright (c) 2003 - 2005 IBM Corporation and others.
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.printing.Printer;

import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.PrinterGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.DrawableRenderedImage;

/**
 * Created on May 8, 2003
 *
 * @author sshaw
 * @version 1.0
 * 
 * Overridden to handle printing problem with transparencies.  Specifically,
 * dropshadows print with as a black bar (RATLC00513998)
 */
public class RenderedPrinterGraphics extends PrinterGraphics 
								implements DrawableRenderedImage {
	
    /**
	* Creates a new PrinterGraphics with Graphics g, using Printer p
	* @param g Graphics object to draw with
	* @param p Printer to print to
	* @param roundFonts the <code>boolean</code> if <code>true</code> indicates that fonts 
	* should be rounded to account for printer font scaling errors.
	*/
	public RenderedPrinterGraphics(Graphics g, Printer p, boolean roundFonts) {
		super(g, p, roundFonts);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.internal.DrawableRenderedImage#drawRenderedImage(org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage, int, int, int, int)
	 */
	public void drawRenderedImage(RenderedImage srcImage, int x, int y, int width, int height) {
		int nNewWidth = (int)Math.round(width * getPrintScale());
		int nNewHeight = (int)Math.round(height * getPrintScale());
		
		RenderInfo info = srcImage.getRenderInfo();
		info.setValues(nNewWidth, nNewHeight, 
						info.getFillColor(), info.getOutlineColor(), 
						info.shouldMaintainAspectRatio(), true);
		
		RenderedImage img = srcImage.getNewRenderedImage(info);
		
		Image swtImg = img.getSWTImage();
		drawImage(swtImg, x, y + height - swtImg.getBounds().height);
	}
}
