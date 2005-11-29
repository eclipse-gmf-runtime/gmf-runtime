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

package org.eclipse.gmf.runtime.draw2d.ui.render.figures;

import java.io.ByteArrayOutputStream;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.factory.RenderedImageFactory;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.DrawableRenderedImage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;


/**
 * An implementation of {@link org.eclipse.draw2d.ImageFigure} that allows scaling the 
 * underlying image to the containing Figure's bounds, rather then being fixed to the image size.
 * 
 * <p>Any image that can be implemented inside the RenderedImage interface can be supported.</p>
 * 
 * @author jcorchis / sshaw
 */
public class ScalableImageFigure extends ImageFigure {

	/** The preferred size of the image */
	private Dimension preferredSize = new Dimension(-1, -1);
	
	private static final int
		FLAG_USE_DEFAULT_IMAGESIZE = MAX_FLAG << 1,
		FLAG_MAINTAIN_ASPECT_RATIO = MAX_FLAG << 2,
		FLAG_ANTI_ALIAS = MAX_FLAG << 3,
		FLAG_USE_ORIGINAL_COLORS = MAX_FLAG << 4;

	/** The original <code>RenderedImage</code> at 100% device coordinate size */
    private RenderedImage origRenderedImage = null;
    
    /** The cached <code>RenderedImage</code> that was last painted to the graphics context */
    private RenderedImage lastRenderedImage = null;

    	/**
	 * Accessor to determine if the rendered image will be anti-aliased (if possible).
	 * 
	 * @return <code>boolean</code> <code>true</code> if anti aliasing is on, <code>false</code> otherwise.
	 */
	public boolean isAntiAlias() {
		return getFlag(FLAG_ANTI_ALIAS);
	}
	
	/**
	 * Sets a property to determine if the rendered image will be anti-aliased (if possible).
	 * 
	 * @param antiAlias <code>boolean</code> <code>true</code> if anti-aliasing is to be turned on, 
	 * <code>false</code> otherwise
	 */
	public void setAntiAlias(boolean antiAlias) {
		setFlag(FLAG_ANTI_ALIAS, antiAlias);
		invalidate();
	}
	
	/**
	 * Accessor to determine if the rendered image will respect the original aspect 
	 * ratio of the default image when resized.
	 * 
	 * @return <code>boolean</code> <code>true</code> if maintain aspect ratio is on, <code>false</code> otherwise.
	 */
	public boolean isMaintainAspectRatio() {
		return getFlag(FLAG_MAINTAIN_ASPECT_RATIO);
	}
	
	/**
	 * Sets a property to determine if the rendered image will respect the original aspect 
	 * ratio of the default image when resized.
	 * 
	 * @param maintainAspectRatio <code>boolean</code> <code>true</code> if maintain aspect ratio is to be turned on, 
	 * <code>false</code> otherwise
	 */
	public void setMaintainAspectRatio(boolean maintainAspectRatio) {
		setFlag(FLAG_MAINTAIN_ASPECT_RATIO, maintainAspectRatio);
		invalidate();
	}

	/**
	 * @param img the <code>Image</code> to render
	 */
	public ScalableImageFigure(Image img) {
		ImageLoader imageLoader = new ImageLoader();
		ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
		imageLoader.data = new ImageData[] {img.getImageData()};
		imageLoader.logicalScreenHeight = img.getBounds().width;
		imageLoader.logicalScreenHeight = img.getBounds().height;
		imageLoader.save(byteOS, SWT.IMAGE_BMP);
		this.origRenderedImage = RenderedImageFactory.getInstance(byteOS.toByteArray());
		
		setFlag(FLAG_USE_DEFAULT_IMAGESIZE, false);
		setFlag(FLAG_USE_ORIGINAL_COLORS, false);
		setFlag(FLAG_MAINTAIN_ASPECT_RATIO, true);
		setFlag(FLAG_ANTI_ALIAS, true);
	}
	
	/**
	 * @param renderedImage
	 */
	public ScalableImageFigure(RenderedImage renderedImage) {
		this(renderedImage, false, false, true);
	}
	
	/**
	 * Constructor for meta image sources.
	 * 
	 * @param renderedImage the <code>RenderedImage</code> that is used for rendering the image.
	 */	
	public ScalableImageFigure(RenderedImage renderedImage, boolean antiAlias) {
		this(renderedImage, false, false, antiAlias);
	}
	
	/**
	 * Constructor for meta image sources.
	 * 
	 * @param renderedImage the <code>RenderedImage</code> that is used for rendering the image.
	 * @param useDefaultImageSize <code>boolean</code> indicating whether to initialize the preferred size 
	 * with the default image size.  Otherwise, a set default will be used instead.
	 * @param useOriginalColors <code>boolean</code> indicating whether to use the original colors of the
	 * <code>RenderedImage</code> or to replace black with outline color and white with the fill color.
	 */
	public ScalableImageFigure(RenderedImage renderedImage, 
				boolean useDefaultImageSize, boolean useOriginalColors, boolean antiAlias) {
		origRenderedImage = renderedImage;
		setFlag(FLAG_USE_DEFAULT_IMAGESIZE, useDefaultImageSize);
		setFlag(FLAG_USE_ORIGINAL_COLORS, useOriginalColors);
		setFlag(FLAG_MAINTAIN_ASPECT_RATIO, true);
		setFlag(FLAG_ANTI_ALIAS, antiAlias);
	}
	
	/**
	 * Sets the preferred size of the image figure.
	 * 
	 * @param w the preferred width of the image
	 * @param h the preferred height of the image
	 */
	public void setPreferredImageSize(int w, int h) {
		preferredSize = new Dimension(w, h);
	}

	/**
	 * Returns the size set specified by setPreferredImageSize() or 
	 * the size specified by the image. In the case of meta-images
	 * a preferred size of 32x32 is returned. 
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		if (preferredSize.height == -1 && preferredSize.width == -1) {
			if (getImage() == null)
				return preferredSize;
			
			int extent = MapModeUtil.getMapMode(this).DPtoLP(32);
			preferredSize =  new Dimension(extent,extent);
			if (getFlag(FLAG_USE_DEFAULT_IMAGESIZE)) {
				if (getRenderedImage() != null) {
					setRenderedImage( getRenderedImage(new Dimension(0, 0)));
					Image swtImage = null;
					if (getRenderedImage() != null)
						swtImage = getRenderedImage().getSWTImage();
					if (swtImage != null) {
						org.eclipse.swt.graphics.Rectangle imgRect = swtImage.getBounds();
						preferredSize.width = MapModeUtil.getMapMode(this).DPtoLP(imgRect.width);
						preferredSize.height = MapModeUtil.getMapMode(this).DPtoLP(imgRect.height);
					}
				}
			}
		}
		return preferredSize;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#setBounds(org.eclipse.draw2d.geometry.Rectangle)
	 */
	public void setBounds(Rectangle rect) {
		Dimension devDim = new Dimension(rect.getSize());
		MapModeUtil.getMapMode(this).LPtoDP(devDim);
		setRenderedImage( getRenderedImage(devDim) );
		
		super.setBounds(rect);
	}

	/**
	 * Override to return an image that is scaled to fit the bounds of the figure.
	 */
	public Image getImage() {
		if (getRenderedImage() == null)
			return null;

		return getRenderedImage().getSWTImage();
	}

	/**
	 * Gets the <code>RenderedImage</code> that is the for the specified <code>Dimension</code>
	 * 
	 * @param dim
	 * @return the <code>RenderedImage</code>
	 */
	private RenderedImage getRenderedImage(Dimension dim) {
		RenderInfo newRenderInfo =
			RenderedImageFactory.createInfo(
				dim.width,
				dim.height,
				useOriginalColors() ? null : translateSWTColorToAWTColor(getBackgroundColor()),
				useOriginalColors() ? null : translateSWTColorToAWTColor(getForegroundColor()),
				isMaintainAspectRatio(), // maintain aspect ratio
				isAntiAlias()); // antialias

		RenderedImage newRenderedImage = getRenderedImage().getNewRenderedImage(newRenderInfo);
		return newRenderedImage;
	}

	/**
	 * @return a <code>boolean</code> <code>true</code> if the original colors of the image should be used for
	 * rendering, or <code>false</code> indicates that black and white colors can replaced by the specified outline
	 * and fill colors respectively of the <code>RenderInfo</code>.
	 */
	public boolean useOriginalColors() {
		return getFlag(FLAG_USE_ORIGINAL_COLORS);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		int x, y;
		Rectangle area = getClientArea().getCopy();
		
		y = (area.height - getBounds().height) / 2 + area.y;
		x = (area.width - getBounds().width) / 2 + area.x;		
		
		if (graphics instanceof DrawableRenderedImage) {
           	lastRenderedImage = ((DrawableRenderedImage) graphics).drawRenderedImage(getRenderedImage(new Dimension(getBounds().width, getBounds().height)),
                       x, y, getBounds().width, getBounds().height);
           	assert lastRenderedImage != null;
		} else { 
			graphics.drawImage(getRenderedImage().getSWTImage(), x, y); 
        }
	}	
	
    /**
     * Returns the corresponding java.awt.Color given an org.eclipse.swt.graphics.Color
     * 
     * @param swtColor to be translated
     * @return the corresponding java.awt.Color
     */    
    private java.awt.Color translateSWTColorToAWTColor(Color swtColor) {
		return (swtColor == null) ? null
			: new java.awt.Color(swtColor.getRed(), swtColor.getGreen(),
				swtColor.getBlue());
	}	
    
	/**
	 * Gets the <code>RenderedImage</code> that is being displayed by this figure.
	 * 
	 * @return <code>RenderedImage</code> that is being displayed by this figure.
	 */
	public RenderedImage getRenderedImage() {
		return origRenderedImage;
	}
	
	/**
	 * Sets the <code>RenderedImage</code> that is to be displayed by this figure
	 * 
	 * @param the <code>RenderedImage</code> that is to being displayed by this figure
	 */
	public void setRenderedImage(RenderedImage renderedImage) {
		this.origRenderedImage = renderedImage;
	}
}
