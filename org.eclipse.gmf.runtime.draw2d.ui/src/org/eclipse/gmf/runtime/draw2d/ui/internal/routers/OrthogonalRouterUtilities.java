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

package org.eclipse.gmf.runtime.draw2d.ui.internal.routers;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;


/**
 * @author sshaw
 *
 */
public class OrthogonalRouterUtilities {
	/**
	 * Calculates the center point that is aligned vertically or 
	 * horizontally with the given reference point.
	 * 
	 * @param rect the <code>Rectangle</code> whose center point is used as 
	 * the location point to be adjusted versus the outside reference point.
	 * @param ref the <code>Point</code> that is used as the reference to
	 * readjust the center point of the given <code>Rectangle</code>
	 * @return a <code>Point</code> that is adjusted horizontally or vertically
	 * with respect to the given reference point.
	 */
	public static Point getAdjustedCenterPoint(
		final Rectangle rect,
		final Point ref) {
		Point ptNewCenter = new Point(rect.getCenter());

		if (ref.x < rect.getRight().x && ref.x > rect.getLeft().x) {
			ptNewCenter.x = ref.x;
		} else if (ref.y < rect.getBottom().y && ref.y > rect.getTop().y) {
			ptNewCenter.y = ref.y;
		}

		return ptNewCenter;
	}

	/**
	 * Resets both of the end points in the polyline to be close to the center point
	 * of the figure.
	 * 
	 * @param conn the <code>Connection</code> that is used to retrieve the ends and map the coordinates 
	 * of the end owners into absolute coordinates.
	 * @param newLine the <code>PointList</code> whose end points are modified to be inside the end owners
	 * bounding boxes.
	 */
	public static void resetEndPointsToCenter(
		Connection conn,
		PointList newLine) {
		Rectangle startRect =
			new Rectangle(conn.getSourceAnchor().getOwner().getBounds());
		conn.getSourceAnchor().getOwner().translateToAbsolute(startRect);

		Point ptStart = newLine.getPoint(1);
		conn.translateToAbsolute(ptStart);
		ptStart = getAdjustedCenterPoint(startRect, ptStart);
		conn.translateToRelative(ptStart);
		newLine.setPoint(ptStart, 0);

		Rectangle endRect =
			new Rectangle(conn.getTargetAnchor().getOwner().getBounds());
		conn.getTargetAnchor().getOwner().translateToAbsolute(endRect);

		Point ptEnd = newLine.getPoint(newLine.size() - 2);
		conn.translateToAbsolute(ptEnd);
		ptEnd = getAdjustedCenterPoint(endRect, ptEnd);
		conn.translateToRelative(ptEnd);
		newLine.setPoint(ptEnd, newLine.size() - 1);
	}
	
	/**
	 * getEdgePoint
	 * Utility method used to calculate the edge point of the source of target shape.
	 * 
	 * @param conn Connection figure used to translate the point coordinates
	 * @param anchor ConnectionAnchor to get the source / target bounds.
	 * @param ptRef Point Reference point from which the edge point is calculated
	 * @return Point that is on the edge of the Source / Target shape.
	 */
	private static Point getEdgePoint(
		Connection conn,
		ConnectionAnchor anchor,
		Point ptRef) {
		Rectangle rect = new Rectangle(anchor.getOwner().getBounds());
		anchor.getOwner().translateToAbsolute(rect);
		conn.translateToRelative(rect);
		
        Point ptRef2 = new Point(ptRef);
        Point ptRef1 = getAdjustedCenterPoint(rect, ptRef2);

        Point ptAbsRef2 = new Point(ptRef2);
        conn.translateToAbsolute(ptAbsRef2);
        Point ptEdge = anchor.getLocation(ptAbsRef2);
		conn.translateToRelative(ptEdge);

		LineSeg lineSeg = new LineSeg(ptRef1, ptRef2);
		Point ptProj = lineSeg.perpIntersect(ptEdge.x, ptEdge.y);

		// account for possible rounding errors and ensure the
		// resulting line is straight
		if (Math.abs(ptProj.x - ptRef2.x) < Math.abs(ptProj.y - ptRef2.y))
			ptProj.x = ptRef2.x;
		else
			ptProj.y = ptRef2.y;
		
		return ptProj;
	}
	
	/**
	 * Utility method used to calculate the orthongaol line segment that connects to the
	 * given anchor location from a reference point.
	 * 
	 * @param conn <code>Connection</code> figure used to translate the point coordinates
	 * @param anchor the <code>ConnectionAnchor</code> to retrieve the location given a reference 
	 * point
	 * @param ref the <code>Point</code> that is a reference from which the edge point is 
	 * calculated
	 * @return <code>Point</code> that is on a legitimate connection location of the 
	 * <code>ConnectionAnchor</code> owner shape.
	 */	
	public static LineSeg getOrthogonalLineSegToAnchorLoc(
			Connection conn,
			ConnectionAnchor anchor,
			Point ref) {
			
			Point ptAbsRef = getEdgePoint(conn, anchor, ref);
			conn.translateToAbsolute(ptAbsRef);
			Point ptEdge = anchor.getLocation(ptAbsRef);
			conn.translateToRelative(ptEdge);
			
			LineSeg result = new LineSeg(ptEdge, ref);
			if (!result.isHorizontal() && !result.isVertical()) {
				if (Math.abs(result.getOrigin().x - result.getTerminus().x) < 
					Math.abs(result.getOrigin().y - result.getTerminus().y)) {
					result.setTerminus(new Point(result.getOrigin().x, result.getTerminus().y));
				}
				else {
					result.setTerminus(new Point(result.getTerminus().x, result.getOrigin().y));
				}
			}
			
			return result;
	}
}
