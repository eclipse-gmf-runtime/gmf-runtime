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

package org.eclipse.gmf.runtime.draw2d.ui.render.internal;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;

/**
 * @author sshaw
 *
 * Interface for drawing a rendered image.
 */
public interface DrawableRenderedImage {
	
	/**
	 * @return <code>true</code> if the implementor can support a delayed rendering
	 * of the <code>RenderedImage</code>.  Implementors should return <code>false</code>
	 * if they need the rendering to occur immediately.
	 */
	public boolean shouldAllowDelayRender();
	
	/**
	 * @return <code>Dimension<code> that is the maximum size in pixels
	 * that a rendered image will be rendered at.  This is useful to 
	 * ensure adequate performance for display.  If <code>null</code>
	 * is returned, then this means no maximum render size is imposed.
	 */
	public Dimension getMaximumRenderSize();
	
	/**
	 * Draws the given RenderedImage at the location (x,y) with a
	 * width and height.
	 * @param srcImage the Image
	 * @param area the <code>Rectangle</code> in logical units to draw the image in
	 * @return the <code>RenderedImage</code> that was finally rendered to the device
	 */
	public abstract RenderedImage drawRenderedImage(
		RenderedImage srcImage, Rectangle area, RenderingListener listener);

}
