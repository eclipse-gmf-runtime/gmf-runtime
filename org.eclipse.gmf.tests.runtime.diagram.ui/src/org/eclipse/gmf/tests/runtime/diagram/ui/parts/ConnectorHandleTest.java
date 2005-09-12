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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.handles.ConnectorHandle;
import org.eclipse.gmf.runtime.diagram.ui.handles.ConnectorHandleLocator;

/**
 * Unit testing of the ConnectorHandle class.
 * 
 * @author cmahoney
 */
public class ConnectorHandleTest
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

	public ConnectorHandleTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(ConnectorHandleTest.class);
	}

	/**
	 * Tests the public API of the ConnectorHandle class.
	 * 
	 * @throws Exception
	 */
	public void testConnectorHandle()
		throws Exception {

		FakeEditPart editpart = new FakeEditPart();
		ConnectorHandle handle = new ConnectorHandle(editpart,
			ConnectorHandle.HandleDirection.OUTGOING, "the tooltip"); //$NON-NLS-1$
		ConnectorHandleLocator locator = new ConnectorHandleLocator(editpart
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

		handle = new ConnectorHandle(editpart,
			ConnectorHandle.HandleDirection.INCOMING, "the tooltip"); //$NON-NLS-1$
		assertTrue(handle.isIncoming());
	}

	/**
	 * Tests the ConnectorHandle class for memory leaks.
	 * 
	 * @throws Exception
	 */
	public void testConnectorHandleForMemoryLeaks()
		throws Exception {

		FakeEditPart editpart = new FakeEditPart();
		ConnectorHandle objTested = new ConnectorHandle(editpart,
			ConnectorHandle.HandleDirection.OUTGOING, "the tooltip"); //$NON-NLS-1$
		ConnectorHandleLocator locator = new ConnectorHandleLocator(editpart
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