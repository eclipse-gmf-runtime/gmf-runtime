/******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.logic;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.LEDEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.TerminalEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.LED;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractTestBase;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.ITestCommandCallback;

/**
 * Tests the
 * {@link org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer}
 * class.
 * 
 * @author cmahoney
 */
public class DiagramGraphicalViewerTests
	extends AbstractTestBase {

	public static Test suite() {
		TestSuite s = new TestSuite(DiagramGraphicalViewerTests.class);
		return s;
	}

	public DiagramGraphicalViewerTests() {
		super("Diagram Graphical Viewer Test Suite");//$NON-NLS-1$
	}

	protected void setTestFixture() {
		testFixture = new LogicTestFixture();
	}

	/** Returns the logic test fixture */
	protected LogicTestFixture getLogicTestFixture() {
		return (LogicTestFixture) getTestFixture();
	}

	/**
	 * Tests the ID to EditPart registry in the
	 * <code>DiagramGraphicalViewer</code>.
	 * 
	 * @throws Exception
	 */
	public void testIDToEditPartRegistry()
		throws Exception {

		final LogicTestFixture fixture = getLogicTestFixture();
		fixture.openDiagram();

		Rectangle rect = new Rectangle(getDiagramEditPart().getFigure()
			.getBounds());
		getDiagramEditPart().getFigure().translateToAbsolute(rect);
		IElementType typeLED = ElementTypeRegistry.getInstance().getType(
			"logic.led");//$NON-NLS-1$
		IElementType typeWire = ElementTypeRegistry.getInstance().getType(
			"logic.wire");//$NON-NLS-1$

		Point createPt = new Point(100, 100);
		LEDEditPart led1EP = (LEDEditPart) getLogicTestFixture()
			.createShapeUsingTool(typeLED, createPt, getDiagramEditPart());
		createPt.getTranslated(led1EP.getFigure().getSize().getExpanded(100,
			100));

		LEDEditPart led2EP = (LEDEditPart) getLogicTestFixture()
			.createShapeUsingTool(typeLED, createPt, getDiagramEditPart());
		createPt.getTranslated(led2EP.getFigure().getSize().getExpanded(100,
			100));

		final IDiagramGraphicalViewer viewer = (IDiagramGraphicalViewer) getDiagramEditPart()
			.getViewer();

		EObject led1 = led1EP.getNotationView()
		.getElement();
		EObject led2 = led2EP.getNotationView()
		.getElement();
		final String led1ID = ((XMLResource) led1.eResource()).getID(led1);
		final String led2ID = ((XMLResource) led2.eResource()).getID(led2);

		// Create a wire relationship between an outgoing terminal on LED1 and
		// an incoming terminal on LED2.
		EObject outputTerminal = (EObject) ((LED) ((View) led1EP.getModel())
				.getElement()).getOutputTerminals().get(0);
		TerminalEditPart outputTerminalEP = (TerminalEditPart) viewer
			.findEditPartsForElement(
				((XMLResource) outputTerminal.eResource()).getID(outputTerminal),
				TerminalEditPart.class).get(0);

		EObject inputTerminal = (EObject) ((LED) ((View) led2EP.getModel())
				.getElement()).getInputTerminals().get(0);
		TerminalEditPart inputTerminalEP = (TerminalEditPart) viewer
			.findEditPartsForElement(
				((XMLResource) inputTerminal.eResource()).getID(inputTerminal),
				TerminalEditPart.class).get(0);
		ConnectionEditPart wireEP = getLogicTestFixture()
			.createConnectorUsingTool(outputTerminalEP, inputTerminalEP,
				typeWire);

		EObject wire = ((View) wireEP.getModel()).getElement();
		String wireID = ((XMLResource) wire.eResource()).getID(wire);

		assertEquals("Number of LEDEditPart for led1ID incorrect", 1, viewer//$NON-NLS-1$
			.findEditPartsForElement(led1ID, LEDEditPart.class).size());
		assertEquals("Number of ConnectionEditParts for led1ID incorrect", 0,//$NON-NLS-1$
			viewer.findEditPartsForElement(led1ID, ConnectionEditPart.class)
				.size());

		View shape3 = createShapeView(getDiagramEditPart(), ViewUtil
			.resolveSemanticElement(led2EP.getNotationView()), new Point(100,
			200));

		assertEquals("Number of ShapeEditParts for led2ID incorrect", 2, viewer//$NON-NLS-1$
			.findEditPartsForElement(led2ID, ShapeEditPart.class).size());

		DeleteCommand delete = new DeleteCommand(getLogicTestFixture().getEditingDomain(), shape3);
		testCommand(delete, new ITestCommandCallback() {

			public void onCommandExecution() {
				assertEquals("Number of LEDEditParts for led1ID incorrect", 1,//$NON-NLS-1$
					viewer.findEditPartsForElement(led1ID, LEDEditPart.class)
						.size());
				assertEquals("Number of LEDEditParts for led2ID incorrect", 1,//$NON-NLS-1$
					viewer.findEditPartsForElement(led2ID, LEDEditPart.class)
						.size());
			}
		});
		clearDiagram();
		assertEquals("Number of IGraphicalEditParts for led1ID incorrect", 0,//$NON-NLS-1$
			viewer.findEditPartsForElement(led1ID, IGraphicalEditPart.class)
				.size());
		assertEquals("Number of IGraphicalEditParts for led2ID incorrect", 0,//$NON-NLS-1$
			viewer.findEditPartsForElement(led2ID, IGraphicalEditPart.class)
				.size());
		assertEquals("Number of IGraphicalEditParts for wireID incorrect", 0,//$NON-NLS-1$
			viewer.findEditPartsForElement(wireID, IGraphicalEditPart.class)
				.size());
	}

}
