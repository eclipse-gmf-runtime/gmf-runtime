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


import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.LEDEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.TerminalEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Circuit;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.LED;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Terminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Wire;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IResizableCompartmentEditPart;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractTestBase;

/**
 * Tests the canonical editpolicies installed on the class attribute and 
 * operation list compartments.
 * @author mhanner
 */
public class LogicCanonicalTests extends AbstractTestBase {
	
	/**
	 * Defines the statechart diagram test suite.
	 * 
	 * @return the test suite.
	 */
	public static Test suite() {
		TestSuite s = new TestSuite(LogicCanonicalTests.class);
		return s;
	}
	
	/** Create an instance. */
	public LogicCanonicalTests() {
		super("Canonical Test Suite");//$NON-NLS-1$
	}

	/** installs the composite state test fixture. */
	protected void setTestFixture() {
		testFixture = new CanonicalTestFixture();
	}

	/** Return <code>(CanonicalTestFixture)getTestFixture();</code> */
	protected CanonicalTestFixture getCanonicalTestFixture() {
		return (CanonicalTestFixture)getTestFixture();
	}
	
	/** 
	 * Tests the ability to disable the canonical editpolicy on  the 
	 * attribute list compartment.
	 */
	public void test_DisableCanonical() {
		try {
			println("test_DisableCanonical() starting ...");//$NON-NLS-1$
			CanonicalTestFixture _testFixture = getCanonicalTestFixture();
			IGraphicalEditPart logicCompartment = _testFixture.getCanonicalCompartment(0);
			_testFixture.enableCanonical( logicCompartment, false );
			final int SIZE = logicCompartment.getChildren().size();
			int count = 5;
			for ( int i = 0; i < count; i++ ) {
				_testFixture.createLED(ViewUtil.resolveSemanticElement(logicCompartment.getNotationView()));
				assertEquals( "Unexpected LED", SIZE, logicCompartment.getChildren().size() );//$NON-NLS-1$
			}
			
			_testFixture.enableCanonical( logicCompartment, true );
			assertEquals( "Unexpected LED", count, logicCompartment.getChildren().size() );//$NON-NLS-1$
		}
		finally {
			println("test_DisableCanonical() complete.");//$NON-NLS-1$
		}
	}
	
	/**
	 * Tests if the canonical editpolicy will refresh when the list compartment
	 * is collapsed.
	 */
	public void test_RefreshWhileCollapsed() {
		try {
			println("test_RefreshWhileCollapsed() starting ...");//$NON-NLS-1$
			CanonicalTestFixture _testFixture = getCanonicalTestFixture();
			IGraphicalEditPart logicCompartment = _testFixture.getCanonicalCompartment(0);
			
			_testFixture.setCollapsed( (IResizableCompartmentEditPart)logicCompartment, true );
			final int SIZE = logicCompartment.getChildren().size();
			int count = 5;
			for ( int i = 0; i < count; i++ ) {
				_testFixture.createLED(ViewUtil.resolveSemanticElement(logicCompartment.getNotationView()));
				assertEquals( "Unexpected LED", SIZE, logicCompartment.getChildren().size() );//$NON-NLS-1$
			}
			_testFixture.setCollapsed( (IResizableCompartmentEditPart)logicCompartment, false );
			assertEquals( "Unexpected LED", count, logicCompartment.getChildren().size() );//$NON-NLS-1$
			
		}
		finally {
			println("test_RefreshWhileCollapsed() complete.");//$NON-NLS-1$
		}
	}
	
	
	/**
	 * Tests if the canonical editpolicy will refresh when the list compartment
	 * is not visible.
	 */
	public void test_RefreshWhileVisible() {
		try {
			CanonicalTestFixture _testFixture = getCanonicalTestFixture();
			println("test_RefreshWhileVisible() starting ...");//$NON-NLS-1$
			IGraphicalEditPart logicCompartment = _testFixture.getCanonicalCompartment(0);
			View view = logicCompartment.getNotationView();
			_testFixture.setVisible( logicCompartment, false );
			final int SIZE = logicCompartment.getChildren().size();
			int count = 5;
			for ( int i = 0; i < count; i++ ) {
				_testFixture.createLED(ViewUtil.resolveSemanticElement(logicCompartment.getNotationView()));
				assertEquals( "Unexpected LED", SIZE, logicCompartment.getChildren().size() );//$NON-NLS-1$
			}
			_testFixture.setVisible( view, true );
			IGraphicalEditPart logicCompartment1 = _testFixture.getCanonicalCompartment(0);
			assertEquals( "Unexpected LED", count, logicCompartment1.getChildren().size() );//$NON-NLS-1$
		}
		finally {
			println("test_RefreshWhileVisible() complete.");//$NON-NLS-1$
		}
	}
	
	/**
	 * Tests the creation and deletion of an attribute view based on the
	 * creation and destruction of semantic elements.
	 */
	public void test_AddRemoveLED() {
		try {
			println("test_AddRemoveLED() starting ...");//$NON-NLS-1$
			CanonicalTestFixture _testFixture = getCanonicalTestFixture();
			IGraphicalEditPart logicCompartment = _testFixture.getCanonicalCompartment(0);
			
			List properties = new ArrayList();
			int size = logicCompartment.getChildren().size();
			int count = 5;
			for ( int i = 0; i < count; i++ ) {
				properties.add( _testFixture.createLED(ViewUtil.resolveSemanticElement(logicCompartment.getNotationView())));
				size++;
				assertEquals( "Unexpected LED count.", size, logicCompartment.getChildren().size() );//$NON-NLS-1$
			}
			
			size = logicCompartment.getChildren().size();
			EObject[] toDelete = new EObject[ properties.size() ];
			properties.toArray( toDelete );
			
			for ( int i = 0; i < toDelete.length; i++ ) {
				_testFixture.destroy( toDelete[i] );
				size--;
				assertEquals( "Unexpected LED count.", size, logicCompartment.getChildren().size() );//$NON-NLS-1$
			}
		}
		finally {
			println("test_AddRemoveAttribute() complete.");//$NON-NLS-1$
		}
	}
	
	public void test_AddDeleteWire() {
		try {
			println("test_AddDeleteWire() starting ...");//$NON-NLS-1$
			CanonicalTestFixture _testFixture = getCanonicalTestFixture();
			IGraphicalEditPart logicCompartment = _testFixture.getCanonicalCompartment(0);
			
			LED led1 = _testFixture.createLED(ViewUtil.resolveSemanticElement(logicCompartment.getNotationView()));
			LED led2 = _testFixture.createLED(ViewUtil.resolveSemanticElement(logicCompartment.getNotationView()));
			Terminal term1 = (Terminal)led1.getOutputTerminals().get(0);
			Terminal term2 = (Terminal)led2.getInputTerminals().get(0);
			
			IElementType typeWire = ElementTypeRegistry.getInstance().getType("logic.wire"); //$NON-NLS-1$
			IElementType typeCircuit = ElementTypeRegistry.getInstance().getType("logic.circuit"); //$NON-NLS-1$
			
			CreateRelationshipRequest crr = new CreateRelationshipRequest(
                getCanonicalTestFixture().getEditingDomain(), term1, term2,
                typeWire);
            ICommand createWire = typeCircuit.getEditHelper().getEditCommand(crr);
			_testFixture.execute(createWire);
			flushEventQueue();
			
			List connectorEPs = getDiagramEditPart().getConnections();
			
			assertEquals( "Unexpected Wire count.", 1, connectorEPs.size()); //$NON-NLS-1$
			ConnectionEditPart ep = (ConnectionEditPart)connectorEPs.get(0);
			assertTrue( "Unexpected source.", ((View)ep.getSource().getModel()).getElement().equals(term1));//$NON-NLS-1$
			assertTrue( "Unexpected target.", ((View)ep.getTarget().getModel()).getElement().equals(term2));//$NON-NLS-1$
			assertTrue(((View)ep.getModel()).getElement() instanceof Wire);
			
			// now destroy it
			_testFixture.destroy( ((View)ep.getModel()).getElement() );
			flushEventQueue();
			
			connectorEPs = getDiagramEditPart().getConnections();
			assertEquals( "Unexpected Wire count.", 0, connectorEPs.size()); //$NON-NLS-1$
		}
		finally {
			println("test_AddDeleteWire() complete.");//$NON-NLS-1$
		}
	}
		

	/**
	 * Tests the creation and deletion of an attribute view based on
	 * moving an semantic attribute between classes.
	 */
	public void test_ReparentLED() {
		try {
			println("test_ReparentLED() starting ...");//$NON-NLS-1$
			CanonicalTestFixture fixture = (CanonicalTestFixture)getTestFixture();
			
			IGraphicalEditPart logicCompartment = fixture.getCanonicalCompartment(0);
			IGraphicalEditPart logicCompartment2 = fixture.getCanonicalCompartment(1);
			Circuit circuit1 = (Circuit)ViewUtil.resolveSemanticElement(logicCompartment.getNotationView());
			Circuit circuit2 = (Circuit)ViewUtil.resolveSemanticElement(logicCompartment2.getNotationView());
			
			LED movingLED = fixture.createLED(circuit1);
			flushEventQueue();
			assertEquals( "Unexpected LED count.", 1, logicCompartment.getChildren().size() );//$NON-NLS-1$
			
			MoveRequest mr = new MoveRequest(getTestFixture()
                .getEditingDomain(), circuit2, movingLED);
            IElementType typeCircuit = ElementTypeRegistry.getInstance().getType("logic.circuit"); //$NON-NLS-1$
			ICommand reparentCmd = typeCircuit.getEditHelper().getEditCommand(mr);
			fixture.execute(reparentCmd);
			flushEventQueue();
			
			assertTrue( "unexpected LED", logicCompartment.getChildren().isEmpty() );//$NON-NLS-1$
			assertEquals( "Unexpected LED count.", 1, logicCompartment2.getChildren().size() );//$NON-NLS-1$
			
			mr = new MoveRequest(getTestFixture().getEditingDomain(), circuit1,
                movingLED);
            reparentCmd = typeCircuit.getEditHelper().getEditCommand(mr);
			fixture.execute(reparentCmd);
			flushEventQueue();
			
			assertTrue( "unexpected LED", logicCompartment2.getChildren().isEmpty() );//$NON-NLS-1$
			assertEquals( "Unexpected LED count.", 1, logicCompartment.getChildren().size() );//$NON-NLS-1$
		}
		finally {
			println("test_ReparentLED() complete.");//$NON-NLS-1$
		}
	}
	
	public void test_createLEDUsingTool() {
		CanonicalTestFixture fixture = (CanonicalTestFixture)getTestFixture();
		IGraphicalEditPart logicCompartment = fixture.getCanonicalCompartment(0);
		Rectangle rect = new Rectangle(logicCompartment.getFigure().getBounds());
		logicCompartment.getFigure().translateToAbsolute(rect);
		IElementType typeLED = ElementTypeRegistry.getInstance().getType("logic.led"); //$NON-NLS-1$
		
		getCanonicalTestFixture().createShapeUsingTool(typeLED, rect.getCenter(), logicCompartment);
		assertEquals( "Unexpected LED count.", 1, logicCompartment.getChildren().size() );//$NON-NLS-1$
	}
	
	public void test_createWireUsingTool() {
		try {
			println("test_AddDeleteWire() starting ...");//$NON-NLS-1$
			CanonicalTestFixture fixture = (CanonicalTestFixture)getTestFixture();
			IGraphicalEditPart logicCompartment = fixture.getCanonicalCompartment(0);
			
			Rectangle rect = new Rectangle(logicCompartment.getFigure().getBounds());
			IElementType typeLED = ElementTypeRegistry.getInstance().getType("logic.led"); //$NON-NLS-1$
			LEDEditPart ledEP1 = (LEDEditPart)getCanonicalTestFixture().createShapeUsingTool(typeLED, rect.getTopLeft().getTranslated(10, 10), logicCompartment);
			rect = new Rectangle(logicCompartment.getFigure().getBounds());
			LEDEditPart ledEP2 = (LEDEditPart)getCanonicalTestFixture().createShapeUsingTool(typeLED, rect.getBottomRight().getTranslated(-10, -10), logicCompartment);
			
			Terminal term1 = (Terminal)((LED)ledEP1.getNotationView().getElement()).getOutputTerminals().get(0);
			TerminalEditPart tep1 = null;
			ListIterator li = ledEP1.getChildren().listIterator();
			while (li.hasNext()) {
				IGraphicalEditPart gep = (IGraphicalEditPart)li.next();
				if (gep.getNotationView().getElement().equals(term1))
					tep1 = (TerminalEditPart)gep;
			}
			
			Terminal term2 = (Terminal)((LED)ledEP2.getNotationView().getElement()).getInputTerminals().get(0);
			TerminalEditPart tep2 = null;
			li = ledEP2.getChildren().listIterator();
			while (li.hasNext()) {
				IGraphicalEditPart gep = (IGraphicalEditPart)li.next();
				if (gep.getNotationView().getElement().equals(term2))
					tep2 = (TerminalEditPart)gep;
			}
			
			IElementType typeWire = ElementTypeRegistry.getInstance().getType("logic.wire"); //$NON-NLS-1$
						
			getCanonicalTestFixture().createConnectorUsingTool(tep1, tep2, typeWire);
			List connectorEPs = getDiagramEditPart().getConnections();
			
			assertEquals( "Unexpected Wire count.", 1, connectorEPs.size()); //$NON-NLS-1$
			ConnectionEditPart ep = (ConnectionEditPart)connectorEPs.get(0);
			assertTrue( "Unexpected source.", ((View)ep.getSource().getModel()).getElement().equals(term1));//$NON-NLS-1$
			assertTrue( "Unexpected target.", ((View)ep.getTarget().getModel()).getElement().equals(term2));//$NON-NLS-1$
			assertTrue(((View)ep.getModel()).getElement() instanceof Wire);
		}
		finally {
			println("test_AddDeleteWire() complete.");//$NON-NLS-1$
		}
	}

}

