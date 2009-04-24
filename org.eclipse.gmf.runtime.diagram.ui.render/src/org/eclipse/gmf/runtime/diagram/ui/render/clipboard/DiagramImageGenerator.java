/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.render.clipboard;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScaledGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.graphics.GraphicsToGraphics2DAdaptor;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.graphics.RenderedMapModeGraphics;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

/**
 * Supports generation of AWT and SWT images of a diagram or a subset of
 * editparts on a diagram.
 * 
 * @author schafe / sshaw
 */
public class DiagramImageGenerator
	extends DiagramGenerator {

	/**
	 * Creates a new instance.
	 * @param dgrmEP
	 *            the diagram editpart
	 */
	public DiagramImageGenerator(DiagramEditPart dgrmEP) {
		super(dgrmEP);
	}

	private GC gc = null;

	private Image image = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.clipboard.DiagramGenerator#setUpGraphics(int,
	 *      int)
	 */
	protected Graphics setUpGraphics(int width, int height) {
		Display display = Display.getDefault();

		image = new Image(display, new org.eclipse.swt.graphics.Rectangle(0, 0, width, height));
		gc = new GC(image);
		SWTGraphics swtG = new SWTGraphics(gc);
		
		/*
		IPreferenceStore preferenceStore =
			(IPreferenceStore) getDiagramEditPart().getDiagramPreferencesHint().getPreferenceStore();
		
		if (preferenceStore.getBoolean(IPreferenceConstants.PREF_ENABLE_ANTIALIAS)) {
			swtG.setAntialias(SWT.ON);
		} else {
			swtG.setAntialias(SWT.OFF);
		}
		*/
		return swtG;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.clipboard.DiagramGenerator#disposeGraphics(org.eclipse.draw2d.Graphics)
	 */
	protected void disposeGraphics(Graphics g) {
		super.disposeGraphics(g);

		image.dispose();

		if (gc != null)
			gc.dispose();
		gc = null;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramGenerator#getImageDescriptor(org.eclipse.draw2d.Graphics)
	 */
	protected ImageDescriptor getImageDescriptor(Graphics g) {
		return new ImageDescriptor() {

			ImageData imgData = image.getImageData();

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.resource.ImageDescriptor#getImageData()
			 */
			public ImageData getImageData() {
				return imgData;
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramGenerator#createAWTImageForParts(java.util.List,
	 *      org.eclipse.swt.graphics.Rectangle)
	 */
	public java.awt.Image createAWTImageForParts(List selectedObjects, org.eclipse.swt.graphics.Rectangle sourceRect) {

		BufferedImage awtImage = null;
		IMapMode mm = getMapMode();
		PrecisionRectangle rect = new PrecisionRectangle();
		rect.setX(sourceRect.x);
		rect.setY(sourceRect.y);
		rect.setWidth(sourceRect.width);
		rect.setHeight(sourceRect.height);

		mm.LPtoDP(rect);

		awtImage = new BufferedImage((int) Math.round(rect.preciseWidth),
				(int) Math.rint(rect.preciseHeight),
				BufferedImage.TYPE_4BYTE_ABGR_PRE);

		Graphics2D g2d = awtImage.createGraphics();
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, awtImage.getWidth(), awtImage.getHeight());

		// Check anti-aliasing preference
		IPreferenceStore preferenceStore = (IPreferenceStore) getDiagramEditPart()
				.getDiagramPreferencesHint().getPreferenceStore();

		if (preferenceStore
				.getBoolean(IPreferenceConstants.PREF_ENABLE_ANTIALIAS)) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		} else {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
		}

		g2d.clip(new java.awt.Rectangle(0, 0, awtImage.getWidth(), awtImage
				.getHeight()));

		Graphics graphics = new GraphicsToGraphics2DAdaptor(g2d,
				new Rectangle(0, 0, awtImage.getWidth(), awtImage.getHeight()));

		RenderedMapModeGraphics mapModeGraphics = new RenderedMapModeGraphics(
				graphics, mm);

		renderToGraphics(mapModeGraphics,
				new Point(sourceRect.x, sourceRect.y), selectedObjects);

		graphics.dispose();
		g2d.dispose();
		return awtImage;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramGenerator#createConstrainedAWTImageForParts(java.util.List, int, int, boolean)
	 */
	public java.awt.Image createConstrainedAWTImageForParts(List editParts,
			int maxDeviceWidth, int maxDeviceHeight, boolean useMargins) {
		BufferedImage awtImage = null;
		IMapMode mm = getMapMode();
		
		ConstrainedImageRenderingData data = getConstrainedImageRenderingData(editParts, maxDeviceWidth, maxDeviceHeight, useMargins);
		
		awtImage = new BufferedImage(data.imageWidth, data.imageHeight, BufferedImage.TYPE_4BYTE_ABGR_PRE);

		Graphics2D g2d = awtImage.createGraphics();
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, awtImage.getWidth(), awtImage.getHeight());

		// Check anti-aliasing preference
		IPreferenceStore preferenceStore = (IPreferenceStore) getDiagramEditPart()
				.getDiagramPreferencesHint().getPreferenceStore();

		if (preferenceStore
				.getBoolean(IPreferenceConstants.PREF_ENABLE_ANTIALIAS)) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		} else {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
		}

		g2d.clip(new java.awt.Rectangle(0, 0, awtImage.getWidth(), awtImage
				.getHeight()));

		
		Graphics graphics = new GraphicsToGraphics2DAdaptor(g2d,
				new org.eclipse.swt.graphics.Rectangle(0, 0, data.imageWidth, data.imageHeight));
		
		ScaledGraphics scaledGraphics = new ScaledGraphics(graphics);

		RenderedMapModeGraphics mapModeGraphics = new RenderedMapModeGraphics(
				scaledGraphics, mm);

		g2d.translate(data.margin, data.margin);
		mapModeGraphics.scale(data.scalingFactor);
		
		Point location = new PrecisionPoint(data.imageOriginalBounds.preciseX(), data.imageOriginalBounds.preciseY());
		mm.DPtoLP(location);
		renderToGraphics(mapModeGraphics,
				location, editParts);

		graphics.dispose();
		g2d.dispose();
		return awtImage;
	}

}