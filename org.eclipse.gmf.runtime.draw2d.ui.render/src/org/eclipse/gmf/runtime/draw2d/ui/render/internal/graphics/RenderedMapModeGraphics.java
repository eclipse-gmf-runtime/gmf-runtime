/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.internal.graphics;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.MapModeGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.internal.mapmode.DiagramMapModeUtil;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.DrawableRenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.RenderHelper;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.RenderingListener;

/**
 * Overridden to implement DrawableRenderedImage interface
 * 
 * @author sshaw
 */
public class RenderedMapModeGraphics
	extends MapModeGraphics
	implements DrawableRenderedImage {

	boolean allowDelayRender = false;
	Dimension maximumRenderSize = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.internal.DrawableRenderedImage#allowDelayRender()
	 */
	public boolean shouldAllowDelayRender() {
		return allowDelayRender;
	}

	/**
	 * Constructor
	 * 
	 * @param g
	 *            <code>Graphics</code> element to delegate render to
	 * @param mm
	 *            <code>IMapMode</code> to retrieve the scale factor from.
	 */
	public RenderedMapModeGraphics(Graphics g, IMapMode mm) {
		this(g, mm, false, null);
	}

	/**
	 * Constructor
	 * 
	 * @param g
	 *            <code>Graphics</code> element to delegate render to
	 * @param mm
	 *            <code>IMapMode</code> to retrieve the scale factor from.
	 * @param allowDelayRender
	 */
	public RenderedMapModeGraphics(Graphics g, IMapMode mm,
			boolean allowDelayRender, Dimension maximumRenderSize) {
		super(g, mm);
		this.allowDelayRender = allowDelayRender;
		this.maximumRenderSize = maximumRenderSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.internal.DrawableRenderedImage#drawRenderedImage(org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage,
	 *      org.eclipse.draw2d.geometry.Rectangle,
	 *      org.eclipse.gmf.runtime.draw2d.ui.render.RenderingListener)
	 */
	public RenderedImage drawRenderedImage(RenderedImage srcImage,
			Rectangle rect, RenderingListener listener) {
		return RenderHelper.getInstance(
			DiagramMapModeUtil.getScale(getMapMode()), true, shouldAllowDelayRender(), getMaximumRenderSize()).drawRenderedImage(getGraphics(),
			srcImage, rect, listener);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.internal.DrawableRenderedImage#getMaximumRenderSize()
	 */
	public Dimension getMaximumRenderSize() {
		// TODO Auto-generated method stub
		return maximumRenderSize;
	}
}
