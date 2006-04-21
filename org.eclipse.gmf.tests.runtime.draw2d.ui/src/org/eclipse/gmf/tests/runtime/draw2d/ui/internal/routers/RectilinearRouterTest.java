/******************************************************************************
 * Copyright (c) 2005 - 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.draw2d.ui.internal.routers;

import java.util.List;
import java.util.ListIterator;

import junit.framework.TestCase;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gmf.runtime.draw2d.ui.figures.BaseSlidableAnchor;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.ObliqueRouter;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.RectilinearRouter;


public class RectilinearRouterTest extends TestCase {
	public RectilinearRouterTest(String name) {
		super(name);
	}
	
	private RectilinearRouter rectilinearRouter;

	protected RectilinearRouter getRectilinearRouter() {
		if (rectilinearRouter == null)
			rectilinearRouter = new RectilinearRouter();
		return rectilinearRouter;
	}
	
	static private class FixedPointConnectionAnchor extends AbstractConnectionAnchor {

		RectangleFigure rf;
		
		public FixedPointConnectionAnchor(RectangleFigure rf) {
			super(rf);
			this.rf = rf;
		}
		
		public Point getLocation(Point reference) {
			return rf.getBounds().getBottomRight();
		}
		
	}
	
	protected void setUp() {
		try {
			super.setUp();
		} catch (Exception e) {
			fail("The RectilinearRouterTest.setUp method caught an exception - " + e); //$NON-NLS-1$
		}
	}
	
	public void verifyConnection(Connection conn) {
		List lineSegs = PointListUtilities.getLineSegments(conn.getPoints());
		ListIterator li = lineSegs.listIterator();
		while (li.hasNext()) {
			LineSeg line = (LineSeg)li.next();
			assertTrue(line.isHorizontal() || line.isVertical());
		}
		
		assertTrue(conn.getPoints().size() >= 2);
		
		// make sure the router is still respecting the end anchor points
		assertTrue(conn.getPoints().getFirstPoint().equals(conn.getSourceAnchor().getLocation(conn.getPoints().getFirstPoint())));
		assertTrue(conn.getPoints().getLastPoint().equals(conn.getTargetAnchor().getLocation(conn.getPoints().getLastPoint())));
	}
	
	public void testRouteConnectionAttachedToConnection() {
		PolylineConnectionEx conn1;
		PolylineConnectionEx conn2;
		
		RectangleFigure node1 = new RectangleFigure(), node2 = new RectangleFigure(), node3 = new RectangleFigure();
		node1.setSize(40, 40);
		node1.setLocation(new Point(100, 100));
			
		node2.setSize(40, 40);
		node2.setLocation(new Point(400, 100));
			
		node3.setSize(40, 40);
		node3.setLocation(new Point(250, 300));
		
		conn1 = new PolylineConnectionEx();
		conn1.setSourceAnchor(new ChopboxAnchor(node1));
		conn1.setTargetAnchor(new ChopboxAnchor(node2));
		conn1.setConnectionRouter(new ObliqueRouter());
		PointList points = new PointList();
		points.addPoint(new Point(node1.getBounds().getCenter()));
		points.addPoint(new Point(200, node1.getBounds().getCenter().y));
		points.addPoint(new Point(300, 200));
		points.addPoint(new Point(node2.getBounds().getCenter()));
		conn1.setPoints(points);
		
		ConnectionAnchor anchor = new BaseSlidableAnchor(conn1);
		conn2 = new PolylineConnectionEx();
		conn2.setSourceAnchor(new ChopboxAnchor(node3));
		conn2.setTargetAnchor(anchor);
		
		getRectilinearRouter().route(conn2);
		verifyConnection(conn2);
	}
	
	public void testRouteConnections() {
		Connection conn1;
		Connection conn2;
		Connection conn3;
		Connection conn4;
		
		RectangleFigure node1 = new RectangleFigure(), node2 = new RectangleFigure(), node3 = new RectangleFigure();
		node1.setSize(40, 40);
		node1.setLocation(new Point(100, 200));
			
		node2.setSize(40, 40);
		node2.setLocation(new Point(200, 200));
			
		node3.setSize(40, 40);
		node3.setLocation(new Point(150, 50));
			
		ConnectionAnchor anchor = new FixedPointConnectionAnchor(node3);
		conn1 = new PolylineConnection();
		conn1.setSourceAnchor(new ChopboxAnchor(node1));
		conn1.setTargetAnchor(anchor);
		conn1.setConnectionRouter(getRectilinearRouter());
		PointList points = new PointList();
		points.addPoint(new Point(node1.getBounds().getCenter()));
		points.addPoint(new Point(node1.getBounds().getCenter().x, 150));
		points.addPoint(new Point(node3.getBounds().getCenter()));
		conn1.setPoints(points);
			
		conn2 = new PolylineConnection();
		conn2.setSourceAnchor(new ChopboxAnchor(node2));
		conn2.setTargetAnchor(anchor);
		conn2.setConnectionRouter(getRectilinearRouter());
		points = new PointList();
		points.addPoint(new Point(node1.getBounds().getCenter()));
		points.addPoint(new Point(node3.getBounds().getCenter()));
		conn2.setPoints(points);
			
		RectangleFigure node4 = new RectangleFigure(), node5 = new RectangleFigure();
		node4.setSize(40, 40);
		node4.setLocation(new Point(100, 300));
			
		node5.setSize(40, 40);
		node5.setLocation(new Point(200, 300));
			
		conn3 = new PolylineConnection();
		conn3.setSourceAnchor(new ChopboxAnchor(node4));
		conn3.setTargetAnchor(anchor);
		conn3.setConnectionRouter(getRectilinearRouter());
		points = new PointList();
		points.addPoint(new Point(node4.getBounds().getCenter()));
		points.addPoint(new Point(200, 250));
		points.addPoint(new Point(50, 150));
		points.addPoint(new Point(node3.getBounds().getCenter()));
		conn3.setPoints(points);
			
		conn4 = new PolylineConnection();
		conn4.setSourceAnchor(new ChopboxAnchor(node5));
		conn4.setTargetAnchor(anchor);
		conn4.setConnectionRouter(getRectilinearRouter());
		points = new PointList();
		points.addPoint(new Point(node4.getBounds().getCenter()));
		points.addPoint(new Point(node4.getBounds().getCenter().x, node3.getBounds().getCenter().y));
		points.addPoint(new Point(node3.getBounds().getCenter()));
		conn4.setPoints(points);
		
		getRectilinearRouter().route(conn1);
		getRectilinearRouter().route(conn2);
		getRectilinearRouter().route(conn3);
		getRectilinearRouter().route(conn4);
		
		verifyConnection(conn1);
		verifyConnection(conn2);
		verifyConnection(conn3);
		verifyConnection(conn4);
	}
}