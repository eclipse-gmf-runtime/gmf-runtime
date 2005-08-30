/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.SelectionRequest;

import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ListItemComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.TextNonResizableEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.tools.DragEditPartsTrackerEx;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import com.ibm.xtools.notation.View;

/**
 * @author melaasar
 * 
 * EditPart for list items
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
			new TextNonResizableEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, 
			new ListItemComponentEditPolicy());
	}
}
