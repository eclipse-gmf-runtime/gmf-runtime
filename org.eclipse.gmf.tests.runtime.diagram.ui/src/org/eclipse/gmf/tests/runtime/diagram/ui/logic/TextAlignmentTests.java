/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.logic;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.TextAlignmentAction;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.type.GeoshapeType;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.type.DiagramNotationType;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.notation.TextAlignment;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractTestBase;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.ITestActionCallback;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Tests for the text alignment feature.
 * 
 * @author Anthony Hunter
 */
public class TextAlignmentTests extends AbstractTestBase {

	public static Test suite() {
		TestSuite s = new TestSuite(TextAlignmentTests.class);
		return s;
	}

	public TextAlignmentTests() {
		super("Group Tests");//$NON-NLS-1$
	}

	public class TextAlignmentTestFixture extends LogicTestFixture {

		public void setup() throws Exception {
			closeWelcome();
			super.setup();
		}

		public void closeWelcome() {
			IWorkbench workbench = PlatformUI.getWorkbench();
			if (workbench != null) {
				IWorkbenchWindow workbenchWindow = workbench
						.getActiveWorkbenchWindow();
				if (workbenchWindow != null) {
					IWorkbenchPage workbenchPage = workbenchWindow
							.getActivePage();
					if (workbenchPage != null) {
						IWorkbenchPart workbenchPart = workbenchPage
								.getActivePart();
						if (workbenchPart != null) {
							IWorkbenchPartSite workbenchPartSite = workbenchPart
									.getSite();
							if (workbenchPartSite != null) {
								if (workbenchPartSite.getId().equals(
										"org.eclipse.ui.internal.introview")) { //$NON-NLS-1$
									IViewPart welcomeView = (IViewPart) workbenchPart;
									workbenchPage.hideView(welcomeView);

								}
							}
						}
					}
				}
			}
		}

		protected void createShapesAndConnectors() throws Exception {
			/**
			 * Override so that the test creates the shapes it wants
			 */
		}
	}

	protected void setTestFixture() {
		testFixture = new TextAlignmentTestFixture();
	}

	protected LogicTestFixture getFixture() {
		return (LogicTestFixture) testFixture;
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	/** the note edit part */
	protected ShapeEditPart noteEditPart;

	/** the square edit part */
	protected ShapeEditPart squareEditPart;

	/** the note shape */
	protected View noteView;

	/** the square shape */
	protected View squareView;

	/**
	 * Create the four shapes to be used to test grouping.
	 */
	protected void setupShapes() {

		final String description = "Text align this text\nThe quick brown fox\njumps over\nthe lazy\ndog"; //$NON-NLS-1$

		/* create a square geoshape */
		squareEditPart = getFixture().createShapeUsingTool(
				GeoshapeType.RECTANGLE, new Point(10, 10),
				new Dimension(-1, -1), getDiagramEditPart());

		squareView = (View) squareEditPart.getModel();

		/* set the description (text) for the square geoshape */
		testProperty(squareView, Properties.ID_DESCRIPTION, description);

		/* create a note */
		noteEditPart = getFixture().createShapeUsingTool(
				DiagramNotationType.NOTE, new Point(200, 200),
				new Dimension(-1, -1), getDiagramEditPart());

		noteView = (View) noteEditPart.getModel();
		
		/* set the description (text) for the note */
		testProperty(noteView, Properties.ID_DESCRIPTION, description);

		flushEventQueue();

	}

	public void testSetTextAlignmentCenterAction() throws Exception {
		setupShapes();

		getDiagramEditPart().getViewer().setSelection(
				new StructuredSelection(squareEditPart));

		TextAlignmentAction action = TextAlignmentAction
				.createTextAlignmentCenterAction(getWorkbenchPage());
		testAction(action, new ITestActionCallback() {

			public void onRunExecution() {

				assertEquals(
						TextAlignment.CENTER_LITERAL,
						squareEditPart
								.getStructuralFeatureValue((EStructuralFeature) PackageUtil
										.getElement(Properties.ID_TEXT_ALIGNMENT)));

			}
		});
		
		getCommandStack().undo();
		
		assertEquals(
				TextAlignment.LEFT_LITERAL,
				squareEditPart
						.getStructuralFeatureValue((EStructuralFeature) PackageUtil
								.getElement(Properties.ID_TEXT_ALIGNMENT)));
		
	}

	public void testSetTextAlignmentRightAction() throws Exception {
		setupShapes();

		getDiagramEditPart().getViewer().setSelection(
				new StructuredSelection(noteEditPart));

		TextAlignmentAction action = TextAlignmentAction
				.createTextAlignmentRightAction(getWorkbenchPage());
		testAction(action, new ITestActionCallback() {

			public void onRunExecution() {

				assertEquals(
						TextAlignment.RIGHT_LITERAL,
						noteEditPart
								.getStructuralFeatureValue((EStructuralFeature) PackageUtil
										.getElement(Properties.ID_TEXT_ALIGNMENT)));

			}
		});
		
		getCommandStack().undo();
		
		assertEquals(
				TextAlignment.LEFT_LITERAL,
				noteEditPart
						.getStructuralFeatureValue((EStructuralFeature) PackageUtil
								.getElement(Properties.ID_TEXT_ALIGNMENT)));
		
	}

	public void testSetTextAlignmentProperty() throws Exception {

		setupShapes();

		testProperty(squareView, Properties.ID_TEXT_ALIGNMENT,
				TextAlignment.RIGHT_LITERAL);

		testProperty(squareView, Properties.ID_TEXT_ALIGNMENT,
				TextAlignment.CENTER_LITERAL);

		testProperty(squareView, Properties.ID_TEXT_ALIGNMENT,
				TextAlignment.LEFT_LITERAL);

		testProperty(noteView, Properties.ID_TEXT_ALIGNMENT,
				TextAlignment.RIGHT_LITERAL);

		testProperty(noteView, Properties.ID_TEXT_ALIGNMENT,
				TextAlignment.CENTER_LITERAL);

		testProperty(noteView, Properties.ID_TEXT_ALIGNMENT,
				TextAlignment.LEFT_LITERAL);
	}

}
