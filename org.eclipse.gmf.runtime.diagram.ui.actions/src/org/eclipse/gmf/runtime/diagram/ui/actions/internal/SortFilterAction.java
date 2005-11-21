/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.SortFilterCompartmentItemsRequest;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.SortFilterContentRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.ui.IWorkbenchPage;

/**
 * Action to sort/filter list compartment items.
 * 
 * @author jcorchis
 * @canBeSeenBy %level1
 */
public class SortFilterAction extends DiagramAction {
	
	public SortFilterAction(IWorkbenchPage workbenchpage) {		
		super(workbenchpage);
	}

	/**
	 * Returns an instance of <code>SortFilterCompartmentItemsRequest</code> 
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return new SortFilterCompartmentItemsRequest();
	}

	public void init() {
		super.init();
		setId(ActionIds.ACTION_SORT_FILTER);
		setText(DiagramActionsResourceManager.getI18NString("SortFilterCompartmentsAction.ActionLabelText")); //$NON-NLS-1$
		setToolTipText(DiagramActionsResourceManager.getI18NString("SortFilterCompartmentsAction.ActionToolTipText")); //$NON-NLS-1$
		setImageDescriptor(DiagramActionsResourceManager
			.getInstance()
			.getImageDescriptor(DiagramActionsResourceManager.IMAGE_SORT_FILTER));
		setDisabledImageDescriptor(DiagramActionsResourceManager.getInstance()
			.getImageDescriptor(
				DiagramActionsResourceManager.IMAGE_SORT_FILTER_DISABLED));	
	}
	/**
	 * Enable this action if only one shape is selected and that 
	 * shape supports has children that support the SortFilterContentRequest.
	 * @returns boolean whether the action is enabled
	 */
	protected boolean calculateEnabled() {
		// Return true if at least one items handles the request.
		Iterator iter = getSelectedObjects().iterator();
		while(iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof IGraphicalEditPart) {
				IGraphicalEditPart selectedEP = 
					(IGraphicalEditPart) obj;
				Object model = selectedEP.getModel();
				if (!(model instanceof View) ||
					ViewUtil.resolveSemanticElement((View)model)==null)
					continue;
				List childContributions = new ArrayList();			
				List children = selectedEP.getChildren();
				for (int i = 0; i < children.size(); i++) {
					if (children.get(i) instanceof ListCompartmentEditPart) {
						SortFilterContentRequest contentRequest =
							new SortFilterContentRequest(childContributions);
						ListCompartmentEditPart editPart =
							(ListCompartmentEditPart) children.get(i);
						editPart.getCommand(contentRequest);
						if (childContributions.size() > 0)
							return true;
					}
				}
			}
		}		
		return false;
	}
	
	
	/**
	 * Filters the selected objects and returns the first editparts that understands the request
	 * @return a list of editparts selected.
	 * 
	 */
	protected List createOperationSet() {
		List selection = getSelectedObjects();
		if (selection.isEmpty() || !(selection.get(0) instanceof IGraphicalEditPart))
			return Collections.EMPTY_LIST;
		Iterator selectedEPs = selection.iterator();
		List targetedEPs = new ArrayList();
		while (selectedEPs.hasNext()) {
			EditPart selectedEP = (EditPart) selectedEPs.next();
			targetedEPs.addAll(getTargetEdiParts(selectedEP));
			if (targetedEPs.size() > 0) {
				EditPart ep = (EditPart)targetedEPs.get(0);
				targetedEPs.clear();
				targetedEPs.add(ep);
				return targetedEPs;
			}
		}
		return targetedEPs.isEmpty() ? Collections.EMPTY_LIST : targetedEPs;
	}
	

	public boolean isSelectionListener() {
		return true;
	}
	
	/**
	 * Updates the request with the selection.
	 */
	protected void updateTargetRequest() {
		((SortFilterCompartmentItemsRequest)getTargetRequest()).setEditParts(getSelectedObjects());
	}	

}
