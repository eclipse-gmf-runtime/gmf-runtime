/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ListItemComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.NonResizableTextEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.label.ILabelDelegate;
import org.eclipse.gmf.runtime.diagram.ui.tools.DragEditPartsTrackerEx;

/**
 * @author melaasar
 * 
 * EditPart for list items
 */
public class ListItemEditPart
	extends TextCompartmentEditPart {

	/**
	 * Creates a new ListItemEditPart
	 * 
	 * @param model
	 */
	public ListItemEditPart(EObject model) {
		super(model);
	}

	protected IFigure createFigure() {
	    IFigure theFigure = super.createFigure();
	    theFigure.setVisible(false);
		return theFigure;
	}

	protected ILabelDelegate createLabelDelegate() {
	    ILabelDelegate labelDelegate = super.createLabelDelegate();
	    labelDelegate.setAlignment(PositionConstants.LEFT);
        return labelDelegate;
    }

    public DragTracker getDragTracker(Request request) {
		if (request instanceof SelectionRequest
			&& ((SelectionRequest) request).getLastButtonPressed() == 3)
			return null;
		return new DragEditPartsTrackerEx(this);
	}

	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE,
			new NonResizableTextEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
			new ListItemComponentEditPolicy());
	}
}
