/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
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
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GroupEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;


/**
 * Action for delete from diagram.  Used by diagram context menus.
 *
 * @author schafe
 * @canBeSeenBy %level1
 */
public class DeleteFromDiagramAction extends DiagramAction{
	
	/**
	 * Creates a <code>DeleteFromDiagramAction</code> with a default label.
	 *
	 * @param editor The part this action will be associated with.
	 */
	public DeleteFromDiagramAction(IWorkbenchPart part) {
		super(part);		
	}
 
	/**
	 * Constructor
	 * @param workbenchPage
	 */
	public DeleteFromDiagramAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);		
	}

	/**
	 * Initializes this action's text and images.
	 */
	public void init() {
		super.init();
		setId(ActionIds.ACTION_DELETE_FROM_DIAGRAM);
		setText(DiagramUIActionsMessages.DeleteFromDiagram_ActionLabelText);
		setToolTipText(DiagramUIActionsMessages.DeleteFromDiagram_ActionToolTipText);
		ISharedImages workbenchImages = PlatformUI.getWorkbench().getSharedImages();
		setHoverImageDescriptor(
			workbenchImages.getImageDescriptor(
				ISharedImages.IMG_TOOL_DELETE));
		setImageDescriptor(
			workbenchImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		setDisabledImageDescriptor(
			workbenchImages.getImageDescriptor(
				ISharedImages.IMG_TOOL_DELETE_DISABLED));
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createTargetRequest()
	 */
	protected Request createTargetRequest(){
		
		/* Create the delete request */
		GroupRequest deleteReq = new GroupRequest(
			RequestConstants.REQ_DELETE);
		return deleteReq;
		
	}
	
	/**
	 * Gets a command to execute on the operation set based on the target request 
	 * @return a command to execute
	 */
	protected Command getCommand() {
		/* Get the selected edit parts */
		List objects = createOperationSet();		
		
		if (!supportViews(objects) || isCanonical(objects)){
			return null;
		}
		
		CompoundCommand deleteCC = new CompoundCommand(getLabel());
		for (Iterator iter = objects.iterator(); iter.hasNext();) {
			/* Get the next part */
			EditPart editPart = (EditPart) iter.next();
			/* Send the request to the edit part */
			deleteCC.add(editPart.getCommand(getTargetRequest()));
		}
		return deleteCC;
	}
	
	private boolean supportViews(List objects) {
		for (Iterator iter = objects.iterator(); iter.hasNext();) {
			Object object = iter.next();
			if (object instanceof GraphicalEditPart &&
				!((GraphicalEditPart)object).hasNotationView()){
				return false;
			}
			
		}
		return true;
	}

	/**
	 * Filters the selected objects and returns only editparts.
	 * @return a list of editparts selected.
	 * 
	 */
	protected List createOperationSet() {
		List selection = getSelectedObjects();
		if (selection.isEmpty() || !(selection.get(0) instanceof IGraphicalEditPart))
			return Collections.EMPTY_LIST;
		return selection;
	}
	
	/**
	 * Return true if any of cntxt's selectedObjects reside in
	 * containers that are canonical.
	 * Returns false if the selectedObjects reside in non-canonical containers, 
	 * or if the selectedObjects do not have semantic elements.
	 * @param cntxt
	 * @return
	 */
	private boolean isCanonical(List selectedItems){
		
		if (selectedItems.isEmpty()) {
 			return false;
 		}
 		for (Iterator i = selectedItems.iterator(); i.hasNext();) {
 			Object selectedObject = i.next(); 	
 			
 			if (!(selectedObject instanceof IGraphicalEditPart)){
 				continue;
 			}
 				
 			//If the selectedObject does not have a semanticElement,
 			//the canonical check is irrelevant.  The delete from
 			//diagram should apply.
 			IGraphicalEditPart gep = (IGraphicalEditPart)selectedObject;
 			Object model = gep.getModel();
			if (!(model instanceof View)){
 				continue;
			}

            EObject element = ((View)model).getElement();
 			if (element == null || element instanceof View){
 				
 				if (selectedObject instanceof ConnectionEditPart) {
 					if (!((ConnectionEditPart) selectedObject).isSemanticConnection()) {
 						// not a reference connection (which has no element, but whose canonical-ness should be checked)
 						continue;
 					}
 				} else {
 	                // If there is no element or the element is a view (e.g. diagram
 	                // link) than we want to support delete from diagram. See
 	                // bugzilla#148453.
 	                continue;
 				}
 			} 				
 			//Check if container of connection is canonical. 
 			//A connection's container is not necessarily the connection editPart's parent.
 			if (selectedObject instanceof ConnectionEditPart){
 				ConnectionEditPart ePart = (ConnectionEditPart)selectedObject;
 				EditPart sEditPart = ePart.getSource();
                EditPart tEditPart = ePart.getTarget();
 				if (isCanonical(sEditPart) && isCanonical(tEditPart))
 					return true;
 			}
 			//Check if container of shape is canonical.
 			else { 				
 				if (isCanonical(gep))
					return true;
 			} 			
 		} 
 		return false;		
	}
	
	/**
	 * @param gep
	 * @return
	 */
	private boolean isCanonical(EditPart gep) {
		if (gep instanceof IGraphicalEditPart){
			return isCanonical((IGraphicalEditPart)gep);
		}
		return false;
	}


	/**
	 * @param gep
	 * @return
	 */
	private boolean isCanonical(IGraphicalEditPart gep) {
		EditPart parent = gep.getParent();
        while (parent instanceof GroupEditPart) {
            parent = parent.getParent();
        }
		if (parent instanceof IGraphicalEditPart) {
			CanonicalEditPolicy cep = (CanonicalEditPolicy)parent.getEditPolicy(EditPolicyRoles.CANONICAL_ROLE);
			if ( cep != null ) {
				if (cep.isEnabled())
					return true;
			}
		}
		return false;
	}

}
