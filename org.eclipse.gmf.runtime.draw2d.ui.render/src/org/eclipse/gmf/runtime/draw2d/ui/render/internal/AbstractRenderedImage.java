/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.draw2d.ui.render.internal;

import java.security.InvalidParameterException;

import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.factory.RenderedImageFactory;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.factory.RenderedImageKey;
import org.eclipse.swt.graphics.Image;

/**
* Abstract class for RenderedImage interface.
*  
* @author sshaw
* @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
*/
abstract public class AbstractRenderedImage implements RenderedImage {
	
	/**
	 * Constructor for AbstractRenderedImage
	 * 
	 * @param buffer
	 *            byte[] array containing an cached SVG image file.
	 * @param key
	 *            ImageKey instance which is unique for the byte array.
	 */
	public AbstractRenderedImage(final byte[] buff, RenderedImageKey key) { 
		if (buff == null || key == null)
			throw new InvalidParameterException();

		this.buffer = buff;
		this.key = key;
	}

	private byte[] buffer = null;
	private RenderedImageKey key = null;
	private Image img = null;
	
	/**
	 * @return Returns the buffer.
	 */
	public byte[] getBuffer() {
		return buffer;
	}
	
	/**
	 * @return Returns the key.
	 */
	public RenderedImageKey getKey() {
		return new RenderedImageKey(key, key.getChecksum(), key.getExtraData());
	}
	
	/**
	 * Overridden so that image can be disposed.
	 * 
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		if (img != null) {
			img.dispose();
			img = null;
		}
		key = null;

		super.finalize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.render.RenderedImage#getRenderInfo()
	 */
	public RenderInfo getRenderInfo() {
		return getKey();
	} 

	/**
     *Implementation of the ResizableImage interface that will allow the SVG
     * to be re-rendered into a different ImageSize.
     * @see org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage#getNewRenderedImage(org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo)
	 */
	public RenderedImage getNewRenderedImage(RenderInfo info) {
		if (!getRenderInfo().equals(info)) { 
			RenderedImage rndImg = RenderedImageFactory.getRelatedInstance(this, info);
			if (rndImg != null) {
				return rndImg;
			} else {
				return RenderedImageFactory.getInstance(getBuffer(), info);
			}
		}

		return this;
	}

	/**
	 * @return <code>true</code> if image has been fully rendered, <code>false</code> if
	 * it needs to be rendered.
	 */
	public boolean isRendered() {
		if (img != null)
			return true;
		
		return false;
	}

	/**
     * Accessor for retrieving the default image for the rendered SVG data.
     * This method will render the image if it doesn't exist yet. This allows
     * for "on-demand" loading. If no-one accesses the image, then it will not
     * be rendered.
     * 
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage#getSWTImage()
	 */
	synchronized final public Image getSWTImage() {
		if (img != null)
			return img;

		img = renderImage();

		return img;
	}
	
	/**
	 * @return the new <code>Image</code> rendered to the specification of the
	 * <code>RenderInfo</code> structure stored with the this <code>RenderedImage</code>
	 */
	abstract protected Image renderImage();

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter.equals(Image.class)) {
			return getSWTImage();
		}
		return null;
	}
}
