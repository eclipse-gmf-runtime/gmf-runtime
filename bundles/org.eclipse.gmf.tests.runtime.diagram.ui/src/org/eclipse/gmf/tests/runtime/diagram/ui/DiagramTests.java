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

import java.util.Date;

import org.eclipse.draw2d.Animation;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.MeasurementUnit;
import org.eclipse.gmf.runtime.notation.NotationFactory;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * @author sshaw
 *
 * Diagram Tests
 */
public class DiagramTests extends AbstractDiagramTests {

	/**
	 * @param arg0
	 */
	public DiagramTests(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	public static Test suite() {
		return new TestSuite(DiagramTests.class);
	}
	
	protected void setTestFixture() {
		testFixture = new DiagramTestFixture();
	}
	
	
	public void testAlignment()
		throws Exception {
		// do nothing since we can't open an editor for the default diagram case.
	}
	
	public void testSelect()
		throws Exception {
		// do nothing since we can't open an editor for the default diagram case.
	}
	
	public void testAnimatedLayout() throws Exception {
		ArrangeRequest request = new ArrangeRequest(
			ActionIds.ACTION_ARRANGE_ALL);
		Command layoutCmd = getDiagramEditPart().getCommand(request);
		
		Animation.markBegin();
		
		assertTrue((layoutCmd != null && layoutCmd.canExecute()));
		getCommandStack().execute(layoutCmd);
		
		long startTime = new Date().getTime();
		Animation.run(800);
		long endTime = new Date().getTime();
		
		assertTrue((endTime - startTime) >= 800);
	}

	public void testSetPixelMeasurementUnit(){
		Diagram diagram = NotationFactory.eINSTANCE.createDiagram();
		diagram.setMeasurementUnit(MeasurementUnit.PIXEL_LITERAL);
		assertEquals("get just after set (pixels)", MeasurementUnit.PIXEL_LITERAL, diagram.getMeasurementUnit()); //$NON-NLS-N$
		try {
			diagram.setMeasurementUnit(MeasurementUnit.HIMETRIC_LITERAL);
		} catch (UnsupportedOperationException e) {
			
		} finally {
			assertEquals("MeasurementUnit can't change now", MeasurementUnit.PIXEL_LITERAL, diagram.getMeasurementUnit()); //$NON-NLS-1$		
		}
	}

	public void testSetHiMetricMeasurementUnit(){
		Diagram diagram = NotationFactory.eINSTANCE.createDiagram();
		diagram.setMeasurementUnit(MeasurementUnit.HIMETRIC_LITERAL);
		assertEquals("get just after set (hi-metric)", MeasurementUnit.HIMETRIC_LITERAL, diagram.getMeasurementUnit()); //$NON-NLS-N$
		try {
			diagram.setMeasurementUnit(MeasurementUnit.PIXEL_LITERAL);
		} catch (UnsupportedOperationException e) {
			
		} finally {
			assertEquals("MeasurementUnit can't change now", MeasurementUnit.HIMETRIC_LITERAL, diagram.getMeasurementUnit()); //$NON-NLS-1$		
		}
	}
	
}
