/******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.render.editparts;

import java.util.List;
import java.util.ListIterator;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.common.ui.util.DisplayUtils;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.MapModeGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScaledGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.render.figures.ScalableImageFigure;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.graphics.RenderedMapModeGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.graphics.RenderedScaledGraphics;
import org.eclipse.gmf.runtime.gef.ui.internal.editparts.AnimatedZoomListener;
import org.eclipse.gmf.runtime.notation.MeasurementUnit;
import org.eclipse.swt.widgets.Display;

/**
 * A specialized <code>DiagramRootEditPart</code> that supports rendering of
 * images.
 * 
 * @author cmahoney
 */
public class RenderedDiagramRootEditPart
	extends DiagramRootEditPart {

	/**
	 * Default constructor
	 */
	public RenderedDiagramRootEditPart() {
		super();
	}
	
	/**
	 * @param mu the <code>MeasurementUnit</code> that is the native coordinate system
	 * for this root edit part.
	 */
	public RenderedDiagramRootEditPart(MeasurementUnit mu) {
		super(mu);
	}

	static protected class DiagramRenderedScalableFreeformLayeredPane
		extends DiagramScalableFreeformLayeredPane implements AnimatedZoomListener {

		@SuppressWarnings("unused")
		private boolean animatedZoomOn;
		static final private Dimension MAX_RENDER_SIZE;
		static {
			Display display = DisplayUtils.getDisplay();
			MAX_RENDER_SIZE = new Dimension(display.getBounds().width, display
					.getBounds().height);
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScalableFreeformLayeredPane#createScaledGraphics(org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.MapModeGraphics)
		 */
		protected ScaledGraphics createScaledGraphics(MapModeGraphics gMM) {
//			if (animatedZoomOn) {
//				return new RenderedScaledGraphics(gMM);
//			}

			return new RenderedScaledGraphics(gMM, true, MAX_RENDER_SIZE);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScalableFreeformLayeredPane#createMapModeGraphics(org.eclipse.draw2d.Graphics)
		 */
		protected MapModeGraphics createMapModeGraphics(Graphics graphics) {
			MapModeGraphics gMM = new RenderedMapModeGraphics(graphics, getMapMode(), true, MAX_RENDER_SIZE);
			return gMM;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gmf.runtime.gef.ui.internal.editparts.AnimatedZoomListener#animatedZoomEnded()
		 */
		public void animatedZoomEnded() {
			animatedZoomOn = false;
			
			// only repaint ScalableImageFigures
			Layer primaryLayer = getLayer(PRINTABLE_LAYERS);
			if (primaryLayer != null)
				paintScalableImageFigures(primaryLayer.getChildren());
		}

		private void paintScalableImageFigures(List figures) {
			ListIterator iter = figures.listIterator();
			Rectangle absbounds = getBounds().getCopy();
			translateToAbsolute(absbounds);
			
			while (iter.hasNext()) {
				IFigure fig = (IFigure)iter.next();
				if (fig instanceof ScalableImageFigure) {
					Rectangle absfigbounds = fig.getBounds().getCopy();
					fig.translateToAbsolute(absfigbounds);
					if (absbounds.intersects(absfigbounds))
						fig.repaint();
				}
				else {
					paintScalableImageFigures(fig.getChildren());
				}
			}
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gmf.runtime.gef.ui.internal.editparts.AnimatedZoomListener#animatedZoomStarted()
		 */
		public void animatedZoomStarted() {
			animatedZoomOn = true;
		}

		public DiagramRenderedScalableFreeformLayeredPane(IMapMode mm) {
			super(mm);
		}
	}

	/**
	 * Creates a scalable freeform layered pane that supports rendering of
	 * images.
	 */
	protected org.eclipse.draw2d.ScalableFreeformLayeredPane createScalableFreeformLayeredPane() {
		setLayers(new DiagramRenderedScalableFreeformLayeredPane(getMapMode()));
		return getLayers();
	}
}