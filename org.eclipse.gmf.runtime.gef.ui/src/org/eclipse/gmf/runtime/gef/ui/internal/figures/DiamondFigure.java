/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
 * @canBeSeenBy %level0
 */
public class DiamondFigure extends NodeFigure implements IPolygonAnchorableFigure{

	private static final int PREF_SIZE = 1058;

	/** 
	 * Create an instance.  This constructor sets the default preferred size
	 * to [1058,1058] in HiMetric coordinates. 
	 * Same as calling <code>DiamondFigure(new Dimension(1058, 1058))</code>.
	 */
	public DiamondFigure() { 
		this(new Dimension(PREF_SIZE, PREF_SIZE));
	}

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
