/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
 * Action to hide all connector labels.
 * 
 * @author jcorchis
 * @canBeSeenBy %level1
 */
public class HideConnectorLabelsAction extends PresentationAction {

	/**
	 * @param workbenchPage
	 */
	public HideConnectorLabelsAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}
	
	/**
	 * Add text and image descriptors.
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#init()
	 */
	public void init() {
		super.init();
		setText(DiagramActionsResourceManager.getI18NString("HideConnectorLabelsAction.label")); //$NON-NLS-1$
		setId(ActionIds.ACTION_HIDE_CONNECTOR_LABELS);
		setToolTipText(DiagramActionsResourceManager.getI18NString("HideConnectorLabelsAction.toolTip")); //$NON-NLS-1$
		setImageDescriptor(Images.DESC_ACTION_HIDE_CONNECTOR_LABELS);	
                setDisabledImageDescriptor(Images.DESC_ACTION_HIDE_CONNECTOR_LABELS_DISABLED);
	}	

	/**
	 * Returns an instance of <code>ToggleConnectorLabelsRequest</code>
	 * @return the request
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return new ToggleConnectorLabelsRequest(false);
	}

	/**
	 * Registers this as a selection listener
	 * @return true
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#isSelectionListener()
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
