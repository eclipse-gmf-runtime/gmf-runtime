/******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.render.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.figures.IExpandableFigure;

/**
 * Utility methods used by various diagram image generators
 * 
 * @author aboyko
 *
 */
public final class DiagramImageUtils {

	/**
	 * Calculates the bounding box around given editparts. The bounding box is relative to printable layer
	 * 
	 * @param editparts given editparts
	 * @param frameSize frame around the bounding box
	 * @param defaultSize if there are no editparts, the size of the bounding box will be the default one.
	 * @return the editparts bounding box
	 */
	public static final Rectangle calculateImageRectangle(
			List<IGraphicalEditPart> editparts, double frameSize, Dimension defaultSize) {
		double minX = editparts.isEmpty() ? 0 : Double.MAX_VALUE;
		double maxX = editparts.isEmpty() ? 0 : Double.MIN_VALUE;
		double minY = editparts.isEmpty() ? 0 : Double.MAX_VALUE;
		double maxY = editparts.isEmpty() ? 0 : Double.MIN_VALUE;

		IFigure printableLayer = null;
		if (!editparts.isEmpty()) {
			printableLayer = LayerManager.Helper.find(editparts.get(0))
					.getLayer(LayerConstants.PRINTABLE_LAYERS);
		}

		for (IGraphicalEditPart editPart : editparts) {
			IFigure figure = editPart.getFigure();
			Rectangle bounds = null;
			if (figure instanceof IExpandableFigure)
				bounds = ((IExpandableFigure) figure).getExtendedBounds();
			else
				bounds = figure.getBounds().getCopy();
			translateTo(bounds, figure, printableLayer);
			
			minX = Math.min(minX, bounds.preciseX());
			maxX = Math.max(maxX, bounds.preciseX() + bounds.preciseWidth());
			minY = Math.min(minY, bounds.preciseY());
			maxY = Math.max(maxY, bounds.preciseY() + bounds.preciseHeight());
		}

		PrecisionRectangle rect = new PrecisionRectangle();
		rect.preciseWidth = maxX - minX;
		rect.preciseHeight = maxY - minY;
		
		if (defaultSize != null) {
			if (rect.preciseHeight <= 0) {
				rect.preciseHeight = defaultSize.preciseWidth();
			}
			if (rect.preciseHeight <= 0) {
				rect.preciseHeight = defaultSize.preciseHeight();
			}
		}
		
		rect.preciseX = minX - frameSize;
		rect.preciseY = minY - frameSize;
		rect.preciseWidth += 2 * frameSize;
		rect.preciseHeight += 2 * frameSize;
		rect.updateInts();
		return rect;
	}
	
	/**
	 * Translates <code>t</code> from one figure coordinate system to its ancestor figure coordinate system 
	 * 
	 * @param t the value to translate
	 * @param translateFrom initial figure
	 * @param translateTo some ancestor figure
	 * @return translated <code>t</code>
	 */
	public static final Translatable translateTo(Translatable t,
			IFigure translateFrom, IFigure translateTo) {
		for (IFigure walker = translateFrom; walker != null
				&& walker != translateTo; walker = walker.getParent()) {
			walker.translateToParent(t);
		}
		return t;
	}
	
	/**
	 * Sorts editparts using editparts figures z-order. All figures must be
	 * ancestors of <code>zOrderRoot</code> figure
	 * 
	 * @param editparts
	 *            list of editparts to sort
	 * @param zOrderRoot
	 *            the common parent
	 */
	public static void zOrderSort(List<GraphicalEditPart> editparts, IFigure zOrderRoot) {
		if (editparts == null || editparts.size() < 2) {
			return;
		}
		final Map<GraphicalEditPart, List<Integer>> indexMap = new IdentityHashMap<GraphicalEditPart, List<Integer>>(editparts.size());
		for (GraphicalEditPart ep : editparts) {
			List<Integer> index = new ArrayList<Integer>();
			for (IFigure fig = ep.getFigure(); fig != zOrderRoot && fig.getParent() != null; fig = fig.getParent()) {
				index.add(fig.getParent().getChildren().indexOf(fig));
			}
			indexMap.put(ep, index);
		}
		Collections.sort(editparts, new Comparator<GraphicalEditPart>() {
			public int compare(GraphicalEditPart ep1, GraphicalEditPart ep2) {
				List<Integer> index1 = indexMap.get(ep1);
				List<Integer> index2 = indexMap.get(ep2);
				int num1, num2;
				for (int i = 0; i < index1.size() && i < index2.size(); i++) {
					num1 = index1.get(index1.size() - 1 - i).intValue();
					num2 = index2.get(index2.size() - 1 - i).intValue();
					if (num1 < num2) {
						return -1;
					} else if (num1 > num2) {
						return 1;
					}
				}
				/*
				 * If we get here then either one editparts figure is the child of another one.
				 * Child figure will be on top of its parent. 
				 * Parent figure will have smaller size of the index
				 */
				if (index1.size() < index2.size()) {
					return 1;
				} else if (index1.size() > index2.size()) {
					return -1;
				} else {
					return 0;
				}
			}
		});
	}
	
}
