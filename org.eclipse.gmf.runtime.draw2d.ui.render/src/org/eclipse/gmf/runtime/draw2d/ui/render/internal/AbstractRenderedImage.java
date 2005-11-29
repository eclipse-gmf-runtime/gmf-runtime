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


package org.eclipse.gmf.runtime.draw2d.ui.render.internal;

import java.awt.image.BufferedImage;
import java.security.InvalidParameterException;

import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.factory.RenderedImageFactory;
import org.eclipse.gmf.runtime.draw2d.ui.render.image.ImageConverter;
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

	protected Image img = null;
	
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
		return key;
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
		return RenderedImageFactory.createInfo(key.getWidth(), key.getHeight(), key.getFillColor(), key.getOutlineColor(),
								key.shouldMaintainAspectRatio(), key.shouldAntiAlias());
	} 

	/**
	 * Implementation of the ResizableImage interface that will allow the SVG
	 * to be re-rendered into a different ImageSize.
	 * 
	 * @see com.ibm.xtools.gef.figure.svg.ResizableImage#getResizedImage(int,
	 *      int)
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.render.RenderedImage#getBufferedImage()
	 */
	public BufferedImage getBufferedImage() {
		return ImageConverter.convert(getSWTImage());
	}
}
