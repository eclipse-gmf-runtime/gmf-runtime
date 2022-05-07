/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gmf.runtime.draw2d.ui.figures.BaseSlidableAnchor;


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
	 * in relative coordinates
	 */
	protected Rectangle getBox() {
		Rectangle rBox = getOwner() instanceof HandleBounds ? new PrecisionRectangle(
				((HandleBounds) getOwner()).getHandleBounds())
				: new PrecisionRectangle(getOwner().getBounds());
		getOwner().translateToAbsolute(rBox);
		return rBox;
	}

}
