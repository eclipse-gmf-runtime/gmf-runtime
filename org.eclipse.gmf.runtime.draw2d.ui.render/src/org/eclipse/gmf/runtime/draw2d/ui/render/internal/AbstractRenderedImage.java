/****************************************************************************
Licensed Materials - Property of IBM
(C) Copyright IBM Corp. 2004. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
*****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.internal;

import java.awt.image.BufferedImage;
import java.security.InvalidParameterException;

import org.eclipse.swt.graphics.Image;

import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.factory.RenderedImageFactory;
import org.eclipse.gmf.runtime.draw2d.ui.render.image.ImageConverter;

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
	public AbstractRenderedImage(byte[] buff, RenderInfo key) { 
		if (buff == null || key == null)
			throw new InvalidParameterException();

		this.buffer = new byte[buff.length];
		this.key = key;

		System.arraycopy(buff, 0, this.buffer, 0, buff.length);
	}

	private byte[] buffer = null;
	private RenderInfo key = null;

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
	public RenderInfo getKey() {
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
			return RenderedImageFactory.getInstance(this.buffer, info);
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
