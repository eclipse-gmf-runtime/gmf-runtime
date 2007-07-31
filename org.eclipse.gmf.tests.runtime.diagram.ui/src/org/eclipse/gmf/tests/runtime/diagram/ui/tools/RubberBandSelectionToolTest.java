/******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.tests.runtime.diagram.ui.tools;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.tools.RubberbandSelectionTool;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditorInput;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.type.DiagramNotationType;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractTestBase;
import org.eclipse.gmf.tests.runtime.diagram.ui.framework.DiagramTestCase;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.DiagramTestEditor;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.PresentationTestsViewProvider;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;


/**
 * Unit tests for the {@link RubberBandSelectionTool}.
 *
 * @author Christian W. Damus (cdamus)
 */
public class RubberBandSelectionToolTest
	extends DiagramTestCase {

	private Diagram diagramView;
	
	private RubberbandSelectionTool tool;
	
	private NoteEditPart note1;
	private NoteEditPart note2;
	
	public RubberBandSelectionToolTest(String name) {
		super(name);
	}

	/**
	 * Test selection of notes where there is currently no focus edit part.
	 */
	public void test_basicSelect() {
		AbstractTestBase.println("** Running test_basicSelect"); //$NON-NLS-1$
		
		AbstractTestBase.println("Activating rubber band tool"); //$NON-NLS-1$
		tool.activate();
		
		AbstractTestBase.println("Drawing rubber band around the shapes"); //$NON-NLS-1$
		tool.mouseDown(createMouseEvent(0, 0), getViewer());
		tool.mouseDrag(createMouseEvent(210, 210), getViewer());
		tool.mouseUp(createMouseEvent(210, 210), getViewer());
		
		AbstractTestBase.println("Deactivating rubber band tool"); //$NON-NLS-1$
		tool.deactivate();
		
		assertEquals("Wrong number of edit parts selected.", //$NON-NLS-1$
			2, getViewer().getSelectedEditParts().size());
		assertTrue("note1 not selected. ", //$NON-NLS-1$
			getViewer().getSelectedEditParts().contains(note1));
		assertTrue("note2 not selected. ", //$NON-NLS-1$
			getViewer().getSelectedEditParts().contains(note2));
	}
	
	/**
	 * Test selection of notes where one of them currently has the focus.  Its
	 * compatments, though selectable by the mouse only because this note has
	 * the focus, should not be selected by the rubber band.
	 */
	public void test_selectWithInitialFocus() {
		AbstractTestBase.println("** Running test_selectWithInitialFocus"); //$NON-NLS-1$
		
		AbstractTestBase.println("Activating rubber band tool"); //$NON-NLS-1$
		tool.activate();
		
		AbstractTestBase.println("Drawing rubber band around the shapes"); //$NON-NLS-1$
		tool.mouseDown(createMouseEvent(0, 0), getViewer());
		tool.mouseDrag(createMouseEvent(210, 210), getViewer());
		tool.mouseUp(createMouseEvent(210, 210), getViewer());
		
		AbstractTestBase.println("Deactivating rubber band tool"); //$NON-NLS-1$
		tool.deactivate();
		
		// we must not have more than two edit parts selected.  We would have
		//    more than two if we didn't suppress note1's focus
		assertEquals("Wrong number of edit parts selected.", //$NON-NLS-1$
			2, getViewer().getSelectedEditParts().size());
		assertTrue("note1 not selected. ", //$NON-NLS-1$
			getViewer().getSelectedEditParts().contains(note1));
		assertTrue("note2 not selected. ", //$NON-NLS-1$
			getViewer().getSelectedEditParts().contains(note2));
	}
	
	//
	// Fixture stuff
	//
	
	MouseEvent createMouseEvent(int x, int y) {
		Event e = new Event();
		
		e.widget = getViewer().getControl();;
		e.display = e.widget.getDisplay();
		e.button = 1;  // left button
		e.x = x;
		e.y = y;
		
		return new MouseEvent(e);
	}
	
	EditPartViewer getViewer() {
		return getDiagramEditPart().getViewer();
	}
	
	/* (non-Javadoc)
	 * Redefines/Implements/Extends the inherited method.
	 */
	protected void setUp()
		throws Exception {
		
		super.setUp();
		
		tool = new RubberbandSelectionTool();
		tool.setEditDomain((EditDomain) getDiagramWorkbenchPart().getDiagramEditDomain());
	}
	
	
	/* (non-Javadoc)
	 * Redefines/Implements/Extends the inherited method.
	 */
	protected void tearDown()
		throws Exception {
		
		super.tearDown();
		
		tool = null;
		diagramView = null;
		note1 = null;
		note2 = null;
	}
	
	/* (non-Javadoc)
	 * Redefines/Implements/Extends the inherited method.
	 */
	protected void createShapesAndConnectors()
		throws Exception {

		// Add two notes.
		AbstractTestBase.println("Creating note shapes"); //$NON-NLS-1$
		note1 = (NoteEditPart) createShapeUsingTool(DiagramNotationType.NOTE,
				new Point(10, 10));
		note2 = (NoteEditPart) createShapeUsingTool(DiagramNotationType.NOTE,
				new Point(100, 100));

		// ensure that the new shapes and connectors are properly laid out
		AbstractTestBase.println("Update figure layout"); //$NON-NLS-1$
		flushEventQueue();
	}
	
	/**
	 * Creates a new shape using the request created by the
	 * <code>CreationTool</code>.
	 * 
	 * @param elementType
	 *            the type of the shape/element to be created
	 * @param location
	 *            the location for the new shape
	 * @return the new shape's editpart
	 */
	public ShapeEditPart createShapeUsingTool(IElementType elementType,
			Point location) {

		class CreationTool
			extends org.eclipse.gmf.runtime.diagram.ui.tools.CreationTool {

			public CreationTool(IElementType theElementType) {
				super(theElementType);
			}

			/** Make public. */
			public Request createTargetRequest() {
				return super.createTargetRequest();
			}

			protected PreferencesHint getPreferencesHint() {
				return PreferencesHint.USE_DEFAULTS;
			}
		}

		CreationTool ctool = new CreationTool(elementType);
		CreateRequest request = (CreateRequest) ctool.createTargetRequest();
		request.setLocation(location);
		Command cmd = getDiagramEditPart().getCommand(request);

		int previousNumPrimaryEPs = getDiagramEditPart().getPrimaryEditParts()
			.size();
		getDiagramWorkbenchPart().getDiagramEditDomain().getDiagramCommandStack().execute(cmd);
		assertEquals(previousNumPrimaryEPs + 1, getDiagramEditPart()
			.getPrimaryEditParts().size());

		Object newView = ((IAdaptable) ((List) request.getNewObject()).get(0))
			.getAdapter(View.class);
		assertNotNull(newView);

		ShapeEditPart newShape = (ShapeEditPart) getDiagramEditPart()
			.getViewer().getEditPartRegistry().get(newView);
		assertNotNull(newShape);

		return newShape;
	}
	
	protected IFile createDiagram()
		throws Exception {
		
		AbstractTestBase.println("Creating diagram"); //$NON-NLS-1$
		
		AbstractEMFOperation operation = new AbstractEMFOperation(
			getEditingDomain(), "") { //$NON-NLS-1$

			protected IStatus doExecute(IProgressMonitor monitor,
					IAdaptable info)
				throws ExecutionException {
				
				diagramView = ViewService.createDiagram(
					PresentationTestsViewProvider.PRESENTATION_TESTS_DIAGRAM_KIND, PreferencesHint.USE_DEFAULTS);
				setDiagram(diagramView);
			
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
//		MEditingDomain.INSTANCE.runAsUnchecked(new MRunnable() {
//			public Object run() {
//				diagramView = ViewService.createDiagram(
//					PresentationTestsViewProvider.PRESENTATION_TESTS_DIAGRAM_KIND, PreferencesHint.USE_DEFAULTS);
//				setDiagram(diagramView);
//                return null;
//			}});
		
		return null;
	}

	protected void openDiagram() throws Exception {

		AbstractTestBase.println("Opening diagram"); //$NON-NLS-1$
		IWorkbenchPage page =
			PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow()
				.getActivePage();

		setDiagramWorkbenchPart((IDiagramWorkbenchPart)IDE.openEditor(
			page,
			new DiagramEditorInput(getDiagramView()),
			DiagramTestEditor.ID,
			true));
	}
	
	protected Diagram getDiagramView() {
		return diagramView;
	}

}
