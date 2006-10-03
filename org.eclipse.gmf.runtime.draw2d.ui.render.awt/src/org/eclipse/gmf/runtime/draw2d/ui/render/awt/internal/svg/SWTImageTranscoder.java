/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg;

import java.awt.Graphics2D;

import org.apache.batik.ext.awt.RenderingHintsKeyExt;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.graphics.Graphics2DToGraphicsAdaptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;


/**
 * Image transcoder that will allow direct rendering to an swt <code>GC</code> object through
 * a delegating class.
 * 
 * @see Graphics2DToGraphicsAdaptor
 * 
 * @author sshaw
 *
 */
public class SWTImageTranscoder
	extends ImageTranscoderEx {

	private Image swtImage = null;
	
	public SWTImageTranscoder() {
		super();
	}

	/**
	 * @return Returns the swtImage.
	 */
	public Image getSWTImage() {
		return swtImage;
	}

	private static final RGB TRANSPARENT_COLOR = new RGB(254, 255, 254);
	private static final RGB REPLACE_TRANSPARENT_COLOR = new RGB(255, 255, 255);
	
	private GC swtGC = null;
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.ImageTranscoderEx#createGraphics(int, int)
	 */
	protected Graphics2D createGraphics(int w, int h) {
		Display display = Display.getDefault();

		ImageData imgData = new ImageData(w, h, 24, new PaletteData(0xFF0000, 0xFF00, 0xFF));
		imgData.transparentPixel = FigureUtilities.RGBToInteger(TRANSPARENT_COLOR).intValue();
		
		swtImage = new Image(display, imgData);
		swtGC = new GC(swtImage);
		
		Color transparentColor = new Color(null, TRANSPARENT_COLOR);
		swtGC.setBackground(transparentColor);
		swtGC.fillRectangle(0, 0, w, h);
		transparentColor.dispose();
		
        Graphics2D g2d = new Graphics2DToGraphicsAdaptor(swtGC, TRANSPARENT_COLOR, REPLACE_TRANSPARENT_COLOR);
        // needed to avoid eroneous error being dumped to console
        g2d.setRenderingHint(RenderingHintsKeyExt.KEY_TRANSCODING,
            RenderingHintsKeyExt.VALUE_TRANSCODING_PRINTING);
        
        return g2d;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.ImageTranscoderEx#postRenderImage(java.awt.Graphics2D)
	 */
	protected void postRenderImage(Graphics2D g2d) {
		super.postRenderImage(g2d);
		
		swtGC.dispose();
	}
	
}
