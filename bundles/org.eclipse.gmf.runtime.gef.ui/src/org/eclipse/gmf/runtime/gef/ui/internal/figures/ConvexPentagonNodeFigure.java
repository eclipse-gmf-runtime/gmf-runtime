/******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and others.
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
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.figures.IPolygonAnchorableFigure;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PrecisionPointList;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;


/**
 * Provides support for Convex Pentagon figure (Send Signal Action for Activity diagram)
 *
 * @author oboyko
 */
public class ConvexPentagonNodeFigure
	extends DefaultSizeNodeFigure implements IPolygonAnchorableFigure{

	private static final double DEFAULT_CONVEX_RATIO = 0.25;

	/**
	 * Constructor
	 * @param defSize
	 */
	public ConvexPentagonNodeFigure(Dimension defSize) {
		super(defSize);
	}

	/**
	 * Constructor
	 * @param width
	 * @param height
	 */
	public ConvexPentagonNodeFigure(int width, int height) {
		super(width, height);
	}

	@Override
    protected void paintFigure(Graphics graphics) {
		PointList pointList = getPolygonPoints();

		graphics.fillPolygon(pointList);
		graphics.drawPolygon(pointList);
	}

	@Override
    public PointList getPolygonPoints() {
	    PrecisionPointList ptList = new PrecisionPointList();
		Rectangle b = getBounds();
        ptList.addPoint(new PrecisionPoint(b.x, b.y));
        ptList.addPoint(new PrecisionPoint(b.x + b.width * (1 - DEFAULT_CONVEX_RATIO), b.y));
        ptList.addPoint(new PrecisionPoint(b.x + b.width, b.y + b.height / 2));
        ptList.addPoint(new PrecisionPoint(b.x + b.width * (1 - DEFAULT_CONVEX_RATIO), b.y + b.height));
        ptList.addPoint(new PrecisionPoint(b.x, b.y + b.height));
        ptList.addPoint(new PrecisionPoint(b.x, b.y));
		return ptList;
	}

}
