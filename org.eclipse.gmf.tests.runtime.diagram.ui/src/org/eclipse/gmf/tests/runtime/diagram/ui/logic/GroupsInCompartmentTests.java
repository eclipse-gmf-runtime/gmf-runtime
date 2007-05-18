/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.logic;

import java.util.LinkedList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.CircuitEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers.LogicConstants;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GroupEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.providers.GeoshapeType;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Repeat all the same tests in <code>GroupTests</code> but within a
 * compartment.
 * 
 * @author crevells
 */
public class GroupsInCompartmentTests
    extends GroupTests {

    public static Test suite() {
        TestSuite s = new TestSuite(GroupsInCompartmentTests.class);
        return s;
    }

    private static IElementType CIRCUIT_TYPE = ElementTypeRegistry
        .getInstance().getType("logic.circuit"); //$NON-NLS-1$

    private IGraphicalEditPart logicCompartmentEP;

    protected IGraphicalEditPart getContainerEP() {
        return logicCompartmentEP;
    }

    protected void setTestFixture() {
        testFixture = new GroupTestFixture() {

            protected void createShapesAndConnectors()
                throws Exception {

                // create the circuit that is the container
                IElementType typeCircuit = ElementTypeRegistry.getInstance()
                    .getType("logic.circuit"); //$NON-NLS-1$
                CircuitEditPart circuitEP = (CircuitEditPart) getFixture()
                    .createShapeUsingTool(typeCircuit, new Point(5, 5),
                        new Dimension(300, 300));
                logicCompartmentEP = circuitEP
                    .getChildBySemanticHint(LogicConstants.LOGIC_SHAPE_COMPARTMENT);
            }
        };
    }

    /**
     * Create LEDs instead of Notes to test for canonical issues.
     */
    protected void setupShapes() {

        ShapeEditPart note1EP = getFixture().createShapeUsingTool(CIRCUIT_TYPE,
            new Point(10, 10), getContainerEP(), new Dimension(50, 50));

        ShapeEditPart note2EP = getFixture().createShapeUsingTool(CIRCUIT_TYPE,
            new Point(100, 10), getContainerEP(), new Dimension(50, 50));

        ShapeEditPart geoshape1EP = getFixture().createShapeUsingTool(
            GeoshapeType.CYLINDER, new Point(10, 100), getContainerEP(),
            new Dimension(50, 50));

        ShapeEditPart geoshape2EP = getFixture().createShapeUsingTool(
            GeoshapeType.DIAMOND, new Point(100, 100), getContainerEP(),
            new Dimension(50, 50));

        flushEventQueue();

        // Cache the views so we can find the editparts again later.
        note1View = (View) note1EP.getModel();
        note2View = (View) note2EP.getModel();
        geoshape1View = (View) geoshape1EP.getModel();
        geoshape2View = (View) geoshape2EP.getModel();

        // Create some connections just to make things more complicated.
        getFixture().createConnectorUsingTool(note1EP, geoshape1EP,
            GeoshapeType.LINE);
        getFixture().createConnectorUsingTool(note2EP, geoshape1EP,
            GeoshapeType.LINE);
        getFixture().createConnectorUsingTool(note2EP, geoshape2EP,
            GeoshapeType.LINE);
        getFixture().createConnectorUsingTool(geoshape1EP, geoshape2EP,
            GeoshapeType.LINE);

        flushEventQueue();
    }

    public void testRefreshCanonicalDoesNotCreateDoubles()
        throws Exception {
        setupShapesAndGroups();

        assertEquals(2, getContainerEP().getChildren().size());

        // trigger a canonical refresh
        getFixture().createShapeUsingTool(CIRCUIT_TYPE, new Point(10, 10),
            getContainerEP(), new Dimension(50, 50));
        flushEventQueue();

        assertEquals(3, getContainerEP().getChildren().size());
    }

    public void testDeleteShapeInGroupDoesNotReappear()
        throws Exception {

        setupShapes();

        List shapes = new LinkedList();
        shapes.add(getNote1EP());
        shapes.add(getGeoshape1EP());
        shapes.add(getGeoshape2EP());

        GroupEditPart groupEP = groupShapes(shapes);

        assertEquals(2, getContainerEP().getChildren().size());
        assertEquals(3, groupEP.getChildren().size());

        Request request = new GroupRequest(RequestConstants.REQ_DELETE);

        // Delete one shape from the inner group.
        getCommandStack().execute(getNote1EP().getCommand(request));

        assertEquals(2, getContainerEP().getChildren().size());
        assertEquals(2, groupEP.getChildren().size());

        // trigger a canonical refresh
        getFixture().createShapeUsingTool(CIRCUIT_TYPE, new Point(10, 10),
            getContainerEP(), new Dimension(50, 50));
        flushEventQueue();

        assertEquals(3, getContainerEP().getChildren().size());
        assertEquals(2, groupEP.getChildren().size());

    }

}
