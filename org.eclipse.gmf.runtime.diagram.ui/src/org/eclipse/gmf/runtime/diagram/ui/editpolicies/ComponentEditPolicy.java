/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IInsertableEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.requests.EditCommandRequestWrapper;
import org.eclipse.gmf.runtime.diagram.ui.requests.GroupRequestViaKeyboard;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.core.edit.MObjectType;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import com.ibm.xtools.notation.View;

/**
 * Concrete class that extends the GEF's <code>ComponentEditPolicy</code>.
 * This edit policy will return a command in response to delete requests.
 * 
 * @author Vishy Ramaswamy
 */
public class ComponentEditPolicy
	extends org.eclipse.gef.editpolicies.ComponentEditPolicy {
	
	private static final String DELETE_FROM_DIAGRAM_DLG_TITLE = PresentationResourceManager
	.getI18NString("PromptingDeleteAction.DeleteFromDiagramDialog.Title"); //$NON-NLS-1$ 
	private static final String DELETE_FROM_DIAGRAM_DLG_MESSAGE = PresentationResourceManager
	.getI18NString("PromptingDeleteAction.DeleteFromDiagramDialog.Message"); //$NON-NLS-1$ 	
	private static final String DELETE_FROM_MODEL_DLG_TOGGLE_LABEL =PresentationResourceManager
	.getI18NString("MessageDialogWithToggle.DoNotPromptAgainToggle.label"); //$NON-NLS-1$ 	


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
			boolean isModellerElement = false;
			if (getHost() instanceof IGraphicalEditPart) {
				EObject semanticElement = ViewUtil
					.resolveSemanticElement((View) ((IGraphicalEditPart) getHost())
						.getModel());
				if ((semanticElement != null)
					&& (EObjectUtil.getType(semanticElement) == MObjectType.MODELING)) {
					isModellerElement = true;
				}
			}
			boolean shouldShowPrompt = ((GroupRequestViaKeyboard) deleteRequest)
				.isShowInformationDialog();
			if (shouldShowPrompt && isModellerElement) {
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
		if (parent instanceof IGraphicalEditPart) {
			CanonicalEditPolicy cep = (CanonicalEditPolicy)parent.getEditPolicy(EditPolicyRoles.CANONICAL_ROLE);
			if ( cep != null ) {
				return cep.isEnabled();						
			} 					
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

		List toDel = deleteRequest.getEditParts();
		if (toDel == null || toDel.isEmpty()) {
			cc.compose(new DeleteCommand((View)getHost().getModel()));
		} else {
			for (int i = 0; i < toDel.size(); i++) {
				IGraphicalEditPart gep = (IGraphicalEditPart) toDel.get(i);
				cc.compose(new DeleteCommand((View)gep.getModel()));
			}
		}
		return new EtoolsProxyCommand(cc.unwrap());
	}

	/** 
	 * Return a command to delete the host's semantic elements.  This method is
	 * called if the host is canonical.
	 * @see #shouldDeleteSemantic()
	 * @param deleteRequest the original delete request.
	 * @return Command
	 */
	protected Command createDeleteSemanticCommand(GroupRequest deleteRequest) {
		EditCommandRequestWrapper editCommandRequest =
			new EditCommandRequestWrapper(new DestroyElementRequest(false));
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
			
			MObjectType theMType = EObjectUtil.getType(hostElement);
			if (theMType.equals(MObjectType.MODELING)) {
				CreateElementRequest theReq =
						new CreateElementRequest(hostElement, insertEP.getElementType());
					
				EditCommandRequestWrapper editCommandRequest = new EditCommandRequestWrapper(theReq);
				Command cmd =
					((IGraphicalEditPart)getHost()).getCommand(editCommandRequest);
					
				return cmd;
			}
		}
		
		return null;
	}
	
	/* (non-Javadoc)
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

 }