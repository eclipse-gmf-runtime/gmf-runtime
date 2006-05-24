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

package org.eclipse.gmf.runtime.diagram.ui.internal.figures;


import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.figures.LabelLocator;

/**
 * @author mmostafa
 */
public class ResizableLabelLocator extends LabelLocator{
	
	/**
	 * Constructor for figure who are located and sized.
	 * @param parent		the parent figure
	 * @param bounds		the bounds
	 * @param alignment		the alignment
	 */
	public ResizableLabelLocator(IFigure parent, Rectangle bounds, int alignment) {
		super(parent,bounds,alignment);
	}
	
	/**
	 * Positions the lable relative to the reference point with the
	 * given offsets.
	 */
	public void relocate(IFigure target) {
		Dimension preferredSize = target.getPreferredSize();
		LabelLocator currentConstraint = (LabelLocator)target.getParent().getLayoutManager().getConstraint(target);
		Dimension currentExtent = currentConstraint.getSize();
		Dimension size = new Dimension(currentExtent);
		if (currentExtent.width == -1) size.width = preferredSize.width;
		if (currentExtent.height == -1) size.height = preferredSize.height;
		target.setSize(size);
		Point location = LabelHelper.relativeCoordinateFromOffset(target, getReferencePoint(), getOffset());
		
		target.setLocation(location);
	}
}
