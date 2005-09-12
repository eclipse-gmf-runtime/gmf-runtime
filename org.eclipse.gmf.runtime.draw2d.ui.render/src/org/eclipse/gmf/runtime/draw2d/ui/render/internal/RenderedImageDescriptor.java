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
