/******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.gef.EditDomain;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeConnectionTool;
import org.eclipse.gmf.runtime.diagram.ui.type.DiagramNotationType;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractTestBase;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.PresentationTestFixture;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Event;

/**
 * Tests for Connection Tools.
 * 
 * @author cmahoney
 */
public class ConnectionToolTests
	extends AbstractTestBase {

	public static Test suite() {
		TestSuite s = new TestSuite(ConnectionToolTests.class);
		return s;
	}

	public ConnectionToolTests(String name) {
		super(name);
	}

	protected void setTestFixture() {
		testFixture = new PresentationTestFixture();
	}

	protected PresentationTestFixture getFixture() {
		return (PresentationTestFixture) testFixture;
	}

	/**
	 * Test selection of notes where there is currently no focus edit part.
	 */
	public void test_doubleClickUnspecifiedTypeConnectionTool()
		throws Exception {

		getFixture().openDiagram();

		List eps = new ArrayList(2);
		eps.add(getFixture().createNote());
		eps.add(getFixture().createNote());

		assertEquals("Notes not created properly.", //$NON-NLS-1$
			2, getDiagramEditPart().getPrimaryEditParts().size());
		assertEquals("There shouldn't be any connections yet.", //$NON-NLS-1$
			0, getDiagramEditPart().getConnections().size());

		getDiagramEditPart().getViewer().setSelection(
			new StructuredSelection(eps));

		UnspecifiedTypeConnectionTool tool = new UnspecifiedTypeConnectionTool(
			Collections.singletonList(DiagramNotationType.NOTE_ATTACHMENT));
		tool.setEditDomain((EditDomain) getDiagramWorkbenchPart()
			.getDiagramEditDomain());
		tool.activate();
		tool.mouseDoubleClick(createMouseEvent(0, 0), getDiagramEditPart()
			.getViewer());
		tool.deactivate();

		assertEquals("Connection wasn't created.", //$NON-NLS-1$
			1, getDiagramEditPart().getConnections().size());
	}

	MouseEvent createMouseEvent(int x, int y) {
		Event e = new Event();

		e.widget = getDiagramEditPart().getViewer().getControl();
		;
		e.display = e.widget.getDisplay();
		e.button = 1; // left button
		e.x = x;
		e.y = y;

		return new MouseEvent(e);
	}

}
