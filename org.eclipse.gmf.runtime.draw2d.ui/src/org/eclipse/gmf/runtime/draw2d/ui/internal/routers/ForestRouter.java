/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.internal.routers;

import java.util.HashMap;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;


/**
 * @author sshaw
 *
 * This router manages the tree routers on a given target.  Potentially multiple trees
 * can exist if they have different semantic types.
 */
public class ForestRouter extends BendpointConnectionRouter implements OrthogonalRouter {

	private HashMap connections = new HashMap();
	private HashMap trunkVertexes = new HashMap();
	
	private class AnchorKey {
    
		private ConnectionAnchor anchor;
		private Object qualifier;
	
		AnchorKey(ConnectionAnchor anchor, Object qualifier) {
			this.anchor = anchor;
			this.qualifier = qualifier;
		}
	
		public boolean equals(Object object) {
			boolean isEqual = false;
			AnchorKey hashKey;
		
			if (object instanceof AnchorKey) {
				hashKey = (AnchorKey)object;
				ConnectionAnchor hkA1 = hashKey.getAnchor();
				Object hkA2 = hashKey.getQualifier();
			
				isEqual = (hkA1.equals(anchor) && hkA2.equals(qualifier));
			}
			return isEqual;
		}
			
		/**
		 * Accessor to retrieve the <code>ConnectionAnchor</code> that is stored as part of the key.
		 * 
		 * @return the <code>ConnectionAnchor</code> that is used for the key.
		 */
		public ConnectionAnchor getAnchor() {
			return anchor;
		}
	
		/**
		 * Accessor to retrieve the qualifier object that is stored as part of the key.
		 * 
		 * @return the <code>Object</code> that is designated the qualifier.
		 */
		public Object getQualifier() {
			return qualifier;	
		}
	
		/* 
		 * (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			return anchor.hashCode() ^ qualifier.hashCode();
		}
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.draw2d.ConnectionRouter#invalidate(org.eclipse.draw2d.Connection)
	 */
	public void invalidate(Connection conn) {
		if (conn.getSourceAnchor() == null || conn.getTargetAnchor() == null)
			return;
		
		ConnectionRouter connectionRouter = getSubRouter(conn);
		if (connectionRouter != null)
			connectionRouter.invalidate(conn);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.draw2d.ConnectionRouter#remove(org.eclipse.draw2d.Connection)
	 */
	public void remove(Connection conn) {
		if (conn.getSourceAnchor() == null || conn.getTargetAnchor() == null)
			return;
		
		ConnectionRouter connectionRouter = getSubRouter(conn);
		if (connectionRouter != null)
			connectionRouter.remove(conn);
		
		super.remove(conn);
	}

	
	/**
	 * Checks for trunk overlap and re-routes to avoid.
	 */
	public void route(Connection conn) {
		if (conn.getTargetAnchor().getOwner() == null ||
			conn.getSourceAnchor().getOwner() == null) {
			PointList points = conn.getPoints();
			points.removeAllPoints();

			Point ref1 = conn.getTargetAnchor().getReferencePoint();
			Point ref2 = conn.getSourceAnchor().getReferencePoint();
			PrecisionPoint precisePt = new PrecisionPoint();

			precisePt.setLocation(conn.getSourceAnchor().getLocation(ref1));
			conn.translateToRelative(precisePt);
			points.addPoint(precisePt);
 
			precisePt.setLocation(conn.getTargetAnchor().getLocation(ref2));
			conn.translateToRelative(precisePt);
			points.addPoint(precisePt);
			conn.setPoints(points);
			return;
		}
		
		TreeRouter treeRouter = getSubRouter(conn);
		
		// remove existing trunk vertex before routing occurs.
		Dimension trunk = treeRouter.getTrunkVertex();
		if (trunk != null) {
			AnchorKey trunkKey = new AnchorKey(conn.getTargetAnchor(), trunk);
			trunkVertexes.remove(trunkKey);
		}
		 
		if (treeRouter != null) {
			treeRouter.route(conn);
		
			trunk = treeRouter.getTrunkVertex();
			Dimension adjustedTrunk = accountForTrunkOverlap(trunk, conn);
			if (!adjustedTrunk.equals(trunk)) {
				treeRouter.setTrunkVertex(adjustedTrunk);
				treeRouter.invalidate(conn);
			}
		}
	}
	
	/**
	 * Makes sure the routed tree doesn't intersect with an existing tree in the "forest".
	 * This is called recursively for each trunk.
	 * 
	 * @param trunk <code>Dimension</code> trunkVertex value to compare
	 * @param conn <code>Connection</code> that is connection currently being routed
	 * @return <code>Dimension</code> new trunk vertex value
	 */
	private Dimension accountForTrunkOverlap(Dimension trunk, Connection conn) {
		if (conn.getTargetAnchor() == null ||
			conn.getTargetAnchor().getOwner() == null)
			return trunk;
		
		AnchorKey trunkKey = new AnchorKey(conn.getTargetAnchor(), trunk);
		
		// check if trunk vertex doesn't exist or if it exceeds a maximum then
		// return.
		int ownerExt = conn.getTargetAnchor().getOwner().getBounds().width / 2;
		int trunkExt = trunk.width;
		
		if (conn instanceof ITreeConnection) {
			if (((ITreeConnection)conn).getOrientation() == ITreeConnection.Orientation.HORIZONTAL) {
				ownerExt = conn.getTargetAnchor().getOwner().getBounds().height / 2;
				trunkExt = trunk.height;
			}
		}
		
		if (trunkVertexes.get(trunkKey) == null ||
			Math.abs(trunkExt) > ownerExt) {
			trunkVertexes.put(trunkKey, new Boolean(true));
			return trunk;
		}
		else {
			Dimension newTrunk = new Dimension(trunk);
			newTrunk.expand(10, 10);
			return accountForTrunkOverlap(newTrunk, conn);
		}
	}

	/**
	 * Utility method to retrieve the sub router that manages the individual trees.
	 * 
	 * @param conn <code>Connection</code> to be routered
	 * @return <code>TreeRouter</code> that will end up routing the given <code>Connection</code>.
	 */
	public TreeRouter getSubRouter(Connection conn) {
		if (conn.getTargetAnchor() == null)
			return null;
		
		String hint = "base"; //$NON-NLS-1$
		if (conn instanceof ITreeConnection) {
			hint = ((ITreeConnection)conn).getHint();
		}
		
		AnchorKey connectionKey = new AnchorKey(conn.getTargetAnchor(), hint);
		TreeRouter connectionRouter = (TreeRouter)connections.get(connectionKey);
		if (connectionRouter == null) {
			connectionRouter = new TreeRouter();
			connections.put(connectionKey, connectionRouter);
		}
		
		return connectionRouter;
	}
}
