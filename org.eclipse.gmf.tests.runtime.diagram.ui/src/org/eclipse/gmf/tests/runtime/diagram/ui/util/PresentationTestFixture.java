/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.util;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.CreateViewRequestFactory;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.DiagramNotationType;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditorInput;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;


/**
 * This is a test fixture for presentation. It creates a non-UML diagram and a
 * diagram editpart. It does not create a project or add any shapes or
 * connectors to the diagram. This class can be subclassed or used as is.
 * 
 * @author cmahoney
 */
public class PresentationTestFixture
	extends AbstractPresentationTestFixture {

	/**
	 * @see org.eclipse.gmf.tests.runtime.diagram.ui.util.AbstractPresentationTestFixture#createProject()
	 */
	protected void createProject()
		throws Exception {

		// Do nothing. Override if a project is required.

	}

	/**
	 * @see org.eclipse.gmf.tests.runtime.diagram.ui.util.AbstractPresentationTestFixture#createDiagram()
	 */
	protected void createDiagram()
		throws Exception {

		AbstractEMFOperation operation = new AbstractEMFOperation(
			getEditingDomain(), "") { //$NON-NLS-1$

			protected IStatus doExecute(IProgressMonitor monitor,
					IAdaptable info)
				throws ExecutionException {
				
				setDiagram(ViewService
					.createDiagram(
						PresentationTestsViewProvider.PRESENTATION_TESTS_DIAGRAM_KIND,
						getPreferencesHint()));
				
				return Status.OK_STATUS;
			};
		};
		try {
			OperationHistoryFactory.getOperationHistory().execute(operation,
					new NullProgressMonitor(), null);
		} catch (ExecutionException e) {
			e.printStackTrace();
			assertFalse(false);
		}
	}
	
	

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.tests.runtime.diagram.ui.util.AbstractPresentationTestFixture#openDiagram()
	 */
	public void openDiagram()
		throws Exception {
		IWorkbenchPage page =
			PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow()
				.getActivePage();

		setDiagramWorkbenchPart((IDiagramWorkbenchPart)IDE.openEditor(
			page,
			new DiagramEditorInput(getDiagram()),
			DiagramTestEditor.ID,
			true));	}

	/**
	 * @see org.eclipse.gmf.tests.runtime.diagram.ui.util.AbstractPresentationTestFixture#createShapesAndConnectors()
	 */
	protected void createShapesAndConnectors()
		throws Exception {

		// Override to create shapes and connectors.

	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.tests.runtime.diagram.ui.util.IPresentationTestFixture#getPreferencesHint()
	 */
	public PreferencesHint getPreferencesHint() {
		return PreferencesHint.USE_DEFAULTS;
	}
	
	/**
	 * Creates a note on the diagram and returns its editpart.
	 */
	public NoteEditPart createNote() {
		CreateViewRequest createRequest = CreateViewRequestFactory
			.getCreateShapeRequest(DiagramNotationType.NOTE,
				PreferencesHint.USE_DEFAULTS);
		createRequest.setLocation(new Point(10, 10));
		createRequest.setSize(new Dimension(100, 100));
		getDiagramEditPart().getCommand(createRequest).execute();
		flushEventQueue();
		return (NoteEditPart) getDiagramEditPart().getViewer()
			.getEditPartRegistry().get(
				((IAdaptable) ((List) createRequest.getNewObject()).get(0))
					.getAdapter(View.class));
	}
	
}