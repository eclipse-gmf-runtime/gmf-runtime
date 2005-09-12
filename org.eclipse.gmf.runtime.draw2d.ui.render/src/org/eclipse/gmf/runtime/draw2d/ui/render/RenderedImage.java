/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.draw2d.ui.render;

import java.awt.image.BufferedImage;

import org.eclipse.swt.graphics.Image;

/**
 * Interface to allow dynamic resizing of an Image.
 * 
 * @author sshaw
 */
public interface RenderedImage {

	/**
	 * Accessor method to return information about the rendered image.
	 * 
	 * @return RenderInfo object that contains information about the rendered image.
	 */
	public RenderInfo getRenderInfo();
	
	/**
	 * Retrieves an equivalent image of the specified size
	 * as specified through the parameters.
	 * 
	 * @param info <code>RenderInfo</code> object containing information about the size and 
	 * general data regarding how the image will be rendered.
	 * @return <code>RenderedImage</code> which is the equivalent image of the source based on the
	 * requested <code>RenderInfo</code> information source.
	 * 
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo
	 */
	public RenderedImage getNewRenderedImage(RenderInfo info);
	
	/**
	 * Accessor for retrieving the AWT Buffered image for this ResizableImage
	 * 
	 * @return AWT Image that this resizable image represents.
	 */
	public BufferedImage getBufferedImage();
	
	/**
	 * Accessor for retrieving the SWT image for this ResizableImage. Typically, this accessor
	 * should implemented for dynamic rendering of the image to an SWT image.  This avoid 
	 * storing the heavy weight image buffer in memory until it is needed.
	 * 
	 * @return SWT Image that this <code>RenderedImage</code> represents.
	 */
	public Image getSWTImage();
}
