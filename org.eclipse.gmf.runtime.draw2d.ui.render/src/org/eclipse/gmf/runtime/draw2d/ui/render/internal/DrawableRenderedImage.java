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

import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;

/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 *
 * Interface for drawing a rendered image.
 */
public interface DrawableRenderedImage {
	
	/**
	 * Draws the given RenderedImage at the location (x,y) with a
	 * width and height.
	 * @param srcImage the Image
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param width the width of the RenderedImage
	 * @param height the height of the RenderedImage
	 * @return the <code>RenderedImage</code> that was finally rendered to the device
	 */
	public abstract RenderedImage drawRenderedImage(
		RenderedImage srcImage,
		int x,
		int y,
		int width,
		int height);

}
