/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.figures.IPolygonAnchorableFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;


/**
 * Draws a diamond figure.
 * @author mmuszyns
 */
public class DiamondFigure extends NodeFigure implements IPolygonAnchorableFigure{

	/** 
	 * Create an instance.
	 * @param dim the preferred size.
	 */
	public DiamondFigure( Dimension dim ) { 
		setPreferredSize(dim);
	}
	
	/**
	 * paints the object flow figure: a rectangular shape.
	 * @param graphics
	 */
	protected void paintFigure(Graphics graphics) {
		Rectangle r = new Rectangle();
		PointList pointList = new PointList();

		r.x = bounds.x;
		r.y = bounds.y;
		r.width = bounds.width - 1;
		r.height = bounds.height - 1;
		pointList.removeAllPoints();
		pointList.addPoint(r.x + r.width / 2, r.y);
		pointList.addPoint(r.x + r.width, r.y + r.height / 2);
		pointList.addPoint(r.x + r.width / 2, r.y + r.height);
		pointList.addPoint(r.x, r.y + r.height / 2);
		graphics.fillPolygon(pointList);
		graphics.drawPolygon(pointList);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.figures.IPolygonAnchorableFigure#getPolygonPoints()
	 */
	public PointList getPolygonPoints() {
		PointList ptList = new PointList();
		ptList.addPoint(new Point(getBounds().x+getBounds().width/2, getBounds().y));
		ptList.addPoint(new Point(getBounds().x+getBounds().width, getBounds().y+getBounds().height/2));
		ptList.addPoint(new Point(getBounds().x+getBounds().width/2, getBounds().y+getBounds().height));
		ptList.addPoint(new Point(getBounds().x, getBounds().y+getBounds().height/2));
		ptList.addPoint(new Point(getBounds().x+getBounds().width/2, getBounds().y));
		return ptList;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#getSlidableAnchorArea()
	 */
	protected double getSlidableAnchorArea() {
		return 0.7;
	}
}
