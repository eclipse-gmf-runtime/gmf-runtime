/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.render;

import java.awt.Color;

/**
 * @author sshaw
 *
 * Contains information needed by RenderedImage to perform the rendering of the 
 * Vector graphics data.
 */
public interface RenderInfo {

	/**
	* Accessor method to return the width of the rendered image.
	* 
	* @return the width of the rendered image.
	*/
	public abstract int getWidth();

	/**
	 * Accessor method to return the height of the rendered image.
	 * 
	 * @return the height of the rendered image.
	 */
	public abstract int getHeight();

	/**
	 * Accessor method to return the fill color of the rendered image.
	 * 
	 * @return <code>Color</code> value that is the fill color of the rendered image.
	 * Note: a <code>null</code> return value is valid and means that the fill color defaults 
	 * to the values stored in the vector graphics file.
	 */
	public abstract Color getFillColor();
	
	/**
	 * Accessor method to return the outline color of the rendered image.
	 * 
	 * @return <code>Color</code> value that is the outline color of the rendered image.
	 * Note: a <code>null</code> return value is valid and means that the outline color defaults 
	 * to the values stored in the vector graphics file.
	 */
	public abstract Color getOutlineColor();
	
	/**
	 * Accessor method to return whether or not the aspect ratio is maintained.
	 * 
	 * @return <code>boolean</code> <code>true</code> if aspect ratio of original vector file is maintained, 
	 * <code>false</code> otherwise.
	 */
	public abstract boolean shouldMaintainAspectRatio();

	/**
	 * Accessor method to return whether or not the vector data is anti-aliased.
	 * 
	 * @return <code>boolean</code> <code>true</code> if vector graphic is to be rendered wih anti-aliasing, 
	 * <code>false</code> otherwise.
	 */
	public abstract boolean shouldAntiAlias();

	/**
	 * Generic setter to set values in the RenderInfo data structure.  This is useful
	 * when retrieving a RenderInfo structure from a RenderedImage object and then 
	 * wishing to re-render the image with new values.
	 * 
	 * @param width the width of the rendered image to set
	 * @param height the height of the rendered image to set
	 * @param fill <code>Color</code> fill color for the whole image: null value defaults to 
	 * stored values for each element in the vector file.
	 * @param outline <code>Color</code> outline color for the whole image: null value defaults to 
	 * stored values for each element in the vector file.
	 * @param maintainAspectRatio <code>boolean</code> <code>true</code> if aspect ratio of original vector file 
	 * is maintained, <code>false</code> otherwise
	 * @param antialias <code>boolean</code> <code>true</code> if vector graphic is to be rendered wih anti-aliasing, 
	 * <code>false</code> otherwise.
	 */
	public abstract void setValues(
		int width,
		int height,
		Color fill, Color outline,
		boolean maintainAspectRatio,
		boolean antialias);
}
