/******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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

import org.eclipse.gmf.runtime.diagram.ui.handles.ConnectionHandle;
import org.eclipse.gmf.runtime.diagram.ui.handles.ConnectionHandleLocator;

/**
 * Unit testing of the ConnectionHandleLocator class.
 * 
 * @author cmahoney
 */
public class ConnectionHandleLocatorTest
	extends TestCase {

	private RectangleFigure rectangle;

	private class FakeConnectionHandle
		extends ConnectionHandle {

		public FakeConnectionHandle() {
			super(null, null, null);
			setSize(1, 1);
		}
	}

	public ConnectionHandleLocatorTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(ConnectionHandleLocatorTest.class);
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp()
		throws Exception {

		rectangle = new RectangleFigure();
		rectangle.setLocation(new Point(100, 100));
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
	 * Tests the public API of the ConnectionHandleLocator class.
	 * 
	 * @throws Exception
	 */
	public void testConnectionHandleLocator()
		throws Exception {

		Object data[][] = {
			{new Point(130, 100), new Integer(PositionConstants.NORTH)},
			{new Point(130, 200), new Integer(PositionConstants.WEST)},
			{new Point(180, 170), new Integer(PositionConstants.EAST)},
			{new Point(140, 290), new Integer(PositionConstants.SOUTH)}};

		for (int i = 0; i < data.length; i++) {
			ConnectionHandleLocator locator = new ConnectionHandleLocator(
				rectangle, (Point) data[i][0]);

			ConnectionHandle handle = new FakeConnectionHandle();

			locator.addHandle(handle);
			locator.relocate(handle);

			assertTrue(rectangle.getBounds().getPosition(handle.getLocation()) == ((Integer) data[i][1])
				.intValue());
			assertTrue(locator.getBorderSide() == ((Integer) data[i][1])
				.intValue());
			assertTrue(!locator.isEastWestSouth());
		}
		for (int i = 0; i < data.length; i++) {
			ConnectionHandleLocator locator = new ConnectionHandleLocator(
				rectangle, (Point) data[i][0]);

			ConnectionHandle handle1 = new FakeConnectionHandle();
			ConnectionHandle handle2 = new FakeConnectionHandle();

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
			{new Point(130, 100), new Integer(PositionConstants.WEST)},
			{new Point(180, 110), new Integer(PositionConstants.EAST)},
			{new Point(130, 200), new Integer(PositionConstants.WEST)},
			{new Point(180, 170), new Integer(PositionConstants.EAST)},
			{new Point(140, 290), new Integer(PositionConstants.SOUTH)}};

		for (int i = 0; i < data.length; i++) {
			ConnectionHandleLocator locator = new ConnectionHandleLocator(
				rectangle, (Point) data2[i][0]);
			locator.setEastWestSouth(true);

			ConnectionHandle handle = new FakeConnectionHandle();

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
	 * Tests the ConnectionHandleLocator class for memory leaks.
	 * 
	 * @throws Exception
	 */
	public void testConnectionHandleLocatorForMemoryLeaks()
		throws Exception {

		ConnectionHandleLocator objTested = new ConnectionHandleLocator(
			rectangle, new Point(10, 10));
		ConnectionHandle handle = new FakeConnectionHandle();
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