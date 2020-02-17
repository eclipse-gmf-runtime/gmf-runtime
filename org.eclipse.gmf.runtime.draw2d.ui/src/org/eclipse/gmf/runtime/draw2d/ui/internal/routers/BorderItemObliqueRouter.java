/******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
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
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.Path;
import org.eclipse.draw2d.graph.ShortestPathRouter;
import org.eclipse.gmf.runtime.draw2d.ui.figures.IBorderItemLocator;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;

/**
 * Implementation of the router that is routing connections, where at least one
 * end is a border item. This router is most suitable to work for small
 * port-like border items sitting on the border of larger shapes.
 * 
 * @author aboyko
 * 
 */
public class BorderItemObliqueRouter extends ObliqueRouter {

	private static int OFFSET = 15;

	/**
	 * utility method to reverses the position
	 * 
	 * @param position
	 *            the position to reverse
	 * @return the reversed position
	 */
	private int reversePosition(int position) {
		int newPosition = position;
		if (position == PositionConstants.SOUTH)
			newPosition = PositionConstants.NORTH;
		else if (position == PositionConstants.NORTH)
			newPosition = PositionConstants.SOUTH;
		else if (position == PositionConstants.WEST)
			newPosition = PositionConstants.EAST;
		else if (position == PositionConstants.EAST)
			newPosition = PositionConstants.WEST;
		return newPosition;
	}

	/**
	 * Returns the position of the border item with respect to its parent
	 * Clients can override this method to change the way postions is calculated
	 * for border items
	 * 
	 * @param borderFigure
	 *            Figure to use to get the position
	 * @return the border item position, see <code>PositionConstants</code>
	 */
	protected int getBorderFigurePosition(IFigure borderFigure) {
		IFigure child = borderFigure;
		IFigure parent = borderFigure.getParent();
		if (parent != null && parent.getLayoutManager() != null) {
			LayoutManager layoutManager = parent.getLayoutManager();
			Object layoutConstraint = layoutManager.getConstraint(child);
			if (layoutConstraint instanceof IBorderItemLocator) {
				return ((IBorderItemLocator) layoutConstraint)
						.getCurrentSideOfParent();
			}
		}
		return PositionConstants.NONE;
	}

	/**
	 * Returns the obstacle that the route should try to avoid, for example if
	 * the isBordereItem flag is ON, it will return the rectangle of the border
	 * item parent. This method can be overriden by clients to provide a client
	 * specific way to find the obstacle
	 * 
	 * @param figure,
	 *            figure to get the obstacle for
	 * @param conn,
	 *            the connection the router is routing
	 * @param isBorderItem,
	 *            indicates if the passed figure is a border item figrue or not
	 * @return obstacle
	 */
	protected Rectangle getObstacle(IFigure figure, Connection conn,
			boolean isBorderItem) {
		IFigure parent = null;
		if (isBorderItem)
			parent = getBorderItemParent(figure);
		else
			parent = figure;
		Rectangle rect = parent.getBounds().getCopy();
		parent.translateToAbsolute(rect);
		conn.translateToRelative(rect);
		return rect;
	}

	/**
	 * return the parent of a border item figure clients can override this
	 * method to find the
	 * 
	 * @param figure,
	 *            the border item figure
	 * @return the parent of the border item
	 */
	protected IFigure getBorderItemParent(IFigure figure) {
		return figure.getParent().getParent();
	}

	private Point getPointOffsetFromRectangle(Rectangle rectangle,
			int position, int offset) {
		Point p = getMiddlePointFromPosition(rectangle, position);
		offsetPointBasedOnPosition(p, position, offset);
		return p;
	}

	/**
	 * Returns the center of the rectangle edge specified by its geographic
	 * position
	 * 
	 * @param rectangle
	 *            rectangle
	 * @param position
	 *            geographic position
	 * @return the center-point
	 */
	private Point getMiddlePointFromPosition(Rectangle rectangle, int position) {
		if (position == PositionConstants.SOUTH)
			return rectangle.getBottom();
		else if (position == PositionConstants.NORTH)
			return rectangle.getTop();
		else if (position == PositionConstants.WEST)
			return rectangle.getLeft();
		else if (position == PositionConstants.EAST)
			return rectangle.getRight();
		return rectangle.getCenter();
	}

	/**
	 * Offsets a point by an offset value based on the geographic direction
	 * 
	 * @param point
	 *            point
	 * @param direction
	 *            geographic direction
	 * @param offset
	 *            offset value
	 */
	private void offsetPointBasedOnPosition(Point point, int direction,
			int offset) {
		if (direction == PositionConstants.SOUTH)
			point.y += offset;
		else if (direction == PositionConstants.NORTH)
			point.y -= offset;
		else if (direction == PositionConstants.WEST)
			point.x -= offset;
		else if (direction == PositionConstants.EAST)
			point.x += offset;
	}

	/**
	 * Based on the start and end points creates a list of points that will
	 * avoid overlapping with border item(s) owner figure(s). Writes the
	 * resultant points in <code>line</code>
	 * 
	 * @param startPoint
	 *            start point
	 * @param endPoint
	 *            end point
	 * @param sourceParentRect
	 *            source border item figure owner bounds
	 * @param targetParentRect
	 *            target border item figure owner bounds
	 * @param line
	 *            list of resultant points
	 * @param conn
	 *            connection
	 * @param offset
	 *            offset value
	 */
	private void avoidOverlappingWithParent(Point startPoint, Point endPoint,
			Rectangle sourceParentRect, Rectangle targetParentRect,
			PointList line, Connection conn, int offset) {
		// use GEF's shortest path router to get reference bend points
		ShortestPathRouter router = new ShortestPathRouter();
		Path path = new Path(startPoint, endPoint);
		router.addPath(path);
		if (sourceParentRect.contains(targetParentRect)) {
			router.addObstacle(targetParentRect);
		} else if (targetParentRect.contains(sourceParentRect)) {
			router.addObstacle(sourceParentRect);
		} else {
			router.addObstacle(sourceParentRect);
			router.addObstacle(targetParentRect);
		}
		router.setSpacing(offset);
		router.solve();
		line.removeAllPoints();
		line.addAll(path.getPoints());
	}

	/**
	 * Calculates and writes the list of bendpoints to <code>newLine</code>
	 * 
	 * Note: All geometric figures are in the same coordinate system (relative
	 * to connection figure usually)
	 * 
	 * @param sourceRect
	 *            source shape rectangle
	 * @param targetRect
	 *            target shape rectangle
	 * @param sourcePosition
	 *            geographic position of the source relative to its owner (if
	 *            source is a border item)
	 * @param targetPosition
	 *            geographic position of the target relative to its owner (if
	 *            target is a border item)
	 * @param sourceParentRect
	 *            owner of the source figure (if source is a border item)
	 * @param targetParentRect
	 *            owner of the target figure (if target is a border item)
	 * @param conn
	 *            connection
	 * @param newLine
	 *            the list for bendpoints
	 * @param offset
	 *            offset value
	 */
	private void getVerticesForBorderItemConnection(Rectangle sourceRect,
			Rectangle targetRect, int sourcePosition, int targetPosition,
			Rectangle sourceParentRect, Rectangle targetParentRect,
			Connection conn, PointList newLine, int offset) {
		if (sourcePosition != PositionConstants.NONE) {
			newLine.setPoint(getPointOffsetFromRectangle(sourceRect,
					sourcePosition, offset), 0);
		}

		if (targetPosition != PositionConstants.NONE) {
			newLine.setPoint(getPointOffsetFromRectangle(targetRect,
					targetPosition, offset), newLine.size() - 1);
		}

		/*
		 * For now will just avoid overlapping with owner figures. This is to be
		 * modified to support "Avoid Obstructions"
		 */
		avoidOverlappingWithParent(newLine.getFirstPoint(), newLine
				.getLastPoint(), sourceParentRect, targetParentRect, newLine,
				conn, offset);
		if (sourcePosition != PositionConstants.NONE) {
			newLine.insertPoint(getMiddlePointFromPosition(sourceRect,
					sourcePosition), 0);
		}

		if (targetPosition != PositionConstants.NONE) {
			newLine.addPoint(getMiddlePointFromPosition(targetRect,
					targetPosition));
		}
	}

	/**
	 * Checks whether the connection needs to routed by this class. The check is
	 * made base on the following criterias:
	 * <li>1. Source and/or target of the
	 * connection is a border item
	 * <li>2. If source and target intersect then the
	 * routing is done by {@link #checkShapesIntersect(Connection, PointList)}
	 * <li>3. There are no extra (introduced by user) bendpoints (i.e just source
	 * and target anchor points)
	 * <li>4. If a line connecting border item(s) intersect bounds of border item owner figure(s)
	 * 
	 * <p>If 1-4 are satisfied then connection bendpoints are calculated and
	 * <code>newLine</code> will containt the list of new bendpoints
	 * 
	 * <p>Note: the router currently does not support "Avoid Obstruction" and
	 * "Shortest Path" routings
	 * 
	 * @param conn
	 *            connection
	 * @param newLine
	 *            connection's list of points - modified by the method as needed
	 *            to return bendpoints for routed connection
	 * @return true if connection bendpoints got calculated (i.e. 1-4 are
	 *         satisfied)
	 */
	private boolean checkBorderItemConnection(Connection conn, PointList newLine) {
		IFigure source = conn.getSourceAnchor().getOwner();
		IFigure target = conn.getTargetAnchor().getOwner();

		int sourcePosition = getBorderFigurePosition(source);
		int targetPosition = getBorderFigurePosition(target);

		/*
		 * Criteria 1. Source and/or target of the connection is a border item
		 */
		if (sourcePosition == PositionConstants.NONE
				&& targetPosition == PositionConstants.NONE) {
			return false;
		}

		Rectangle sourceParentRect = getObstacle(source, conn,
				(sourcePosition != PositionConstants.NONE));
		Rectangle targetParentRect = getObstacle(target, conn,
				(targetPosition != PositionConstants.NONE));
		if (!sourceParentRect.equals(targetParentRect)) {
			if (sourceParentRect.contains(targetParentRect)) {
				sourcePosition = reversePosition(sourcePosition);
			} else if (targetParentRect.contains(sourceParentRect)) {
				targetPosition = reversePosition(targetPosition);
			}
		}

		// protection code to prevent NPE while creating the connection
		if (newLine == null) {
			return false;
		}

		PrecisionRectangle sourceRect = new PrecisionRectangle(source
				.getBounds());
		source.translateToAbsolute(sourceRect);
		conn.translateToRelative(sourceRect);
		PrecisionRectangle targetRect = new PrecisionRectangle(target
				.getBounds());
		target.translateToAbsolute(targetRect);
		conn.translateToRelative(targetRect);

		/*
		 * Criteria 2. Check if connection is between border item and the owner
		 * of the border item, which are intersecting
		 */
		if (sourceRect.intersects(targetRect)
				&& !sourceRect.contains(targetRect)
				&& !targetRect.contains(sourceRect)) {
			return false;
		}

		/*
		 * Criteria 3. There are no extra (introduced by user) bendpoints (i.e
		 * just source and target anchor points)
		 */
		if (newLine.size() < 3) {
			/*
			 * Criteria 4. If a line connecting border item(s) intersect bounds
			 * of border item owner figure(s)
			 */
			PrecisionPoint sourceAnchorPoint = new PrecisionPoint(conn
					.getSourceAnchor().getLocation(
							conn.getTargetAnchor().getReferencePoint()));
			PrecisionPoint targetAnchorPoint = new PrecisionPoint(conn
					.getTargetAnchor().getLocation(
							conn.getSourceAnchor().getReferencePoint()));
			conn.translateToRelative(sourceAnchorPoint);
			conn.translateToRelative(targetAnchorPoint);
			PointList connLine = new PointList();
			connLine.addPoint(sourceAnchorPoint);
			connLine.addPoint(targetAnchorPoint);
			boolean sourceOk = false, targetOk = false;
			sourceOk = targetOk = !sourceParentRect.equals(targetParentRect);
			sourceOk &= sourcePosition == PositionConstants.NONE
					|| sourceParentRect.contains(targetParentRect)
					|| (!sourceParentRect.contains(connLine.getFirstPoint()) && !PointListUtilities
							.findIntersections(connLine, PointListUtilities
									.createPointsFromRect(sourceParentRect),
									new PointList(), new PointList()));
			targetOk &= !sourceOk
					|| targetPosition == PositionConstants.NONE
					|| targetParentRect.contains(sourceParentRect)
					|| (!targetParentRect.contains(connLine.getLastPoint()) && !PointListUtilities
							.findIntersections(connLine, PointListUtilities
									.createPointsFromRect(targetParentRect),
									new PointList(), new PointList()));

			if (!sourceOk || !targetOk) {
				PrecisionPoint offsetPt = new PrecisionPoint();
				offsetPt.preciseX = OFFSET;
				offsetPt.updateInts();
				if (!RouterHelper.getInstance().isFeedback(conn)) {
					offsetPt = (PrecisionPoint) MapModeUtil.getMapMode(conn)
							.DPtoLP(offsetPt);
				}
				int offset = offsetPt.x;

				getVerticesForBorderItemConnection(sourceRect, targetRect,
						sourcePosition, targetPosition, sourceParentRect,
						targetParentRect, conn, newLine, offset);
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.draw2d.ui.internal.routers.ObliqueRouter#calculateBendPoints(org.eclipse.draw2d.Connection)
	 */
	protected PointList calculateBendPoints(Connection conn) {
		/*
		 * Do not support "Avoid Obstruction" and "Shortest Path" routings
		 */
		IFigure source = conn.getSourceAnchor().getOwner();
		IFigure target = conn.getTargetAnchor().getOwner();		
		if (source == null || target == null || isAvoidingObstructions(conn)
				|| isClosestDistance(conn)) {
			return super.calculateBendPoints(conn);
		}	
		
		/*
		 * Get bendpoints stored in the model first and based on the number of
		 * them and presence of border items either on the end or on the target
		 * of the connection decide whether the connection needs to be routed by
		 * this class
		 */
		PointList newLine = RouterHelper.getInstance()
				.routeFromConstraint(conn);
		if (checkBorderItemConnection(conn, newLine)) {
			return newLine;
		}
		return super.calculateBendPoints(conn);
	}
}
