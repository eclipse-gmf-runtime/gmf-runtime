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

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.actions.ZoomContributionItem;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractDiagramTests;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Event;
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
	
	/**
	 * Tests the CTRL-D keystroke which initiates a delete from model action.
	 * See Bugzilla 115108.
	 */
	public void testDeleteFromModel()
		throws Exception {

		getTestFixture().openDiagram();

		List primaryEditParts = getDiagramEditPart().getPrimaryEditParts();
		int initialCount = primaryEditParts.size();

		if (initialCount < 1) {
			fail("Test requires at least one edit part on the diagram"); //$NON-NLS-1$
		}

		// Get the element to be deleted.
		IGraphicalEditPart editPartToDelete = (IGraphicalEditPart) primaryEditParts
			.get(0);
		EObject semanticElement = (EObject) editPartToDelete
			.getAdapter(EObject.class);
		EObject semanticContainer = semanticElement.eContainer();

		// Select the edit part to be deleted.
		EditPartViewer rootViewer = getDiagramEditPart().getRoot().getViewer();
		rootViewer.deselectAll();
		rootViewer.select(editPartToDelete);

		// Set the preference to not confirm the element deletion.
		((IPreferenceStore) getDiagramEditPart().getDiagramPreferencesHint()
			.getPreferenceStore()).setValue(
			IPreferenceConstants.PREF_PROMPT_ON_DEL_FROM_MODEL, false);

		// Create the CTRL-D event
		Event e = new Event();
		e.character = (char) 0x4;
		e.keyCode = 100;
		e.stateMask = SWT.CTRL;
		e.widget = editPartToDelete.getViewer().getControl();

		// Simulate the CTRL-D keystroke
		SelectionTool tool = new SelectionTool();
		tool.setEditDomain((EditDomain) getDiagramWorkbenchPart()
			.getDiagramEditDomain());
		tool.activate();
		tool.keyDown(new KeyEvent(e), rootViewer);

		// Verify that the edit part and the semantic element have been deleted.
		primaryEditParts = getDiagramEditPart().getPrimaryEditParts();

		assertEquals(
			"Size of primary edit parts should have decreased.", initialCount - 1, primaryEditParts.size()); //$NON-NLS-1$
		
		assertFalse(
			"Primary edit part not deleted.", primaryEditParts.contains(editPartToDelete)); //$NON-NLS-1$

		assertFalse(
			"Semantic element not deleted.", semanticContainer.eContents().contains(semanticElement)); //$NON-NLS-1$
	}

}
