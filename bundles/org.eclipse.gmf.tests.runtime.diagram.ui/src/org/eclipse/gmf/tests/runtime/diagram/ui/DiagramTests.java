/******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.eclipse.draw2d.Animation;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.MeasurementUnit;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.junit.jupiter.api.Test;

/**
 * @author sshaw
 *
 *         Diagram Tests
 */
public class DiagramTests extends AbstractDiagramTests {

	/**
	 * @param arg0
	 */
	@Override
	protected void setTestFixture() {
		testFixture = new DiagramTestFixture();
	}

	@Override
	@Test
	public void testAlignment() throws Exception {
		// do nothing since we can't open an editor for the default diagram case.
	}

	@Override
	@Test
	public void testSelect() throws Exception {
		// do nothing since we can't open an editor for the default diagram case.
	}

	@Test
	public void testAnimatedLayout() throws Exception {
		ArrangeRequest request = new ArrangeRequest(ActionIds.ACTION_ARRANGE_ALL);
		Command layoutCmd = getDiagramEditPart().getCommand(request);

		Animation.markBegin();

		assertTrue((layoutCmd != null && layoutCmd.canExecute()));
		getCommandStack().execute(layoutCmd);

		long startTime = new Date().getTime();
		Animation.run(800);
		long endTime = new Date().getTime();

		assertTrue((endTime - startTime) >= 800);
	}

	@Test
	public void testSetPixelMeasurementUnit() {
		Diagram diagram = NotationFactory.eINSTANCE.createDiagram();
		diagram.setMeasurementUnit(MeasurementUnit.PIXEL_LITERAL);
		assertEquals(MeasurementUnit.PIXEL_LITERAL, diagram.getMeasurementUnit(), "get just after set (pixels)");
		try {
			diagram.setMeasurementUnit(MeasurementUnit.HIMETRIC_LITERAL);
		} catch (UnsupportedOperationException e) {

		} finally {
			assertEquals(MeasurementUnit.PIXEL_LITERAL, diagram.getMeasurementUnit(),
					"MeasurementUnit can't change now"); //$NON-NLS-1$
		}
	}

	@Test
	public void testSetHiMetricMeasurementUnit() {
		Diagram diagram = NotationFactory.eINSTANCE.createDiagram();
		diagram.setMeasurementUnit(MeasurementUnit.HIMETRIC_LITERAL);
		assertEquals(MeasurementUnit.HIMETRIC_LITERAL, diagram.getMeasurementUnit(), "get just after set (hi-metric)");
		try {
			diagram.setMeasurementUnit(MeasurementUnit.PIXEL_LITERAL);
		} catch (UnsupportedOperationException e) {

		} finally {
			assertEquals(MeasurementUnit.HIMETRIC_LITERAL, diagram.getMeasurementUnit(),
					"MeasurementUnit can't change now"); //$NON-NLS-1$
		}
	}

}
