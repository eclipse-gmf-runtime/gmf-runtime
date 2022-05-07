/******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.internal.routers;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.figures.OrthogonalConnectionAnchor;
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
        
        Rectangle startRect = getBounds(conn.getSourceAnchor().getOwner());
        conn.getSourceAnchor().getOwner().translateToAbsolute(startRect);

		Point ptStart = newLine.getPoint(1);
		conn.translateToAbsolute(ptStart);
		ptStart = getAdjustedCenterPoint(startRect, ptStart);
		conn.translateToRelative(ptStart);
		newLine.setPoint(ptStart, 0);
        
        Rectangle endRect = getBounds(conn.getTargetAnchor().getOwner());
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
		Rectangle rect = getBounds(anchor.getOwner());
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
			
			assert anchor != null && anchor.getOwner() != null;
			
			if (anchor instanceof OrthogonalConnectionAnchor) {
				PrecisionPoint refAbs = new PrecisionPoint(ref);
				conn.translateToAbsolute(refAbs);
				PrecisionPoint anchorPoint = new PrecisionPoint(((OrthogonalConnectionAnchor)anchor).getOrthogonalLocation(refAbs));
				conn.translateToRelative(anchorPoint);
				return new LineSeg(anchorPoint, ref);
			}
			
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
    
    /**
     * Returns a copy of the bounds of this figure or if the figure is a
     * <code>Connection</code> the bounds of the pointlist will be returned.
     * 
     * @param figure
     * @return a copy of the bounds
     */
    private static Rectangle getBounds(IFigure figure) {
        return figure instanceof Connection ? ((Connection) figure).getPoints()
            .getBounds().getCopy()
            : figure.getBounds().getCopy();
    }

	/**
	 * Returns true if the points form a rectilinear line.
	 * 
	 * @param points polyline's points
	 * @return
	 */
	public static boolean isRectilinear(PointList points) {
		for (int i = 1; i < points.size(); i++) {
			Point currentPt = points.getPoint(i);
			Point previousPt = points.getPoint(i - 1);
			if (currentPt.x != previousPt.x && currentPt.y != previousPt.y) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Polylines points are modified to form a rectilinear polyline (or connection in perspective).
	 * Essentially extra points are added and list of points will form a rectilinear polyline.
	 * The method has options for specifying orientation of start and end segments of a rectilinear
	 * polyline  
	 * 
	 * @param points points list to be made rectilinear
	 * @param offStartDirection orientation of the start segment
	 * @param offEndDirection orientation of the end segment
	 */
	public static void transformToOrthogonalPointList(PointList points, int offStartDirection, int offEndDirection) {
		if (points.size() > 1) {
			PointList startPoints = new PointList(points.size());
			PointList endPoints = new PointList(points.size());
			boolean isOffSourceDirectionSet = offStartDirection == PositionConstants.HORIZONTAL || offStartDirection == PositionConstants.VERTICAL;
			boolean isOffTargetDirectionSet = offEndDirection == PositionConstants.VERTICAL || offEndDirection == PositionConstants.HORIZONTAL;
			if (!isOffSourceDirectionSet && !isOffTargetDirectionSet) {
				/*
				 * If there is no off start and off end direction passed in, determine
				 * the off start direction.
				 */
				Point first = points.getPoint(0);
				Point second = points.getPoint(1);
				offStartDirection = Math.abs(first.x - second.x) < Math
						.abs(first.y - second.y) ? PositionConstants.HORIZONTAL
						: PositionConstants.VERTICAL;
				isOffSourceDirectionSet = true;
			}
			startPoints.addPoint(points.removePoint(0));
			endPoints.addPoint(points.removePoint(points.size() - 1));
			while (points.size() != 0) {
				if (isOffSourceDirectionSet) {
					Point nextPt = points.removePoint(0);
					Point lastStartPt = startPoints.getLastPoint();
					if (nextPt.x != lastStartPt.x && nextPt.y != lastStartPt.y) {
						/*
						 * If segment is not rectilinear insert a point to make it
						 * rectilinear
						 */
						if (offStartDirection == PositionConstants.VERTICAL) {
							startPoints.addPoint(new Point(lastStartPt.x, nextPt.y));
							offStartDirection = PositionConstants.HORIZONTAL;
						} else {
							startPoints.addPoint(new Point(nextPt.x, lastStartPt.y));
							offStartDirection = PositionConstants.VERTICAL;
						}
					} else {
						offStartDirection = nextPt.x == lastStartPt.x ? PositionConstants.VERTICAL
								: PositionConstants.HORIZONTAL;
					}
					startPoints.addPoint(nextPt);
				}
				if (isOffTargetDirectionSet && points.size() != 0) {
					Point nextPt = points.removePoint(points.size() - 1);
					Point firstEndPt = endPoints.getFirstPoint();
					if (nextPt.x != firstEndPt.x && nextPt.y != firstEndPt.y) {
						/*
						 * If segment is not rectilinear insert a point to make it
						 * rectilinear
						 */
						if (offEndDirection == PositionConstants.VERTICAL) {
							endPoints.insertPoint(new Point(firstEndPt.x, nextPt.y), 0);
							offEndDirection = PositionConstants.HORIZONTAL;
						} else {
							endPoints.insertPoint(new Point(nextPt.x, firstEndPt.y), 0);
							offEndDirection = PositionConstants.VERTICAL;
						}
					} else {
						offEndDirection = nextPt.x == firstEndPt.x ? PositionConstants.VERTICAL
								: PositionConstants.HORIZONTAL;
					}
					endPoints.insertPoint(nextPt, 0);
				}
			}
			/*
			 * Now we need to merge the two point lists such that the polyline formed by the
			 * points is still rectilinear. Hence there is a chance that one more point needs
			 * to be added.
			 */
			Point lastStartPt = startPoints.getLastPoint();
			Point firstEndPt = endPoints.getFirstPoint();
			if (lastStartPt.x != firstEndPt.x && lastStartPt.y != firstEndPt.y) {
				/*
				 * We need to add extra point. Now there is a dilemma: Should we
				 * use off source orientation or off target? We'll use off
				 * target orientation in 2 cases: 
				 * 1. Off source direction has not been set and off target direction was 
				 * 2. Off target direction is set, but the start points list has more points
				 * than the end points list.
				 * Otherwise off start direction will be used.
				 */
				if ((!isOffSourceDirectionSet && isOffTargetDirectionSet) || (isOffTargetDirectionSet && endPoints.size() < startPoints.size())) {
					if (offEndDirection == PositionConstants.VERTICAL) {
						startPoints.addPoint(new Point(firstEndPt.x, lastStartPt.y));
					} else {
						startPoints.addPoint(new Point(lastStartPt.x, firstEndPt.y));
					}
				}
				else if (offStartDirection == PositionConstants.VERTICAL) {
					startPoints.addPoint(new Point(lastStartPt.x, firstEndPt.y));
				} else {
					startPoints.addPoint(new Point(firstEndPt.x, lastStartPt.y));
				}
			}
			points.addAll(startPoints);
			points.addAll(endPoints);
		}
	}
	
}
