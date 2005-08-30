/****************************************************************************
Licensed Materials - Property of IBM
(C) Copyright IBM Corp. 2002, 2004. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
*****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.internal.image;

import java.io.ByteArrayInputStream;
import java.security.InvalidParameterException;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.AbstractRenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.Draw2dRenderDebugOptions;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.Draw2dRenderPlugin;

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
	public ImageRenderedImage(byte[] buff, RenderInfo key) { 
		super(buff, key); 
	}

	/**
	 * Accessor for retrieving the default image for the rendered SVG data.
	 * This method will render the image if it doesn't exist yet. This allows
	 * for "on-demand" loading. If no-one accesses the image, then it will not
	 * be rendered.
	 * 
	 * @see com.ibm.xtools.gef.figure.svg.ResizableImage#getDefaultImage()
	 */
	public Image getSWTImage() {
		if (img != null)
			return img;

		try {
			img = loadImageFromBuffer();
		}
		catch (Exception e) {
			Trace.throwing(Draw2dRenderPlugin.getInstance(), Draw2dRenderDebugOptions.EXCEPTIONS_THROWING, ImageRenderedImage.class, 
				"ImageRenderedImage.getSWTImage() : couldn't load image from buffer", //$NON-NLS-1$
				e);
			
			// handle failure gracefully - we can't predict all the failures
			// that may occur in the 3rd party library.
			//img = new Image(Display.getDefault(), 8, 8);
		}
		return img;
	}

	/**
	 * loadImageFromBuffer
	 * Utility to load the image file.  Throws an exception if the image
	 * buffer is not legitimate.
	 * 
	 * @throws Exception indicating that the buffer is not a legitimate or recognizable
	 * to the SWT ImageLoader class.
	 */
	private Image loadImageFromBuffer() throws Exception {
		ImageLoader loader = new ImageLoader();
		ByteArrayInputStream byteIS = new ByteArrayInputStream(getBuffer());
		// otherwise render the image.
		ImageData[] imgData = loader.load(byteIS);
		if (imgData == null)
			throw new InvalidParameterException();
			
		int origWidth = imgData[0].width;
		int origHeight = imgData[0].height;
		
		int newWidth = getKey().getWidth() == 0 ? origWidth : getKey().getWidth();
		int newHeight = getKey().getHeight() == 0 ? origHeight : getKey().getHeight();
		
		if (getKey().shouldMaintainAspectRatio()) {
			if (origWidth < origHeight)
				newWidth = (int)Math.round(newHeight * origWidth / (double)origHeight);
			else
				newHeight = (int)Math.round(newWidth * origHeight / (double)origWidth);
		}
			
		ImageData scaledImgData = imgData[0].scaledTo(newWidth, newHeight);
		return new Image(Display.getDefault(), scaledImgData);

	}
}
