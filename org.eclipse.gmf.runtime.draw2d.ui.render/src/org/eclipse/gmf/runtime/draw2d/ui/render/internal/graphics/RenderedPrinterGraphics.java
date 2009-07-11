/******************************************************************************
 * Copyright (c) 2003, 2009 IBM Corporation and others.
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
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.PrinterGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.DrawableRenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.RenderingListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.printing.Printer;

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
	
    /* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.internal.DrawableRenderedImage#allowDelayRender()
	 */
	public boolean shouldAllowDelayRender() {
		return false;
	}
	
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.internal.DrawableRenderedImage#drawRenderedImage(org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage, org.eclipse.draw2d.geometry.Rectangle, org.eclipse.gmf.runtime.draw2d.ui.render.RenderingListener)
	 */
	public RenderedImage drawRenderedImage(RenderedImage srcImage, Rectangle rect, RenderingListener listener) {
        int nNewWidth = (int)Math.round(rect.width * getPrintScale());
        int nNewHeight = (int)Math.round(rect.height * getPrintScale());
            
        RenderInfo info = srcImage.getRenderInfo();
        info.setValues(nNewWidth, nNewHeight, 
                        info.shouldMaintainAspectRatio(), false, 
                        info.getBackgroundColor(), info.getForegroundColor());
            
        RenderedImage img = srcImage.getNewRenderedImage(info);
        
        Image swtImg = null;
        try {
            swtImg = img.getSWTImage();
        }
        catch (OutOfMemoryError e) {
            // don't do any scaling and rerender with anti-aliasing on
            nNewWidth = rect.width;
            nNewHeight = rect.height;
            info.setValues(nNewWidth, nNewHeight, 
                info.shouldMaintainAspectRatio(), true, 
                info.getBackgroundColor(), info.getForegroundColor());
            img = srcImage.getNewRenderedImage(info);
            swtImg = img.getSWTImage();
        }
        catch (Exception ex) {
            // don't do any scaling and rerender with anti-aliasing on
            nNewWidth = rect.width;
            nNewHeight = rect.height;
            info.setValues(nNewWidth, nNewHeight, 
                info.shouldMaintainAspectRatio(), true, 
                info.getBackgroundColor(), info.getForegroundColor());
            img = srcImage.getNewRenderedImage(info);
            swtImg = img.getSWTImage();
        }
        
        drawImage(swtImg, 0, 0, nNewWidth, nNewHeight, 
                rect.x, rect.y, rect.width, rect.height);
        
		/*
		 * Printed rendered image should not be set on the figure. Printing has
		 * its own scaling independent of the diagram
		 */
        return srcImage;
	}
	
    /* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.internal.DrawableRenderedImage#getMaximumRenderSize()
	 */
	public Dimension getMaximumRenderSize() {
        return null;
	}
}
