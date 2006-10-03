/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
 * Action to show all connection labels.
 * 
 * @author jcorchis
 * @canBeSeenBy %level1
 */
public class ShowConnectionLabelsAction extends DiagramAction {
	
	public ShowConnectionLabelsAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage); 
	}
	
	/**
	 * Add text and image descriptors
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#init()
	 */
	public void init() {
		super.init();
		setText(DiagramUIActionsMessages.ShowConnectionLabelsAction_label);
		setId(ActionIds.ACTION_SHOW_CONNECTION_LABELS);
		setToolTipText(DiagramUIActionsMessages.ShowConnectionLabelsAction_toolTip);
		setImageDescriptor(DiagramUIActionsPluginImages.DESC_SHOW_CONNECTION_LABELS);
		setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_SHOW_CONNECTION_LABELS_DISABLED);
	}	
	
	/**
	 * Returns an instance of <code>ToggleConnectionLabelsRequest</code>
	 * @return the request
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {		
		return new ToggleConnectionLabelsRequest(true);
	}
	
	/**
	 * Registers this action as a selection listener
	 * @return true
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
	
	/** 
	 * Filters the selected objects and returns only ConnectionEditParts  
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
