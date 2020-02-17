/******************************************************************************
 * Copyright (c) 2002, 2010 IBM Corporation and others.
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.Path;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.draw2d.ui.figures.IOvalAnchorableFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.IPolygonAnchorableFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.internal.Draw2dDebugOptions;
import org.eclipse.gmf.runtime.draw2d.ui.internal.Draw2dPlugin;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;

public class ObliqueRouter extends BendpointConnectionRouter {

	static public class ArrayListMap {
		private HashMap<Object, Object> map = new HashMap<Object, Object>();

		public ArrayListMap() {
			super();
		}

		public ArrayList<Object> get(Object key) {
			Object value = map.get(key);
			if (value == null)
				return null;

			if (value instanceof ArrayList)
				return (ArrayList<Object>) value;
			ArrayList<Object> v = new ArrayList<Object>(1);
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
				ArrayList<Object> arrayList = (ArrayList<Object>) arrayListObject;
				if (!arrayList.contains(value))
					arrayList.add(value);
				return;
			}
			if (arrayListObject != value) {
				ArrayList<Object> arrayList = new ArrayList<Object>(2);
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
	private ArrayListMap intersectingShapesConnections = new ArrayListMap();
	
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
        if (conn.isVisible())
            routeBendpoints(conn);
	}
	
	/**
	 * Route the connection accordingly to the router paradigm.
	 */
	public void routeBendpoints(Connection conn) {
		if ((conn.getSourceAnchor() == null)
			|| (conn.getTargetAnchor() == null))
			return;

        PointList points = calculateBendPoints(conn);
             
   		// points could be null if routing is already finished in calculateBendPoints
    	if (points != null) {
    		routeLine(conn, 0, points);
    		conn.setPoints(points);
    	}
	}
	
	/**
     * Return a point list that contains the bend points on the connections.
     * Clients can override this method to introduce calculated bend points 
     * on the connection 
	 * @param conn the connection to get the bend points for
	 * @return bend points as a Point List
	 */
	protected PointList calculateBendPoints(Connection conn) {
		RouterHelper helper = RouterHelper.getInstance();
        PointList points = null;    
    	boolean routed = false;    
    	if (isAvoidingObstructions(conn) && helper.getUseGEFRouter()) {
    		routed = routeAroundObstructions_GEF(conn);
    	}
    	if (!routed) {       
    		points = new PointList();
    		if (isAvoidingObstructions(conn)) {
    			points = helper.routeAroundObstructions(conn);
    		} else if (isClosestDistance(conn)) {
    			points = helper.routeClosestDistance(conn);
    		} else {
    			points = helper.routeFromConstraint(conn);
    		}
    	}
		return points;
	}
	
	/**
	 * Incorporating use of GEF's ShortestPathConnectionRouter into GMF's
	 * ObliqueRouter in order to enable instant re-routing when an obstacle is
	 * placed on or removed from a connection which has Avoid Obstacles property
	 * set. The rules for routing: 
	 * 
	 * <li>If the connection is completely within
	 * one container (doesn't matter if the container is nested), then GEF's
	 * router is used, meaning that the connection will be appropriately
	 * re-routed in case when an obstacle is being placed on the way. 
	 * 
	 * <li>If the connection spans between two containers, then this method returns null
	 * and the old GMF algorithm for avoiding obstructions is used (meaning that
	 * re-routing will not happen right away when an object is placed on the
	 * connection).
	 * 
	 * <p>
	 * Note that connection container may change by either attaching a
	 * connection anchor to a shape from a different container, or by moving
	 * source or target shape to a different container.
	 * 
	 * <p>
	 * Self connections and connections whose source and target intersect are dealt
	 * with in <code>routeLine</code> and avoid obstructions is irrelevant
	 * (just like when non-GEF avoid obstructions router is used). 
	 * 
	 * <p>
	 * Known issue: if an obstacle contains a connection start or
	 * end point, that obstacle is ignored, meaning that the connection can be
	 * routed through it.
	 * 
	 * @param conn
	 * @return true if routing was done by GEF algorithm, false otherwise
	 */
    public boolean routeAroundObstructions_GEF(Connection conn) {
    	RouterHelper helper = RouterHelper.getInstance();
		boolean routed = false;
		if (helper.getUseGEFRouter()) {
			ShortestPathConnectionRouter spcr = helper.getConnRouter(conn, true);
			if (spcr != null) {
				// Do routing only if spcr says there is something to route, or if
				// this is the first time conn is routed by spcr
				if (spcr.isDirty() || !spcr.containsConnection(conn)) {
					// add conn to spcr if needed
					// (in our case, spcr takes into account only end points, manual bendpoints
					// in constraint will be ignored)
					if (!spcr.containsConnection(conn)) {
						helper.setConstraint(spcr, conn, null);
					}									
					// spcr has to ignore invalidation of connections that are rooted as a
					// result of this call (otherwise, invalidation of those connections would dirty spcr
					// therefore causing routing to be done again)
					spcr.setIgnoreInvalidate(true);
					
					List<Path> allPaths = spcr.getPathsAfterRouting();
					if (allPaths != null && allPaths.size() > 0) {
						routed = true;
						IFigure container = helper.getSourceContainer(conn);
						// Source and target containers are the same (we know since GEF router is used)
						// Exception: user is moving a connection anchor and it is currently not inside any figure.						
						if (container == null) {
							container = helper.getTargetContainer(conn);
						}						
						if (container != null) { // should never be null at this point
							PointList points;
							for (int i = 0; i < allPaths.size(); i++) {
								Path path = allPaths.get(i);								
								Connection currentConn = (Connection) path.data;
								points = new PointList();
								// spcr needed path coordinates to be relative to the container.
								// Now translate them back to be relative to the connection.  
								for (int j = 0; j < path.getPoints().size(); j++) {
									PrecisionPoint pt = new PrecisionPoint(path.getPoints().getPoint(j));
									container.translateToAbsolute(pt);
									currentConn.translateToRelative(pt);
									points.addPoint(pt);
								}
								// Adjust start and end points, check for self connection, and source and target intersecting
								// Problem: framework is designed for connections to be routed one at the time, but GEF's 
								// algorithm routes several connections at once. Now we have to check if currentConn is routed 
								// by the same router as conn, it could be routed by child (e.g. rectilinear router), or conn could be 
								// routed by child and currentConn by this router. 
								ConnectionRouter currentConnRouter = currentConn.getConnectionRouter();
								if (!currentConnRouter.equals(conn.getConnectionRouter())) {
									if (currentConnRouter instanceof ObliqueRouter) { 
										((ObliqueRouter)currentConnRouter).routeLine(currentConn, 0, points);
									} else if ((currentConnRouter instanceof FanRouter) && 
											(((FanRouter)currentConnRouter).getRouter() instanceof ObliqueRouter)) {
										 // this handles the case when ObliqueRouter is delegate of FanRouter
										((ObliqueRouter)((FanRouter)currentConnRouter).getRouter()).routeLine(currentConn, 0, points);
									} else {		
										routeLine(currentConn, 0, points);
									}

									// (another way would be to revalidate currentConn so it would be routed by its own router
									// through the process of validation, but would have to prevent endless loop)									
								} else {		
									routeLine(currentConn, 0, points);
								}
								
								// Check if this path really changed or not. This check will reduce the number of 
								// revalidation calls, but it is not necessary (even if revalidation is called on a 
								// connection that didn't change, spcr.getIsDirty() will return false and routing will not happen)
								PointList oldPoints = currentConn.getPoints();
								boolean route = false;
								if (oldPoints == null || oldPoints.size() != points.size()
										|| (currentConn == conn )) {
									route = true;
								} else {
									for (int j = 0; j <= oldPoints.size() - 1; j++) {
										if (oldPoints.getPoint(j).x != points.getPoint(j).x
												|| oldPoints.getPoint(j).y != points.getPoint(j).y) {
											route = true;
											break;
										}
									}
								}
								if (route) {
									currentConn.setPoints(points);
									// don't revalidate conn since it already went through the whole process of layout
									if (conn != currentConn) {
										// Revalidate to ensure that currentConn goes through the complete layout.
										currentConn.revalidate();
									}
								}
							}
						}
					}
					spcr.setIgnoreInvalidate(false);
				} else {
					// There is nothing to route
					// Still, call setPoints since it ensures calculating bounds that may be needed later on
					conn.setPoints(conn.getPoints());
					routed = true;
				}
			}
		}
		return routed;
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
        
        IFigure sourceOwner = conn.getSourceAnchor().getOwner();
        IFigure targetOwner = conn.getTargetAnchor().getOwner();
		if (sourceOwner == null)
			return false;
		if (targetOwner == null)
			return false;
	
		PointList startPolygon = null;
        if (!(sourceOwner instanceof Connection)) {
            startPolygon = getFigurePolygon(sourceOwner,conn);
        }

        PointList endPolygon = null;
        if (!(targetOwner instanceof Connection)) {
            endPolygon = getFigurePolygon(targetOwner,conn);
        }

		// Ignore the first and last points
		PointList newPoints = new PointList(newLine.size());
		for (int i = 0; i < newLine.size(); i++) {
			
			Point pt = newLine.getPoint(i);
			if (i == 0 || i == newLine.size() - 1)
				newPoints.addPoint(pt);
			else if ((startPolygon == null || !PointListUtilities.containsPoint(startPolygon,pt))
                && (endPolygon == null || !PointListUtilities.containsPoint(endPolygon,pt))) {
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
    
     protected PointList getFigurePolygon(IFigure owner, Connection conn) {
        PointList polygon = new PointList();
        if (owner instanceof IOvalAnchorableFigure) {
        	Rectangle rect = new PrecisionRectangle(((IOvalAnchorableFigure)owner).getOvalBounds());
        	owner.translateToAbsolute(rect);
        	conn.translateToRelative(rect);
            polygon.addPoint(rect.getTopLeft());
            polygon.addPoint(rect.getTopRight());
            polygon.addPoint(rect.getBottomRight());
            polygon.addPoint(rect.getBottomLeft());
            polygon.addPoint(rect.getTopLeft());
        } else if (owner instanceof IPolygonAnchorableFigure){
            PointList points =  ((IPolygonAnchorableFigure)owner).getPolygonPoints();
            for(int index = 0 ; index < points.size(); index++){
                Point point  = points.getPoint(index).getCopy();
                owner.translateToAbsolute(point);
                conn.translateToRelative(point);
                polygon.addPoint(point);
            }
        } else {
           Rectangle rect =  new PrecisionRectangle(owner.getBounds());
           owner.translateToAbsolute(rect);
           conn.translateToRelative(rect);
           polygon.addPoint(rect.getTopLeft());
           polygon.addPoint(rect.getTopRight());
           polygon.addPoint(rect.getBottomRight());
           polygon.addPoint(rect.getBottomLeft());
           polygon.addPoint(rect.getTopLeft());
        }
        return polygon;
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
		if (!checkSelfRelConnection(conn, newLine) && !checkShapesIntersect(conn, newLine)) {
			removePointsInViews(conn, newLine);
		}
		resetEndPointsToEdge(conn, newLine);
	}
	
	/**
	 * Checks if source shape and target shape of the connection intersect (only intersect - not one contained in another)
	 * and if they are calculates the bendpoints for the connection. Calculated bendpoints are stored in <code>newLine</code>.
	 * Initially <code>newLine</code> contains the list of bendpoints calculated by the router, however
	 * if for intersecting shapes we have a default connection (i.e. no extra bendpoints), bendpoints will
	 * be calculated and <code>newLine</code> will be cleared and calculated bendpoints will be stored there.
	 * 
	 * Criterias for calculation of bendpoints for connection between 2 intersecting shapes:
	 * 1. No extra bendpoints introduced by user (only source and target anchor points present)
	 * 2. Source and target shapes intersect (one contained in another = do not intersect) 
	 * 
	 * @param conn connection
	 * @param newLine list to store calculated bendpoints (contains bendpoints read from the model initially
	 * @return <code>true</code> if bendpoints were calculated here for intersecting shapes
	 */
	protected boolean checkShapesIntersect(Connection conn, PointList newLine) {
		if (conn.getSourceAnchor().getOwner() == null 
				|| conn.getSourceAnchor().getOwner() instanceof Connection
				|| conn.getTargetAnchor().getOwner() == null
				|| conn.getTargetAnchor().getOwner() instanceof Connection)
			return false;
		
		if (newLine.size() < 3) {
			PrecisionRectangle sourceBounds = getShapeBounds(conn.getSourceAnchor().getOwner()); 
			PrecisionRectangle targetBounds = getShapeBounds(conn.getTargetAnchor().getOwner());
			conn.getSourceAnchor().getOwner().translateToAbsolute(sourceBounds);
			conn.getTargetAnchor().getOwner().translateToAbsolute(targetBounds);
			if (sourceBounds.intersects(targetBounds) && !sourceBounds.contains(targetBounds) && !targetBounds.contains(sourceBounds)
					|| sourceBounds.equals(targetBounds)) {
				getVerticesForIntersectingShapes(conn, newLine);
				return true;
			}
		} else {
			removeIntersectingShapesConnection(conn);
		}
		return false;
	}
	
	private PrecisionRectangle getShapeBounds(IFigure figure) {
		if (figure instanceof IOvalAnchorableFigure) {
			return new PrecisionRectangle(((IOvalAnchorableFigure) figure)
					.getOvalBounds());
		} else if (figure instanceof IPolygonAnchorableFigure) {
			return new PrecisionRectangle(((IPolygonAnchorableFigure) figure)
					.getPolygonPoints().getBounds());
		}
		return new PrecisionRectangle(figure.getBounds());

	}
	
	/**
	 * Determines geographic position of the source figure relative to the
	 * connection area
	 * 
	 * @param connRectangle
	 *            connection area
	 * @param sourceRect
	 *            bounds of the source figure
	 * @param position
	 *            geographic position of the connection area relative to the
	 *            union of intersecting source and target figures
	 * @return geographic position of the source figure relative to the
	 *         connection area
	 */
	private int getSourcePositionFromConnectionRectangle(
			Rectangle connRectangle, Rectangle sourceRect, int position) {
		Dimension diff = null;
		switch (position) {
		case PositionConstants.NORTH_WEST:
			diff = connRectangle.getBottomRight().getDifference(
					sourceRect.getTopLeft());
			if (diff.width == 0) {
				return PositionConstants.EAST;
			} else {
				return PositionConstants.SOUTH;
			}
		case PositionConstants.NORTH_EAST:
			diff = connRectangle.getBottomLeft().getDifference(
					sourceRect.getTopRight());
			if (diff.width == 0) {
				return PositionConstants.WEST;
			} else {
				return PositionConstants.SOUTH;
			}
		case PositionConstants.SOUTH_EAST:
			diff = connRectangle.getTopLeft().getDifference(
					sourceRect.getBottomRight());
			if (diff.width == 0) {
				return PositionConstants.WEST;
			} else {
				return PositionConstants.NORTH;
			}
		case PositionConstants.SOUTH_WEST:
			diff = connRectangle.getTopRight().getDifference(
					sourceRect.getBottomLeft());
			if (diff.width == 0) {
				return PositionConstants.EAST;
			} else {
				return PositionConstants.NORTH;
			}
		case PositionConstants.NONE:
			diff = connRectangle.getCenter().getDifference(sourceRect.getCenter());
			if (diff.width == 0) {
				return diff.height < 0 ? PositionConstants.SOUTH : PositionConstants.NORTH;  
			} else {
				return diff.width < 0 ? PositionConstants.EAST : PositionConstants.WEST;
			}
		}
		return PositionConstants.NONE;
	}
	
	/**
	 * Stores bendpoints for the connection in <code>line</code> based on the
	 * precise connection area, geographic position of the source figure
	 * relative to the connection area and geographic position of the connection
	 * area relative to the union of intersecting shapes
	 * 
	 * @param connRect
	 *            precise connection area
	 * @param position
	 *            geographic position of the connection area relative to the
	 *            union of intersecting shapes
	 * @param sourcePosition
	 *            geographic position of the source figure relative to the
	 *            connection area
	 * @param line
	 *            list for storing bendpoints (cleared at the start)
	 */
	private void getConnectionPoints(Rectangle connRect, int position,
			int sourcePosition, PointList line) {
		line.removeAllPoints();
		switch (position) {
		case PositionConstants.NORTH_WEST:
			if (sourcePosition == PositionConstants.EAST) {
				line.addPoint(connRect.getTopRight());
				line.addPoint(connRect.getTopLeft());
				line.addPoint(connRect.getBottomLeft());
			} else {
				line.addPoint(connRect.getBottomLeft());
				line.addPoint(connRect.getTopLeft());
				line.addPoint(connRect.getTopRight());
			}
			break;
		case PositionConstants.NORTH_EAST:
			if (sourcePosition == PositionConstants.WEST) {
				line.addPoint(connRect.getTopLeft());
				line.addPoint(connRect.getTopRight());
				line.addPoint(connRect.getBottomRight());
			} else {
				line.addPoint(connRect.getBottomRight());
				line.addPoint(connRect.getTopRight());
				line.addPoint(connRect.getTopLeft());
			}
			break;
		case PositionConstants.SOUTH_EAST:
			if (sourcePosition == PositionConstants.WEST) {
				line.addPoint(connRect.getBottomLeft());
				line.addPoint(connRect.getBottomRight());
				line.addPoint(connRect.getTopRight());
			} else {
				line.addPoint(connRect.getTopRight());
				line.addPoint(connRect.getBottomRight());
				line.addPoint(connRect.getBottomLeft());
			}
			break;
		case PositionConstants.SOUTH_WEST:
			if (sourcePosition == PositionConstants.EAST) {
				line.addPoint(connRect.getBottomRight());
				line.addPoint(connRect.getBottomLeft());
				line.addPoint(connRect.getTopLeft());
			} else {
				line.addPoint(connRect.getTopLeft());
				line.addPoint(connRect.getBottomLeft());
				line.addPoint(connRect.getBottomRight());
			}
			break;
		case PositionConstants.NONE:
			if (sourcePosition == PositionConstants.NORTH) {
				line.addPoint(connRect.getTopLeft());
				line.addPoint(connRect.getBottomLeft());
			} else if (sourcePosition == PositionConstants.SOUTH) {
				line.addPoint(connRect.getBottomLeft());
				line.addPoint(connRect.getTopLeft());
			} else if (sourcePosition == PositionConstants.WEST) {
				line.addPoint(connRect.getTopLeft());
				line.addPoint(connRect.getTopRight());
			} else {
				line.addPoint(connRect.getTopRight());
				line.addPoint(connRect.getTopLeft());
			}
		}
	}
	
	/**
	 * Transforms width and height of the dimension into absolute values
	 * 
	 * @param d
	 *            dimension
	 */
	private void absDimension(Dimension d) {
		d.width = Math.abs(d.width);
		d.height = Math.abs(d.height);
	}
	
	/**
	 * Calculates and stores bendpoints (or vertices) for the connection between
	 * 2 intersecting shapes and stores them in <code>newLine</code>
	 * 
	 * @param conn
	 *            connection
	 * @param newLine
	 *            list to store calculated bendpoints (oe vertices)
	 */
	private void getVerticesForIntersectingShapes(Connection conn,
			PointList newLine) {
		Object key = getIntersectingShapesConnectionKey(conn);
		int nSelfIncr = 0;
		int nIndex = 0;
		/*
		 * Check if this connection is 2nd, 3rd, ..., or n-th connection between
		 * the same 2 intersecting shapes. If yes, determine what's the index.
		 * (i.e the n>1)
		 */
		ArrayList<Object> connectionList = intersectingShapesConnections.get(key);
		if (connectionList != null) {
			if (!connectionList.contains(conn)) {
				intersectingShapesConnections.put(key, conn);
				connectionList = intersectingShapesConnections.get(key);
			}

			nIndex = connectionList.indexOf(conn);
			assert nIndex >= 0;
		} else {
			intersectingShapesConnections.put(key, conn);
		}

		/*
		 * Translate properly the default offset value between multiple
		 * connections connecting the same 2 intersecting shapes. The default
		 * value is in pixels, hence for feedback connection it must stay the
		 * same and translated to logical units otherwise.
		 */
		PrecisionPoint selfrelsizeincr = new PrecisionPoint(SELFRELSIZEINCR, 0);
		boolean isFeedbackConn = RouterHelper.getInstance().isFeedback(conn);
		if (!isFeedbackConn)
			selfrelsizeincr = (PrecisionPoint) MapModeUtil.getMapMode(conn)
					.DPtoLP(selfrelsizeincr);

		/*
		 * Translate bounds of the source and target figures into coordinates
		 * relative to the connection figure. (PrecisionRectangle is used to
		 * avoid precision losses during non-integer scaling) Also calculate the
		 * union of the source and target figures bounds and their intersection
		 * rectangle for further calculations. All geometric figures are
		 * translated to the coordinates relative to the connection figure!
		 */
		IFigure sourceFig = conn.getSourceAnchor().getOwner();
		PrecisionRectangle sourceRect = getShapeBounds(sourceFig);
		sourceFig.translateToAbsolute(sourceRect);
		conn.translateToRelative(sourceRect);

		IFigure targetFig = conn.getTargetAnchor().getOwner();
		PrecisionRectangle targetRect = getShapeBounds(targetFig);
		targetFig.translateToAbsolute(targetRect);
		conn.translateToRelative(targetRect);
		PrecisionRectangle union = sourceRect.getPreciseCopy()
				.union(targetRect);

		/*
		 * Calculate the final offset value to space out multiple connections
		 * between 2 intersecting shapes
		 */
		nSelfIncr = selfrelsizeincr.x * (nIndex);

		Rectangle intersection = sourceRect.getCopy().intersect(targetRect);

		/*
		 * Determine the rough connection area and its geographic position
		 * relative to the union of the intersecting shapes. This is the area
		 * around which the connection will be routed. It's rough because it
		 * will be expanded and spaced out from other connections connecting the
		 * same shapes. The rough connection area is the smallest blank
		 * rectangle located within the union rectangle but not intersecting
		 * both source and traget figures bounds. The possible geographic
		 * locations for connection area are: NW, NE, SW, SE.
		 */
		Rectangle connArea = new Rectangle();
		int position = PositionConstants.NONE;
		int minArea = 0;
		Point unionTopLeft = union.getTopLeft();
		Point unionTopRight = union.getTopRight();
		Point unionBottomRight = union.getBottomRight();
		Point unionBottomLeft = union.getBottomLeft();

		if (!unionTopLeft.equals(sourceRect.getTopLeft())
				&& !unionTopLeft.equals(targetRect.getTopLeft())) {
			Dimension diffVector = unionTopLeft.getDifference(intersection
					.getTopLeft());
			absDimension(diffVector);
			int areaTopLeft = diffVector.getArea();
			if (minArea == 0 || minArea > areaTopLeft) {
				position = PositionConstants.NORTH_WEST;
				connArea.setSize(diffVector);
				connArea.setLocation(unionTopLeft.x, unionTopLeft.y);
				minArea = areaTopLeft;
			}
		}

		if (!unionTopRight.equals(sourceRect.getTopRight())
				&& !unionTopRight.equals(targetRect.getTopRight())) {
			Dimension diffVector = unionTopRight.getDifference(intersection
					.getTopRight());
			absDimension(diffVector);
			int areaTopRight = diffVector.getArea();
			if (minArea == 0 || minArea > areaTopRight) {
				position = PositionConstants.NORTH_EAST;
				connArea.setSize(diffVector);
				connArea.setLocation(unionTopRight.x - connArea.width,
						unionTopRight.y);
				minArea = areaTopRight;
			}
		}

		if (!unionBottomRight.equals(sourceRect.getBottomRight())
				&& !unionBottomRight.equals(targetRect.getBottomRight())) {
			Dimension diffVector = unionBottomRight.getDifference(intersection
					.getBottomRight());
			absDimension(diffVector);
			int areaBottomRight = diffVector.getArea();
			if (minArea == 0 || minArea > areaBottomRight) {
				position = PositionConstants.SOUTH_EAST;
				connArea.setSize(diffVector);
				connArea.setLocation(unionBottomRight.x - connArea.width,
						unionBottomRight.y - connArea.height);
				minArea = areaBottomRight;
			}
		}

		if (!unionBottomLeft.equals(sourceRect.getBottomLeft())
				&& !unionBottomLeft.equals(targetRect.getBottomLeft())) {
			Dimension diffVector = unionBottomLeft.getDifference(intersection
					.getBottomLeft());
			absDimension(diffVector);
			int areaBottomLeft = diffVector.getArea();
			if (minArea == 0 || minArea > areaBottomLeft) {
				position = PositionConstants.SOUTH_WEST;
				connArea.setSize(diffVector);
				connArea.setLocation(unionBottomLeft.x, unionBottomLeft.y
						- connArea.height);
				minArea = areaBottomLeft;
			}
		}
		
		if (position == PositionConstants.NONE) {
			connArea = intersection;
		}

		/*
		 * Determine the geographic position of the source figure relative to
		 * the rough connection area. This will help determining the order for
		 * bendpoints list from the precise connection area
		 */
		int sourcePosition = getSourcePositionFromConnectionRectangle(connArea,
				sourceRect, position);

		if (position != PositionConstants.NONE) {
			/*
			 * Determine the value by which the connection area has to become
			 * primary precise connection area. The value is chosen to be such that
			 * connections made from shapes intersecting on the same edge don't
			 * overlap
			 */
			PrecisionPoint translateExpansion = new PrecisionPoint(Math.max(connArea.width,
					connArea.height), 0);
			if (!isFeedbackConn) {
				IMapMode mm = MapModeUtil.getMapMode(conn);
				translateExpansion = (PrecisionPoint) mm.LPtoDP(translateExpansion);
				translateExpansion.preciseX = Math.pow(translateExpansion.preciseX,
						0.8);
				translateExpansion = (PrecisionPoint) mm.DPtoLP(translateExpansion);
			} else {
				translateExpansion.preciseX = Math.pow(translateExpansion.preciseX,
						0.8);
			}
			translateExpansion.updateInts();
	
			/*
			 * Transform rough connection area to primary precise connection area
			 */
			getPrimaryPreciseConnectionArea(connArea, translateExpansion.x, position);
		} else {
			connArea.expand(selfrelsizeincr.x<<1, selfrelsizeincr.x<<1);
		}

		/*
		 * Transform the primary precise connection area to precise connection
		 * area by accounting for multiple connection between the same 2
		 * intersecting shapes
		 */
		connArea.expand(nSelfIncr, nSelfIncr);

		/*
		 * Calculates the bendpoints for the connection from the precise
		 * connection area
		 */
		getConnectionPoints(connArea, position, sourcePosition, newLine);

		PrecisionPoint ptS2 = new PrecisionPoint(newLine.getPoint(0));
		PrecisionPoint ptS1 = new PrecisionPoint(conn.getSourceAnchor().getReferencePoint());
		conn.translateToRelative(ptS1);
		Point ptAbsS2 = new Point(ptS2);
		conn.translateToAbsolute(ptAbsS2);
		PrecisionPoint ptEdge = new PrecisionPoint(conn.getSourceAnchor().getLocation(ptAbsS2));
		conn.translateToRelative(ptEdge);
		ptS1 = new PrecisionPoint(getStraightEdgePoint(ptEdge, ptS1, ptS2));

		PrecisionPoint ptE2 = new PrecisionPoint(newLine.getPoint(newLine.size() - 1));
		PrecisionPoint ptE1 = new PrecisionPoint(conn.getTargetAnchor().getReferencePoint());
		conn.translateToRelative(ptE1);
		PrecisionPoint ptAbsE2 = (PrecisionPoint)ptE2.getCopy();
		conn.translateToAbsolute(ptAbsE2);
		ptEdge = new PrecisionPoint(conn.getTargetAnchor().getLocation(ptAbsE2));
		conn.translateToRelative(ptEdge);
		ptE1 = new PrecisionPoint(getStraightEdgePoint(ptEdge, ptE1, ptE2));

		newLine.insertPoint(new Point(Math.round(ptS1.preciseX), Math.round(ptS1.preciseY)), 0);
		newLine.insertPoint(new Point(Math.round(ptE1.preciseX), Math.round(ptE1.preciseY)), newLine.size());

	}
	
	/**
	 * Transforms rough connection area into primary precise connection area.
	 * Primary precise connection area is the one that doesn't account for
	 * multiple connections between same intersecting shapes
	 * 
	 * @param r
	 *            rough connection area rectangle
	 * @param size
	 *            size used for expansion
	 * @param positionOfConnArea
	 *            geographic position of the connection area relative to the
	 *            union of intersecting shapes
	 */
	private void getPrimaryPreciseConnectionArea(Rectangle r, int size, int positionOfConnArea) {
		r.expand(size, size);
		if (r.width < r.height) {
			r.height -= size;
			if ((positionOfConnArea & PositionConstants.SOUTH) != 0) {
				r.y += size;
			}
		} else {
			r.width -= size;
			if ((positionOfConnArea & PositionConstants.EAST) != 0) {
				r.x += size;
			}
		}
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
		ArrayList<Object> connectionList = selfRelConnections.get(connectionKey);
		if (connectionList != null) {
			int index = connectionList.indexOf(conn);
			if (index == -1)
				return;
			selfRelConnections.remove(connectionKey, conn);
		}
	}
	
	/**
	 * Method removeIntersectingShapesConnection.
	 * Removes the given connection from the intersecting shapes connections hash map
	 * @param conn Connection to remove from the map
	 */
	private void removeIntersectingShapesConnection(Connection conn) {
		if (conn.getSourceAnchor() == null || conn.getTargetAnchor() == null
				|| conn.getSourceAnchor().getOwner() == null
				|| conn.getTargetAnchor().getOwner() == null)
				return;
		Object key = getIntersectingShapesConnectionKey(conn);
		ArrayList<Object> connectionList = intersectingShapesConnections.get(key);
		if (connectionList != null) {
			int index = connectionList.indexOf(conn);
			if (index == -1)
				return;
			intersectingShapesConnections.remove(key, conn);
		}
	}
	
	/**
	 * Calculates the key for a connection made between 2 intersecting shapes.
	 * Key is determined from the key of the source and target figures hash
	 * codes, since we want connections made between the same 2 intersected
	 * shapes to be mapped to one value
	 * 
	 * @param conn
	 *            connection
	 * @return hash code
	 */
	private Object getIntersectingShapesConnectionKey(Connection conn) {
		return new Integer(conn.getSourceAnchor().getOwner().hashCode()
				^ conn.getTargetAnchor().getOwner().hashCode());
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
		ArrayList<Object> connectionList = selfRelConnections.get(connectionKey);
		if (connectionList != null) {
			if (!connectionList.contains(conn)) {
				selfRelConnections.put(connectionKey, conn);
				connectionList = selfRelConnections.get(connectionKey);
			}

			nIndex = connectionList.indexOf(conn);
            assert nIndex >= 0;
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
		removeIntersectingShapesConnection(connection);
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
