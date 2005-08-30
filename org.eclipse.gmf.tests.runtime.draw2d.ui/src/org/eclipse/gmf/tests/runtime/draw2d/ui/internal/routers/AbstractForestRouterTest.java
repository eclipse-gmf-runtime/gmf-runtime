/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.draw2d.ui.internal.routers;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.PointList;

import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.ForestRouter;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.ITreeConnection;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.TreeRouter;

/**
 * @author sshaw
 * 
 * Abstract ForestRouter test class.
 */
abstract public class AbstractForestRouterTest extends TestCase {
	public AbstractForestRouterTest(String name) {
		super(name);
	}
	
	public static class TreeConnection extends PolylineConnection
			implements ITreeConnection {
		
		String hint;
		Orientation orientation;
		
		public TreeConnection() {
			// Empty Constructor
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.xtools.gef.internal.figures.routers.ITreeConnection#getHint()
		 */
		public String getHint() {
			return hint;
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.xtools.gef.internal.figures.routers.ITreeConnection#getOrientation()
		 */
		public Orientation getOrientation() {
			return orientation;
		}
		/**
		 * @param hint The hint to set.
		 */
		public void setHint(String hint) {
			this.hint = hint;
		}
		/**
		 * @param orient The orient to set.
		 */
		public void setOrientation(Orientation orient) {
			this.orientation = orient;
		}
	}
	
	private ForestRouter forestRouter;
	private TreeConnection conn1;
	private TreeConnection conn2;
	private TreeConnection conn3;
	private TreeConnection conn4;
	
	protected TreeConnection getConnection1() {
		return conn1;
	}
	protected TreeConnection getConnection2() {
		return conn2;
	}
	protected TreeConnection getConnection3() {
		return conn3;
	}
	protected TreeConnection getConnection4() {
		return conn4;
	}
	protected ForestRouter getForestRouter() {
		return forestRouter;
	}
	
	protected void setUp() {
		try {
			forestRouter = new ForestRouter();
			RectangleFigure node1 = new RectangleFigure(), node2 = new RectangleFigure(), node3 = new RectangleFigure();
			node1.setSize(40, 40);
			node2.setSize(40, 40);
			node3.setSize(40, 40);
			ConnectionAnchor anchor = new ChopboxAnchor(node3);
			conn1 = new TreeConnection();
			conn1.setSourceAnchor(new ChopboxAnchor(node1));
			conn1.setTargetAnchor(anchor);
			conn1.setHint("tree1"); //$NON-NLS-1$
			conn1.setConnectionRouter(forestRouter);
			
			conn2 = new TreeConnection();
			conn2.setSourceAnchor(new ChopboxAnchor(node2));
			conn2.setTargetAnchor(anchor);
			conn2.setHint("tree1"); //$NON-NLS-1$
			conn2.setConnectionRouter(forestRouter);
			
			RectangleFigure node4 = new RectangleFigure(), node5 = new RectangleFigure();
			node4.setSize(40, 40);
			node5.setSize(40, 40);
			conn3 = new TreeConnection();
			conn3.setSourceAnchor(new ChopboxAnchor(node4));
			conn3.setTargetAnchor(anchor);
			conn3.setHint("tree2"); //$NON-NLS-1$
			conn3.setConnectionRouter(forestRouter);
			
			conn4 = new TreeConnection();
			conn4.setSourceAnchor(new ChopboxAnchor(node5));
			conn4.setTargetAnchor(anchor);
			conn4.setHint("tree2"); //$NON-NLS-1$
			conn4.setConnectionRouter(forestRouter);
			
		} catch (Exception e) {
			fail("The ForestRouterTest.setUp method caught an exception - " + e); //$NON-NLS-1$
		}
	}
	/**
	 * @see org.eclipse.draw2d.ConnectionRouter#invalidate(Connection)
	 */
	public void testInvalidate() {
		getForestRouter().invalidate(getConnection1());
		getForestRouter().invalidate(getConnection2());
	}
	public void testRemove() {
		getForestRouter().route(getConnection1());
		getForestRouter().remove(getConnection1());
	}
	
	protected void routeConnections(String testDesc) {
		getForestRouter().route(getConnection1());
		getForestRouter().route(getConnection2());
		
		updateConstraint(getConnection1());
		updateConstraint(getConnection2());
		
		assertTrue(testDesc
				+ " Connection1 trunk not equal to Connection2 trunk", //$NON-NLS-1$
				getConnection1().getPoints().getPoint(2).equals(
						getConnection2().getPoints().getPoint(2)));
		assertTrue(testDesc
				+ " Connection1 isOrthogonalTreeBranch", //$NON-NLS-1$
				isOrthogonalTreeBranch(getConnection1()));
		assertTrue(testDesc
				+ " Connection2 isOrthogonalTreeBranch", //$NON-NLS-1$
				isOrthogonalTreeBranch(getConnection2()));
	}
	
	/**
	 * 
	 */
	private void updateConstraint(Connection conn) {
		PointList pts = conn.getPoints();
		List newConstraint = new ArrayList(pts.size());
		for (int i = 0; i < pts.size(); i++) {
			Bendpoint abp = new AbsoluteBendpoint(pts.getPoint(i));
			newConstraint.add(abp);
		}
		getForestRouter().setConstraint(conn, newConstraint);
	}
	
	/**
	 * Utility exposed for testing purposes
	 * 
	 * @param conn the <code>Connection</code> to check if it's orthogonal or not.
	 * @return true is tree branch is orthogonal, false otherwise
	 */
	static public boolean isOrthogonalTreeBranch(Connection conn) {
		TreeRouter treeRouter = new TreeRouter();
		
		return treeRouter.isOrthogonalTreeBranch(conn, conn.getPoints());
	}
	
	protected void routeMultiTreeConnections(String testDesc) {
		routeConnections(testDesc);
		getForestRouter().route(getConnection3());
		getForestRouter().route(getConnection4());
		
		updateConstraint(getConnection1());
		updateConstraint(getConnection2());
		
		assertTrue(testDesc
				+ " Connection3 trunk not equal to Connection4 trunk", //$NON-NLS-1$
				getConnection3().getPoints().getPoint(2).equals(
						getConnection4().getPoints().getPoint(2)));
		assertTrue(testDesc
				+ " Connection3 isOrthogonalTreeBranch", //$NON-NLS-1$
				isOrthogonalTreeBranch(getConnection3()));
		assertTrue(testDesc
				+ " Connection4 isOrthogonalTreeBranch", //$NON-NLS-1$
				isOrthogonalTreeBranch(getConnection4()));
		
		assertTrue(testDesc
				+ " tree1 trunk not equal to tree2 trunk", //$NON-NLS-1$
				!getConnection1().getPoints().getPoint(2).equals(
						getConnection3().getPoints().getPoint(2)));
	}
}
