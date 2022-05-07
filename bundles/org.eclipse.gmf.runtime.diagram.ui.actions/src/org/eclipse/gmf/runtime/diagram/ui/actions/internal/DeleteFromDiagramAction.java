/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
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
    
	    boolean isCanonical = false;
        if ( !selectedItems.isEmpty()) {

            for  (Iterator si = selectedItems.iterator(); si.hasNext() && !isCanonical;) {
                Object selected = si.next();   
                if ( selected instanceof EditPart ) {
                    EditPart child = (EditPart)selected;
                    View view = (View)child.getAdapter(View.class);

                    if (  view == null 
                            || view.getElement() == null
                            || view.getElement() instanceof View ) {
                        // If there is no element or the element is a view (e.g. diagram
                        // link) than we want to support delete from diagram. See
                        // bugzilla#148453.
                        isCanonical = false;
                        continue;
                    }
                    if (child instanceof ConnectionEditPart) {
                        ConnectionEditPart connection = (ConnectionEditPart)child;
                        isCanonical = ( !connection.isSemanticConnection()
                                || (isCanonical(connection.getSource())
                                        && isCanonical(connection.getTarget())) );
                    } 
                    else {
                        isCanonical = isCanonical(child);
                    }
                }
            }
        }
        return isCanonical;

	    
	}
	
	/**
	 * @param gep
	 * @return
	 */
	private boolean isCanonical(EditPart ep) {
	    EObject eObject = (EObject)ep.getAdapter(EObject.class);
        EditPart parent = ep.getParent();
        if ( eObject != null && parent != null ) { //sanity checks
            CanonicalEditPolicy cep = (CanonicalEditPolicy)parent.getEditPolicy(EditPolicyRoles.CANONICAL_ROLE);
            return cep != null
                && cep.isEnabled()
                && cep.canCreate(eObject);
        }
        return false;
	}

}
