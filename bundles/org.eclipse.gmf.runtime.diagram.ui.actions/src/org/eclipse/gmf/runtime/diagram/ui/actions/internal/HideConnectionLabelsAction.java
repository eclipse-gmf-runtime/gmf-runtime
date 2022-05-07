/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.ToggleConnectionLabelsRequest;
import org.eclipse.ui.IWorkbenchPage;

/**
 * Action to hide all connection labels.
 * 
 * @author jcorchis
 */
public class HideConnectionLabelsAction extends DiagramAction {

	/**
	 * @param workbenchPage
	 */
	public HideConnectionLabelsAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}
	
	/**
	 * Add text and image descriptors.
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#init()
	 */
	public void init() {
		super.init();
		setText(DiagramUIActionsMessages.HideConnectionLabelsAction_label);
		setId(ActionIds.ACTION_HIDE_CONNECTION_LABELS);
		setToolTipText(DiagramUIActionsMessages.HideConnectionLabelsAction_toolTip);
		setImageDescriptor(DiagramUIActionsPluginImages.DESC_HIDE_CONNECTION_LABELS);
		setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_HIDE_CONNECTION_LABELS_DISABLED);
	}	

	/**
	 * Returns an instance of <code>ToggleConnectionLabelsRequest</code>
	 * 
	 * @return the request
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return new ToggleConnectionLabelsRequest(false);
	}

	/**
	 * Registers this as a selection listener
	 * @return true
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
	
	/** 
	 * Filters the selected objects and returns only ConnectionEditParts that understand
	 * the property change request to hide labels.
	 * @return the operation set 
	 */
	protected List createOperationSet() {
		List selection = getSelectedObjects();
		if (selection.isEmpty() || !(selection.get(0) instanceof EditPart)) {
			return Collections.EMPTY_LIST;
		} 		
		
		List connections = new ArrayList();
		if (selection.get(0) instanceof ShapeCompartmentEditPart || selection.get(0) instanceof DiagramEditPart) {
			List diagramConnections = getDiagramEditPart().getConnections();
			if (diagramConnections != null && !diagramConnections.isEmpty())
				connections.addAll(diagramConnections);
		} else {
			connections.addAll(selection);
		}	
		
		Iterator selectedEPs = connections.iterator();
		List targetedEPs = new ArrayList();
		while (selectedEPs.hasNext()) {
		    EditPart selectedEP = (EditPart)selectedEPs.next();
	    	targetedEPs.addAll(getTargetEditParts(selectedEP));
		}
		return targetedEPs.isEmpty() ? Collections.EMPTY_LIST : targetedEPs;
	}
}
