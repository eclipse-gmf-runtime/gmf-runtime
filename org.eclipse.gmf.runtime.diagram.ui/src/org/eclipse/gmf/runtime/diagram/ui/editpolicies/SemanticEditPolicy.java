/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITreeBranchEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.EditCommandRequestWrapper;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;

/**
 * An editpolicy to handle creation and updating of semantic model elements
 * 
 * @author melaasar
 */
public class SemanticEditPolicy
	extends AbstractEditPolicy {

	private static final String DELETE_FROM_MODEL_DLG_TITLE = DiagramUIMessages.PromptingDeleteFromModelAction_DeleteFromModelDialog_Title;

	private static final String DELETE_FROM_MODEL_DLG_MESSAGE = DiagramUIMessages.PromptingDeleteFromModelAction_DeleteFromModelDialog_Message;

	private static final String DELETE_FROM_MODEL_DLG_TOGGLE_LABEL = DiagramUIMessages.MessageDialogWithToggle_DoNotPromptAgainToggle_label; 	

	/**
	 * @see org.eclipse.gef.EditPolicy#getCommand(Request)
	 */
	public Command getCommand(Request request) {
		if (RequestConstants.REQ_SEMANTIC_WRAPPER.equals(request.getType())) {
			return getSemanticCommand(((EditCommandRequestWrapper) request)
				.getEditCommandRequest());
		} else if (REQ_RECONNECT_SOURCE.equals(request.getType())
			&& relationshipSourceHasChanged((ReconnectRequest) request)) {
			EditPart connectionEP = ((ReconnectRequest) request)
				.getConnectionEditPart();
			if (ViewUtil.resolveSemanticElement((View) connectionEP.getModel()) == null) {
				return getReorientRefRelationshipSourceCommand((ReconnectRequest) request);
			} else {
				return getReorientRelationshipSourceCommand((ReconnectRequest) request);
			}
		} else if (REQ_RECONNECT_TARGET.equals(request.getType())
			&& relationshipTargetHasChanged((ReconnectRequest) request)) {
			EditPart connectionEP = ((ReconnectRequest) request)
				.getConnectionEditPart();
			if (ViewUtil.resolveSemanticElement((View) connectionEP.getModel()) == null) {
				return getReorientRefRelationshipTargetCommand((ReconnectRequest) request);
			} else {
				return getReorientRelationshipTargetCommand((ReconnectRequest) request);
			}
		}

		return super.getCommand(request);
	}

	/**
	 * Has the relationship target changed? If not, then it is not necessary to
	 * return a command that will change the relationship's target.
	 * 
	 * @param request
	 *            the request to reconnect the target of a relationship
	 * @return true if the target has changed; false otherwise
	 */
	private boolean relationshipTargetHasChanged(ReconnectRequest request) {
		return !request.getConnectionEditPart().getTarget().equals(
			request.getTarget());
	}

	/**
	 * Has the relationship source changed? If not, then it is not necessary to
	 * return a command that will change the relationship's source.
	 * 
	 * @param request
	 *            the request to reconnect the source of a relationship
	 * @return true if the source has changed; false otherwise
	 */
	private boolean relationshipSourceHasChanged(ReconnectRequest request) {
		return !request.getConnectionEditPart().getSource().equals(
			request.getTarget());
	}

	/**
	 * Method getSemanticCommand.
	 * 
	 * @param request
	 * @return Command
	 */
	protected Command getSemanticCommand(IEditCommandRequest request) {

		IEditCommandRequest completedRequest = completeRequest(request);
		
		IElementType elementType = ElementTypeRegistry.getInstance()
			.getElementType(completedRequest.getEditHelperContext());
		
		ICommand semanticCommand = (elementType != null) ? elementType
			.getEditCommand(completedRequest)
			: null;

		if (semanticCommand == null)
			return null;
		
		
		boolean shouldProceed = true;
		if (completedRequest instanceof DestroyRequest) {
			shouldProceed = shouldProceed((DestroyRequest) completedRequest);
		}
		if (shouldProceed) {
			Command c = new ICommandProxy(semanticCommand);
			if (completedRequest instanceof DestroyRequest) {
				TransactionalEditingDomain domain = ((IGraphicalEditPart) getHost()).getEditingDomain();
				ICommand ic = new DeleteCommand(domain, (View)getHost().getModel());
				CompositeTransactionalCommand cc = new CompositeTransactionalCommand(domain, semanticCommand
					.getLabel());
				cc.setTransactionNestingEnabled(true);
				cc.compose(semanticCommand);
				cc.compose(ic);
				c = new ICommandProxy(cc);
			}
			return c;
		}
		
		return null;
	}

	/**
	 * @see org.eclipse.gef.EditPolicy#understandsRequest(Request)
	 */
	public boolean understandsRequest(Request request) {
		if (request instanceof EditCommandRequestWrapper)
			return true;
		
		if (REQ_RECONNECT_SOURCE.equals(request.getType())
			|| REQ_RECONNECT_TARGET.equals(request.getType())) {
			EObject parentElement = null;
			if (getHost().getParent() instanceof IGraphicalEditPart) {
				parentElement = ViewUtil
					.resolveSemanticElement((View) getHost().getParent()
						.getModel());
			}
			
			if (getHostElement() != parentElement)
				return true;
		}
		
		return false;
	}

	private EObject getHostElement() {
		return ViewUtil.resolveSemanticElement((View) getHost()
			.getModel());
	}

	/**
	 * Returns a new completed version of the <code>request</code>, mainly
	 * based on targetting the host of this edit policy.
	 * 
	 * @param request
	 *            the request to be completed
	 * @return the completed version of the request. This may or may not be a
	 *         new instance.
	 */
	protected IEditCommandRequest completeRequest(IEditCommandRequest request) {

		IEditCommandRequest result = request;

		if (result instanceof DestroyRequest) {
			DestroyRequest destroyRequest = (DestroyRequest) result;

			
			if (getHostElement() != null) {
				// Destroy element request

				if (destroyRequest instanceof DestroyElementRequest) {
					((DestroyElementRequest) destroyRequest)
						.setElementToDestroy(getHostElement());
					((DestroyElementRequest) destroyRequest).getParameters().clear();
				} else {
					result = new DestroyElementRequest(request
                        .getEditingDomain(), getHostElement(), destroyRequest
                        .isConfirmationRequired());
					result.addParameters(request.getParameters());
                }

				
			} else if (getHost() instanceof ConnectionEditPart) {
				// Destroy reference request

				EObject container = ViewUtil
					.resolveSemanticElement(((Edge) getHost().getModel())
						.getSource());

				EObject referenceObject = ViewUtil
					.resolveSemanticElement(((Edge) getHost().getModel())
						.getTarget());

				if (destroyRequest instanceof DestroyReferenceRequest) {
					DestroyReferenceRequest destroyReferenceRequest = (DestroyReferenceRequest) result;

					destroyReferenceRequest.setContainer(container);
					destroyReferenceRequest
						.setReferencedObject(referenceObject);

				} else {
					result = new DestroyReferenceRequest(((IGraphicalEditPart)getHost())
                        .getEditingDomain(), container, null, referenceObject,
                        destroyRequest.isConfirmationRequired());
					result.addParameters(request.getParameters());
				}
			}
		}
		return result;
	}

	/**
	 * Method getReorientRelationshipSourceCommand.
	 * 
	 * @param request
	 * @return Command
	 */
	protected Command getReorientRelationshipSourceCommand(
			ReconnectRequest request) {
		EObject connectionSemElement = ViewUtil.resolveSemanticElement(((View) request.getConnectionEditPart()
				.getModel()));
		EObject targetSemElement = ViewUtil.resolveSemanticElement(((View) request.getTarget().getModel()));
		EObject oldSemElement = ViewUtil.resolveSemanticElement(((View) request.getConnectionEditPart()
				.getSource().getModel())); 

        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
            .getEditingDomain();
        ReorientRelationshipRequest semRequest = new ReorientRelationshipRequest(
            editingDomain, connectionSemElement, targetSemElement,
            oldSemElement, ReorientRelationshipRequest.REORIENT_SOURCE);
        
        semRequest.addParameters(request.getExtendedData());
		
		return getSemanticCommand(semRequest);
	}

	/**
	 * Method getReorientRelationshipTargetCommand.
	 * 
	 * @param request
	 * @return Command
	 */
	protected Command getReorientRelationshipTargetCommand(
			ReconnectRequest request) {
		EObject connectionSemElement = ViewUtil.resolveSemanticElement((View) request.getConnectionEditPart().getModel());
		EObject targetSemElement = ViewUtil.resolveSemanticElement((View) request.getTarget().getModel());
		EObject oldSemElement = ViewUtil.resolveSemanticElement((View) request.getConnectionEditPart()
			.getTarget().getModel());

		// check if we need to redirect the semantic request because of a tree
		// gesture.
		String connectionHint = ViewUtil
			.getSemanticElementClassId((View) request.getConnectionEditPart()
				.getModel());
		if (((View) request.getTarget().getModel()).getElement() != null) {
			String targetHint = ViewUtil
				.getSemanticElementClassId((View) request.getTarget()
					.getModel());
			if (request.getConnectionEditPart() instanceof ITreeBranchEditPart
				&& request.getTarget() instanceof ITreeBranchEditPart
				&& connectionHint.equals(targetHint)) {
				ITreeBranchEditPart targetBranch = (ITreeBranchEditPart) request
					.getTarget();

				targetSemElement = ViewUtil.resolveSemanticElement((View) targetBranch.getTarget().getModel());
			}
		}

        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
            .getEditingDomain();
        ReorientRelationshipRequest semRequest = new ReorientRelationshipRequest(
            editingDomain, connectionSemElement, targetSemElement,
            oldSemElement, ReorientRelationshipRequest.REORIENT_TARGET);
        
        semRequest.addParameters(request.getExtendedData());
		
		return getSemanticCommand(semRequest);
	}

	/*
	 * Returns the getHost() if the policy understands the request.
	 * 
	 * @see org.eclipse.gef.EditPolicy#getTargetEditPart(org.eclipse.gef.Request)
	 */
	public EditPart getTargetEditPart(Request request) {
		if (understandsRequest(request)) {
			return getHost();
		} else {
			return super.getTargetEditPart(request);
		}

	}

	/**
	 * Method getReorientRelationshipSourceCommand.
	 * 
	 * @param request
	 * @return Command
	 */
	protected Command getReorientRefRelationshipSourceCommand(
			ReconnectRequest request) {

		EditPart sourceEditPart = request.getConnectionEditPart().getSource();
		EditPart targetEditPart = request.getConnectionEditPart().getTarget();
		EObject referenceOwner = ViewUtil
			.resolveSemanticElement((View) sourceEditPart.getModel());
		EObject oldTarget = ViewUtil
			.resolveSemanticElement((View) targetEditPart.getModel());
		EObject newTarget = ViewUtil
			.resolveSemanticElement((View) request.getTarget().getModel());

        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
            .getEditingDomain();
        
		ReorientReferenceRelationshipRequest semRequest = new ReorientReferenceRelationshipRequest(
            editingDomain, referenceOwner, newTarget, oldTarget,
            ReorientReferenceRelationshipRequest.REORIENT_SOURCE);
		
		semRequest.addParameters(request.getExtendedData());

		return getSemanticCommand(semRequest);
	}

	/**
	 * Method getReorientRefRelationshipTargetCommand. Removes the reference the
	 * ConnectionEditPart current has an add the new TargetEditPart
	 * 
	 * @param request
	 * @return Command
	 */
	protected Command getReorientRefRelationshipTargetCommand(
			ReconnectRequest request) {

		EditPart sourceEditPart = request.getConnectionEditPart().getSource();
		EditPart targetEditPart = request.getConnectionEditPart().getTarget();
		EObject referenceOwner = ViewUtil
			.resolveSemanticElement((View) sourceEditPart.getModel());
		EObject oldTarget = ViewUtil
			.resolveSemanticElement((View) targetEditPart.getModel());
		EObject newTarget = ViewUtil
			.resolveSemanticElement((View) request.getTarget().getModel());

        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
            .getEditingDomain();

        ReorientReferenceRelationshipRequest semRequest = new ReorientReferenceRelationshipRequest(
            editingDomain, referenceOwner, newTarget, oldTarget,
            ReorientReferenceRelationshipRequest.REORIENT_TARGET);
        
        semRequest.addParameters(request.getExtendedData());

		return getSemanticCommand(semRequest);
	}

	/**
	 * should proceed
	 * @param destroyRequest the destroy request
	 * @return true or false
	 */
	protected boolean shouldProceed(DestroyRequest destroyRequest) {
		EditPart parent = null;
		if ((getHost() instanceof ConnectionEditPart)&&
				(((ConnectionEditPart)getHost()).getSource() != null)){
			parent = ((ConnectionEditPart)getHost()).getSource().getParent();
		}else{
			parent = getHost().getParent();
		}
		if ((parent instanceof GraphicalEditPart)&& 
				((GraphicalEditPart) parent).isCanonical()){
			return true;
		}
		
		if (!(destroyRequest.isConfirmationRequired())){
			return true;
		}else{
			destroyRequest.setConfirm(false);
			return showMessageDialog();					
		}
	}


	/**
	 * launches the prompting dialogBox on deletion of elements from the model
	 * for the end user.
	 * 
	 * @return boolean true if user pressed YES; false otherwise
	 */
	private boolean showMessageDialog() {
		MessageDialogWithToggle dialog = MessageDialogWithToggle
			.openYesNoQuestion(Display.getCurrent().getActiveShell(),
				DELETE_FROM_MODEL_DLG_TITLE, DELETE_FROM_MODEL_DLG_MESSAGE,
				DELETE_FROM_MODEL_DLG_TOGGLE_LABEL, false,
				(IPreferenceStore) ((IGraphicalEditPart) getHost())
					.getDiagramPreferencesHint().getPreferenceStore(),
				IPreferenceConstants.PREF_PROMPT_ON_DEL_FROM_MODEL);

		if (dialog.getReturnCode() == IDialogConstants.YES_ID)
			return true;
		else
			return false;
	}
}