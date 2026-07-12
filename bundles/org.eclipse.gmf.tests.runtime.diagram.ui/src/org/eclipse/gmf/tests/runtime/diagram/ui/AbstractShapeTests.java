/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetBoundsCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.ITestCommandCallback;
import org.junit.jupiter.api.Test;

/**
 * @author chmahone,choang
 *
 *         <p>
 *         Abstract Test class that implements the shapes test. This abstract
 *         class doesn't not know what diagram or if we are dealing with EMF or
 *         RMS Diagram Test that wish to run the shapes test in the specific
 *         context of thier digram should extend this abstract class.
 *         </p>
 *
 *         <p>
 *         What's left for you to do
 *         </p>
 *         <br>
 *         1. Implement all the abstract methods and the suite() as described in
 *         the javadoc for </br>
 *         <br>
 *         @see org.eclipse.gmf.tests.runtime.diagram.ui.AbstractTestBase
 */
public abstract class AbstractShapeTests extends AbstractTestBase {

	/**
	 * Executes a generic command on all the EditParts in the diagram (assuming they
	 * provide the command). Checks that the property value is updated after the
	 * command.
	 *
	 * @param request       request for the command to be executed
	 * @param propertyValue value that the property will be changed to
	 * @param propertyID    property id string that identifies this view property
	 */
	protected void testCommandOnEditParts(final Request request, final Object propertyValue,
			final EStructuralFeature feature) {
		Iterator objects = getShapesIn(getDrawSurfaceEditPart()).iterator();
		while (objects.hasNext()) {
			IGraphicalEditPart editPart = (IGraphicalEditPart) objects.next();
			Command cmd = editPart.getCommand(request);
			if (cmd != null) {
				final View view = (View) editPart.getModel();
				testCommand(cmd, new ITestCommandCallback() {
					@Override
					public void onCommandExecution() {
						assertTrue(ViewUtil.getStructuralFeatureValue(view, feature).equals(propertyValue));
					}
				});
			}
		}
	}

	/**
	 * Method testRATLC00046844. Tests to make sure undo is working when resizing an
	 * editpart.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRATLC00046844() {

		// get the first edit part
		IGraphicalEditPart editPart = null;
		Iterator objects = getShapesIn(getDrawSurfaceEditPart()).iterator();
		while (objects.hasNext()) {
			editPart = (IGraphicalEditPart) objects.next();
			break;
		}

		View shapeView = (View) editPart.getModel();

		getDrawSurfaceFigure().invalidate();
		getDrawSurfaceFigure().validate();

		final Dimension newDim = new Dimension(400, 400);
		final Dimension oldDim = editPart.getFigure().getSize();

		SetBoundsCommand cmd = new SetBoundsCommand(getTestFixture().getEditingDomain(), "testRATLC00046844", //$NON-NLS-1$
				new EObjectAdapter(shapeView), newDim);

		testCommand(cmd, new ITestCommandCallback() {
			@Override
			public void onCommandExecution() {
				// do nothing
			}
		});

		// force an undo to test value is restored.
		getTestFixture().getCommandStack().undo();
		getDrawSurfaceFigure().invalidate();
		getDrawSurfaceFigure().validate();

		assertTrue(editPart.getFigure().getSize().equals(oldDim));
	}
}
