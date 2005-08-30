/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.util;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

/**
 * Allows one image descriptor to be overlayed on another image descriptor to
 * generate a new image. Commonly used to decorate an image with a second image
 * decoration.
 * 
 * @author Michael Yee
 */
public class OverlayImageDescriptor
	extends CompositeImageDescriptor {

	/** default image width */
	private final int DEFAULT_IMAGE_WIDTH = 16;

	/** default image height */
	private final int DEFAULT_IMAGE_HEIGHT = 16;

	/** image width */
	private int imageWidth = DEFAULT_IMAGE_WIDTH;

	/** image height */
	private int imageHeight = DEFAULT_IMAGE_HEIGHT;

	/** base image */
	private Image srcImage = null;

	/** overlay image */
	private ImageDescriptor overlayDesc = null;

	/**
	 * OverlayImageDescriptor constructor
	 * 
	 * @param srcImage
	 *            the base image
	 * @param overlayDesc
	 *            the overlay image
	 */
	public OverlayImageDescriptor(Image srcImage, ImageDescriptor overlayDesc) {
		assert null != srcImage;
		assert null != overlayDesc;
		this.srcImage = srcImage;
		this.overlayDesc = overlayDesc;
	}

	/**
	 * OverlayImageDescriptor constructor where you can set the width and height
	 * 
	 * @param srcImage
	 *            the base image
	 * @param overlayDesc
	 *            the overlay image
	 * @param width
	 *            an int with the width of the image in pixels
	 * @param height
	 *            an int with the height of the image in pixels
	 */
	public OverlayImageDescriptor(Image srcImage, ImageDescriptor overlayDesc,
			int width, int height) {
		this(srcImage, overlayDesc);
		imageWidth = width;
		imageHeight = height;
	}

	/**
	 * Draws the given source image data into this composite image at the given
	 * position.
	 * 
	 * @param width
	 *            the width of the image.
	 * @param height
	 *            the height of the image.
	 * 
	 * @see org.eclipse.jface.resource.CompositeImageDescriptor#drawCompositeImage(int,
	 *      int)
	 */
	protected void drawCompositeImage(int width, int height) {
		// draw the base image
		ImageData backgroundData = srcImage.getImageData();
		if (backgroundData != null) {
			drawImage(backgroundData, 0, 0);
		}

		// draw the overlay image
		ImageData overlayData = overlayDesc.getImageData();
		if (overlayData != null) {
			drawImage(overlayData, 0, 0);
		}
	}

	/**
	 * Retrieve the size of this composite image.
	 * 
	 * @return the x and y size of the image expressed as a point object
	 * @see org.eclipse.jface.resource.CompositeImageDescriptor#getSize()
	 */
	protected Point getSize() {
		return new Point(imageWidth, imageHeight);
	}

}