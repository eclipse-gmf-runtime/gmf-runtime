/******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
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

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.IContainedEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.DelegatingMouseEventsEditPolicy;
import org.eclipse.gmf.runtime.gef.ui.internal.tools.DelegatingDragEditPartsTracker;

/**
 * @author melaasar
 * 
 * An editpart that controls a compartment view
 */
public abstract class CompartmentEditPart extends GraphicalEditPart implements IContainedEditPart {

	/**
	 * Constructs a new compartment edit part
	 * @param view
	 */
	public CompartmentEditPart(EObject model) {
		super(model);
	}

	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.CONNECTION_HANDLES_ROLE,
			new DelegatingMouseEventsEditPolicy(EditPolicyRoles.CONNECTION_HANDLES_ROLE));
		installEditPolicy(EditPolicyRoles.POPUPBAR_ROLE,
			new DelegatingMouseEventsEditPolicy(EditPolicyRoles.POPUPBAR_ROLE));
	}
	
	/**
	 * By default, all compartment edit parts get selectable only if 
	 * the compartment's top level container matches the top level
	 * container of the editpart that currently has the keyboard focus
	 * i.e: you need to select the shape first before selecting a compartment
	 * but once a compartment is selected, a peer compartment can also be selected
	 * @see org.eclipse.gef.EditPart#isSelectable()
	 */
	public boolean isSelectable() {
		if (super.isSelectable()) {
			EditPart focusPart = getViewer().getFocusEditPart();
			if (focusPart instanceof IGraphicalEditPart) {
				TopGraphicEditPart focusTopEP =
					((IGraphicalEditPart) focusPart).getTopGraphicEditPart();
				TopGraphicEditPart myTopEP = getTopGraphicEditPart();
				if (myTopEP == focusTopEP) {
					// check if the selection contains only editparts belonging to 
					// the same top level editpart
					Iterator selection =
						getViewer().getSelectedEditParts().iterator();
					while (selection.hasNext()) {
						Object editPart = selection.next();
						if (editPart instanceof IGraphicalEditPart
							&& (((IGraphicalEditPart) editPart)
								.getTopGraphicEditPart()
								!= myTopEP))
							return false;
					}
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker(Request request) {
		if (request instanceof SelectionRequest
			&& ((SelectionRequest) request).getLastButtonPressed() == 3)
			return null;
		return new DelegatingDragEditPartsTracker(
			this,
			getTopGraphicEditPart());
	}

}
