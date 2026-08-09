/******************************************************************************
 * Copyright (c) 2004, 2026 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.internal;

import java.lang.ref.Cleaner;
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
 */
public abstract class AbstractRenderedImage implements RenderedImage {

	private static final Cleaner CLEANER = Cleaner.create();

	private byte[] buffer = null;
	private RenderedImageKey key = null;
	private Image img = null;
	private final ImageCleanup imageCleanup = new ImageCleanup();

	/**
	 * Constructor for AbstractRenderedImage
	 *
	 * @param buffer byte[] array containing an cached SVG image file.
	 * @param key    ImageKey instance which is unique for the byte array.
	 */
	protected AbstractRenderedImage(byte[] buff, RenderedImageKey key) {
		if (buff == null || key == null) {
			throw new InvalidParameterException();
		}

		this.buffer = buff;
		this.key = key;
		CLEANER.register(this, this.imageCleanup);
	}

	/**
	 * @return Returns the buffer.
	 */
	public byte[] getBuffer() {
		return this.buffer;
	}

	/**
	 * @return Returns the key.
	 */
	public RenderedImageKey getKey() {
		return new RenderedImageKey(this.key, this.key.getChecksum(), this.key.getExtraData(), this.key.getURLString());
	}

	@Override
	public RenderInfo getRenderInfo() {
		return this.getKey();
	}

	/**
	 * Implementation of the ResizableImage interface that will allow the SVG to be
	 * re-rendered into a different ImageSize.
	 *
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage#getNewRenderedImage(org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo)
	 */
	@Override
	public RenderedImage getNewRenderedImage(RenderInfo info) {
		if (!this.getRenderInfo().equals(info)) {
			RenderedImage rndImg = RenderedImageFactory.getRelatedInstance(this, info);
			if (rndImg != null) {
				return rndImg;
			} else {
				return RenderedImageFactory.getInstance(this.getBuffer(), info);
			}
		}

		return this;
	}

	@Override
	public boolean isRendered() {
		return this.img != null;
	}

	@Override
	public final synchronized Image getSWTImage() {
		if (this.img == null) {
			this.img = this.renderImage();
			this.imageCleanup.setImage(this.img);
		}
		return this.img;
	}

	/**
	 * @return the new <code>Image</code> rendered to the specification of the
	 *         <code>RenderInfo</code> structure stored with the this
	 *         <code>RenderedImage</code>
	 */
	protected abstract Image renderImage();

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.equals(Image.class)) {
			return this.getSWTImage();
		}
		return null;
	}

	private static final class ImageCleanup implements Runnable {
		private Image image;

		synchronized void setImage(Image image) {
			this.image = image;
		}

		@Override
		public synchronized void run() {
			if (this.image != null) {
				this.image.dispose();
				this.image = null;
			}
		}
	}
}
