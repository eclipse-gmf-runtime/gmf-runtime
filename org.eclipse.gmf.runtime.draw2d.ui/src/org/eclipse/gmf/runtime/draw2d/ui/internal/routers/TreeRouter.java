/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.draw2d.ui.internal.routers;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.draw2d.ui.internal.Draw2dDebugOptions;
import org.eclipse.gmf.runtime.draw2d.ui.internal.Draw2dPlugin;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;


/**
 * @author sshaw
 *
 * This class is a top level router for managing the individual branches in a set of
 * tree routed connections.
 */
public class TreeRouter extends BendpointConnectionRouter implements OrthogonalRouter {

	private BranchRouter branchRouter = new BranchRouter(this);
	private ArrayList connectionList = new ArrayList();
	private Dimension trunkVertex;
	private Orientation trunkOrientation;
	private boolean updatingPeers = false;
	
	static private class Orientation {

		private Orientation() {
			// Empty constructor
		}
		
		/**
		 * Constant for the top orientation 
		 */
		static public Orientation TOP = new Orientation();//$NON-NLS-1$
		
		/**
		 * Constant for the bottom orientation 
		 */
		static public Orientation BOTTOM = new Orientation();//$NON-NLS-1$
		
		/**
		 * Constant for the right orientation 
		 */
		static public Orientation RIGHT = new Orientation(); //$NON-NLS-1$
		
		/**
		 * Constant for the left orientation 
		 */
		static public Orientation LEFT = new Orientation(); //$NON-NLS-1$
		
		/**
		 * getEdge
		 * Method to return the edge point of the given Rectangle representative
		 * of the orientation value of the instance.
		 * 
		 * @param bounds Rectangle to retrieve the edge value from.
		 * @return Point that is the edge of the rectangle for the orientation of this.
		 */
		public Point getEdge(Rectangle bounds) {
			if (this == TOP)
				return bounds.getTop();
			else if (this == BOTTOM)
				return bounds.getBottom();
			else if (this == RIGHT)
				return bounds.getRight();

			return bounds.getLeft();
		}
	}
	
	/**
	 * 
	 */
	public TreeRouter() {
		super();
	}

	
	/**
	 * @see org.eclipse.draw2d.ConnectionRouter#invalidate(Connection)
	 */
	public void invalidate(Connection conn) {
		if (conn.getSourceAnchor() == null || conn.getSourceAnchor().getOwner() == null ||
			conn.getTargetAnchor() == null || conn.getTargetAnchor().getOwner() == null)
			return;

		ListIterator li = connectionList.listIterator();
		while (li.hasNext()) {
			Connection connNext = (Connection)li.next();
			
			if (!trunkVertexEqual(connNext, conn)) {
				updateConstraint(connNext);
			}
		}
	}
	
	private boolean trunkVertexEqual(Connection connMaster, Connection connSlave) {
		PointList cmPts = connMaster.getPoints();
		PointList csPts = connSlave.getPoints();
		if (cmPts.size() > 2 && csPts.size() > 2)
			return cmPts.getPoint(2).equals(csPts.getPoint(2));
		
		return false;
	}
	
	/**
	 * getTrunkLocation
	 * Method to retrieve the trunk location in relative coordinates based on 
	 * current tree state.
	 * 
	 * @param conn Connection being routed
	 * @return Point that is the trunk location in relative coordinates.
	 */
	public Point getTrunkLocation(Connection conn) {
		Dimension vertex = getTrunkVertex();
		Point target = getTrunkOrientation().getEdge(conn.getTargetAnchor().getOwner().getBounds());

		Point ptTrunkLoc = new Point(vertex.width, vertex.height);
		ptTrunkLoc = ptTrunkLoc.getTranslated(target);
		
		return ptTrunkLoc;
	}
	
	/**
	 * setTrunkLocation
	 * Setter method to set the trunk location.  Translates the point into a relative
	 * point from the target edge.
	 * 
	 * @param conn Connection being routed
	 * @param ptTrunkLoc Point that is the trunk location in relative coordinates.
	 */
	public void setTrunkLocation(Connection conn, Point ptTrunkLoc) {
		Point ptRelTrunkLoc = new Point(ptTrunkLoc);
		
		// update orientation
		if (isTopDown(conn)) {
			if (ptTrunkLoc.y < conn.getTargetAnchor().getOwner().getBounds().getCenter().y)
				setTrunkOrientation(Orientation.TOP);
			else
				setTrunkOrientation(Orientation.BOTTOM);
		}
		else {
			if (ptTrunkLoc.x < conn.getTargetAnchor().getOwner().getBounds().getCenter().x)
				setTrunkOrientation(Orientation.LEFT);
			else
				setTrunkOrientation(Orientation.RIGHT);
		}

		Point target = getTrunkOrientation().getEdge(conn.getTargetAnchor().getOwner().getBounds());
		
		Dimension currentVertex = ptRelTrunkLoc.getDifference(target);
		setTrunkVertex(currentVertex);
	}
	
	/**
	 * updateConstraint
	 * Updates the constraint value for the connection based on the tree vertex
	 *
	 * @param conn Connection whose constraint is to be updated.
	 */
	protected void updateConstraint(Connection conn) {
		if (isUpdatingPeers())
			return;
		
		List bendpoints = (List)conn.getRoutingConstraint(); 
		if (bendpoints == null)
			bendpoints = new ArrayList(conn.getPoints().size());
		
		if (bendpoints != null) {
			Point sourceRefPoint = conn.getSourceAnchor().getReferencePoint();
			conn.translateToRelative(sourceRefPoint);

			Point targetRefPoint = conn.getTargetAnchor().getReferencePoint();
			conn.translateToRelative( targetRefPoint);

			Point ptTrunk = getTrunkLocation(conn);
			Point ptSource = getBranchRouter().getSourceLocation(conn, ptTrunk);
			
			bendpoints.clear();
			PointList pts = getBranchRouter().recreateBranch(conn, ptSource, ptTrunk);
			for (int i=0; i<pts.size(); i++) {
				Bendpoint bp = new AbsoluteBendpoint(pts.getPoint(i));
				bendpoints.add(bp);
			}
		}
		
		setUpdatingPeers(true);
		
		try {
			setConstraint(conn, bendpoints);
			conn.invalidate();
			conn.validate();
		}
		catch (Exception e) {
			Trace.catching(Draw2dPlugin.getInstance(), Draw2dDebugOptions.EXCEPTIONS_CATCHING, TreeRouter.class, "updateConstraint", //$NON-NLS-1$
				e);
		}
		finally {
			setUpdatingPeers(false);
		}
	}
	
	/**
	 * getPointsFromConstraint
	 * Utility method retrieve the PointList equivalent of the bendpoint constraint
	 * set in the Connection.
	 * 
	 * @param conn Connection to retrieve the constraint from.
	 * @return PointList list of points that is the direct equivalent of the set constraint.
	 */
	public PointList getPointsFromConstraint(Connection conn) {
		List bendpoints = (List)conn.getRoutingConstraint();
		if (bendpoints == null)
			return new PointList();
		
		PointList points = new PointList(bendpoints.size());
		for (int i = 0; i < bendpoints.size(); i++) {
			Bendpoint bp = (Bendpoint) bendpoints.get(i);
			points.addPoint(bp.getLocation());
		}
		
		straightenPoints(points, MapMode.DPtoLP(3));
		return points;
	}
	
    /**
     * straightenPoints
     * This is a simpler version of the @see updateIfNotRectilinear that simply ensures
     * that the lines are horizontal or vertical without any intelligence in terms of 
     * shortest distance around a rectangle.
     * 
	 * @param newLine PointList to check for rectilinear qualities and change if necessary.
	 * @param tolerance int tolerance value by which points will be straightened in HiMetrics
	 */
	static protected void straightenPoints(PointList newLine, int tolerance) {
        for (int i=0; i<newLine.size()-1; i++) {
            Point ptCurrent = newLine.getPoint(i);
            Point ptNext = newLine.getPoint(i+1);
            
            int xDelta = Math.abs(ptNext.x - ptCurrent.x);
            int yDelta = Math.abs(ptNext.y - ptCurrent.y);
            
            if (xDelta < yDelta) {
            	if (xDelta > tolerance)
            		return;
                ptNext.x = ptCurrent.x;
            } else {
            	if (yDelta > tolerance)
            		return;
                ptNext.y = ptCurrent.y;
            }
            
            newLine.setPoint(ptNext, i+1);
        }
    }
	
	/**
	 * Returns the branch router in the chain.
	 * @return The getBranchRouter router
	 * @since 2.0
	 */
	protected BranchRouter getBranchRouter() {
		return branchRouter;
	}

	/**
	 * @see org.eclipse.draw2d.ConnectionRouter#remove(Connection)
	 */
	public void remove(Connection conn) {
		if (conn.getSourceAnchor() == null || conn.getTargetAnchor() == null)
			return;
		
		int index = connectionList.indexOf(conn);
		connectionList.remove(conn);
		for (int i = index + 1; i < connectionList.size(); i++)
			((Connection)connectionList.get(i)).revalidate();
	
		getBranchRouter().remove(conn);
	}

	/**
	 * isTopDown
	 * Utility method to determine if the connection should routed in a top-down fashion
	 * or in a horizontal fashion.
     * 
	 * @param conn Connection to query
	 * @return boolean true if connection should be routed top-down, false otherwise.
	 */
	public boolean isTopDown(Connection conn) {
		boolean vertical = true;
		if (conn instanceof ITreeConnection) {
			vertical = ((ITreeConnection)conn).getOrientation().equals(ITreeConnection.Orientation.VERTICAL) ? vertical = true : false;
		}
		
		return vertical;
	}
	
	private int DEFAULT_TRUNK_HEIGHT = 32;
	
	/**
	 * checkTrunkVertex
	 * Method to initialize the trunk vertex to a default value if not already set
	 * 
	 * @param conn Connection to be routed.
	 */
	private void checkTrunkVertex(Connection conn) {
		if (getTrunkVertex() == null) {
			Rectangle sourceRect = conn.getSourceAnchor().getOwner().getBounds();
			Rectangle targetRect = conn.getTargetAnchor().getOwner().getBounds();
			
			Dimension default_trunk = new Dimension(0, DEFAULT_TRUNK_HEIGHT);
			conn.translateToRelative(default_trunk);
			
			if (isTopDown(conn)) {
				if (sourceRect.getCenter().y < targetRect.getCenter().y) {
					setTrunkVertex(new Dimension(0, -default_trunk.height));
					setTrunkOrientation(Orientation.TOP);
				}
				else {
					setTrunkVertex(new Dimension(0, default_trunk.height));
					setTrunkOrientation(Orientation.BOTTOM);
				}
			}
			else {
				if (sourceRect.getCenter().x < targetRect.getCenter().x) {
					setTrunkVertex(new Dimension(-default_trunk.height, 0));
					setTrunkOrientation(Orientation.LEFT);
				}
				else {
					setTrunkVertex(new Dimension(default_trunk.height, 0));
					setTrunkOrientation(Orientation.RIGHT);
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * Routes the given connection.  Calls the 'getBranchRouter' router method first
	 * @see org.eclipse.draw2d.ConnectionRouter#route(org.eclipse.draw2d.Connection)
	 */
	public void route(Connection conn) {
		
		if (conn.getSourceAnchor() == null || conn.getSourceAnchor().getOwner() == null ||
			conn.getTargetAnchor() == null || conn.getTargetAnchor().getOwner() == null) {
			super.route(conn);
			return;
		}
		
		if (!connectionList.contains(conn)) {
			connectionList.add(conn);
		}
		
		checkTrunkVertex(conn);
		
		getBranchRouter().route(conn);
		invalidate(conn);
	}

	/**
	 * @return Returns the truckVertex.
	 */
	protected Dimension getTrunkVertex() {
		return trunkVertex;
	}

	/**
	 * @param trunkVertex The trunkVertex to set.
	 */
	protected void setTrunkVertex(Dimension trunkVertex) {
		this.trunkVertex = trunkVertex;
	}
	
	/**
	 * @return Returns the trunkOrientation.
	 */
	protected Orientation getTrunkOrientation() {
		return trunkOrientation;
	}
	
	/**
	 * @param trunkOrientation The trunkOrientation to set.
	 */
	protected void setTrunkOrientation(Orientation trunkOrientation) {
		this.trunkOrientation = trunkOrientation;
	}
	
	/**
	 * Utility method to determine if the given set of points conforms to the constraints
	 * of being an orthogonal connection tree-branch.
	 * 1. Points size must be 4.
	 * 2. Source point resides with-in boundary of source shape based on orientation
	 * 3. Target point resides with-in boundary of target shape based on orientation
	 * 4. Middle line is perpendicular to the 2 end lines.
	 * 
	 * @param conn the <code>Connection</code> to test
	 * @param points <code>PointList</code> to test constraints against
	 * @return <code>boolean</code> <code>true</code> if points represent valid orthogaonl tree 
	 * branch, <code>false</code> otherwise.
	 */
	public boolean isOrthogonalTreeBranch(Connection conn, PointList points) {
		if (isTreeBranch(conn, points)) {
			LineSeg branch = new LineSeg(points.getPoint(0), points.getPoint(1));
			LineSeg trunkShoulder = new LineSeg(points.getPoint(1), points.getPoint(2));
			LineSeg trunk = new LineSeg(points.getPoint(2), points.getPoint(3));
			
			if (isTopDown(conn))
				return branch.isVertical() && trunkShoulder.isHorizontal() && trunk.isVertical();
			else
				return branch.isHorizontal() && trunkShoulder.isVertical() && trunk.isHorizontal();
		}
		
		return false;
	}
	
	/**
	 * Utility method to determine if the given set of points conforms to the constraints
	 * of being a connection tree-branch.
	 * 1. Points size must be 4.
	 * 2. Source point resides with-in boundary of source shape based on orientation
	 * 3. Target point resides with-in boundary of target shape based on orientation
	 * 
	 * @param conn the <code>Connection</code> to test
	 * @param points the <code>PointList</code> to test constraints against
	 * @return <code>boolean</code> <code>true</code> if points represent valid tree branch, 
	 * <code>false</code> otherwise.
	 */
	public boolean isTreeBranch(Connection conn, PointList points) {
		if (points.size() == 4) {
			// just check if ends are with-in the owner bounding box
			Rectangle targetBounds = conn.getTargetAnchor().getOwner().getBounds();
			Rectangle sourceBounds = conn.getSourceAnchor().getOwner().getBounds();
			
			if (isTopDown(conn)) {
				return (points.getPoint(0).x > sourceBounds.x && 
					    points.getPoint(0).x < sourceBounds.x + sourceBounds.width) &&
					   (points.getPoint(3).x > targetBounds.x && 
						points.getPoint(3).x < targetBounds.x + targetBounds.width);
			}
			else
			{
				return (points.getPoint(0).y > sourceBounds.y && 
					    points.getPoint(0).y < sourceBounds.y + sourceBounds.height) &&
					   (points.getPoint(3).y > targetBounds.y && 
						points.getPoint(3).y < targetBounds.y + targetBounds.height);
			}
		}
		
		return false;
	}
	
	/**
	 * @return Returns the connectionList List which is a copy of the internal list.
	 */
	public List getConnectionList() {
		return (List)connectionList.clone();
	}
	/**
	 * @return Returns the updatingPeers.
	 */
	protected boolean isUpdatingPeers() {
		return updatingPeers;
	}
	/**
	 * @param updatingPeers The updatingPeers to set.
	 */
	protected void setUpdatingPeers(boolean updatingPeers) {
		this.updatingPeers = updatingPeers;
	}
}
