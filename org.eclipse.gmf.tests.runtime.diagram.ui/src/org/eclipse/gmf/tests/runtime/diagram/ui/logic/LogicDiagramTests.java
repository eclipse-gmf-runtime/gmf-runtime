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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.gmf.runtime.diagram.ui.internal.actions.ZoomContributionItem;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractDiagramTests;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.IEditorSite;

/**
 * Diagram tests for the logic diagram and general diagrams.
 * 
 * @author cmahoney
 */
public class LogicDiagramTests
	extends AbstractDiagramTests {

	public LogicDiagramTests(String arg0) {
		super(arg0);
	}

	public static Test suite() {
		return new TestSuite(LogicDiagramTests.class);
	}

	protected void setTestFixture() {
		testFixture = new LogicTestFixture();
	}

	/**
	 * Tests the initial enablement of the zoom toolbar entry. See Bugzilla
	 * 110815.
	 * 
	 * @throws Exception
	 */
	public void testZoomToolbarEnablement()
		throws Exception {

		getTestFixture().openDiagram();

		IContributionItem[] items = ((IEditorSite) getDiagramWorkbenchPart()
			.getSite()).getActionBars().getToolBarManager().getItems();
		boolean foundIt = false;
		for (int i = 0; i < items.length; i++) {
			IContributionItem item = items[i];
			if (item instanceof ZoomContributionItem) {
				foundIt = true;
				assertTrue(item.isEnabled());
			}
		}
		assertTrue(foundIt);
	}

}
