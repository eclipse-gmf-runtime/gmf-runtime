/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
	 */
	public abstract void drawRenderedImage(
		RenderedImage srcImage,
		int x,
		int y,
		int width,
		int height);

}
