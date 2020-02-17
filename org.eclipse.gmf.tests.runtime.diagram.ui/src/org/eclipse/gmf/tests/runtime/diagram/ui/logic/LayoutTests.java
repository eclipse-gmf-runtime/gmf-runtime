/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.logic;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers.LogicConstants;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.type.GeoshapeType;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractTestBase;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * Tests for layout scenarios.
 * 
 * @author crevells
 */
public class LayoutTests
    extends AbstractTestBase {

    public static Test suite() {
        TestSuite s = new TestSuite(LayoutTests.class);
        return s;
    }

    public class LayoutTestFixture
        extends LogicTestFixture {

        protected void createShapesAndConnectors()
            throws Exception {
            // do nothing, each test will create the shapes it wants
        }
    }

    IElementType CIRCUIT_TYPE = ElementTypeRegistry.getInstance().getType(
        "logic.circuit"); //$NON-NLS-1$

    IElementType LED_TYPE = ElementTypeRegistry.getInstance().getType(
        "logic.led"); //$NON-NLS-1$

    public LayoutTests() {
        super("Layout Tests");//$NON-NLS-1$
    }

    protected void setTestFixture() {
        testFixture = new LayoutTestFixture();
    }

    protected LayoutTestFixture getFixture() {
        return (LayoutTestFixture) testFixture;
    }

    protected IGraphicalEditPart getContainerEP() {
        return getDiagramEditPart();
    }

    protected void assertNotEquals(Object object1, Object object2) {
        assertFalse(object1.equals(object2));
    }

    /**
     * Tests the scenario where a multiple arrange on a circuit was continually
     * moving the circuit.
     * 
     * @throws Exception
     */
    public void test151214MultipleArrangeOneCircuit()
        throws Exception {
        getTestFixture().openDiagram();

        ShapeEditPart circuitEP = getFixture().createShapeUsingTool(
            CIRCUIT_TYPE, new Point(25, 55), new Dimension(100, 100),
            getContainerEP());
        flushEventQueue();
        Rectangle prevBounds = circuitEP.getFigure().getBounds().getCopy();

        getContainerEP().getViewer().setSelection(
            new StructuredSelection(getContainerEP()));
        ArrangeRequest request = new ArrangeRequest(
            ActionIds.ACTION_ARRANGE_ALL);

        getCommandStack().execute(getContainerEP().getCommand(request));
        flushEventQueue();

        assertNotEquals(prevBounds, circuitEP.getFigure().getBounds());
        prevBounds = circuitEP.getFigure().getBounds().getCopy();

        getCommandStack().execute(getContainerEP().getCommand(request));
        flushEventQueue();

        assertEquals(prevBounds, circuitEP.getFigure().getBounds());
    }

    /**
     * Tests the scenario where an arrange with a circuit not in autosize mode
     * (i.e. its size will not change), was arranging other siblings as if the
     * circuit had been in autosize mode.
     * 
     * @throws Exception
     */
    public void test151214DoesNotAssumeAutosizeIsOn()
        throws Exception {
        getTestFixture().openDiagram();

        // create circuit1 with autosize on
        ShapeEditPart circuit1EP = getFixture().createShapeUsingTool(
            CIRCUIT_TYPE, new Point(22, 58), getContainerEP());

        // create circuit2 with autosize off
        ShapeEditPart circuit2EP = getFixture().createShapeUsingTool(
            CIRCUIT_TYPE, new Point(27, 199), new Dimension(111, 111),
            getContainerEP());

        // create circuit3 with autosize on
        ShapeEditPart circuit3EP = getFixture().createShapeUsingTool(
            CIRCUIT_TYPE, new Point(373, 55), getContainerEP());

        // Add a bunch of LEDs to circuit2 so it's autosize would be big after
        // the arrange
        EObject circuit2 = circuit2EP.getNotationView().getElement();
        getFixture().createLED(circuit2);
        getFixture().createLED(circuit2);
        getFixture().createLED(circuit2);
        getFixture().createLED(circuit2);
        getFixture().createLED(circuit2);

        flushEventQueue();

        getContainerEP().getViewer().setSelection(
            new StructuredSelection(getContainerEP()));
        ArrangeRequest request = new ArrangeRequest(
            ActionIds.ACTION_ARRANGE_ALL);

        getCommandStack().execute(getContainerEP().getCommand(request));
        flushEventQueue();

        int diffX_12 = circuit2EP.getFigure().getBounds().getLeft().x
            - circuit1EP.getFigure().getBounds().getRight().x;
        int diffX_23 = circuit3EP.getFigure().getBounds().getLeft().x
            - circuit2EP.getFigure().getBounds().getRight().x;

        assertTrue(diffX_23 < diffX_12 * 2);
    }

    /**
     * The Composite Layout should accomodate for the fact that container shapes
     * in autosize mode will grow. If one circuit is in autosize mode and it
     * ends up growing vertically, any shapes below the circuit will be
     * positioned wrongly until Arrange All is done a second time.
     * 
     * @throws Exception
     */
    public void test151214CompositeLayoutRespectsNewSize()
        throws Exception {
        getTestFixture().openDiagram();

        // create circuit with autosize on
        ShapeEditPart circuitEP = getFixture().createShapeUsingTool(
            CIRCUIT_TYPE, new Point(39, 81), getContainerEP());

        // Add a bunch of LEDs the circuit so it's autosize will be big after
        // the arrange
        EObject circuit = circuitEP.getNotationView().getElement();
        getFixture().createLED(circuit);
        getFixture().createLED(circuit);
        getFixture().createLED(circuit);
        getFixture().createLED(circuit);
        getFixture().createLED(circuit);
        flushEventQueue();

        IGraphicalEditPart compartmentEP = circuitEP
            .getChildBySemanticHint(LogicConstants.LOGIC_SHAPE_COMPARTMENT);
        ShapeEditPart innerLed1EP = (ShapeEditPart) compartmentEP.getChildren()
            .get(0);
        ShapeEditPart innerLed2EP = (ShapeEditPart) compartmentEP.getChildren()
            .get(1);

        ShapeEditPart geoshapeEP = getFixture().createShapeUsingTool(
            GeoshapeType.DIAMOND, new Point(161, 25), getContainerEP());

        getFixture().createConnectorUsingTool(innerLed1EP, innerLed2EP,
            GeoshapeType.LINE);

        getFixture().createConnectorUsingTool(innerLed2EP, geoshapeEP,
            GeoshapeType.LINE);

        flushEventQueue();

        getContainerEP().getViewer().setSelection(
            new StructuredSelection(getContainerEP()));
        ArrangeRequest request = new ArrangeRequest(
            ActionIds.ACTION_ARRANGE_ALL);

        getCommandStack().execute(getContainerEP().getCommand(request));
        flushEventQueue();

        assertFalse(geoshapeEP.getFigure().getBounds().intersects(
            circuitEP.getFigure().getBounds()));
    }

//    /**
//     * The Composite Layout should place all the shapes in the top row at the
//     * same y value. See bugzilla 214077.
//     * 
//     * @throws Exception
//     */
//    public void test215077CompositeLayoutAlignedAtTop()
//        throws Exception {
//        getTestFixture().openDiagram();
//
//        // create circuit with autosize on
//        ShapeEditPart circuitEP = getFixture().createShapeUsingTool(
//            CIRCUIT_TYPE, new Point(44, 177), getContainerEP());
//
//        ShapeEditPart ledEP = getFixture().createShapeUsingTool(LED_TYPE,
//            new Point(174, 177), getContainerEP());
//
//        ShapeEditPart geoshapeEP = getFixture().createShapeUsingTool(
//            GeoshapeType.DIAMOND, new Point(259, 25), getContainerEP());
//
//        getFixture().createConnectorUsingTool(ledEP, geoshapeEP,
//            GeoshapeType.LINE);
//
//        flushEventQueue();
//
//        getContainerEP().getViewer().setSelection(
//            new StructuredSelection(getContainerEP()));
//        ArrangeRequest request = new ArrangeRequest(
//            ActionIds.ACTION_ARRANGE_ALL);
//
//        getCommandStack().execute(getContainerEP().getCommand(request));
//        flushEventQueue();
//
//        assertEquals(circuitEP.getFigure().getBounds().getTop().y, ledEP
//            .getFigure().getBounds().getTop().y);
//    }

}
