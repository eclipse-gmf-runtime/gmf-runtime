/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.internal.routers;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.draw2d.ui.internal.Draw2dDebugOptions;
import org.eclipse.gmf.runtime.draw2d.ui.internal.Draw2dPlugin;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.jface.util.Assert;

/*
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.*
 */
public class ObliqueRouter extends BendpointConnectionRouter {

	static public class ArrayListMap {
		private HashMap map = new HashMap();

		public ArrayListMap() {
			super();
		}

		public ArrayList get(Object key) {
			Object value = map.get(key);
			if (value == null)
				return null;

			if (value instanceof ArrayList)
				return (ArrayList) value;
			ArrayList v = new ArrayList(1);
			v.add(value);
			return v;
		}

		public void put(Object key, Object value) {
			Object arrayListObject = map.get(key);
			if (arrayListObject == null) {
				map.put(key, value);
				return;
			}
			if (arrayListObject instanceof ArrayList) {
				ArrayList arrayList = (ArrayList) arrayListObject;
				if (!arrayList.contains(value))
					arrayList.add(value);
				return;
			}
			if (arrayListObject != value) {
				ArrayList arrayList = new ArrayList(2);
				arrayList.add(arrayListObject);
				arrayList.add(value);
				map.put(key, arrayList);
			}
		}

		public void remove(Object key, Object value) {
			Object arrayListObject = map.get(key);
			if (arrayListObject != null) {
				if (arrayListObject instanceof ArrayList) {
					ArrayList arrayList = (ArrayList) arrayListObject;
					arrayList.remove(value);
					if (arrayList.isEmpty())
						map.remove(key);
					return;
				}
				map.remove(key);
			}
		}

		public int size() {
			return map.size();
		} 
	}

	private ArrayListMap selfRelConnections = new ArrayListMap();
	private class ArrayListKey {

		private ConnectionAnchor connectAnchor1;
		private ConnectionAnchor connectAnchor2;

		ArrayListKey(Connection conn) {
			connectAnchor1 = conn.getSourceAnchor();
			connectAnchor2 = conn.getTargetAnchor();
		}

		public ConnectionAnchor getSourceAnchor() {
			return connectAnchor1;
		}

		public ConnectionAnchor getTargetAnchor() {
			return connectAnchor2;
		}

		public int hashCode() {
			return connectAnchor1.hashCode() ^ connectAnchor2.hashCode();
		}

		public boolean equals(Object object) {
			boolean isEqual = false;
			ArrayListKey listKey;

			if (object instanceof ArrayListKey) {
				listKey = (ArrayListKey) object;
				ConnectionAnchor lk1 = listKey.getSourceAnchor();
				ConnectionAnchor lk2 = listKey.getTargetAnchor();

				isEqual =
					(lk1.equals(connectAnchor1) && lk2.equals(connectAnchor2))
						|| (lk1.equals(connectAnchor2) && lk2.equals(connectAnchor1));
			}
			return isEqual;
		}
	}

	public static final int ROUTER_FLAG_SKIPNORMALIZATION = 1;

	protected int routerFlags;

	public ObliqueRouter() {
		routerFlags = 0;
	}

	/**
	 * Determines whether the router is going to avoid obstructions during the
	 * routing algorithm.
	 */
	public boolean isAvoidingObstructions(Connection conn) {
		if (conn instanceof PolylineConnectionEx) {
			return ((PolylineConnectionEx) conn).isAvoidObstacleRouting();
		}

		return false;
	}

	/**
	 * Determines whether the router is going use the closest distance during the
	 */
	public boolean isClosestDistance(Connection conn) {
		if (conn instanceof PolylineConnectionEx) {
			return ((PolylineConnectionEx) conn).isClosestDistanceRouting();
		}

		return false;
	}

	/**
	 * Check if this connection is currently being reoriented by seeing if the
	 * source or target owner are null.
	 */
	protected boolean isReorienting(Connection conn) {
		if (conn.getSourceAnchor().getOwner() == null
			|| conn.getTargetAnchor().getOwner() == null) {
			return true;
		}

		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.draw2d.ConnectionRouter#route(org.eclipse.draw2d.Connection)
	 */
	final public void route(Connection conn) {
		routeBendpoints(conn);
	}
	
	/**
	 * Route the connection accordingly to the router paradigm.
	 */
	public void routeBendpoints(Connection conn) {
		if ((conn.getSourceAnchor() == null)
			|| (conn.getTargetAnchor() == null))
			return;

        PointList points = new PointList();
             
		if (isAvoidingObstructions(conn)) {
            points = RouterHelper.getInstance().routeAroundObstructions(conn);	
		} 
        else if (isClosestDistance(conn)) {
            points = RouterHelper.getInstance().routeClosestDistance(conn);
        }
        else {
            points = RouterHelper.getInstance().routeFromConstraint(conn);
        }
        
        routeLine(conn, 0, points);
		conn.setPoints(points);
	}


	/**
	 * Method removePointsInViews.
	 * This method will parse through all the points in the given 
	 * polyline and remove any of the points that intersect with the 
	 * start and end figures.
	 * 
	 * @param conn Connection figure that is currently being routed
	 * @param newLine PointList that will contain the filtered list of points
     * @return boolean true if newLine points changed, false otherwise.
     * @throws IllegalArgumentException if either paramter is null.
	 */
	protected boolean removePointsInViews(
		Connection conn,
		PointList newLine) {
		
        boolean bChanged = false;
        
		// error checking
		if (conn == null || newLine == null ) {
			IllegalArgumentException iae = new IllegalArgumentException();
			Trace.throwing(
				Draw2dPlugin.getInstance(),
				Draw2dDebugOptions.EXCEPTIONS_THROWING,
				getClass(),
				"removePointsInViews()", //$NON-NLS-1$
				iae);
			throw iae;
		}
		
		// check whether the method should be executed.
		if (newLine.size() < 3)
			return false;
		if (conn.getSourceAnchor().getOwner() == null)
			return false;
		if (conn.getTargetAnchor().getOwner() == null)
			return false;
	
		Rectangle startRect = new Rectangle(conn.getSourceAnchor().getOwner().getBounds());
		conn.getSourceAnchor().getOwner().translateToAbsolute(startRect);
		conn.translateToRelative(startRect);

		Rectangle endRect = new Rectangle(conn.getTargetAnchor().getOwner().getBounds());
		conn.getTargetAnchor().getOwner().translateToAbsolute(endRect);
		conn.translateToRelative(endRect);

		// Ignore the first and last points
		PointList newPoints = new PointList(newLine.size());
		for (int i = 0; i < newLine.size(); i++) {
			
			Point pt = newLine.getPoint(i);
			if (i == 0 || i == newLine.size() - 1)
				newPoints.addPoint(pt);
			else if (!startRect.contains(pt) && !endRect.contains(pt)) {
				newPoints.addPoint(pt);
			}
            else {
                bChanged = true;
            }
		}

		if (newPoints.size() != newLine.size()) {
			newLine.removeAllPoints();
			for (int i = 0; i < newPoints.size(); i++)
				newLine.addPoint(new Point(newPoints.getPoint(i)));
		}
        
        return bChanged;
	}
	
	/**
	 * Helper method for "route" to just do the core routing of this router without any
	 * additional ideology (i.e. no closest distance, obstructions routing).
	 */
	public void routeLine(
		Connection conn,
		int nestedRoutingDepth,
		PointList newLine) {

		// get the original line	
		if (!checkSelfRelConnection(conn, newLine)) {
			removePointsInViews(conn, newLine);
		}
		resetEndPointsToEdge(conn, newLine);
	}

	/**
	 * getStraightEdgePoint
	 * Gets the anchored edge point that intersects with the given line segment.
	 * 
	 * @param ptEdge Point on the edge of the end shape in relative coordinates
	 * @param ptRef1 Point that is the first reference in relative coordinates
	 * @param ptRef2 Point that is the second reference in relative coordiantes
	 * @return Point that is the straight edge point in relative coordinates
	 */
	protected static Point getStraightEdgePoint(
		final Point ptEdge,
		final Point ptRef1,
		final Point ptRef2) {
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
	 * Reset the end points of the connection line to the appropriate anchor position on the start
	 * and end figures.
	 */
	protected void resetEndPointsToEdge(
    Connection conn,
    PointList newLine) {
		RouterHelper.getInstance().resetEndPointsToEdge(conn, newLine);
	}
	
	protected static final int SELFRELSIZEINIT = 62;
	protected static final int SELFRELSIZEINCR = 10;

	/**
	 * Method checkSelfRelConnection.
	 * Checks to see if this connection should be routed specially as a self relation.
	 * @param conn Connection to check if it's a self relation
	 * @param newLine PointList of the routed points
	 * @return boolean True if Connection is a self relation, False otherwise.
	 */
	protected boolean checkSelfRelConnection(
		Connection conn,
		PointList newLine) {
		if ((conn.getSourceAnchor().getOwner() == conn.getTargetAnchor()
			.getOwner())
			&& newLine.size() < 4) {
			getSelfRelVertices(conn, newLine);
			return true;
		} else {
			removeSelfRelConnection(conn);
			return false;
		}
	}

	/**
	 * Method removeSelfRelConnection.
	 * Removes the given connection from the self relation hash map
	 * @param conn Connection to remove from the map
	 */
	private void removeSelfRelConnection(Connection conn) {
		if (conn.getSourceAnchor() == null || conn.getTargetAnchor() == null
			|| conn.getSourceAnchor().getOwner() == null
			|| conn.getTargetAnchor().getOwner() == null)
			return;

		ArrayListKey connectionKey = new ArrayListKey(conn);
		ArrayList connectionList = selfRelConnections.get(connectionKey);
		if (connectionList != null) {
			int index = connectionList.indexOf(conn);
			if (index == -1)
				return;
			selfRelConnections.remove(connectionKey, conn);
		}
	}

	/**
	 * Method insertSelfRelVertices.
	 * This method will create a routed line that routes from and to the same figure.
	 * @param conn
	 * @param newLine
	 */
	protected void getSelfRelVertices(Connection conn, PointList newLine) {
		if (conn.getSourceAnchor().getOwner() == null)
			return;

		ArrayListKey connectionKey = new ArrayListKey(conn);
		int nSelfIncr = 0;
		int nIndex = 0;
		ArrayList connectionList = selfRelConnections.get(connectionKey);
		if (connectionList != null) {
			if (!connectionList.contains(conn)) {
				selfRelConnections.put(connectionKey, conn);
				connectionList = selfRelConnections.get(connectionKey);
			}

			nIndex = connectionList.indexOf(conn);
			Assert.isTrue(nIndex >= 0);
		} else {
			selfRelConnections.put(connectionKey, conn);
		}

		Dimension selfrelsizeincr = new Dimension(SELFRELSIZEINCR, 0);
		if (!RouterHelper.getInstance().isFeedback(conn))
			selfrelsizeincr = (Dimension)MapModeUtil.getMapMode(conn).DPtoLP(selfrelsizeincr);
		
		IFigure owner = conn.getSourceAnchor().getOwner();
		Rectangle bBox = owner.getClientArea();
		owner.translateToAbsolute(bBox);
		conn.translateToRelative(bBox);

		nSelfIncr = selfrelsizeincr.width * (nIndex / 8);
		newLine.removeAllPoints();

		switch (nIndex % 8) {
			case 0 :
				getCornerSelfRelVertices(conn, bBox, newLine,	nSelfIncr, 1, 1, bBox.getBottomRight());
				break;
			case 1 :
				getVerticalSelfRelVertices(conn, bBox, newLine, nSelfIncr, 1, bBox.getBottom());
				break;
			case 2 :
				getCornerSelfRelVertices(conn, bBox, newLine, nSelfIncr, -1, 1, bBox.getBottomLeft());
				break;
			case 3 :
				getHorizontalSelfRelVertices(conn, bBox, newLine, nSelfIncr, -1, bBox.getLeft());
				break;
			case 4 :
				getCornerSelfRelVertices(conn, bBox, newLine, nSelfIncr, -1, -1, bBox.getTopLeft());
				break;
			case 5 :
				getVerticalSelfRelVertices(conn, bBox, newLine, nSelfIncr, -1, bBox.getTop());
				break;
			case 6 :
				getCornerSelfRelVertices(conn, bBox, newLine, nSelfIncr, 1, -1, bBox.getTopRight());
				break;
			case 7 :
				getHorizontalSelfRelVertices(conn, bBox, newLine, nSelfIncr, 1, bBox.getRight());
				break;
		}
		
		// ensure that the end points are anchored properly to the shape.
		Point ptS2 = newLine.getPoint(0);
		Point ptS1 = conn.getSourceAnchor().getReferencePoint();
		conn.translateToRelative(ptS1);
		Point ptAbsS2 = new Point(ptS2);
		conn.translateToAbsolute(ptAbsS2);
		Point ptEdge = conn.getSourceAnchor().getLocation(ptAbsS2);
		conn.translateToRelative(ptEdge);
		ptS1 = getStraightEdgePoint(ptEdge, ptS1, ptS2);

		Point ptE2 = newLine.getPoint(newLine.size() - 1);
		Point ptE1 = conn.getTargetAnchor().getReferencePoint();
		conn.translateToRelative(ptE1);
		Point ptAbsE2 = new Point(ptE2);
		conn.translateToAbsolute(ptAbsE2);
		ptEdge = conn.getTargetAnchor().getLocation(ptAbsE2);
		conn.translateToRelative(ptEdge);
		ptE1 = getStraightEdgePoint(ptEdge, ptE1, ptE2);

		newLine.setPoint(ptS1, 0);
		newLine.setPoint(ptE1, newLine.size() - 1); 
	}

	/**
	 * Method getCornerSelfRelVertices.
	 * Retrieves the relation points for the self relation given a corner point and direction factors.
	 * @param bBox Rectangle representing the shape extents to create the self relation around.
	 * @param newLine PointList of the line to receive the new points
	 * @param nOffset Incremental offset of the self relation to prevent overlapping relations.
	 * @param nXDir int Direction (either -1, 1) indicating the horizontal growth direction 
	 * @param nYDir int Direction (either -1, 1) indicating the vertical growth direction 
	 * @param ptOrient Point which represents the starting location for the self relation to 
	 * grow from.
	 */
	private void getCornerSelfRelVertices(
		Connection conn,
		Rectangle bBox,
		PointList newLine,
		int nOffset,
		int nXDir,
		int nYDir,
		Point ptOrient) {

		int x = ptOrient.x;
		int y = bBox.getCenter().y + (nYDir * bBox.height / 4 );
		Point p1 = new Point(x, y);
		newLine.addPoint(p1);

		int xNew, yNew;

		Dimension selfrelsizeinit = new Dimension(SELFRELSIZEINIT, 0);
		if (!RouterHelper.getInstance().isFeedback(conn))
			selfrelsizeinit = (Dimension)MapModeUtil.getMapMode(conn).DPtoLP(selfrelsizeinit);
		
		xNew = x + (nXDir * (selfrelsizeinit.width + nOffset));
		Point p2 = new Point(xNew, y);
		newLine.addPoint(p2);

		yNew = ptOrient.y + (nYDir * (selfrelsizeinit.width + nOffset));
		Point p3 = new Point(xNew, yNew);
		newLine.addPoint(p3);

		xNew = ptOrient.x - (nXDir * bBox.width / 4);
		Point p4 = new Point(xNew, yNew);
		newLine.addPoint(p4);

		yNew = ptOrient.y;
		Point p5 = new Point(xNew, yNew);
		newLine.addPoint(p5);
	}

	/**
	 * Method getVerticalSelfRelVertices.
	 * @param bBox Rectangle representing the shape extents to create the self relation around.
	 * @param newLine PointList of the line to receive the new points
	 * @param nOffset Incremental offset of the self relation to prevent overlapping relations.
	 * @param nDir int Direction (either -1, 1) indicating the vertical growth direction 
	 * @param ptOrient Point which represents the starting location for the self relation to 
	 * grow from.
	 */
	private void getVerticalSelfRelVertices(
		Connection conn,
		Rectangle bBox,
		PointList newLine,
		int nOffset,
		int nDir,
		Point ptOrient) {
		
		int nWidth = bBox.width / 4;
		
		int x = ptOrient.x - nWidth / 2;
		int y = ptOrient.y;
		Point p1 = new Point(x, y);
		newLine.addPoint(p1);

		int xNew, yNew;

		Dimension selfrelsizeinit = new Dimension(SELFRELSIZEINIT, 0);
		if (!RouterHelper.getInstance().isFeedback(conn))
			selfrelsizeinit = (Dimension)MapModeUtil.getMapMode(conn).DPtoLP(selfrelsizeinit);
		
		yNew = y + (nDir * (selfrelsizeinit.width + nOffset));
		Point p2 = new Point(x, yNew);
		newLine.addPoint(p2);

		xNew = ptOrient.x + nWidth / 2;
		Point p3 = new Point(xNew, yNew);
		newLine.addPoint(p3);

		yNew = ptOrient.y;
		Point p4 = new Point(xNew, yNew);
		newLine.addPoint(p4);
	}
	
	/**
	 * Method getHorizontalSelfRelVertices.
	 * @param bBox Rectangle representing the shape extents to create the self relation around.
	 * @param newLine PointList of the line to receive the new points
	 * @param nOffset Incremental offset of the self relation to prevent overlapping relations.
	 * @param nDir int Direction (either -1, 1) indicating the horizontal growth direction 
	 * @param ptOrient Point which represents the starting location for the self relation to 
	 * grow from.
	 */
	private void getHorizontalSelfRelVertices(
		Connection conn,
		Rectangle bBox,
		PointList newLine,
		int nOffset,
		int nDir,
		Point ptOrient) {
		
		int nHeight = bBox.height / 4;
		
		int y = ptOrient.y - nHeight / 2;
		int x = ptOrient.x;
		Point p1 = new Point(x, y);
		newLine.addPoint(p1);

		int xNew, yNew;
		
		Dimension selfrelsizeinit = new Dimension(SELFRELSIZEINIT, 0);
		if (!RouterHelper.getInstance().isFeedback(conn))
			selfrelsizeinit = (Dimension)MapModeUtil.getMapMode(conn).DPtoLP(selfrelsizeinit);
		
		xNew = x + (nDir * (selfrelsizeinit.width + nOffset));
		Point p2 = new Point(xNew, y);
		newLine.addPoint(p2);

		yNew = ptOrient.y + nHeight / 2;
		Point p3 = new Point(xNew, yNew);
		newLine.addPoint(p3);

		xNew = ptOrient.x;
		Point p4 = new Point(xNew, yNew);
		newLine.addPoint(p4);
	}

	/**
	 * @see org.eclipse.draw2d.BendpointConnectionRouter#remove(Connection)
	 */
	public void remove(Connection connection) {
		super.remove(connection);
		
        RouterHelper.getInstance().remove(connection);
		removeSelfRelConnection(connection);
	}

	/* 
	 * Added to support GEF's shortest path routing
	 */
	public void invalidate(Connection connection) {
        super.invalidate(connection);
		RouterHelper.getInstance().invalidate(connection);
	}
    
    /**
     * Sets the constraint for the given {@link Connection}.
     *
     * @param connection The connection whose constraint we are setting
     * @param constraint The constraint
     */
    public void setConstraint(Connection connection, Object constraint) {
        super.setConstraint(connection, constraint);
        RouterHelper.getInstance().setConstraint(connection, constraint);
    }
}
