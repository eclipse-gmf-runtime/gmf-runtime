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

import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;
import org.eclipse.swt.graphics.RGB;

/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 * 
 * Contains information needed by RenderedImage to perform the rendering of the
 * graphics data.
 */
class RenderInfoImpl
	implements RenderInfo {

	private boolean maintainAspectRatio;

	private boolean antialias;

	private int width;

	private int height;

	private RGB fill = null;

	private RGB outline = null;

	/**
	 * getWidth Accessor method to return the width of the rendered image.
	 * 
	 * @return int width of the rendered image.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * getHeight Accessor method to return the height of the rendered image.
	 * 
	 * @return int height of the rendered image.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * shouldMaintainAspectRatio Accessor method to return whether or not the
	 * aspect ratio is maintained.
	 * 
	 * @return boolean true if aspect ratio of original vector file is
	 *         maintained, false otherwise.
	 */
	public boolean shouldMaintainAspectRatio() {
		return maintainAspectRatio;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.render.RenderInfo#shouldAntiAlias()
	 */
	public boolean shouldAntiAlias() {
		return antialias;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo#getBackgroundColor()
	 */
	public RGB getBackgroundColor() {
		if (fill == null)
			return null;
		return new RGB(fill.red, fill.green, fill.blue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo#getForegroundColor()
	 */
	public RGB getForegroundColor() {
		if (outline == null)
			return null;
		return new RGB(outline.red, outline.green, outline.blue);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo#setValues(int, int, boolean, boolean, org.eclipse.swt.graphics.RGB, org.eclipse.swt.graphics.RGB)
	 */
	public void setValues(int width, int height,
			boolean maintainAspectRatio, boolean antialias, RGB fill, RGB outline) {
		this.width = width;
		this.height = height;
		this.fill = fill == null ? null : new RGB(fill.red, fill.green, fill.blue);
		this.outline = outline == null ? null : new RGB(outline.red, outline.green, outline.blue);
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
		this.fill = info.getBackgroundColor();
		this.outline = info.getForegroundColor();
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
				&& shouldMaintainAspectRatio() == info
					.shouldMaintainAspectRatio() && shouldAntiAlias() == info
				.shouldAntiAlias())) {

			if (getBackgroundColor() == null) {
				if (info.getBackgroundColor() != null)
					return false;
			} else if (!getBackgroundColor().equals(info.getBackgroundColor()))
				return false;

			if (getForegroundColor() == null) {
				if (info.getForegroundColor() != null)
					return false;
			} else if (!getForegroundColor().equals(info.getForegroundColor()))
				return false;

			return true;
		}

		return false;
	}

}
