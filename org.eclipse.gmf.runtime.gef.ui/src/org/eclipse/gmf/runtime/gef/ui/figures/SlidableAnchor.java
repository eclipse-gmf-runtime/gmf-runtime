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

package org.eclipse.gmf.runtime.gef.ui.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.BaseSlidableAnchor;


/**
 * Provides the implementation of Slidable anchor
 * 
 * @author oboyko
 *
 */
public class SlidableAnchor
	extends BaseSlidableAnchor {
	
	/**
	 * Empty constructor 
	 */
	public SlidableAnchor() {
		// empty constructor
	}
	
	/**
	 * Default constructor. The anchor will have the center of the figure as the
	 * reference point 
	 * 
	 * @param f <code>IFigure</code> that this anchor is associated with.
	 */
	public SlidableAnchor(IFigure f) {
		super(f);
	}

	/**
	 * Constructor. Takes point p to store the reference point 
	 * 
	 * @param f <code>IFigure</code> that this anchor is associated with.
	 * @param p the <code>PrecisionPoint</code> that the anchor will initially attach to.
	 */
	public SlidableAnchor(IFigure f, PrecisionPoint p) {
		super(f, p);
	}
	
	/**
	 * Gets the anchors associated figure's bounding box in absolute coordinates.
	 * 
	 * @return a <code>Rectangle</code> that is the bounding box of the owner figure 
	 * in absolute coordinates
	 */
	protected Rectangle getBox() {
		Rectangle rBox = getOwner().getBounds().getCopy();
		if (getOwner() instanceof HandleBounds)
			rBox = ((HandleBounds)getOwner()).getHandleBounds().getCopy();
		
		getOwner().translateToAbsolute(rBox);
		return rBox;
	}
	
	
}
