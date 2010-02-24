/******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.draw2d.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.figures.IPolygonAnchorableFigure;
import org.eclipse.swt.graphics.Path;

/**
 * Base class for polygons in the Geometric shapes palette
 * 
 * @author jschofie
 */
public abstract class GeoShapePolygonFigure
	extends GeoShapeFigure
	implements IPolygonAnchorableFigure {

	/**
	 * sub-class must return their list of points based on the object size
	 */
	abstract protected PointList calculatePoints(Rectangle rect);

	public GeoShapePolygonFigure(int width, int height, int spacing) {
		super(width, height, spacing);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics g) {
		PointList points = calculatePoints(getWidthSpecificBounds());

		g.pushState();
		// don't apply transparency to the outline
		applyTransparency(g);
		if (!isUsingGradient()) {
			g.fillPolygon(points);
		} else {
			fillGradient(g, getPath(points));
		}
		g.popState();

		// set the line type and line width
		g.setLineStyle(getLineStyle());
		g.setLineWidth(getLineWidth());

		g.drawPolygon(points);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.draw2d.figures.IPolygonAnchorableFigure#getPolygonPoints()
	 */
	public PointList getPolygonPoints() {
		return calculatePoints(getBounds().getCopy());
	}
	
	/**
	 * Returns bounds used for painting the figure. Normally, the bounds decreases as the line width increases
	 * in order to avoid cropping of shape outline.
	 * 
	 * @return customized bounds
	 */
	protected Rectangle getWidthSpecificBounds() {
		return getBounds().getCopy().shrink(getLineWidth() / 2, getLineWidth() / 2);
	}

	/**
	 * @param points
	 * @return
	 * @since 1.2
	 */
	protected Path getPath(PointList points) {
		Path path = new Path(null);
		if (points.size() > 2) {
			Point pt = points.getFirstPoint();
			path.moveTo(pt.x, pt.y);
			for (int index = 1; index < points.size(); index++) {
				pt = points.getPoint(index);
				path.lineTo(pt.x, pt.y);
			}
			path.close();
		}
		return path;
	}
}