/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.AbstractRenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.Draw2dRenderPlugin;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.factory.RenderedImageKey;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.PlatformUI;

/**
* Class that represents a Image image. This is a useful abstraction so that it
* can be used similar to an SWT Image object.
*  
* @author sshaw
* @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
*/
public final class ImageRenderedImage extends AbstractRenderedImage {

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
			ImageData[] imgData = loader.load(byteIS);
			if (imgData == null)
				throw new InvalidParameterException();
				
			int origWidth = imgData[0].width;
			int origHeight = imgData[0].height;
			
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
				
				Image origImage = new Image(PlatformUI.getWorkbench().getDisplay(), imgData[0]);
				Image image = new Image(PlatformUI.getWorkbench().getDisplay(), new Rectangle(0, 0, bufferWidth, bufferHeight));
				GC gc = new GC(image);
				SWTGraphics swtG = new SWTGraphics(gc);
				swtG.drawImage(origImage, 0, 0, origWidth, origHeight, (bufferWidth - newWidth) / 2, (bufferHeight - newHeight) / 2, newWidth, newHeight);
				swtG.dispose();
				gc.dispose();
				origImage.dispose();
				
				return image;
			}
			else {
				ImageData scaledImgData = imgData[0].scaledTo(newWidth, newHeight);
				return new Image(PlatformUI.getWorkbench().getDisplay(), scaledImgData);
			}
		} catch (Exception e) {
            Log.error(Draw2dRenderPlugin.getInstance(),
                IStatus.ERROR, e.toString(), e);
			return null;
		}

	}
}
