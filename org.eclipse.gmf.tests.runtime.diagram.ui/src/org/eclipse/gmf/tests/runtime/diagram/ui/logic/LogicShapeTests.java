/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.CircuitEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.LEDEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers.LogicConstants;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.ISurfaceEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ApplyAppearancePropertiesRequest;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.ShapeStyle;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractShapeTests;
import org.eclipse.swt.graphics.Color;


public class LogicShapeTests extends AbstractShapeTests {

	public LogicShapeTests(String arg0) {
		super(arg0);
	}

	public static Test suite() {
		return new TestSuite(LogicShapeTests.class);
	}

	protected void setTestFixture() {
		testFixture = new LogicTestFixture();
	}
	
	/** Return <code>(CanonicalTestFixture)getTestFixture();</code> */
	protected LogicTestFixture getLogicTestFixture() {
		return (LogicTestFixture)getTestFixture();
	}
	
	/**
	 * Test to verify that copy appearance properties is working properly
	 * @throws Exception
	 */
	public void testCopyAppearanceProperties()
		throws Exception {
		
		Rectangle rect = new Rectangle(getDiagramEditPart().getFigure().getBounds());
		getDiagramEditPart().getFigure().translateToAbsolute(rect);
		IElementType typeLED = ElementTypeRegistry.getInstance().getType("logic.led"); //$NON-NLS-1$
		
		Point createPt = new Point(100, 100);
		final LEDEditPart ledEP1 = (LEDEditPart)getLogicTestFixture().createShapeUsingTool(typeLED, createPt, getDiagramEditPart());
		createPt.getTranslated(ledEP1.getFigure().getSize().getExpanded(100, 100));
		
		final LEDEditPart ledEP2 = (LEDEditPart)getLogicTestFixture().createShapeUsingTool(typeLED, createPt, getDiagramEditPart());
		final Color red = new Color(null, 255, 0, 0);
		final int fontHeight = 10;
		
		getLogicTestFixture().execute(new AbstractModelCommand("", null) { //$NON-NLS-1$
			protected CommandResult doExecute(IProgressMonitor progressMonitor) {
				View ledView = ledEP2.getNotationView();
				ShapeStyle shapeStyle = (ShapeStyle)ledView.getStyle(NotationPackage.eINSTANCE.getShapeStyle());
				shapeStyle.setFillColor((FigureUtilities.colorToInteger(red)).intValue());
				shapeStyle.setLineColor((FigureUtilities.colorToInteger(red)).intValue());
				shapeStyle.setFontColor((FigureUtilities.colorToInteger(red)).intValue());
				shapeStyle.setFontHeight(fontHeight);
				return newOKCommandResult();
			}
		});
		
		ApplyAppearancePropertiesRequest request = new ApplyAppearancePropertiesRequest();;
		request.setViewToCopyFrom(ledEP2.getNotationView());
		Command cmd = ledEP1.getCommand(request);
		getCommandStack().execute(cmd);
		flushEventQueue();
		
		MEditingDomainGetter.getMEditingDomain((View)ledEP1.getModel()).runAsRead( new MRunnable() {
			public Object run() {
				View ledView = ledEP1.getNotationView();
				ShapeStyle shapeStyle = (ShapeStyle)ledView.getStyle(NotationPackage.eINSTANCE.getShapeStyle());
				assertTrue(shapeStyle.getFillColor() == ((FigureUtilities.colorToInteger(red)).intValue()));
				assertTrue(shapeStyle.getLineColor() == ((FigureUtilities.colorToInteger(red)).intValue()));
				assertTrue(shapeStyle.getFontColor() == ((FigureUtilities.colorToInteger(red)).intValue()));
				assertTrue(shapeStyle.getFontHeight() == fontHeight);
				return null;
			}
		});
	}
	
	/**
	 * Tests <code>ISurfaceEditPart.getPrimaryEditParts()</code> by creating a
	 * half-adder.
	 * 
	 * @throws Exception
	 */
	public void testGetPrimaryEditParts()
		throws Exception {

		Rectangle rect = new Rectangle(getDiagramEditPart().getFigure()
			.getBounds());
		getDiagramEditPart().getFigure().translateToAbsolute(rect);
		IElementType typeHalfAdder = ElementTypeRegistry.getInstance().getType(
			"logic.halfAdder"); //$NON-NLS-1$

		Point createPt = new Point(100, 100);
		CircuitEditPart circuitEP = (CircuitEditPart) getLogicTestFixture()
			.createShapeUsingTool(typeHalfAdder, createPt, getDiagramEditPart());

		ISurfaceEditPart logicCompartmentEP = (ISurfaceEditPart) circuitEP
			.getChildBySemanticHint(LogicConstants.LOGIC_SHAPE_COMPARTMENT);
		assertEquals(8, logicCompartmentEP.getPrimaryEditParts().size());
	}
}
