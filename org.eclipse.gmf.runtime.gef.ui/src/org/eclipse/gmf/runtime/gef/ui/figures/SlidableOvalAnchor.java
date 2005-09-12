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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.draw2d.ui.figures.IOvalAnchorableFigure;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;


/**
 * @author oboyko
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SlidableOvalAnchor
	extends SlidableAnchor {

	/**
	 * Default constructor
	 */
	public SlidableOvalAnchor() {
		super();
	}
	
	/**
	 * Creates default <Code>SlidableOvalAnchor</Code> with a reference points
	 * at the center of the figure
	 * 
	 * @param f the <code>IOvalAnchorableFigure</code> that this anchor will be associated with
	 */
	public SlidableOvalAnchor(IOvalAnchorableFigure f) {
		super(f);
	}
	
	/**
	 * Creates <Code>SlidableOvalAnchor</Code> with a specified reference points
	 * 
	 * @param f the <code>IOvalAnchorableFigure</code> that this anchor will be associated with
	 * @param p the <code>PrecisionPoint</code> that the anchor will initially attach to.
	 */
	public SlidableOvalAnchor(IOvalAnchorableFigure f, PrecisionPoint p) {
		super(f,p);
	}	
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.SlidableAnchor#getBox()
	 */
	protected Rectangle getBox() {
		Rectangle rBox = (((IOvalAnchorableFigure) getOwner()).getOvalBounds()).getCopy();
		getOwner().translateToAbsolute(rBox);
		return rBox;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.SlidableAnchor#getIntersectionPoints(org.eclipse.draw2d.geometry.Point, org.eclipse.draw2d.geometry.Point)
	 */
	protected PointList getIntersectionPoints(Point ownReference, Point foreignReference) {
		return (new LineSeg(ownReference, foreignReference)).getLineIntersectionsWithEllipse(getBox());
	}
}

