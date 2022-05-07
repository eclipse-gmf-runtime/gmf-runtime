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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gmf.runtime.diagram.ui.actions.AlignAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.SelectAllAction;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.ITestActionCallback;
import org.eclipse.jface.viewers.StructuredSelection;

public abstract class AbstractDiagramTests extends AbstractTestBase {

	/**
	 * 
	 * @param TestName name for the test
	 */
	public AbstractDiagramTests(String arg0) {
		super(arg0);
	}

	protected ZoomManager getZoomManager() {
		RootEditPart root = getDiagramEditPart().getViewer().getRootEditPart();
		if (root instanceof DiagramRootEditPart) {
			return ((DiagramRootEditPart)root).getZoomManager();
		}
		
		return null;
	}
	
	public void testZoom() throws Exception {
		getTestFixture().openDiagram();

		assertNotNull("no drawing surface", getDrawSurfaceEditPart()); //$NON-NLS-1$
		ZoomManager zoomManager = getZoomManager();
		// Ensure the zoom manager exists
		assertTrue(zoomManager != null);

		// Zoom in
		zoomManager.setZoom(zoomManager.getMinZoom());
		while (zoomManager.canZoomIn()) {
			double state = zoomManager.getNextZoomLevel();
			assertTrue(state <= zoomManager.getMaxZoom());
			assertTrue(state >= zoomManager.getMinZoom());
			zoomManager.setZoom(state);
		}

		// Zoom out
		while (zoomManager.canZoomOut()) {
			double state = zoomManager.getPreviousZoomLevel();
			assertTrue(state <= zoomManager.getMaxZoom());
			assertTrue(state >= zoomManager.getMinZoom());
			zoomManager.setZoom(state);
		}

		// Zoom to fit
		zoomManager.zoomTo(getDrawSurfaceFigure().getBounds());
	}
	
	public void testZoomToolFunctionality() throws Exception {
		getTestFixture().openDiagram();

		RootEditPart root = getDiagramEditPart().getViewer().getRootEditPart();
		DiagramRootEditPart dgrmRoot = (DiagramRootEditPart)root;
		double currentZoom = dgrmRoot.getZoomManager().getZoom();
		dgrmRoot.zoomIn(new Point(200, 200));
		double newZoom = dgrmRoot.getZoomManager().getZoom();
		assertTrue(newZoom > currentZoom);
		
		currentZoom = newZoom;
		dgrmRoot.zoomOut(new Point(300, 300));
		newZoom = dgrmRoot.getZoomManager().getZoom();
		assertTrue(newZoom < currentZoom);
		
		currentZoom = newZoom;
		dgrmRoot.zoomTo(new Rectangle(20, 20, 500, 500));
		newZoom = dgrmRoot.getZoomManager().getZoom();
		assertTrue(newZoom < currentZoom);
	}

	/** Return the supplied editpart's {@link ShapeNodeEditPart}children. */
	protected List getSelectableShapesIn(IGraphicalEditPart parent) {
		assertNotNull(parent);
		List shapes = new ArrayList();
		
		Iterator it = parent.getChildren().iterator();
		while (it.hasNext()) {
			Object child = it.next();
			if (child instanceof ShapeNodeEditPart) {
				if (((ShapeNodeEditPart)child).isSelectable())
					shapes.add(child);
			}
			shapes.addAll(getSelectableShapesIn((IGraphicalEditPart)child));
		}
		return shapes;
	}
	
	public void testSelect() throws Exception {
		getTestFixture().openDiagram();

		final List connectors = getConnectors();
		final List shapes = getSelectableShapesIn(getDrawSurfaceEditPart());
		final List all = new ArrayList();
		all.addAll(connectors);
		all.addAll(shapes);
		
		selectAll(getDrawSurfaceEditPart(), all);
	}
	
	/**
	 * @param currentSelection <code>EditPart</code> to that will be given the current
	 * selection before the action executes.  Can be <code>null</code> indicating no current 
	 * selection.
	 * @throws Exception
	 */
	protected void selectAll(final IGraphicalEditPart currentSelection, final List shouldSelect) throws Exception {
		assertNotNull("no drawing surface", getDrawSurfaceEditPart()); //$NON-NLS-1$

		// test select all
		//
		// prime the selection action to perform the selection within currentSelection
		if (currentSelection != null) {
			getDiagramEditPart().getViewer().setSelection(
				new StructuredSelection(currentSelection));
		}
		SelectAllAction selectAction =
			SelectAllAction.createSelectAllAction(getWorkbenchPage());

		testAction(selectAction, new ITestActionCallback() {
			public void onRunExecution() {

				List selectedParts = getDiagramEditPart().getViewer()
					.getSelectedEditParts();
				if (!shouldSelect.isEmpty()) {
					assertTrue(
						shouldSelect.containsAll(selectedParts)
							&& selectedParts.containsAll(shouldSelect));
				}
				else {
					assertTrue( selectedParts.contains(currentSelection));
				}
			}
		});
	}

	public void testAlignment() throws Exception {

		getTestFixture().openDiagram();

		assertNotNull("no drawing surface", getDrawSurfaceEditPart()); //$NON-NLS-1$

		AlignAction a1 =
			new AlignAction(getWorkbenchPage(), GEFActionConstants.ALIGN_LEFT, PositionConstants.LEFT);
		AlignAction a2 =
			new AlignAction(getWorkbenchPage(), GEFActionConstants.ALIGN_RIGHT, PositionConstants.RIGHT);
		AlignAction a3 =
			new AlignAction(getWorkbenchPage(), GEFActionConstants.ALIGN_TOP, PositionConstants.TOP);
		AlignAction a4 =
			new AlignAction(getWorkbenchPage(), GEFActionConstants.ALIGN_BOTTOM, PositionConstants.BOTTOM);
		AlignAction a5 =
			new AlignAction(getWorkbenchPage(), GEFActionConstants.ALIGN_CENTER, PositionConstants.CENTER);
		AlignAction a6 =
			new AlignAction(getWorkbenchPage(), GEFActionConstants.ALIGN_MIDDLE, PositionConstants.MIDDLE);

		EditPartViewer viewer = getDiagramEditPart().getRoot().getViewer();

		// test alignment for all shapes selected
		viewer.deselectAll();

		List shapeChildren = getDrawSurfaceEditPart().getChildren();
		for (int i = 0; i < shapeChildren.size(); i++) {
			viewer.appendSelection((EditPart) shapeChildren.get(i));
		}

		testAction(a1, new ITestActionCallback() {
			public void onRunExecution() {
				// empty block
			}
		});
		testAction(a2, new ITestActionCallback() {
			public void onRunExecution() {
				//empty block
			}
		});
		testAction(a3, new ITestActionCallback() {
			public void onRunExecution() {
				// empty block
			}
		});
		testAction(a4, new ITestActionCallback() {
			public void onRunExecution() {
				// emtpy block
			}
		});
		testAction(a5, new ITestActionCallback() {
			public void onRunExecution() {
				// empty block
			}
		});
		testAction(a6, new ITestActionCallback() {
			public void onRunExecution() {
				// empty block
			}
		});
	}
	
}
