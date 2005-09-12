/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.draw2d.ui.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.PointList;

/**
 * @author jschofie
 *
 * This interface is used by the Polygon Connection Anchor to obtain
 * the list of points that form a polygon.
 */
public interface IPolygonAnchorableFigure extends IFigure {

	/**
	 * This method allows the implementing class to return a
	 * list of points that represent the polygon to be connected
	 * to.
	 * @return a list of points forming the polygon
	 */
	public PointList getPolygonPoints();
}
