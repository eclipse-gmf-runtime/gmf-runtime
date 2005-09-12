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

package org.eclipse.gmf.runtime.diagram.ui.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.diagram.ui.internal.figures.FixedDistanceGatedPane;


/**
 * A locator that locates a child centered on it's parent.  The locator assume
 * the parent figure is a GatedPaneFigure and the target is a child of the
 * GatePane.
 * 
 * @author jcorchis
 */
public class ResizableFixedLocator
	extends LabelLocator {
	
	/**
	 * @param parent
	 * @param extent
	 */
	public ResizableFixedLocator(IFigure parent, Dimension extent) {
		super(parent, new Rectangle(0, 0, extent.width, extent.height), 0);
	}
	
	/**
	 * Positions the label centered above the parent figure with the given extent.
	 */
	public void relocate(IFigure target) {
		FixedDistanceGatedPane fdgp = (FixedDistanceGatedPane) target.getParent();
		FixedDistanceGatedPaneFigure fdgpf = (FixedDistanceGatedPaneFigure) fdgp.getParent();
		ResizableFixedLocator currentConstraint = (ResizableFixedLocator) fdgpf.getGatePane().getLayoutManager().getConstraint(target);
		if (currentConstraint != null) {
			Dimension currentExtent = currentConstraint.getSize();
			Dimension size = new Dimension(currentExtent);	
			
			if (currentExtent.width == -1) size.width = target.getPreferredSize().width;
			if (currentExtent.height == -1) size.height = target.getPreferredSize().height;
			
			if (size.width < target.getMinimumSize().width) size.width = target.getMinimumSize().width;
			if (size.height < target.getMinimumSize().height) size.height = target.getMinimumSize().height;	
			
			target.setSize(size);
			
			IFigure mp = fdgpf.getElementPane();
			Rectangle mpBounds = mp.getBounds().getCopy();
			Rectangle parentBounds = fdgpf.getBounds().getCopy();
			target.setLocation(new Point(mpBounds.x + ((mpBounds.width - size.width) / 2), parentBounds.y ));
		}
	}
}
