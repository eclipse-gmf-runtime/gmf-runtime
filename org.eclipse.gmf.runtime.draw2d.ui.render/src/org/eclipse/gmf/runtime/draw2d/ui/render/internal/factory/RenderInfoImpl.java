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

package org.eclipse.gmf.runtime.draw2d.ui.render.internal.factory;

import java.awt.Color;

import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;


/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 *
 * Contains information needed by RenderedImage to perform the rendering of the 
 * graphics data.
 */
public final class RenderInfoImpl implements RenderInfo {

	private boolean maintainAspectRatio;
	private boolean antialias;
	private int width;
	private int height;
	private Color fill;
	private Color outline;

	/**
	* getWidth
	* Accessor method to return the width of the rendered image.
	* 
	* @return int width of the rendered image.
	*/
	public int getWidth() {
		return width;
	}

	/**
	 * getHeight
	 * Accessor method to return the height of the rendered image.
	 * 
	 * @return int height of the rendered image.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * shouldMaintainAspectRatio
	 * Accessor method to return whether or not the aspect ratio is maintained.
	 * 
	 * @return boolean true if aspect ratio of original vector file is maintained, false otherwise.
	 */
	public boolean shouldMaintainAspectRatio() {
		return maintainAspectRatio;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.render.RenderInfo#shouldAntiAlias()
	 */
	public boolean shouldAntiAlias() {
		return antialias;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.render.RenderInfo#getFillColor()
	 */
	public Color getFillColor() {
		return fill;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.render.RenderInfo#getOutlineColor()
	 */
	public Color getOutlineColor() {
		return outline;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.render.RenderInfo#setValues(int, int, boolean)
	 */
	public void setValues(
		int width,
		int height,
		Color fill, Color outline,
		boolean maintainAspectRatio,
		boolean antialias) {
		this.width = width;
		this.height = height;
		this.fill = fill;
		this.outline = outline;
		this.maintainAspectRatio = maintainAspectRatio;
		this.antialias = antialias;
	}

	/**
	 * Default Constructor for the SVGInfo class
	 */
	public RenderInfoImpl() {
		super();

		this.width = 0;
		this.height = 0;
		this.fill = null;
		this.outline = null;
		this.maintainAspectRatio = true;
		this.antialias = true;
	}

	/**
	 * Copy Constructor for the SVGInfo class
	 */
	public RenderInfoImpl(RenderInfo info) {
		super();

		this.width = info.getWidth();
		this.height = info.getHeight();
		this.fill = info.getFillColor();
		this.outline = info.getOutlineColor();
		this.maintainAspectRatio = info.shouldMaintainAspectRatio();
		this.antialias = info.shouldAntiAlias();
	}

	/**
	 * Retrieves a hash code value for this output operation. This method is 
	 * supported for the benefit of hashtables such as those provided by 
	 * <code>java.util.Hashtable</code>.
	 * 
	 * @return A hash code value for this output operation.
	 * 
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int hashCode = (new Integer(width)).hashCode();
		hashCode = hashCode + (new Integer(height)).hashCode();
		hashCode = hashCode + (Boolean.valueOf(maintainAspectRatio)).hashCode();
		hashCode = hashCode + (Boolean.valueOf(antialias)).hashCode();
		if (fill != null)
			hashCode = hashCode + fill.hashCode();
		if (outline != null)
			hashCode = hashCode + outline.hashCode();
		return hashCode;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {

		RenderInfoImpl info = null;
		if (object instanceof RenderInfoImpl) {
			info = (RenderInfoImpl) object;
		}

		if (info != null
			&& (getWidth() == info.getWidth()
				&& getHeight() == info.getHeight()
				&& shouldMaintainAspectRatio()
					== info.shouldMaintainAspectRatio()
				&& shouldAntiAlias() == info.shouldAntiAlias())) {
			
			if (getFillColor() == null)	{
				if (info.getFillColor() != null)
					return false;	
			}
			else if (!getFillColor().equals(info.getFillColor()))
				return false;
				
			if (getOutlineColor() == null) {
				if (info.getOutlineColor() != null)
					return false;
			}
			else if (!getOutlineColor().equals(info.getOutlineColor()))
				return false;
				
			return true;
		}

		return false;
	}

}
