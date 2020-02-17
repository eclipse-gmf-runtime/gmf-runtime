/******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.draw2d.ui;

import junit.framework.TestCase;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;


/**
 * @author jschofie
 *
 * This test case is intended to test the LineSeg Class functionality
 */
public class LineSegTest extends TestCase {

	private LineSeg lineSeg;
	private LineSeg verticalLine;
	private LineSeg horizontalLine;

	private Point origin;
	private Point terminus;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
		origin = new Point( 5, 5 );
		terminus = new Point( 10, 10 );
		
		lineSeg = new LineSeg( origin, terminus );
		
		verticalLine = new LineSeg( new Point( 0, 5 ), new Point( 0, 105 ) );
		horizontalLine = new LineSeg( new Point( 0, 5 ), new Point( 100, 5 ) );
	}
	
	/*
	 * test the equals method
	 */
	public void testEqualsObject() {
		
		LineSeg seg1 = new LineSeg( new Point( 0, 0 ), new Point( 10, 10 ) );
		LineSeg seg2 = new LineSeg( new Point( 1, 1 ), new Point( 9, 9 ) );
		LineSeg seg3 = new LineSeg( new Point( 0, 0 ), new Point( 5, 5 ) );
		LineSeg seg4 = new LineSeg( new Point( 0, 0 ), new Point( 10, 10 ) );

		// Both line segment points are not equal
		assertFalse( seg1.equals( seg2 ) );
		
		// The origin points are equal but the ends are not
		assertFalse( seg1.equals( seg3 ) );
		
		// Both the origin and end points are equal
		assertTrue( seg1.equals( seg4 ) );
	}

	public void testGetOrigin() {
		assertEquals( origin, lineSeg.getOrigin() );
	}

	public void testGetTerminus() {
		assertEquals( terminus, lineSeg.getTerminus() );
	}

	public void testSetOrigin() {
		Point p1 = new Point( -5, -5 );
		
		lineSeg.setOrigin( p1 );
		assertEquals( p1, lineSeg.getOrigin() );
	}

	public void testSetTerminus() {
		Point p1 = new Point( -5, -5 );
		
		lineSeg.setTerminus( p1 );
		assertEquals( p1, lineSeg.getTerminus() );
	}

	public void testGetSupremum() {	
		assertEquals( terminus.x, lineSeg.getSupremum().x );
		assertEquals( terminus.y, lineSeg.getSupremum().y );
	}

	public void testGetInfimum() {
		assertEquals( origin.x, lineSeg.getInfimum().x );
		assertEquals( origin.y, lineSeg.getInfimum().y );
	}

	public void testIsHorizontal() {
		assertFalse( "verticalLine shouldn't be horizontal", verticalLine.isHorizontal() ); //$NON-NLS-1$
		assertTrue( "horizontalLine should be horizontal", horizontalLine.isHorizontal() ); //$NON-NLS-1$
	}

	public void testIsVertical() {
		assertTrue( "verticalLine should be vertical", verticalLine.isVertical() ); //$NON-NLS-1$
		assertFalse( "horizontalLine shouldn't be vertical", horizontalLine.isVertical() ); //$NON-NLS-1$
	}

	public void testSlope() {
	
		// Line is vertical
		assertTrue( "The line should be vertical", 9999 == verticalLine.slope() ); //$NON-NLS-1$

		// Line is horizontal
		assertTrue( "The line should be horizontal", 0.0 == horizontalLine.slope() ); //$NON-NLS-1$
	}

	public void testPerpSlope() {
	
		// Line is vertical
		assertEquals( "Perp Slope of a vertical line should be horizontal", 0, (int)verticalLine.perpSlope() ); //$NON-NLS-1$

		// Line is horizontal
		assertEquals( "Perp slope of a horizontal line should be vertical", 9999, (int)horizontalLine.perpSlope() ); //$NON-NLS-1$
	}

	public void testLength() {
		
		// Compute length of the vertical line
		assertEquals( "Vertical line seg length is not correct", 100, (int)verticalLine.length() ); //$NON-NLS-1$
		
		// Compute the length of the horizontal line
		assertEquals( "Horizontal line seg length is not correct", 100, (int)horizontalLine.length() ); //$NON-NLS-1$
	}

	public void testContainsPoint() {

		Point isContained  = new Point( 6, 6 );
		Point notContained = new Point( 3, 3 );
		
		assertTrue( lineSeg.containsPoint( isContained, 0 ) );
		assertFalse( lineSeg.containsPoint( notContained, 0 ) );
	}

	public void testDistanceAlong() {

		// Test nominal case
		assertEquals( "The distance has been incorrectly computed", 0, (int)lineSeg.distanceAlong( new Point( 5, 5 ) ) ); //$NON-NLS-1$
	
		// Test divide by zero
		Point p2 = new Point( 10, 10 );
		LineSeg seg2 = new LineSeg( p2, p2 );
		assertEquals( -1, (int)seg2.distanceAlong( new Point( 2, 2 ) ) );
	}

//	public void testIntersect() {
//	}
//
//	public void testDistanceToPoint() {
//	}
//
//	public void testPerpIntersect() {
//	}
//
//	public void testProjection() {
//	}
//
//	public void testPositionRelativeTo() {
//	}
//
//	public void testLocatePoint() {
//	}
//
//	public void testPointOn() {
//	}
//
//	public void testGetTrigValues() {
//	}
//
//	public void testGetParallelLineSegThroughPoint() {
//	}
	
	public void testLineEquation() {
		// Normal case
		Point p1 = new Point(1, 0);
		Point p2 = new Point(2, 1);
		LineSeg line = new LineSeg(p1,p2);
		double[] equation = line.getEquation();
		assertTrue("Equation is incomplete", equation.length == 3); //$NON-NLS-1$
		assertTrue("Coefficient 'a' is not correct", equation[0] == -1); //$NON-NLS-1$
		assertTrue("Coefficient 'b' is not correct", equation[1] == 1); //$NON-NLS-1$
		assertTrue("Coefficient 'c' is not correct", equation[2] == -1); //$NON-NLS-1$
	}

	public void testFindLineIntersections() {
		final PointList points = new PointList();
		points.addPoint(new Point(0, 10));
		points.addPoint(new Point(10, 0));
		points.addPoint(new Point(0, -10));
		points.addPoint(new Point(-10, 0));
		points.addPoint(new Point(0, 10));

		// Nominal case, i.e. 2 intersection points
		Point p1 = new Point(-20, 20);
		Point p2 = new Point(30, -30);
		LineSeg line = new LineSeg(p1,p2);
		PointList intersections = line.getLineIntersectionsWithLineSegs(points);
		assertTrue(
			"Line passing through " + p1 + " and " + p2 + "must have exactly 2 intersections with the figure", intersections.size() == 2); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		Point first = PointListUtilities.pickClosestPoint(intersections, p2);
		assertTrue(
			"This is not the closest intersection to " + p2, first.x == 5 && first.y == -5); //$NON-NLS-1$
		Point second = PointListUtilities.pickFarestPoint(intersections, p2);
		assertTrue(
			"This is not the farest intersection to " + p2, second.x == -5 && second.y == 5); //$NON-NLS-1$

		// 1 intersection point
		p1 = new Point(10, 10);
		p2 = new Point(10, -10);
		line = new LineSeg(p1,p2);
		intersections = line.getLineIntersectionsWithLineSegs(points);
		assertTrue(
			"Line passing through " + p1 + " and " + p2 + "must have exactly 1 intersections with the figure", intersections.size() == 2); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		first = PointListUtilities.pickClosestPoint(intersections, p2);
		second = PointListUtilities.pickFarestPoint(intersections, p2);
		assertTrue(second.x == first.x && second.y == first.y); 
		assertTrue(
			"Incorrect value for intersection point", first.x == 10 && first.y == 0); //$NON-NLS-1$

		// 0 intersection points
		p1 = new Point(20, 20);
		p2 = new Point(-10, 10);
		line = new LineSeg(p1,p2);
		intersections = line.getLineIntersectionsWithLineSegs(points);
		assertTrue(
			"Line passing through " + p1 + " and " + p2 + "must have no intersections with the figure", intersections.size() == 0); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		// Infinite number of intersections => must result in the two points
		// (ends of the linesegment
		// that lies on the line that passes through p1 and p2)
		p1 = new Point(20, -10);
		p2 = new Point(-10, 20);
		line = new LineSeg(p1,p2);
		intersections = line.getLineIntersectionsWithLineSegs(points);
		assertTrue(
			"Line passing through " + p1 + " and " + p2 + "must have exactly 2 intersections with the figure", intersections.size() == 4); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		first = PointListUtilities.pickClosestPoint(intersections, p2);
		assertTrue(
			"This is not the closest intersection to " + p2, first.x == 0 && first.y == 10); //$NON-NLS-1$
		second = PointListUtilities.pickFarestPoint(intersections, p2);
		assertTrue(
			"This is not the farest intersection to " + p2, second.x == 10 && second.y == 0); //$NON-NLS-1$
	}

	public void testAllLineIntersectionsWithEllipse() {
		Rectangle oval = new Rectangle(Rectangle.SINGLETON);
		oval.setLocation(-2, -1);
		oval.setSize(4, 2);

		// 2 points intersection
		Point p1 = new Point(0, 0);
		Point p2 = new Point(3, 0);
		LineSeg line = new LineSeg(p1,p2);
		PointList intersections = line.getLineIntersectionsWithEllipse(oval);
		assertTrue(
			"Must have 2 intersections with line passing through points " + p1 + " and " + p2, intersections.size() == 2); //$NON-NLS-1$//$NON-NLS-2$
		Point first = PointListUtilities.pickClosestPoint(intersections, p2);
		assertTrue(
			"This is not the closes intersection to " + p2, first.x == 2 && first.y == 0); //$NON-NLS-1$
		Point second = PointListUtilities.pickFarestPoint(intersections, p2);
		assertTrue(
			"This is not the farest intersection to " + p2, second.x == -2 && second.y == 0); //$NON-NLS-1$

		// 1 point intersection
		p1 = new Point(2, -2);
		p2 = new Point(2, 2);
		line = new LineSeg(p1,p2);
		intersections = line.getLineIntersectionsWithEllipse(oval);
		assertTrue(
			"Line passing through " + p1 + " and " + p2 + "must have exactly 1 intersections with the figure", intersections.size() == 2); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		first = PointListUtilities.pickClosestPoint(intersections, p2);
		second = PointListUtilities.pickFarestPoint(intersections, p2);
		assertTrue(second.x == first.x && second.y == first.y); 
		assertTrue(
			"Incorrect value for intersection point", first.x == 2 && first.y == 0); //$NON-NLS-1$

		// no intersections
		p1 = new Point(3, 0);
		p2 = new Point(0, 3);
		line = new LineSeg(p1,p2);
		intersections = line.getLineIntersectionsWithEllipse(oval);
		assertTrue(
			"Line passing through " + p1 + " and " + p2 + "must have no intersections with the figure", intersections.size() == 0); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}
