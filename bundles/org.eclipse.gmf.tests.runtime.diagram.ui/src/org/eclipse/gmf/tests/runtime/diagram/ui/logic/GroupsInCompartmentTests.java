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

package org.eclipse.gmf.tests.runtime.diagram.ui.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.CircuitEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers.LogicConstants;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GroupEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.junit.jupiter.api.Test;

/**
 * Repeat all the same tests in <code>GroupTests</code> but within a
 * compartment. There are also additional tests here.
 *
 * @author crevells
 */
public class GroupsInCompartmentTests extends GroupTests {
	private static IElementType CIRCUIT_TYPE = ElementTypeRegistry.getInstance().getType("logic.circuit"); //$NON-NLS-1$

	private IGraphicalEditPart logicCompartmentEP;

	@Override
	protected IGraphicalEditPart getContainerEP() {
		return logicCompartmentEP;
	}

	@Override
	protected void setTestFixture() {
		testFixture = new GroupTestFixture() {

			@Override
			protected void createShapesAndConnectors() throws Exception {

				// create the circuit that is the container
				CircuitEditPart circuitEP = (CircuitEditPart) getFixture().createShapeUsingTool(CIRCUIT_TYPE,
						new Point(5, 5), new Dimension(300, 300));
				logicCompartmentEP = circuitEP.getChildBySemanticHint(LogicConstants.LOGIC_SHAPE_COMPARTMENT);
			}
		};
	}

	@Test
	public void testRefreshCanonicalDoesNotCreateDoubles() throws Exception {
		setupShapesAndGroups();

		assertEquals(2, getContainerEP().getChildren().size());

		// trigger a canonical refresh
		getFixture().createShapeUsingTool(CIRCUIT_TYPE, new Point(10, 10), new Dimension(50, 50), getContainerEP());
		flushEventQueue();

		assertEquals(3, getContainerEP().getChildren().size());
	}

	@Test
	public void testDeleteShapeInGroupDoesNotReappear() throws Exception {

		setupShapes();

		List<ShapeEditPart> shapes = new LinkedList<>();
		shapes.add(getNWShape());
		shapes.add(getSWShape());
		shapes.add(getSEShape());

		GroupEditPart groupEP = groupShapes(shapes);

		assertEquals(2, getContainerEP().getChildren().size());
		assertEquals(3, groupEP.getChildren().size());

		Request request = new GroupRequest(org.eclipse.gef.RequestConstants.REQ_DELETE);

		// Delete one shape from the inner group.
		getCommandStack().execute(getNWShape().getCommand(request));

		assertEquals(2, getContainerEP().getChildren().size());
		assertEquals(2, groupEP.getChildren().size());

		// trigger a canonical refresh
		getFixture().createShapeUsingTool(CIRCUIT_TYPE, new Point(10, 10), new Dimension(50, 50), getContainerEP());
		flushEventQueue();

		assertEquals(3, getContainerEP().getChildren().size());
		assertEquals(2, groupEP.getChildren().size());

	}

}
