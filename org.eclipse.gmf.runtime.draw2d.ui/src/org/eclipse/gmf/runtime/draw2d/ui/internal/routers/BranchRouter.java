/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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

import org.eclipse.draw2d.AbstractRouter;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;

/**
 * @author sshaw
 *
 * Final router class for routing a specific branch on a connection tree.
 * Package protected - no should be using this router directly.  Use the Forest
 * Router as the top level entry point.
 */
public class BranchRouter extends AbstractRouter {

	private TreeRouter tree;
	
	/**
	 * @param tree
	 */
	public BranchRouter(TreeRouter tree) {
		super();
		
		this.tree = tree;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.ConnectionRouter#route(org.eclipse.draw2d.Connection)
	 * 
	 * case 1: connection has never been routed before and needs
	 * points to be populated.  points.size() < 4 
	 * 
	 * case 2: user moved the trunk vertex of the connection by either
	 * moving the line attached to the target or second last target line
	 * 
	 * case 3: user moved the source or target shape causing a layout
	 * of the connection.  
	 * 
	 * case 4: user moved the source line attached to the source shape. 
	 * 
	 * case 5: connection is being rerouted as a result of an invalidation
	 * from case 2.  
	 * 
	 */
	public void route(Connection conn) {
		Point ptTrunkLoc = getTrunkLocation(conn);
		
		getTree().setTrunkLocation(conn, ptTrunkLoc);
		
		Point ptSourceLoc = getSourceLocation(conn, ptTrunkLoc);
		
		PointList points = recreateBranch(conn, ptSourceLoc, ptTrunkLoc);
		conn.setPoints(points);
	}
	
	/**
	 * getTrunkLocation
	 * Method to retrieve the trunk location in relative coordinates
	 * 
	 * @param conn Connection being routed
	 * @return Point that is the trunk location in relative coordinates.
	 */
	protected Point getTrunkLocation(Connection conn) {
		PointList points = getTree().getPointsFromConstraint(conn);
		Point ptTrunkLoc = getTree().getTrunkLocation(conn); // default;
		
		// check valid again based on constraint
		if (getTree().isTreeBranch(conn, points)) {
			if (getTree().isTopDown(conn))
				ptTrunkLoc.x = points.getPoint(3).x;
			else
				ptTrunkLoc.y = points.getPoint(3).y;
			
			if (getTree().isOrthogonalTreeBranch(conn, points)) {
				if (getTree().isTopDown(conn))
					ptTrunkLoc.y = points.getPoint(2).y;
				else
					ptTrunkLoc.x = points.getPoint(2).x;
			}
		}

		return ptTrunkLoc;
	}
	
	/**
	 * getSourceLocation
	 * Method to retrieve the source location where the connection is connected
	 * to the source element.
	 * 
	 * @param conn Connection to be routed.
	 * @param ptTrunkLoc Point trunk location in relative coordinates
	 * @return Point source location in relative coordinates
	 */
	public Point getSourceLocation(Connection conn, Point ptTrunkLoc) {
		Point ptSourceRef = conn.getSourceAnchor().getReferencePoint();
		conn.translateToRelative(ptSourceRef);
		boolean bTopDown = getTree().isTopDown(conn);
		
		int branchOffset = getSourceBranchOffset(conn) + (bTopDown ? ptSourceRef.x : ptSourceRef.y);
		
		Point ref;
		if (bTopDown)
			ref = new Point(branchOffset, ptTrunkLoc.y);
		else
			ref = new Point(ptTrunkLoc.x, branchOffset);
		
		LineSeg line = OrthogonalRouterUtilities.getOrthogonalLineSegToAnchorLoc(conn, conn.getSourceAnchor(), ref);
		return line.getOrigin();
	}

	/**
	 * recreateBranch
	 * Utility method used to recreate the points list for the branch connection given
	 * a trunk vertex location and a source attachpoint location.
	 * 
	 * @param conn Connection used to do translate points to relative coordinates.
	 * @param ptSourceLoc Point that is attached to the source node
	 * @param ptTrunkLoc Point that is the vertex between the line attached to the target
	 * and the "shoulder" line that holds the individual source branches.
	 * @return PointList that represents the full connection tree branch.
	 */
	public PointList recreateBranch(Connection conn, Point ptSourceLoc, Point ptTrunkLoc) {
		PointList points = new PointList(4);
		boolean bTopDown = getTree().isTopDown(conn);
		
		points.addPoint(new Point(ptSourceLoc));
		
		Point pt2 = bTopDown ? new Point(ptSourceLoc.x, ptTrunkLoc.y) : new Point(ptTrunkLoc.x, ptSourceLoc.y);
		points.addPoint(pt2);
		
		points.addPoint(new Point(ptTrunkLoc));
		
		LineSeg line = OrthogonalRouterUtilities.getOrthogonalLineSegToAnchorLoc(conn, conn.getTargetAnchor(), ptTrunkLoc);
		Point ptTargetLoc = line.getOrigin();
		
		Point pt4 = bTopDown ? new Point(ptTrunkLoc.x, ptTargetLoc.y) : new Point(ptTargetLoc.x, ptTrunkLoc.y);
		points.addPoint(pt4);
		
		return points;
	}
	
	/**
	 * getTree
	 * Getter method for the container tree router.
	 * 
	 * @return Returns the tree.
	 */
	protected TreeRouter getTree() {
		return tree;
	}
	
	
	/**
	 * getSourceBranchOffset
	 * Utility method to retrieve the branch offset value either as a default
	 * value based on the source anchor or utilizes the constraint to find a 
	 * current value.
	 * 
	 * @param conn Connection to retrieve the constraint from.
	 * @return int value that represents an offset from the source anchor 
	 * reference point.
	 */
	private int getSourceBranchOffset(Connection conn) {
		Point ptSourceRef = conn.getSourceAnchor().getReferencePoint();
		conn.translateToRelative(ptSourceRef);
		boolean bTopDown = getTree().isTopDown(conn);
		
		int branchOffset = bTopDown ? ptSourceRef.x : ptSourceRef.y;
		
		// if constraint is valid, then retrieve just the branchOffset (ie the connection
		// location to the source shape from the values.
		PointList constraintPoints = getTree().getPointsFromConstraint(conn);
		if (getTree().isTreeBranch(conn, constraintPoints)) {
			Point ptLoc = constraintPoints.getFirstPoint();
			branchOffset = bTopDown ? ptLoc.x : ptLoc.y;	
		}
		
		return branchOffset - (bTopDown ? ptSourceRef.x : ptSourceRef.y);
	}

}
