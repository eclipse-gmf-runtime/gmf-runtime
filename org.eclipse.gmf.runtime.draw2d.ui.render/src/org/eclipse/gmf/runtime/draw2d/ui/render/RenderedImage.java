/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Image;

/**
 * Interface to allow dynamic resizing of an Image.
 * 
 * IMPORTANT: This interface is <EM>not</EM> intended to be implemented by
 * clients. 
 * New methods may be added in the future.
 * 
 * @author sshaw
 */
public interface RenderedImage extends IAdaptable {

	/**
	 * Accessor method to return information about the rendered image.
	 * 
	 * @return RenderInfo object that contains information about the rendered
	 *         image.
	 */
	public RenderInfo getRenderInfo();

	/**
	 * Retrieves an equivalent image of the specified size as specified through
	 * the parameters.
	 * 
	 * @param info
	 *            <code>RenderInfo</code> object containing information about
	 *            the size and general data regarding how the image will be
	 *            rendered.
	 * @return <code>RenderedImage</code> which is the equivalent image of the
	 *         source based on the requested <code>RenderInfo</code>
	 *         information source.
	 * 
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo
	 */
	public RenderedImage getNewRenderedImage(RenderInfo info);

	/**
	 * Accessor for retrieving the SWT image for this ResizableImage. Typically,
	 * this accessor should implemented for dynamic rendering of the image to an
	 * SWT image. This avoid storing the heavy weight image buffer in memory
	 * until it is needed.
	 * 
	 * @return SWT Image that this <code>RenderedImage</code> represents.
	 */
	public Image getSWTImage();
	
	/**
	 * @return <code>true</code> if image has been fully rendered, <code>false</code> if
	 * it needs to be rendered.
	 */
	public boolean isRendered();
}
