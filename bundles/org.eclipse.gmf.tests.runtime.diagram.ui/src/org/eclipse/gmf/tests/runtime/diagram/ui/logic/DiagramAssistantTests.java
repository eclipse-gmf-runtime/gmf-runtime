/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DiagramAssistantEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractTestBase;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.junit.jupiter.api.Test;

/**
 * Tests for the diagram assistant functionality.
 *
 * @author cmahoney
 */
public class DiagramAssistantTests extends AbstractTestBase {

	/** installs the composite state test fixture. */
	@Override
	protected void setTestFixture() {
		testFixture = new LogicTestFixture();
	}

	/**
	 * A diagram assistant editpolicy that exposes the
	 * {@link #shouldShowDiagramAssistant()} method.
	 *
	 * @author cmahoney
	 */
	class TestDiagramAssistantEditPolicy extends DiagramAssistantEditPolicy {

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.gmf.runtime.diagram.ui.editpolicies.DiagramAssistantEditPolicy#
		 * isDiagramAssistant(java.lang.Object)
		 */
		@Override
		protected boolean isDiagramAssistant(Object object) {
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.gmf.runtime.diagram.ui.editpolicies.DiagramAssistantEditPolicy#
		 * isDiagramAssistantShowing()
		 */
		@Override
		protected boolean isDiagramAssistantShowing() {
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.gmf.runtime.diagram.ui.editpolicies.DiagramAssistantEditPolicy#
		 * showDiagramAssistant(org.eclipse.draw2d.geometry.Point)
		 */
		@Override
		protected void showDiagramAssistant(Point referencePoint) {
			// do nothing
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.gmf.runtime.diagram.ui.editpolicies.DiagramAssistantEditPolicy#
		 * hideDiagramAssistant()
		 */
		@Override
		protected void hideDiagramAssistant() {
			// do nothing
		}

		/**
		 * Make public for testing purposes.
		 */
		@Override
		public boolean shouldShowDiagramAssistant() {
			return super.shouldShowDiagramAssistant();
		}

	}

	/**
	 * Tests {@link DiagramAssistantEditPolicy#shouldShowDiagramAssistant()}
	 *
	 * @throws Exception
	 */
	@Test
	public void testShouldShowDiagramAssistant() throws Exception {

		getTestFixture().openDiagram();

		IGraphicalEditPart ep = (IGraphicalEditPart) getDiagramEditPart().getPrimaryEditParts().get(0);

		TestDiagramAssistantEditPolicy da = new TestDiagramAssistantEditPolicy();
		ep.installEditPolicy("TestDiagramAssistantRole", //$NON-NLS-1$
				da);
		da.activate();

		assertTrue(da.shouldShowDiagramAssistant());

		// should not be shown if the editpart is not editable
		ep.disableEditMode();
		assertFalse(da.shouldShowDiagramAssistant());

		ep.enableEditMode();
		assertTrue(da.shouldShowDiagramAssistant());

		// should not be shown if the editpart is inactive
		ep.deactivate();
		assertFalse(da.shouldShowDiagramAssistant());

		ep.activate();
		assertTrue(da.shouldShowDiagramAssistant());

		// should not be shown if the diagram editor is not the active editor
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IDiagramWorkbenchPart diagramPart = getDiagramWorkbenchPart();
		if (page.getViewReferences().length > 0) {
			// Activate the first view found, if there is one.
			for (int i = 0; i < page.getViewReferences().length; i++) {
				IWorkbenchPart part = page.getViewReferences()[i].getPart(false);
				if (part != null & part != diagramPart) {
					page.activate(part);
					assertFalse(da.shouldShowDiagramAssistant());
					break;
				}
			}
		}

		page.activate(getDiagramWorkbenchPart());
		assertTrue(da.shouldShowDiagramAssistant());

	}

}
