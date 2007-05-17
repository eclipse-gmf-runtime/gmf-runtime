/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.SelectConnectionEndRequest;
import org.eclipse.ui.IWorkbenchPage;


/**
 * @author mmostafa
 * 
 * Action to select one of the connection's ends
 *
 */
public class SelectConnectionEndAction extends DiagramAction{
	
	private boolean isSource = false;

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return new SelectConnectionEndRequest(isSource);
	}
	
	
	/**
     * creats a select connection source action
	 * @param workbenchPage
	 * @return action that will select the connection source
	 */
	static SelectConnectionEndAction createSelectConnectionSourceAction(IWorkbenchPage workbenchPage){
		return new SelectConnectionEndAction(workbenchPage,true); 
	}
	
    /**
     * creats a select connection target action
     * @param workbenchPage
     * @return action that will select the connection target
     */
	static SelectConnectionEndAction createSelectConnectionTargetAction(IWorkbenchPage workbenchPage){
		return new SelectConnectionEndAction(workbenchPage,false);
	}

	/**
     * constructor
	 * @param workbenchPage
	 * @param true means select connection source, false means select connection end
	 */
	public SelectConnectionEndAction(IWorkbenchPage workbenchPage, boolean source) {
	        super(workbenchPage);
	        isSource = source;
	        if (isSource){
	        	setText(DiagramUIActionsMessages.SelectConnectionEndAction_SelectSource_ActionLabelText);
	        	setText(DiagramUIActionsMessages.SelectConnectionEndAction_SelectSource_ActionToolTipText);
	        }else {
	        	setText(DiagramUIActionsMessages.SelectConnectionEndAction_SelectTarget_ActionLabelText);
	        	setText(DiagramUIActionsMessages.SelectConnectionEndAction_SelectTarget_ActionToolTipText);
	        }
	        
			
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return false;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		List operationSet = getOperationSet();
		if (operationSet.size()!=1)
			return false;
		return true;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#doRun(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void doRun(IProgressMonitor progressMonitor) {
		List operationSet = getOperationSet();
		if (operationSet.isEmpty())
			return;
		ConnectionEditPart connectionEditPart = 
			(ConnectionEditPart)operationSet.get(0);
		GraphicalEditPart editPartToSelect = null;
		if (isSource){
			editPartToSelect = (GraphicalEditPart)connectionEditPart.getSource();
		}
		else {
			editPartToSelect = (GraphicalEditPart)connectionEditPart.getTarget();
		}
		editPartToSelect.getViewer().reveal(editPartToSelect);
        editPartToSelect.getViewer().select(editPartToSelect);
	}


	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#getTargetEditParts(org.eclipse.gef.EditPart)
	 */
	protected List getTargetEditParts(EditPart editpart) {
		return Collections.singletonList(editpart);
	}
	
	
}
