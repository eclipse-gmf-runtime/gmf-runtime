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

public class SelectConnectionEndAction extends DiagramAction{
	
	private boolean isSource = false;

	protected Request createTargetRequest() {
		return new SelectConnectionEndRequest(isSource);
	}
	
	
	static SelectConnectionEndAction createSelectConnectionSourceAction(IWorkbenchPage workbenchPage){
		return new SelectConnectionEndAction(workbenchPage,true); 
	}
	
	static SelectConnectionEndAction createSelectConnectionTargetAction(IWorkbenchPage workbenchPage){
		return new SelectConnectionEndAction(workbenchPage,false);
	}

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

	protected boolean isSelectionListener() {
		return false;
	}
	
	
	protected boolean calculateEnabled() {
		List operationSet = getOperationSet();
		if (operationSet.size()!=1)
			return false;
		return true;
	}


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


	protected List getTargetEditParts(EditPart editpart) {
		return Collections.singletonList(editpart);
	}
	
	
}
