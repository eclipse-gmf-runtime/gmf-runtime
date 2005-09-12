/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Display;

/**
 * @author sshaw
 *
 * The is a specialization of the ImageTranscoder class to support the SWT Image
 * format.  
 */
class SWTImageTranscoder extends ImageTranscoderEx {

	protected Image swtImage = null;
	
	/**
	 * Constructor to create an instance of SWTImageTranscoder.
	 */
	public SWTImageTranscoder() {
		// empty constructor
	}
		
	/**
	 * Override to create a BufferedImage type that support an alpha channel for
	 * transparency.
	 * 
	 * @see org.apache.batik.transcoder.image.ImageTranscoder#createImage(int, int)
	 */
	public BufferedImage createImage(int w, int h) {
		return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	}

	/** 
	 * Override to support the translation of the BufferedImage type into the SWT Image
	 * format.
	 * 
	 * @see org.apache.batik.transcoder.image.ImageTranscoder#writeImage(java.awt.image.BufferedImage, org.apache.batik.transcoder.TranscoderOutput)
	 */
	public void writeImage(BufferedImage img, TranscoderOutput arg1)
		throws TranscoderException {

		PaletteData palette = new PaletteData(0xFF0000, 0xFF00, 0xFF);

		// We can force bitdepth to be 24 bit because BufferedImage getRGB allows us to always
		// retrieve 24 bit data regardless of source color depth.
		ImageData swtImageData =
			new ImageData(img.getWidth(), img.getHeight(), 24, palette);

		// ensure scansize is aligned on 32 bit.
		int scansize = (((img.getWidth() * 3) + 3) * 4) / 4;
		
		WritableRaster alphaRaster = img.getAlphaRaster();
			
		for (int y=0; y<img.getHeight(); y++) {
			int[] buff = img.getRGB(0, y, img.getWidth(), 1, null, 0, scansize);
			swtImageData.setPixels(0, y, img.getWidth(), buff, 0);
			
			// check for alpha channel
			if (alphaRaster != null) {
				int[] alpha = alphaRaster.getPixels(0, y, img.getWidth(), 1, (int[])null);
				byte[] alphaBytes = new byte[img.getWidth()];
				for (int i=0; i<img.getWidth(); i++)
					alphaBytes[i] = (byte)alpha[i];
				swtImageData.setAlphas(0, y, img.getWidth(), alphaBytes, 0);
			}
		}
	
		swtImage = new Image(Display.getDefault(), swtImageData);
	}
	
	/**
	 * getSWTImage
	 * Accessor to retrieve the rendered SWT Image.
	 * 
	 * @return Image that conains the rendered SVG data.
	 */
	public Image getSWTImage(){
		return swtImage;
	}

}
