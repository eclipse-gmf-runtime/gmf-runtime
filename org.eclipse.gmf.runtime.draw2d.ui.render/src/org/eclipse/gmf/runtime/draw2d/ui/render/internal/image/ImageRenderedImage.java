/******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.draw2d.ui.render.internal.image;

import java.io.ByteArrayInputStream;
import java.security.InvalidParameterException;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.AbstractRenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.Draw2dRenderDebugOptions;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.Draw2dRenderPlugin;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.factory.RenderedImageKey;
import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.PlatformUI;

/**
* Class that represents a Image image. This is a useful abstraction so that it
* can be used similar to an SWT Image object.
*  
* @author sshaw
* @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
*/
public final class ImageRenderedImage extends AbstractRenderedImage {
	
	private final RGB TRANSPARENT_COLOR = new RGB(0,0,0);
	
	/**
	 * Draws two images, one that represents the background, and another one
	 * centered within the bounds of the first one.
	 * 
	 * @author lgrahek
	 * 
	 */
	private static class TwoImageDescriptor extends CompositeImageDescriptor {

		/** size of the combined image */
		private Point size;		
		/** background image (transparent) */
		private ImageData bgImgData;
		/** image to be drawn on top of it */
		private ImageData actualImgData;
		

		/**
		 * @param bgImgData
		 *            Image to drawn first
		 * @param actualImgData
		 *            Image to be drawn second
		 * @param bufferWidth
		 *            Width of the final image
		 * @param bufferHeight
		 *            Height of the final image
		 */
		public TwoImageDescriptor(ImageData bgImgData, ImageData actualImgData,
				int bufferWidth, int bufferHeight) {
			this.bgImgData = bgImgData;
			this.actualImgData = actualImgData;
			this.size = new Point(bufferWidth, bufferHeight);
		}	
		
		/* (non-Javadoc)
		 * @see org.eclipse.jface.resource.CompositeImageDescriptor#drawCompositeImage(int, int)
		 */
		protected void drawCompositeImage(int width, int height) {
			// draw the base image
			drawImage(bgImgData, 0, 0);
			drawImage(actualImgData, 
					(size.x - actualImgData.width) / 2, (size.y - actualImgData.height) / 2);
		}
		
		/**
		 * @see org.eclipse.jface.resource.CompositeImageDescriptor#getSize()
		 */
		protected Point getSize() {
			return size;
		}
	}			
	
	/**
	 * Constructor for SVGImage
	 * 
	 * @param buffer
	 *            byte[] array containing an cached SVG image file.
	 * @param key
	 *            ImageKey instance which is unique for the byte array.
	 */
	public ImageRenderedImage(byte[] buff, RenderedImageKey key) { 
		super(buff, key); 
	}

	/**
	 * loadImageFromBuffer
	 * Utility to load the image file.  Throws an exception if the image
	 * buffer is not legitimate.
	 * 
	 * @throws Exception indicating that the buffer is not a legitimate or recognizable
	 * to the SWT ImageLoader class.
	 */
	protected Image renderImage() {
		try {
			ImageLoader loader = new ImageLoader();
			ByteArrayInputStream byteIS = new ByteArrayInputStream(getBuffer());
			// otherwise render the image.
			ImageData[] origImgData = loader.load(byteIS);
			if (origImgData == null)
				throw new InvalidParameterException();
				
			int origWidth = origImgData[0].width;
			int origHeight = origImgData[0].height;
			
			int bufferWidth = getKey().getWidth() == 0 ? origWidth : getKey().getWidth();
			int bufferHeight = getKey().getHeight() == 0 ? origHeight : getKey().getHeight();
			
			int newWidth = bufferWidth;
            int newHeight = bufferHeight;
 			
            if (getKey().shouldMaintainAspectRatio()) {
                double origAspectRatio = origHeight / (double)origWidth;
                if (origAspectRatio > newHeight / (double)newWidth) {
                    newWidth = (int)Math.round(newHeight / origAspectRatio);
                } else {
                    newHeight = (int)Math.round(newWidth * origAspectRatio);
                }
				
				double scale = 1.0;
				if (newWidth > bufferWidth)
					scale = bufferWidth / newWidth;
				if (newHeight > bufferHeight)
					scale = Math.min(scale, bufferHeight / (double)newHeight);
				
				newWidth *= scale;
				newHeight *= scale;
				
				// Ensure that pixels outside of the actual image are
				// transparent by creating a background image which is transparent.
				// TransparentBgImageDescriptor does the job.
				PaletteData paletteData = new PaletteData(0xFF, 0xFF00, 0xFF0000);
				ImageData imgData = new ImageData(bufferWidth, bufferHeight, 24, paletteData);		
				imgData.transparentPixel = paletteData.getPixel(TRANSPARENT_COLOR);				
				TwoImageDescriptor desc = new TwoImageDescriptor(
						imgData, origImgData[0].scaledTo(newWidth, newHeight), bufferWidth, bufferHeight);
				return desc.createImage();
			}
			else {
				ImageData scaledImgData = origImgData[0].scaledTo(newWidth, newHeight);
				return new Image(PlatformUI.getWorkbench().getDisplay(), scaledImgData);
			}
		} catch (Exception e) {
			Trace.throwing(Draw2dRenderPlugin.getInstance(), Draw2dRenderDebugOptions.EXCEPTIONS_THROWING, ImageRenderedImage.class, 
				"ImageRenderedImage.renderImage() : couldn't load image from buffer", //$NON-NLS-1$
				e);
			return null;
		}

	}
}
