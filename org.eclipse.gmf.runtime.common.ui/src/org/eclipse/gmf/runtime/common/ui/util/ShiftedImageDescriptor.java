/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.util;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

/**
 * An image descriptor that is cached and shifted from the left or from the
 * top or both.  The shifted area contains the original image and the area
 * on the left or top contains a background color.  Both the original image
 * and background color are specified in the constructor.
 * 
 * @author wdiu, Wayne Diu
 */
public class ShiftedImageDescriptor
	extends CompositeImageDescriptor {

	/**
	 * The cached size of the shifted image
	 */
	private Point size = null;

	/**
	 * How much to shift from the left
	 */
	private short shiftX = 0;

	/**
	 * How much to shift from the top
	 */
	private short shiftY = 0;

	/**
	 * Original image data for the image that will be shifted
	 */
	private ImageData imageData = null;

	/**
	 * Make a new image descriptor of an image that is shifted.
	 * 
	 * @param shiftX
	 *            how much to shift from the left
	 * @param shiftY
	 *            how much to shift from the top
	 * @param imageDescriptor
	 *            original image to shift by shiftX and shiftY
	 */
	public ShiftedImageDescriptor(short shiftX, short shiftY,
		ImageDescriptor imageDescriptor) {

		assert (shiftX >= 0);
		assert (shiftY >= 0);
		assert (imageDescriptor.getImageData().width >= 0);
		assert (imageDescriptor.getImageData().height >= 0);
		assert null != imageDescriptor;

		this.shiftX = shiftX;
		this.shiftY = shiftY;
		this.imageData = imageDescriptor.getImageData();
	}

	/**
	 * Draw the image in the shifted position
	 * 
	 * @param width
	 *            int ignored
	 * @param height
	 *            int ignored
	 * 
	 * @see org.eclipse.jface.resource.CompositeImageDescriptor#drawCompositeImage(int,
	 *      int)
	 */
	protected void drawCompositeImage(int width, int height) {
		drawImage(imageData, shiftX, shiftY);

	}

	/**
	 * Return the size of the shifted image and cache it
	 * 
	 * @return Point with the size of the shifted image 
	 * 
	 * @see org.eclipse.jface.resource.CompositeImageDescriptor#getSize()
	 */
	protected Point getSize() {
		if (size == null) {
			size = new Point(imageData.width + shiftX, imageData.height
				+ shiftY);
		}
		return size;
	}
}