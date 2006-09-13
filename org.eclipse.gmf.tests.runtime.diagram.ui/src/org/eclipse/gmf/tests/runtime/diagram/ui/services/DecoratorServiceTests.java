/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.services;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.decorator.Decoration;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.AbstractDecorator;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.CreateDecoratorsOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoration;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractTestBase;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.PresentationTestFixture;

/**
 * Tests for the Decorator Service.
 * 
 * @author cmahoney
 */
public class DecoratorServiceTests
	extends AbstractTestBase {

	/**
	 * Provides decorators for this test. This class is referenced in the
	 * plugin.xml file.
	 * 
	 * @author cmahoney
	 */
	public static class TestDecoratorProvider
		extends AbstractProvider
		implements IDecoratorProvider {

		public static final String TEST_DECORATOR = "TestDecorator"; //$NON-NLS-1$

		/**
		 * Is this provider active? False by default.
		 */
		private static boolean isActive = false;

		/**
		 * Is this provider active?
		 * 
		 * @return true if this provider is active; false otherwise
		 */
		public static boolean isActive() {
			return isActive;
		}

		/**
		 * Sets the flag indicating if this provider is active.
		 * 
		 * @param isProviderActive
		 *            true if this provider is active; false otherwise
		 */
		public static void setActive(boolean isProviderActive) {
			isActive = isProviderActive;
		}

		/**
		 * Cache the one and only decorator.
		 */
		private static NoteDecorator myNoteDecorator;

		/**
		 * Gets the one and only decorator for testing purposes.
		 * 
		 * @return the decorator
		 */
		public static NoteDecorator getMyNoteDecorator() {
			return myNoteDecorator;
		}

		public static void deactivate() {
			TestDecoratorProvider.myNoteDecorator = null;
		}

		public TestDecoratorProvider() {
			super();
			myNoteDecorator = null;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorProvider#createDecorators(org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget)
		 */
		public void createDecorators(IDecoratorTarget decoratorTarget) {
			NoteEditPart noteEP = (NoteEditPart) decoratorTarget
				.getAdapter(NoteEditPart.class);
			if (noteEP != null) {
				if (myNoteDecorator == null) {
					myNoteDecorator = new NoteDecorator(decoratorTarget);
				}
				decoratorTarget.installDecorator(TEST_DECORATOR,
					myNoteDecorator);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gmf.runtime.common.core.internal.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
		 */
		public boolean provides(IOperation operation) {
			if (!isActive()) {
				return false;
			}

			IDecoratorTarget decoratorTarget = ((CreateDecoratorsOperation) operation)
				.getDecoratorTarget();
			return decoratorTarget.getAdapter(NoteEditPart.class) != null;
		}
	}

	/**
	 * Decorates notes with a circle figure.
	 * 
	 * @author cmahoney
	 */
	public static class NoteDecorator
		extends AbstractDecorator {

		static final int DIAMETER = 20;

		/**
		 * True if the decoration's visibility is not to be affected by the
		 * parent's visibility; false otherwise.
		 */
		static boolean ignoreParentVisibility = false;

		/**
		 * Creates a new instance.
		 * 
		 * @param decoratorTarget
		 */
		public NoteDecorator(IDecoratorTarget decoratorTarget) {
			super(decoratorTarget);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gmf.runtime.diagram.ui.internal.services.decorator.IDecoratorBase#activate()
		 */
		public void activate() {
			refresh();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gmf.runtime.diagram.ui.internal.services.decorator.IDecoratorBase#refresh()
		 */
		public void refresh() {
			removeDecoration();
			NoteEditPart noteEP = (NoteEditPart) getDecoratorTarget()
				.getAdapter(NoteEditPart.class);
			if (noteEP != null) {
				Ellipse circle = new Ellipse();
				circle.setSize(DIAMETER, DIAMETER);
				IDecoration decoration = getDecoratorTarget()
					.addShapeDecoration(circle,
						IDecoratorTarget.Direction.NORTH_EAST,
						MapModeUtil.getMapMode(noteEP.getFigure()).DPtoLP(-4), false);
				decoration.setIgnoreParentVisibility(ignoreParentVisibility);
				setDecoration(decoration);
			}
		}

	}

	public DecoratorServiceTests(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(DecoratorServiceTests.class);
	}

	protected void setUp()
		throws Exception {
		super.setUp();
		TestDecoratorProvider.setActive(true);
		NoteDecorator.ignoreParentVisibility = false;
	}

	protected void tearDown()
		throws Exception {
		super.tearDown();
		TestDecoratorProvider.setActive(false);
		TestDecoratorProvider.deactivate();
	}

	protected void setTestFixture() {
		testFixture = new PresentationTestFixture();
	}

	protected PresentationTestFixture getFixture() {
		return (PresentationTestFixture) testFixture;
	}

	/**
	 * Some basic decorator tests.
	 * 
	 * @throws Exception
	 */
	public void testNoteDecorator()
		throws Exception {

		getFixture().createNote();

		NoteDecorator myDecorator = TestDecoratorProvider.getMyNoteDecorator();
		Decoration decoration = myDecorator.getDecoration();
		assertNotNull(decoration);

		NoteEditPart noteEP = (NoteEditPart) getDiagramEditPart()
			.getPrimaryEditParts().get(0);
		assertEquals(noteEP.getFigure(), decoration.getOwnerFigure());

		assertTrue(decoration.isVisible());
		assertTrue(noteEP.getFigure().getBounds().contains(
			decoration.getBounds()));
		assertEquals(NoteDecorator.DIAMETER, decoration.getSize().height);

		// verify decoration is hidden if owner figure is not visible
		noteEP.getFigure().setVisible(false);
		assertFalse(decoration.isVisible());

		// bring back to visible
		noteEP.getFigure().setVisible(true);
		assertTrue(decoration.isVisible());

		// verify decoration is removed when shape is deleted
		noteEP.getCommand(new GroupRequest(RequestConstants.REQ_DELETE))
			.execute();
		decoration = myDecorator.getDecoration();
		assertNull(decoration);
	}

	/**
	 * Tests the ability to have the decoration continue to appear even if the
	 * parent is made invisible. See RATLC00538197.
	 * 
	 * @throws Exception
	 */
	public void testIgnoreParentVisibility()
		throws Exception {

		NoteDecorator.ignoreParentVisibility = true;

		getFixture().createNote();

		NoteDecorator myDecorator = TestDecoratorProvider.getMyNoteDecorator();
		Decoration decoration = myDecorator.getDecoration();
		assertNotNull(decoration);

		NoteEditPart noteEP = (NoteEditPart) getDiagramEditPart()
			.getPrimaryEditParts().get(0);
		assertEquals(noteEP.getFigure(), decoration.getOwnerFigure());

		assertTrue(decoration.isVisible());
		assertTrue(noteEP.getFigure().getBounds().contains(
			decoration.getBounds()));
		assertEquals(NoteDecorator.DIAMETER, decoration.getSize().height);

		// verify decoration is not hidden if owner figure is not visible
		noteEP.getFigure().setVisible(false);
		assertTrue(decoration.isVisible());

	}

}
