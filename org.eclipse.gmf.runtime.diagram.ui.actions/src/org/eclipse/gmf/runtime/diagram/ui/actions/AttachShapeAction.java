/******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;

/**
 * Provides support for action which add an attached shapes to another shape.
 * Puts the added shape in direct edit mode after all the shapes and connections
 * are created.
 * 
 * @author jcorchis
 */
public abstract class AttachShapeAction
	extends DiagramAction {

	/**
	 * Constructor
	 * 
	 * @param workbenchPage
	 *            the active workbenchPage
	 */
	public AttachShapeAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	/**
	 * Method selectAddedObject. Selects Select the newly added shape view by
	 * default
	 * 
	 * @param request
	 *            the request object that holds a reference for the newly
	 *            created object
	 * @param viewer
	 *            the viewer that contains the shapes to be selected. The shapes
	 *            correspond to the newly created object.
	 */
	protected void selectAddedObject(EditPartViewer viewer,
			CreateRequest request) {
		final Object model = request.getNewObject();
		if (model == null || !(model instanceof Collection))
			return;
		final Iterator models = ((Collection) model).iterator();
		final List editparts = new ArrayList();

		while (models.hasNext()) {
			IAdaptable viewAdapter = (IAdaptable) models.next();
			if (viewAdapter != null) {
				Object editPart = viewer.getEditPartRegistry().get(
					viewAdapter.getAdapter(View.class));
				if (editPart != null)
					editparts.add(editPart);
			}
		}

		if (!editparts.isEmpty()) {
			viewer.setSelection(new StructuredSelection(editparts));

			// automatically put the first shape into edit-mode
			Display.getCurrent().asyncExec(new Runnable() {

				public void run() {
					EditPart editPart = (EditPart) editparts.get(0);
					editPart.performRequest(new Request(
						RequestConstants.REQ_DIRECT_EDIT));
				}
			});
		}
	}

	/**
	 * Determines the location of for the shape to be created in relation to the
	 * some other shape.
	 * 
	 * @param editParts
	 *            the existing editparts of the shapes whose location is used to
	 *            determine the location of the to be created shape
	 * @return Point the point representing the location of the to be created
	 *         shape
	 */
	protected Point getLocation(List editParts) {

		Point referenceLocation = new Point(0, 0);

		Rect compoundBounds = new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE,
			Integer.MIN_VALUE, Integer.MIN_VALUE);

		for (int i = 0; i < editParts.size(); i++) {
			GraphicalEditPart part = (GraphicalEditPart) editParts.get(i);
			Rectangle bounds = null;
			if (part.getFigure() instanceof PolylineConnectionEx)
				bounds = ((PolylineConnectionEx) part.getFigure())
					.getSimpleBounds();
			else
				bounds = part.getFigure().getBounds();
			compoundBounds = compoundRectangle(compoundBounds, bounds);
		}

		referenceLocation = new Point(
			(compoundBounds.x1 + compoundBounds.x2) / 2, compoundBounds.y1);

		GraphicalEditPart part = (GraphicalEditPart) editParts.get(0);
		int vertOffset = MapModeUtil.getMapMode(part.getFigure()).DPtoLP(100);
		Point location = new Point();
		location.x = referenceLocation.x;
		location.y = referenceLocation.y - vertOffset;

		// convert the location to screen coordinates as that is what the
		// creation command expects
		// this is done so that the new node don;t hide up in the scrollpane of
		// the shapecompartment.
		if ((location.y < 0)
			&& (part.getParent() instanceof ShapeCompartmentEditPart)) {
			location.y = referenceLocation.y;
			location.x = part.getFigure().getBounds().getRight().x
				+ vertOffset;
		}
		part.getFigure().translateToAbsolute(location);

		return location;
	}

	/**
	 * Builds a compound rectangle out of contributing rectangles
	 * 
	 * @param base
	 *            the base rectangle to start with
	 * @param added
	 *            the newly added rectangle to be compounded
	 * @return Rect the resulting compound rectangle
	 * 
	 */
	private Rect compoundRectangle(Rect base, Rectangle added) {
		if (added == null)
			return base;

		if (added.x + added.width > base.x2)
			base.x2 = added.x + added.width;
		if (added.y + added.height > base.y2)
			base.y2 = added.y + added.height;
		if (added.x < base.x1)
			base.x1 = added.x;
		if (added.y < base.y1)
			base.y1 = added.y;

		return base;
	}

	private class Rect {

		public int x1, y1, x2, y2;

		Rect(int x1, int y1, int x2, int y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}

	/**
	 * Return null since this action doesn't use request to execute its
	 * commands.
	 */
	protected Request createTargetRequest() {
		return null;
	}

	/**
	 * Return the first non {@link ShapeEditPart} and non
	 * {@link ConnectionEditPart} instance in the supplied editparts editpart
	 * hierarchy.
	 * 
	 * @param editPart
	 *            starting editpart
	 * @return an editpart the first non <code>ShapeEditPart</code> non
	 *         <code>ConnectionEditPart</code> it found traversing the
	 *         hierarchy upwards.
	 */
	protected EditPart getContainer(EditPart editPart) {
		EditPart walker = editPart;
		while (walker != null
			&& (walker instanceof ShapeEditPart || walker instanceof ConnectionEditPart)) {
			walker = walker.getParent();
		}
		if (walker instanceof RootEditPart) {
			return ((RootEditPart) walker).getContents();
		}
		return walker;
	}

}
