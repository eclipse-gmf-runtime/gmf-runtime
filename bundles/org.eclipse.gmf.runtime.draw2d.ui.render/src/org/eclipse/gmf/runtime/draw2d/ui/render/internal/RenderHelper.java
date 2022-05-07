/******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScaledGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.swt.graphics.Image;

/**
 * @author sshaw
 */
public class RenderHelper {

	double scale = 1.0;

	boolean scaleTargetCoordinates = true;

	boolean allowDelayRender = false;
	
	private Dimension maximumRenderSize = null;

	private RenderHelper(double scale, boolean scaleTargetCoordinates,
			boolean allowDelayRender, Dimension maximumRenderSize) {
		super();
		this.scale = scale;
		this.allowDelayRender = allowDelayRender;
		this.scaleTargetCoordinates = scaleTargetCoordinates;
		this.maximumRenderSize = maximumRenderSize;
	}

	static public RenderHelper getInstance(double scale,
			boolean scaleDrawable, boolean allowDelayRender, Dimension maximumRenderSize) {
		return new RenderHelper(scale, scaleDrawable,
			allowDelayRender, maximumRenderSize);
	}

	private boolean shouldScaleTargetCoordinates() {
		return scaleTargetCoordinates;
	}

	private boolean shouldAllowDelayRender() {
		return allowDelayRender;
	}

	private double getScale() {
		return scale;
	}
	
	/**
	 * @return Returns the maximumRenderSize.
	 */
	private Dimension getMaximumRenderSize() {
		return maximumRenderSize;
	}

	private static Map threadMap = new HashMap();
	
	/**
	 * Allows for asynchronous execution of the rendering. The
	 * <code>RenderingListener</code
	 * will be notified when the rendering operation has been completed.
	 * 
	 * @param srcImage the <code>RenderedImage that is to be rendered into an <code>Image</code>
	 * @param listener the <code>RenderingListener</code
	 * that be notified when the rendering operation has been completed.
	 * @return <code>true</code> if rendering is already completed and clients can simply call
	 * <code>getSWTImage</code> to retrieve image immediately, <code>false</code>
	 * if it has to be calculated and has spawned a thread to do the rendering.  After the rendering
	 * is completed the <code>RenderingListener</code> listener will be notified
	 */
	private boolean renderSWTImage(RenderedImage srcImage,
			final RenderingListener listener) {
		if (srcImage.isRendered())
			return true;

		Thread existingThread = (Thread)threadMap.get(listener);
		if (existingThread != null) {
			existingThread.interrupt();
			threadMap.remove(listener);
		}
		
		final RenderedImage rndImg = srcImage;
		final Thread renderThread = new Thread(new Runnable() {
			public void run() {
				
				Image img = null;
				try {
					img = rndImg.getSWTImage();
				} catch (RuntimeException e) {
					threadMap.remove(listener);
					throw e;
				}
				
				if (img != null)
					listener.imageRendered(rndImg);
					threadMap.remove(listener);
			}
		});
		
		threadMap.put(listener, renderThread);
		renderThread.start();

		return false;
	}

	private static final int RENDER_TOLERANCE = 5;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.internal.DrawableRenderedImage#drawRenderedImage(org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage,
	 *      int, int, int, int)
	 */
	public RenderedImage drawRenderedImage(Graphics g, RenderedImage srcImage,
			Rectangle rect, RenderingListener listener) {
		Rectangle targetRect = rect.getCopy();
		if (shouldScaleTargetCoordinates())
			targetRect.performScale(getScale());
		
		// delegate directly in case of an GMF graphics
		if (g instanceof DrawableRenderedImage && g instanceof ScaledGraphics) {
			return ((DrawableRenderedImage) g).drawRenderedImage(srcImage, targetRect,
				listener);
		} else {
			Rectangle sourceRect = rect.getCopy();
			sourceRect.performScale(getScale());
			
			RenderedImage trgImage = calculateTargetImage(srcImage, sourceRect);
			
			if (!shouldAllowDelayRender() || renderSWTImage(trgImage, listener)) {
				Image swtImg = trgImage.getSWTImage();
				if (swtImg != null) {
					org.eclipse.swt.graphics.Rectangle imgBox = swtImg.getBounds();
					g.drawImage(swtImg, 0, 0, imgBox.width, imgBox.height, 
						targetRect.x, targetRect.y, targetRect.width, targetRect.height);
				}
			} else {
				// check if the srcImage is rendered and if so scale it's image for
				// display
				if (srcImage.isRendered()) {
					Image swtImg = srcImage.getSWTImage();
					
					org.eclipse.swt.graphics.Rectangle imgBox = swtImg.getBounds();
					g.drawImage(swtImg, 0, 0, imgBox.width, imgBox.height, 
						targetRect.x, targetRect.y, targetRect.width, targetRect.height);
					return srcImage;
				} 
			}

			return trgImage;
		}
	}

	/**
	 * @param srcImage
	 * @param sourceRect
	 * @return
	 */
	private RenderedImage calculateTargetImage(RenderedImage srcImage, Rectangle sourceRect) {
		RenderedImage trgImage = srcImage;
		RenderInfo info = trgImage.getRenderInfo();
		// Account for rounding errors - if the size of the image and source rectangle
		// are within a tolerance, don't re-render the image.
		if (Math.abs(sourceRect.width - info.getWidth()) > RENDER_TOLERANCE ||
			Math.abs(sourceRect.height - info.getHeight()) > RENDER_TOLERANCE) {
			info.setValues(sourceRect.width, sourceRect.height, info.shouldMaintainAspectRatio(), info
				.shouldAntiAlias(), info.getBackgroundColor(), info
				.getForegroundColor());

			trgImage = srcImage.getNewRenderedImage(info);
			info = trgImage.getRenderInfo();
		}
		
		// test for maximum rendering size
		if (getMaximumRenderSize() == null)
			return trgImage;
		
		if (sourceRect.width > getMaximumRenderSize().width || 
			sourceRect.height > getMaximumRenderSize().height) {
			if (sourceRect.width > getMaximumRenderSize().width) {
				double ratio = (double)sourceRect.height / sourceRect.width;
				sourceRect.width = getMaximumRenderSize().width;
				sourceRect.height = (int)Math.round(sourceRect.width * ratio);
			}
			
			if (sourceRect.height > getMaximumRenderSize().height) {
				double ratio = (double)sourceRect.width / sourceRect.height;
				sourceRect.height = getMaximumRenderSize().height;
				sourceRect.width = (int)Math.round(sourceRect.height * ratio);
			}
			
			info.setValues(sourceRect.width, sourceRect.height, info.shouldMaintainAspectRatio(), info
				.shouldAntiAlias(), info.getBackgroundColor(), info
				.getForegroundColor());

			trgImage = srcImage.getNewRenderedImage(info);
			info = trgImage.getRenderInfo();
		}
		
		return trgImage;
	}
}
