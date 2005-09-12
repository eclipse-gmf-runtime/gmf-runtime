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

package org.eclipse.gmf.tests.runtime.diagram.ui.parts;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gmf.runtime.diagram.ui.handles.ConnectorHandle;
import org.eclipse.gmf.runtime.diagram.ui.handles.ConnectorHandleLocator;

/**
 * Unit testing of the ConnectorHandleLocator class.
 * 
 * @author cmahoney
 */
public class ConnectorHandleLocatorTest
	extends TestCase {

	private RectangleFigure rectangle;

	private class FakeConnectorHandle
		extends ConnectorHandle {

		public FakeConnectorHandle() {
			super(null, null, null);
			setSize(1, 1);
		}
	}

	public ConnectorHandleLocatorTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(ConnectorHandleLocatorTest.class);
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp()
		throws Exception {

		rectangle = new RectangleFigure();
		rectangle.setLocation(new Point(0, 0));
		rectangle.setSize(100, 200);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown()
		throws Exception {

		rectangle = null;
	}

	/**
	 * Tests the public API of the ConnectorHandleLocator class.
	 * 
	 * @throws Exception
	 */
	public void testConnectorHandleLocator()
		throws Exception {

		Object data[][] = {
			{new Point(30, 0), new Integer(PositionConstants.NORTH)},
			{new Point(30, 100), new Integer(PositionConstants.WEST)},
			{new Point(80, 70), new Integer(PositionConstants.EAST)},
			{new Point(40, 190), new Integer(PositionConstants.SOUTH)}};

		for (int i = 0; i < data.length; i++) {
			ConnectorHandleLocator locator = new ConnectorHandleLocator(
				rectangle, (Point) data[i][0]);

			ConnectorHandle handle = new FakeConnectorHandle();

			locator.addHandle(handle);
			locator.relocate(handle);

			assertTrue(rectangle.getBounds().getPosition(handle.getLocation()) == ((Integer) data[i][1])
				.intValue());
			assertTrue(locator.getBorderSide() == ((Integer) data[i][1])
				.intValue());
			assertTrue(!locator.isEastWestSouth());
		}
		for (int i = 0; i < data.length; i++) {
			ConnectorHandleLocator locator = new ConnectorHandleLocator(
				rectangle, (Point) data[i][0]);

			ConnectorHandle handle1 = new FakeConnectorHandle();
			ConnectorHandle handle2 = new FakeConnectorHandle();

			locator.addHandle(handle1);
			locator.addHandle(handle2);
			locator.relocate(handle1);
			locator.relocate(handle2);

			int expectedSide = ((Integer) data[i][1]).intValue();
			if (expectedSide != PositionConstants.NORTH) { // North will be off
				// to left.
				assertTrue(rectangle.getBounds().getPosition(
					handle1.getLocation()) == expectedSide);
				assertTrue(rectangle.getBounds().getPosition(
					handle2.getLocation()) == expectedSide);
			}
			assertTrue(locator.getBorderSide() == expectedSide);
			if (expectedSide == PositionConstants.NORTH
				|| expectedSide == PositionConstants.SOUTH) {
				assertTrue(handle1.getLocation().y == handle2.getLocation().y);
				assertTrue(handle1.getLocation().x != handle2.getLocation().x);
			} else {
				assertTrue(handle1.getLocation().y != handle2.getLocation().y);
				assertTrue(handle1.getLocation().x == handle2.getLocation().x);
			}
		}

		Object data2[][] = {
			{new Point(30, 0), new Integer(PositionConstants.WEST)},
			{new Point(80, 10), new Integer(PositionConstants.EAST)},
			{new Point(30, 100), new Integer(PositionConstants.WEST)},
			{new Point(80, 70), new Integer(PositionConstants.EAST)},
			{new Point(40, 190), new Integer(PositionConstants.SOUTH)}};

		for (int i = 0; i < data.length; i++) {
			ConnectorHandleLocator locator = new ConnectorHandleLocator(
				rectangle, (Point) data2[i][0]);
			locator.setEastWestSouth(true);

			ConnectorHandle handle = new FakeConnectorHandle();

			locator.addHandle(handle);
			locator.relocate(handle);

			assertTrue(rectangle.getBounds().getPosition(handle.getLocation()) == ((Integer) data2[i][1])
				.intValue());
			assertTrue(locator.getBorderSide() == ((Integer) data2[i][1])
				.intValue());
			assertTrue(locator.isEastWestSouth());
		}
	}

	/**
	 * Tests the ConnectorHandleLocator class for memory leaks.
	 * 
	 * @throws Exception
	 */
	public void testConnectorHandleLocatorForMemoryLeaks()
		throws Exception {

		ConnectorHandleLocator objTested = new ConnectorHandleLocator(
			rectangle, new Point(10, 10));
		ConnectorHandle handle = new FakeConnectorHandle();
		objTested.addHandle(handle);
		objTested.relocate(handle);

		ReferenceQueue spy = new ReferenceQueue();
		PhantomReference refObjTested = new PhantomReference(objTested, spy);

		// Release the reference to object tested.
		objTested = null;

		System.gc();

		if (!refObjTested.isEnqueued()) {
			System.runFinalization();
		}

		assertTrue(refObjTested.isEnqueued());

		Reference removedRef = spy.remove();
		if (removedRef != null)
			removedRef.clear();
	}

}