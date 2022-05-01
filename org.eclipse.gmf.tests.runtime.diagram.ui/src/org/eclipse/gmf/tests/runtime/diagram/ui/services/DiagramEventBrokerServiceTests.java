/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.services;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.CircuitEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.LEDEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.LogicGateEditPart;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractTestBase;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.LogicTestFixture;

import junit.framework.Test;
import junit.framework.TestSuite;


public class DiagramEventBrokerServiceTests extends AbstractTestBase{

    /** Create an instance. */
    public DiagramEventBrokerServiceTests() {
        super("Diagram Event Broker Service Test Suite");//$NON-NLS-1$
    }

    protected void setTestFixture() {
        testFixture = new LogicTestFixture();        
    }
    
    /**
     * Defines the statechart diagram test suite.
     * 
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite s = new TestSuite(DiagramEventBrokerServiceTests.class);
        return s;
    }
    
    /** Return <code>(LogicTestFixture)getTestFixture();</code> */
    protected LogicTestFixture getLogicTestFixture() {
        return (LogicTestFixture)getTestFixture();
    }
    
    public void test_DiagramEventBrokerProvider() {
        Rectangle rect = new Rectangle(getDiagramEditPart().getFigure().getBounds());
        getDiagramEditPart().getFigure().translateToAbsolute(rect);
        IElementType typeLED = ElementTypeRegistry.getInstance().getType("logic.led"); //$NON-NLS-1$
        IElementType typeCircuit = ElementTypeRegistry.getInstance().getType("logic.circuit"); //$NON-NLS-1$
        IElementType typeAndGate = ElementTypeRegistry.getInstance().getType("logic.andgate"); //$NON-NLS-1$
        IElementType typeXorGate = ElementTypeRegistry.getInstance().getType("logic.xorgate"); //$NON-NLS-1$
        IElementType typeFlowContainer = ElementTypeRegistry.getInstance().getType("logic.flowcontainer"); //$NON-NLS-1$
        IElementType typeOrGate = ElementTypeRegistry.getInstance().getType("logic.orgate"); //$NON-NLS-1$
        
        Point createPt = new Point(100, 100);
        LEDEditPart ledEP = (LEDEditPart)getLogicTestFixture().createShapeUsingTool(typeLED, createPt, getDiagramEditPart());
        createPt.getTranslated(ledEP.getFigure().getSize().getExpanded(100, 100));
        
        CircuitEditPart circuitEP = (CircuitEditPart)getLogicTestFixture().createShapeUsingTool(typeCircuit, createPt, getDiagramEditPart());
        createPt.getTranslated(circuitEP.getFigure().getSize().getExpanded(100, 100));
        
        LogicGateEditPart andGateEP = (LogicGateEditPart)getLogicTestFixture().createShapeUsingTool(typeAndGate, createPt, getDiagramEditPart());
        createPt.getTranslated(andGateEP.getFigure().getSize().getExpanded(100, 100));
        
        LogicGateEditPart orGateEP = (LogicGateEditPart)getLogicTestFixture().createShapeUsingTool(typeOrGate, createPt, getDiagramEditPart());
        createPt.getTranslated(orGateEP.getFigure().getSize().getExpanded(100, 100));
        
        LogicGateEditPart xorGateEP = (LogicGateEditPart)getLogicTestFixture().createShapeUsingTool(typeXorGate, createPt, getDiagramEditPart());
        createPt.getTranslated(xorGateEP.getFigure().getSize().getExpanded(100, 100));
        
        getLogicTestFixture().createShapeUsingTool(typeFlowContainer, createPt, getDiagramEditPart());
        
        assertTrue(LogicDiagramEventBroker.isCreated());
        assertTrue(LogicDiagramEventBroker.isMethodInvoked());
    }

}
