/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.draw2d.ui.internal.routers;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.RelativeBendpoint;
import org.eclipse.draw2d.XYAnchor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.ITreeConnection;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.TreeRouter;
import org.eclipse.gmf.tests.runtime.draw2d.ui.internal.routers.AbstractForestRouterTest.TreeConnection;
/**
 * @author sshaw
 * 
 * Abstract BranchRouter test class.
 */
public class TreeRouterTest extends TestCase {
	public TreeRouterTest(String name) { 
		super(name);
	}
	
	private TreeRouter treeRouter;
	private TreeConnection conn1;
	private TreeConnection conn2;
	
	protected TreeConnection getConnection1() {
		return conn1;
	}

	protected TreeConnection getConnection2() {
		return conn2;
	}
	
	protected TreeRouter getTreeRouter() {
		return treeRouter;
	}
	
	protected void setUp() {
		try {
			treeRouter = new TreeRouter();
			RectangleFigure node1 = new RectangleFigure(), node2 = new RectangleFigure(), 
							node3 = new RectangleFigure();
			ConnectionAnchor anchor = new ChopboxAnchor(node2);
			conn1 = new TreeConnection();
			conn1.setSourceAnchor(new ChopboxAnchor(node1));
			conn1.setTargetAnchor(anchor);
			conn1.setHint("tree1"); //$NON-NLS-1$
			conn1.setConnectionRouter(treeRouter);
			
			conn2 = new TreeConnection();
			conn2.setSourceAnchor(new ChopboxAnchor(node3));
			conn2.setTargetAnchor(anchor);
			conn2.setHint("tree1"); //$NON-NLS-1$
			conn2.setConnectionRouter(treeRouter);
			
			getConnection1().setOrientation(ITreeConnection.Orientation.VERTICAL);
			getConnection2().setOrientation(ITreeConnection.Orientation.VERTICAL);
			
		} catch (Exception e) {
			fail("The ForestRouterTest.setUp method caught an exception - " + e); //$NON-NLS-1$
		}
	}
	
	Rectangle test1Other = new Rectangle(3900, 4906, 2200, 1300);
	Rectangle test1Start = new Rectangle(9986, 5250, 2200, 1300);
	Rectangle test1End = new Rectangle(6440, 831, 2200, 1300);
	Dimension[] test1 = { new Dimension(-265, -635), new Dimension(3281, 3784),
						  new Dimension(-265, -2196), new Dimension(3281, 2223),
						  new Dimension(-3440, -2196), new Dimension(106, 2223),
						  new Dimension(-3440, -3784), new Dimension(106, 635)
	};
	
	Rectangle test2Start = new Rectangle(3995, 4974, 2593, 1246);
	Rectangle test2End = new Rectangle(6535, 899, 2169, 1246);
	Rectangle test2Other = new Rectangle(9986, 5250, 2200, 1300);
	Dimension[] test2 = { new Dimension(-238, -635), new Dimension(-2778, 3440),
						  new Dimension(-238, -1852), new Dimension(-2778, 2223),
						  new Dimension(2434, -1852), new Dimension(-106, 2223),
						  new Dimension(2434, -3440), new Dimension(-106, 635)
	};
	
	public void testOrthogonalConstraint() {
		List newConstraint = initializeConstraint(test1, test1Start, test1End, test1Other);
		
		validateConstraint(newConstraint);
	}
	
	public void testRATLC00531806() {
		List newConstraint = initializeConstraint(test2, test2Start, test2End, test2Other);
		
		validateConstraint(newConstraint);
	}
	
	// test reorient of source 
	public void testRATLC00534189() {
		List newConstraint = initializeConstraint(test1, test1Start, test1End, test1Other);
		
		getConnection1().setSourceAnchor(new XYAnchor(new Point(100, 100)));
		
		getTreeRouter().setConstraint(getConnection1(), newConstraint);
		getTreeRouter().route(getConnection2());
		getTreeRouter().route(getConnection1());
		
		getTreeRouter().invalidate(getConnection1());
		getTreeRouter().invalidate(getConnection2());
	}
	
	public void testMoveShape() {
		List newConstraint = initializeConstraint(test1, test1Start, test1End, test1Other);
		
		getTreeRouter().setConstraint(getConnection1(), newConstraint);
		getTreeRouter().route(getConnection2());
		getTreeRouter().route(getConnection1());
		
		Rectangle newBounds = new Rectangle(test1Start);
		newBounds.translate(4000, 4000);
		getConnection1().getSourceAnchor().getOwner().setBounds(newBounds);
		getTreeRouter().route(getConnection1());
		
		getConnection2().validate();
		
		PointList c1Pts = getConnection1().getPoints();
		PointList c2Pts = getConnection2().getPoints();
		assertTrue("Trunk values don't match after tree routing", 	//$NON-NLS-1$
			c1Pts.getPoint(2).equals(c2Pts.getPoint(2)));
	}

	/**
	 * @param newConstraint
	 */
	private void validateConstraint(List newConstraint) {
		getTreeRouter().setConstraint(getConnection1(), newConstraint);
		PointList ptl1 = getTreeRouter().getPointsFromConstraint(getConnection1());
		
		getTreeRouter().route(getConnection2());
		getTreeRouter().route(getConnection1());
	
		assertTrue("Connection1 points aren't orthogonal",			//$NON-NLS-1$
			getTreeRouter().isOrthogonalTreeBranch(getConnection1(), ptl1));
		
		PointList c1Pts = getConnection1().getPoints();
		PointList c2Pts = getConnection2().getPoints();
		assertTrue("Connection2 points aren't orthogonal",			//$NON-NLS-1$
			getTreeRouter().isOrthogonalTreeBranch(getConnection2(), c2Pts));
		assertTrue("Trunk values don't match after tree routing", 	//$NON-NLS-1$
			c1Pts.getPoint(2).equals(c2Pts.getPoint(2)));
	}

	/**
	 * @param newConstraint
	 * @param dim
	 * @param pt1
	 * @param pt2
	 * @param pt3
	 */
	private List initializeConstraint(Dimension[] testData, Rectangle start, Rectangle end, Rectangle other) {
		List newConstraint = new ArrayList(getConnection1().getPoints().size());
		
		getConnection1().getSourceAnchor().getOwner().setBounds(start);
		getConnection1().getTargetAnchor().getOwner().setBounds(end);
		getConnection2().getSourceAnchor().getOwner().setBounds(other);
		
		RelativeBendpoint rbp1 =
			new RelativeBendpoint(getConnection1());
		rbp1.setRelativeDimensions(
			testData[0], testData[1]);
		rbp1.setWeight(1 / ((float) 5));
		newConstraint.add(rbp1);
		
		RelativeBendpoint rbp2 =
			new RelativeBendpoint(getConnection1());
		rbp2.setRelativeDimensions(
			testData[2], testData[3]);
		rbp2.setWeight(2 / ((float) 5));
		newConstraint.add(rbp2);
		
		RelativeBendpoint rbp3 =
			new RelativeBendpoint(getConnection1());
		rbp3.setRelativeDimensions(
			testData[4], testData[5]);
		rbp3.setWeight(3 / ((float) 5));
		newConstraint.add(rbp3);
		
		RelativeBendpoint rbp4 =
			new RelativeBendpoint(getConnection1());
		rbp4.setRelativeDimensions(
			testData[6], testData[7]);
		rbp4.setWeight(4 / ((float) 5));
		newConstraint.add(rbp4);
		
		return newConstraint;
	}
}
