/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.draw2d.ui.internal.routers;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.ObliqueRouter;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.RectilinearRouter;

import junit.framework.TestCase;


/**
 * @author sshaw
 *
 */
public class AvoidObstructionsRoutingTest extends TestCase {
	public AvoidObstructionsRoutingTest(String name) {
		super(name);
	}
	
	private ObliqueRouter obliqueRouter;
    private RectilinearRouter rectilinearRouter;

	protected ObliqueRouter getObliqueRouter() {
		if (obliqueRouter == null)
            obliqueRouter = new ObliqueRouter();
		return obliqueRouter;
	}
    
    protected RectilinearRouter getRectilinearRouter() {
        if (rectilinearRouter == null)
            rectilinearRouter = new RectilinearRouter();
        return rectilinearRouter;
    }
	
	protected void setUp() {
		try {
			super.setUp();
		} catch (Exception e) {
			fail("The AvoidObstructionsRoutingTest.setUp method caught an exception - " + e); //$NON-NLS-1$
		}
	}
	
	public boolean isConnectionObstructed(Connection conn, Rectangle obstruction) {
		PointList rectPoints = PointListUtilities.createPointsFromRect(obstruction);
        
        PointList intersections = new PointList();
        PointList distances = new PointList();
        return PointListUtilities.findIntersections(conn.getPoints(), rectPoints, intersections, distances);
	}
	
	private void testSameContainerObstruction(boolean horizontal, BendpointConnectionRouter router) {
		RectangleFigure topNode = new RectangleFigure();
        topNode.setLayoutManager(new FreeformLayout());
        topNode.setBounds(new Rectangle(0, 0, 400, 400));
        
        RectangleFigure node1 = new RectangleFigure(), node2 = new RectangleFigure(), node3 = new RectangleFigure();
		node1.setSize(40, 40);
		node1.setLocation(new Point(100, 100));
			
		node2.setSize(40, 40);
		node2.setLocation(horizontal ? new Point(200, 100) : new Point(100, 200));
			
		node3.setSize(40, 40);
		node3.setLocation(horizontal ? new Point(300, 100) : new Point(100, 300));
		
        topNode.add(node1);
        topNode.add(node2);
        topNode.add(node3);
        
        verifyObstructionIsCleared(topNode, node1, node3, node2, router);
	}
    
    private void testDifferentSourceContainerObstruction(boolean horizontal, BendpointConnectionRouter router) {
        RectangleFigure topNode = new RectangleFigure();
        topNode.setLayoutManager(new FreeformLayout());
        topNode.setBounds(new Rectangle(0, 0, 400, 400));
        
        RectangleFigure subNode = new RectangleFigure() {
            protected boolean useLocalCoordinates() {
                return true;
            }
        };
        subNode.setSize(60, 60);
        subNode.setLayoutManager(new XYLayout());
        topNode.add(subNode);
        subNode.setLocation(new Point(100, 100));
        
        RectangleFigure node1 = new RectangleFigure(), node2 = new RectangleFigure(), node3 = new RectangleFigure();
        node1.setSize(40, 40);
        subNode.add(node1);
        node1.setLocation(new Point(10, 10));
        
        node2.setSize(40, 40);
        node2.setLocation(horizontal ? new Point(200, 100) : new Point(100, 200));
            
        node3.setSize(40, 40);
        node3.setLocation(horizontal ? new Point(300, 100) : new Point(100, 300));
        
        topNode.add(node2);
        topNode.add(node3);
        
        verifyObstructionIsCleared(topNode, node1, node3, node2, router);
    }
    
    private void testDifferentTargetContainerObstruction(boolean horizontal, BendpointConnectionRouter router) {
        RectangleFigure topNode = new RectangleFigure();
        topNode.setLayoutManager(new FreeformLayout());
        topNode.setBounds(new Rectangle(0, 0, 400, 400));
        
        RectangleFigure node1 = new RectangleFigure(), node2 = new RectangleFigure(), node3 = new RectangleFigure();
        
        node1.setSize(40, 40);
        node1.setLocation(new Point(100, 100));
        
        node2.setSize(40, 40);
        node2.setLocation(horizontal ? new Point(200, 100) : new Point(100, 200));
        
        RectangleFigure subNode = new RectangleFigure() {
            protected boolean useLocalCoordinates() {
                return true;
            }
        };
        subNode.setSize(60, 60);
        subNode.setLayoutManager(new XYLayout());
        topNode.add(subNode);
        subNode.setLocation(horizontal ? new Point(300, 100) : new Point(100, 300));
        
        node3.setSize(40, 40);
        subNode.add(node3);
        node3.setLocation(new Point(10, 10));
        
        topNode.add(node1);
        topNode.add(node2);
        
        verifyObstructionIsCleared(topNode, node1, node3, node2, router);
    }
    
    private void testDifferentSourceAndTargetContainerObstruction(boolean horizontal, BendpointConnectionRouter router) {
        RectangleFigure topNode = new RectangleFigure();
        topNode.setLayoutManager(new FreeformLayout());
        topNode.setBounds(new Rectangle(0, 0, 400, 400));
        
        RectangleFigure node1 = new RectangleFigure(), node2 = new RectangleFigure(), node3 = new RectangleFigure();
        
        RectangleFigure subNode = new RectangleFigure() {
            protected boolean useLocalCoordinates() {
                return true;
            }
        };
        subNode.setSize(60, 60);
        subNode.setLayoutManager(new XYLayout());
        topNode.add(subNode);
        subNode.setLocation(new Point(100, 100));
        
        node1.setSize(40, 40);
        subNode.add(node1);
        node1.setLocation(new Point(10, 10));
        
        node2.setSize(40, 40);
        node2.setLocation(horizontal ? new Point(200, 100) : new Point(100, 200));
        
        RectangleFigure subNode2 = new RectangleFigure() {
            protected boolean useLocalCoordinates() {
                return true;
            }
        };
        subNode2.setSize(60, 60);
        subNode2.setLayoutManager(new XYLayout());
        topNode.add(subNode2);
        subNode2.setLocation(horizontal ? new Point(300, 100) : new Point(100, 300));
        
        node3.setSize(40, 40);
        subNode2.add(node3);
        node3.setLocation(new Point(10, 10));
        
        topNode.add(node2);
        
        verifyObstructionIsCleared(topNode, node1, node3, node2, router);
    }
    
    public void testSameContainerObstruction_horizontal() {
        testSameContainerObstruction(true, getObliqueRouter());
        
        testSameContainerObstruction(true, getRectilinearRouter());
    }
    
    public void testDifferentSourceContainerObstruction_horizontal() {
        testDifferentSourceContainerObstruction(true, getObliqueRouter());
        
        testDifferentSourceContainerObstruction(true, getRectilinearRouter());
    }
    
    public void testDifferentTargetContainerObstruction_horizontal() {
        testDifferentTargetContainerObstruction(true, getObliqueRouter());
        
        testDifferentTargetContainerObstruction(true, getRectilinearRouter());
    }
    
    public void testDifferentSourceAndTargetContainerObstruction_horizontal() {
        testDifferentSourceAndTargetContainerObstruction(true, getObliqueRouter());
        
        testDifferentSourceAndTargetContainerObstruction(true, getRectilinearRouter());
    }
    
    public void testSameContainerObstruction_vertical() {
        testSameContainerObstruction(false, getObliqueRouter());
        
        testSameContainerObstruction(false, getRectilinearRouter());
    }
    
    public void testDifferentSourceContainerObstruction_vertical() {
        testDifferentSourceContainerObstruction(false, getObliqueRouter());
        
        testDifferentSourceContainerObstruction(false, getRectilinearRouter());
    }
    
    public void testDifferentTargetContainerObstruction_vertical() {
        testDifferentTargetContainerObstruction(false, getObliqueRouter());
        
        testDifferentTargetContainerObstruction(false, getRectilinearRouter());
    }
    
    public void testDifferentSourceAndTargetContainerObstruction_vertical() {
        testDifferentSourceAndTargetContainerObstruction(false, getObliqueRouter());
    
        testDifferentSourceAndTargetContainerObstruction(false, getRectilinearRouter());
    }
	
    private void verifyObstructionIsCleared(IFigure topNode, IFigure node1, IFigure node3, IFigure obstruction, BendpointConnectionRouter router) {
        PolylineConnectionEx conn1 = new PolylineConnectionEx();
        conn1.setSourceAnchor(new ChopboxAnchor(node1));
        conn1.setTargetAnchor(new ChopboxAnchor(node3));
        conn1.setConnectionRouter(router);
        PointList points = new PointList();
        
        Point pt1 = node1.getBounds().getCenter();
        node1.translateToAbsolute(pt1);
        conn1.translateToRelative(pt1);
        points.addPoint(pt1);
        
        Point pt2 = node3.getBounds().getCenter();
        node3.translateToAbsolute(pt2);
        conn1.translateToRelative(pt2);
        points.addPoint(pt2);
        conn1.setPoints(points);
        
        assertTrue(isConnectionObstructed(conn1, obstruction.getBounds().getCopy()));
        
        conn1.setRoutingStyles(true, true);
        getObliqueRouter().route(conn1);
        
        assertFalse(isConnectionObstructed(conn1, obstruction.getBounds().getCopy()));
    }
}