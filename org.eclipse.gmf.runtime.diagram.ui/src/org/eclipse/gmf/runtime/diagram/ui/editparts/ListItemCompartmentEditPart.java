/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.SelectionRequest;

import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ListItemComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.NonResizableTextEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.tools.DragEditPartsTrackerEx;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author melaasar
 * 
 * EditPart for list items
 * @deprecated Renamed to {@link org.eclipse.gmf.runtime.diagram.ui.editparts.ListItemEditPart}
 */
public class ListItemCompartmentEditPart extends TextCompartmentEditPart {
	/**
	 * Creates a new ListItemCompartmentEditPart
	 * 
	 * @param view
	 */
	public ListItemCompartmentEditPart(View view) {
		super(view);
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		WrapLabel label = (WrapLabel) super.createFigure();
		label.setLabelAlignment(PositionConstants.LEFT);
		label.setVisible(false);
		return label;
	}

	/**
	 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker(Request request) {
		if (request instanceof SelectionRequest
			&& ((SelectionRequest) request).getLastButtonPressed() == 3)
			return null;
		return new DragEditPartsTrackerEx(this);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#createDefaultEditPolicies()
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(
			EditPolicy.PRIMARY_DRAG_ROLE,
			new NonResizableTextEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, 
			new ListItemComponentEditPolicy());
	}
}
