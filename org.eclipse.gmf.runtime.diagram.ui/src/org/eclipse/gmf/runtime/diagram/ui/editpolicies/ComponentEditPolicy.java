/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GroupEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IInsertableEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.SemanticListCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.EditCommandRequestWrapper;
import org.eclipse.gmf.runtime.diagram.ui.requests.GroupRequestViaKeyboard;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;

/**
 * Concrete class that extends the GEF's <code>ComponentEditPolicy</code>.
 * This edit policy will return a command in response to delete requests.
 * 
 * @author Vishy Ramaswamy
 */
public class ComponentEditPolicy
	extends org.eclipse.gef.editpolicies.ComponentEditPolicy {
	
	private static final String DELETE_FROM_DIAGRAM_DLG_TITLE = DiagramUIMessages.PromptingDeleteAction_DeleteFromDiagramDialog_Title;

	private static final String DELETE_FROM_DIAGRAM_DLG_MESSAGE = DiagramUIMessages.PromptingDeleteAction_DeleteFromDiagramDialog_Message;

	private static final String DELETE_FROM_MODEL_DLG_TOGGLE_LABEL = DiagramUIMessages.MessageDialogWithToggle_DoNotPromptAgainToggle_label; 	


	/**
	 * Returns a delete command to honour the supplied request.  
	 * Calls {@link #createDeleteSemanticCommand(GroupRequest)} if 
	 * {@link #shouldDeleteSemantic()} returns <tt>true</tt>; othwerise
	 * {@link #createDeleteViewCommand(GroupRequest)} is called.
	 * @see org.eclipse.gef.editpolicies.ComponentEditPolicy#createDeleteCommand(GroupRequest)
	 */
	protected final Command createDeleteCommand(GroupRequest deleteRequest) {

		if (shouldDeleteSemantic()){
			return createDeleteSemanticCommand(deleteRequest);
		}
		if (deleteRequest instanceof GroupRequestViaKeyboard) {
			boolean shouldShowPrompt = ((GroupRequestViaKeyboard) deleteRequest)
				.isShowInformationDialog();
			if (shouldShowPrompt) {
				((GroupRequestViaKeyboard) deleteRequest)
					.setShowInformationDialog(false);
				if (showPrompt() == false) {
					return UnexecutableCommand.INSTANCE;
				}
			}
		}
		return createDeleteViewCommand(deleteRequest);
		
	}

	/**
	 * Return <tt>true</tt> if the host element should delete its semantic
	 * element; otherwise <tt>false</tt> to delete its view. A <tt>true</tt>
	 * return value implies that the host's parent is using a canonical
	 * model manager.
	 * @return true or false
	 */
	protected boolean shouldDeleteSemantic() {
		EditPart parent = getHost().getParent();
        if (parent instanceof SemanticListCompartmentEditPart){
            SemanticListCompartmentEditPart semListCompartment  = 
                (SemanticListCompartmentEditPart)parent;
            return semListCompartment.isCanonicalOn();
            
        } else {

            // If the parent is a group, then we want to get the first parent
            // that isn't a group and test for a canonical editpolicy there.
            while (parent instanceof GroupEditPart) {
                parent = parent.getParent();
            }
        }
        
        EObject eObject = (EObject)getHost().getAdapter(EObject.class);
        if (eObject != null && parent != null ) { //sanity checks
			CanonicalEditPolicy cep = (CanonicalEditPolicy)parent.getEditPolicy(EditPolicyRoles.CANONICAL_ROLE);
			return cep != null 
				&& cep.isEnabled()
				&& cep.canCreate(eObject); 					
		} 	
		return false;
	}

	/** 
	 * Return a command to delete the host's view.  The host's primary view
	 * is deleted if {@link GroupRequest#getEditParts()} returns a 
	 * <tt>null</tt> or empty list; otherwise each editpart's view is 
	 * deleted.
	 * @param deleteRequest the original delete request.
	 * @return Command
	 */
	protected Command createDeleteViewCommand(GroupRequest deleteRequest) {
		CompositeCommand cc = new CompositeCommand(null);

        TransactionalEditingDomain editingDomain = getEditingDomain();
		if (editingDomain == null) {
			return null;
		}
		List toDel = deleteRequest.getEditParts();
		if (toDel == null || toDel.isEmpty()) {
			cc.compose(new DeleteCommand(editingDomain, (View) getHost()
				.getModel()));
		} else {
			for (int i = 0; i < toDel.size(); i++) {
				IGraphicalEditPart gep = (IGraphicalEditPart) toDel.get(i);
				cc.compose(new DeleteCommand(editingDomain, (View) gep
					.getModel()));
			}
		}
		return new ICommandProxy(cc.reduce());
	}

	/**
	 * Return a command to delete the host's semantic elements. This method is
	 * called if the host is canonical.
	 * 
	 * @see #shouldDeleteSemantic()
	 * @param deleteRequest
	 *            the original delete request.
	 * @return Command
	 */
	protected Command createDeleteSemanticCommand(GroupRequest deleteRequest) {
        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost()).getEditingDomain();
        
           boolean shouldShowPrompt = (deleteRequest instanceof GroupRequestViaKeyboard) ? ((GroupRequestViaKeyboard) deleteRequest)
            .isShowInformationDialog()
            : false;

        EditCommandRequestWrapper editCommandRequest = new EditCommandRequestWrapper(
            new DestroyElementRequest(editingDomain, shouldShowPrompt),
            deleteRequest.getExtendedData());
		
		Command semanticCmd = getHost().getCommand(editCommandRequest);
		if (semanticCmd != null && semanticCmd.canExecute()) {
			CompoundCommand cc = new CompoundCommand();
			cc.add(semanticCmd);
			return cc;
		}
		return createDeleteViewCommand(deleteRequest);
	}

	/**
	 * Returns the view element to be deleted.
	 * @return the host's primary view element.
	 */
	protected View getView() {
	   if (getHost().getModel() instanceof View &&
			getHost() instanceof GraphicalEditPart){
			GraphicalEditPart ePart = (GraphicalEditPart)getHost();
			return ePart.getPrimaryView();
		}
		return null;
	}
	
		
	/**
	 * Performs the delete action on the selected objects.
	 */
	private boolean showPrompt() {
		boolean prompt = ((IPreferenceStore) ((IGraphicalEditPart) getHost())
			.getDiagramPreferencesHint().getPreferenceStore())
			.getBoolean(IPreferenceConstants.PREF_PROMPT_ON_DEL_FROM_DIAGRAM);
		
		if(prompt) 
			if(showMessageDialog()) 
				return true; 
			else
				return false;
											
		return true;		
		
	}	

	/**
	 * launches the prompting dialogBox on deletion of elements from the diagram for the end user.
	 * 
	 * @return boolean  true if user pressed YES; false otherwise
	 */
	private boolean showMessageDialog() {	
		MessageDialogWithToggle dialog = MessageDialogWithToggle
			.openYesNoQuestion(Display.getCurrent().getActiveShell(),
				DELETE_FROM_DIAGRAM_DLG_TITLE, DELETE_FROM_DIAGRAM_DLG_MESSAGE,
				DELETE_FROM_MODEL_DLG_TOGGLE_LABEL, false,
				(IPreferenceStore) ((IGraphicalEditPart) getHost())
					.getDiagramPreferencesHint().getPreferenceStore(),
				IPreferenceConstants.PREF_PROMPT_ON_DEL_FROM_DIAGRAM);
		
		if (dialog.getReturnCode() == IDialogConstants.YES_ID) 
			return true;
		else 
			return false;
	}
	
	/**
	 * getInsertableEditPart
	 * Returns the EditPart that can be recipient of the default insert command.
	 * 
	 * @return IInsertableEditPart 
	 */
	protected IInsertableEditPart getInsertableEditPart() {
		if (getHost() instanceof IInsertableEditPart) {
			return (IInsertableEditPart)getHost();
		}
		
		return null;
	}
	
	/**
	 * getInsertCommand
	 * Returns an creation command to honour the supplied request. 
	 * 
	 * @param insertRequest
	 * @return Command that will create the default sementic element responding to insert.
	 */
	protected Command getInsertCommand(GroupRequest insertRequest) {
		IInsertableEditPart insertEP = getInsertableEditPart();
		if (null == insertEP)
			return null;
		
		EObject hostElement = ViewUtil.resolveSemanticElement((View)insertEP.getModel());
		if (hostElement != null) {

            TransactionalEditingDomain editingDomain = getEditingDomain();
            if (editingDomain == null) {
                return null;
            }

            CreateElementRequest theReq = new CreateElementRequest(
                 editingDomain, hostElement, insertEP.getElementType());

            EditCommandRequestWrapper editCommandRequest = new EditCommandRequestWrapper(
                theReq, insertRequest.getExtendedData());
            Command cmd = ((IGraphicalEditPart) getHost())
                .getCommand(editCommandRequest);

            return cmd;
		}
		
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.ComponentEditPolicy#getCommand(org.eclipse.gef.Request)
	 */
	public Command getCommand(Request request) {
		if (!(request instanceof GroupRequest))
			return null;
		
		GroupRequest grpRequest = (GroupRequest)request;
		if (grpRequest instanceof GroupRequestViaKeyboard){
			return getDeleteCommand(grpRequest);
		}
		else if (grpRequest.getType().equals(RequestConstants.REQ_INSERT_SEMANTIC)) {
			return getInsertCommand(grpRequest);
		}
		
		return super.getCommand(request);
	}
    
    private TransactionalEditingDomain getEditingDomain() {
         if (getHost() instanceof IGraphicalEditPart) {
            return ((IGraphicalEditPart) getHost()).getEditingDomain();
        } else if (getHost() instanceof IEditingDomainProvider) {
            Object domain = ((IEditingDomainProvider) getHost())
                .getEditingDomain();
            if (domain instanceof TransactionalEditingDomain) {
                return (TransactionalEditingDomain) domain;
            }
        }
        return null;
    }

 }