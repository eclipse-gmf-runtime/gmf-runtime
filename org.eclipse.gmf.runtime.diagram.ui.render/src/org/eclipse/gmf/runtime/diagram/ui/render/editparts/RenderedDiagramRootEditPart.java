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

package org.eclipse.gmf.runtime.diagram.ui.render.editparts;

import java.util.List;
import java.util.ListIterator;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.MapModeGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScalableFreeformLayeredPane;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScaledGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.render.figures.ScalableImageFigure;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.graphics.RenderedMapModeGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.graphics.RenderedScaledGraphics;
import org.eclipse.gmf.runtime.gef.ui.internal.editparts.AnimatedZoomListener;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * A specialized <code>DiagramRootEditPart</code> that supports rendering of
 * images.
 * 
 * @author cmahoney
 */
public class RenderedDiagramRootEditPart
	extends DiagramRootEditPart {

	static protected class DiagramRenderedScalableFreeformLayeredPane
		extends DiagramScalableFreeformLayeredPane implements AnimatedZoomListener {

		private boolean animatedZoomOn;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScalableFreeformLayeredPane#createScaledGraphics(org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.MapModeGraphics)
		 */
		protected ScaledGraphics createScaledGraphics(MapModeGraphics gMM) {
			if (animatedZoomOn) {
				return new ScaledGraphics(gMM);
			}

			return new RenderedScaledGraphics(gMM);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScalableFreeformLayeredPane#createMapModeGraphics(org.eclipse.draw2d.Graphics)
		 */
		protected MapModeGraphics createMapModeGraphics(Graphics graphics) {
			MapModeGraphics gMM = new RenderedMapModeGraphics(graphics);
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
	}

	/**
	 * Creates a scalable freeform layered pane that supports rendering of
	 * images.
	 */
	protected org.eclipse.draw2d.ScalableFreeformLayeredPane createScalableFreeformLayeredPane() {
		setLayers(new DiagramRenderedScalableFreeformLayeredPane());
		return getLayers();
	}

	/**
	 * 
	 */
	protected void refreshEnableAntiAlias() {
		IPreferenceStore preferenceStore = (IPreferenceStore) getPreferencesHint()
			.getPreferenceStore();
		boolean antiAlias = preferenceStore
			.getBoolean(IPreferenceConstants.PREF_ENABLE_ANTIALIAS);
		if (getLayers() instanceof ScalableFreeformLayeredPane)
			((ScalableFreeformLayeredPane) getLayers()).setAntiAlias(antiAlias);
	}
}