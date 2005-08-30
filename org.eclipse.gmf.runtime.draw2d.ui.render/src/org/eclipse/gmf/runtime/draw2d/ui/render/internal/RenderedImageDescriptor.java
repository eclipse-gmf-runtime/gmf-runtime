/****************************************************************************
  Licensed Materials - Property of IBM
  (C) Copyright IBM Corp. 2004. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
*****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.internal;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;


/**
 * Image Descriptor class that encapsulates an RenderedImage object.
 *
 * @author sshaw
 */
public class RenderedImageDescriptor extends ImageDescriptor {
	
	/**
	 * Creates and returns a new image descriptor from a RenderedImage.
	 *
	 * @param renderedImage RenderedImage to be encapsulated with-in the image descriptor.
	 * @return a new image descriptor
	 */
	public static ImageDescriptor createFromRenderedImage(RenderedImage renderedImage) {
		return new RenderedImageDescriptor(renderedImage);
	}
	
	private RenderedImage renderedImage;
	
	private RenderedImageDescriptor(RenderedImage renderedImage) {
		this.renderedImage = renderedImage;
	}
	
	/* (
	 * non-Javadoc)
	 * @see org.eclipse.jface.resource.ImageDescriptor#getImageData()
	 */
	public ImageData getImageData() {
		Image image = renderedImage.getSWTImage();
		return image.getImageData();
	}
}
