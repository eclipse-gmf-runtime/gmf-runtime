/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.handles.ConnectionHandle;
import org.eclipse.gmf.runtime.diagram.ui.handles.ConnectionHandleLocator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Unit testing of the ConnectionHandle class.
 * 
 * @author cmahoney
 */
public class ConnectionHandleTest
	extends TestCase {

	private class FakeEditPart
		extends ShapeEditPart {

		public FakeEditPart() {
			super(null);
		}

		protected IFigure createFigure() {
			RectangleFigure rectangle = new RectangleFigure();
			rectangle.setLocation(new Point(0, 0));
			rectangle.setSize(100, 200);
			return rectangle;
		}

	}

	public ConnectionHandleTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(ConnectionHandleTest.class);
	}

	/**
	 * Tests the public API of the ConnectionHandle class.
	 * 
	 * @throws Exception
	 */
	public void testConnectionHandle()
		throws Exception {

		FakeEditPart editpart = new FakeEditPart();
		ConnectionHandle handle = new ConnectionHandle(editpart,
			ConnectionHandle.HandleDirection.OUTGOING, "the tooltip"); //$NON-NLS-1$
		ConnectionHandleLocator locator = new ConnectionHandleLocator(editpart
			.getFigure(), new Point(90, 90));

		handle.setLocator(locator);

		assertTrue(handle.getChildren().isEmpty());

		handle.validate();

		assertTrue(handle.getChildren().size() == 1);
		assertTrue(handle.getLocator() == locator);
		assertTrue(handle.getOwner() == editpart);
		assertTrue(!handle.isIncoming());
		assertTrue(editpart.getFigure().getBounds().getPosition(
			handle.getBounds().getCenter()) == PositionConstants.EAST);
		assertTrue(handle.findFigureAt(handle.getBounds().getCenter()) == handle);

		handle.addErrorIcon();

		assertTrue(handle.getChildren().size() == 2);

		handle.removeErrorIcon();

		assertTrue(handle.getChildren().size() == 1);

		handle = new ConnectionHandle(editpart,
			ConnectionHandle.HandleDirection.INCOMING, "the tooltip"); //$NON-NLS-1$
		assertTrue(handle.isIncoming());
	}

	/**
	 * Tests the ConnectionHandle class for memory leaks.
	 * 
	 * @throws Exception
	 */
	public void testConnectionHandleForMemoryLeaks()
		throws Exception {

		FakeEditPart editpart = new FakeEditPart();
		ConnectionHandle objTested = new ConnectionHandle(editpart,
			ConnectionHandle.HandleDirection.OUTGOING, "the tooltip"); //$NON-NLS-1$
		ConnectionHandleLocator locator = new ConnectionHandleLocator(editpart
			.getFigure(), new Point(90, 90));
		objTested.setLocator(locator);
		objTested.validate();

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