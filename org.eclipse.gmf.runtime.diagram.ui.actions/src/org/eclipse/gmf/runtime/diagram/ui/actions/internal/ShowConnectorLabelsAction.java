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

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Images;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.requests.ToggleConnectorLabelsRequest;

/**
 * Action to show all connector labels.
 * 
 * @author jcorchis
 * @canBeSeenBy %level1
 */
public class ShowConnectorLabelsAction extends PresentationAction {
	
	public ShowConnectorLabelsAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage); 
	}
	
	/**
	 * Add text and image descriptors
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#init()
	 */
	public void init() {
		super.init();
		setText(DiagramActionsResourceManager.getI18NString("ShowConnectorLabelsAction.label")); //$NON-NLS-1$
		setId(ActionIds.ACTION_SHOW_CONNECTOR_LABELS);
		setToolTipText(DiagramActionsResourceManager.getI18NString("ShowConnectorLabelsAction.toolTip")); //$NON-NLS-1$
		setImageDescriptor(Images.DESC_ACTION_SHOW_CONNECTOR_LABELS);		
               	setDisabledImageDescriptor(Images.DESC_ACTION_SHOW_CONNECTOR_LABELS_DISABLED);
	}	
	
	/**
	 * Returns an instance of <code>ToggleConnectorLabelsRequest</code>
	 * @return the request
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {		
		return new ToggleConnectorLabelsRequest(true);
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
		
		List connectors = new ArrayList();
		if (selection.get(0) instanceof ShapeCompartmentEditPart || selection.get(0) instanceof DiagramEditPart) {
			List diagramConnectors = getDiagramEditPart().getConnectors();
			if (diagramConnectors != null && !diagramConnectors.isEmpty())
				connectors.addAll(diagramConnectors);
		} else {
			connectors.addAll(selection);
		}
				
		Iterator selectedEPs = connectors.iterator();
		List targetedEPs = new ArrayList();
		while (selectedEPs.hasNext()) {
		    EditPart selectedEP = (EditPart)selectedEPs.next();
		    targetedEPs.addAll(getTargetEdiParts(selectedEP));
		}
		return targetedEPs.isEmpty() ? Collections.EMPTY_LIST : targetedEPs;
	}	
	
}
