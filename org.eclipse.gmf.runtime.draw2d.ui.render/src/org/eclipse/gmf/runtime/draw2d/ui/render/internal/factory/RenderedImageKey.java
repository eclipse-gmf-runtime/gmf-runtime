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
 * This class defines the unique key used to get the appropriate RenderedImage from the map.
 */
public final class RenderedImageKey implements RenderInfo {

	public RenderedImageKey(long checksum, RenderInfo info) {
		this.checksum = checksum;
		this.info = info;
	}

	private long checksum;
	private RenderInfo info;

	/**
	 * @return Long value that is the checksum
	 */
	public long getChecksum() {
		return checksum;
	}

	/**
	 * getRenderInfo
	 * Accessor to return RenderInfo object.
	 * 
	 * @return RenderInfo object.
	 */
	public RenderInfo getRenderInfo() {
		return info;
	}

	/* (non-Javadoc)
	 * @see com.ibm.xtools.gef.figure.svg.RenderInfo#getWidth()
	 */
	public int getWidth() {
		return info.getWidth();
	}

	/* (non-Javadoc)
	 * @see com.ibm.xtools.gef.figure.svg.RenderInfo#getHeight()
	 */
	public int getHeight() {
		return info.getHeight();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.render.RenderInfo#getFillColor()
	 */
	public Color getFillColor() {
		return info.getFillColor();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.render.RenderInfo#getOutlineColor()
	 */
	public Color getOutlineColor() {
		return info.getOutlineColor();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.render.RenderInfo#setValues(int, int, java.awt.Color, java.awt.Color, boolean, boolean)
	 */
	public void setValues(int width, int height, Color fill, Color outline,
							boolean maintainAspectRatio, 
							boolean antialias) {
		// Don't do anything - this class implementation is used as a key and as
		// such should be immutable.  A copy should be made if they wish to 
		// modify the values.
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.ibm.xtools.gef.figure.svg.RenderInfo#shouldMaintainAspectRatio()
	 */
	public boolean shouldMaintainAspectRatio() {
		return info.shouldMaintainAspectRatio();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.render.RenderInfo#shouldAntiAlias()
	 */
	public boolean shouldAntiAlias() {
		return info.shouldAntiAlias();
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
		int hashCode = new Long(checksum).hashCode();
		hashCode = hashCode + getRenderInfo().hashCode();

		return hashCode;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {

		RenderedImageKey imagekey = null;
		if (object instanceof RenderedImageKey) {
			imagekey = (RenderedImageKey) object;
		}

		if (imagekey != null
			&& getChecksum() == imagekey.getChecksum()
			&& getRenderInfo().equals(imagekey.getRenderInfo())) {
			return true;
		}

		return false;
	}
}
