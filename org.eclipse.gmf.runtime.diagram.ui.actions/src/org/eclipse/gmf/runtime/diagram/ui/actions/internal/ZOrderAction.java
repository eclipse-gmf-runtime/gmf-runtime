/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import java.util.Iterator;

import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.IEditableEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.l10n.DiagramUIPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.requests.ZOrderRequest;
import org.eclipse.ui.IWorkbenchPage;

/**
 * This action is used to change the order of views within it's parent container
 */
/*
 * @canBeSeenBy %level1
 */
public class ZOrderAction
	extends DiagramAction {

	/**
	 * Protected constructor so that object can not be instantiated directly the
	 * client should call the create methods
	 * 
	 * @param workbenchPage
	 */
	protected ZOrderAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	/**
	 * Creates the Bring to Front Action
	 * 
	 * @param workbenchPage
	 * @return ZOrderAction for Bring to Front
	 */
	public static ZOrderAction createBringToFrontAction(
			IWorkbenchPage workbenchPage) {
		ZOrderAction theAction = new ZOrderAction(workbenchPage);
		theAction.setId(ActionIds.ACTION_BRING_TO_FRONT);
		theAction.setText(DiagramUIActionsMessages.ZOrderAction_BringToFront_ActionLabelText);
		theAction.setToolTipText(DiagramUIActionsMessages.ZOrderAction_BringToFront_ActionToolTipText);

		theAction.setImageDescriptor(DiagramUIActionsPluginImages.DESC_BRING_TO_FRONT);
		theAction.setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_BRING_TO_FRONT);
		return theAction;
	}

	/**
	 * Creates the Bring Forward Action
	 * 
	 * @param workbenchPage
	 * @return ZOrderAction for Bring Forward
	 */
	public static ZOrderAction createBringForwardAction(
			IWorkbenchPage workbenchPage) {
		ZOrderAction theAction = new ZOrderAction(workbenchPage);
		theAction.setId(ActionIds.ACTION_BRING_FORWARD);
		theAction.setText(DiagramUIActionsMessages.ZOrderAction_BringForward_ActionLabelText);
		theAction.setToolTipText(DiagramUIActionsMessages.ZOrderAction_BringForward_ActionToolTipText);
		
		theAction.setImageDescriptor(DiagramUIPluginImages.DESC_BRING_FORWARD);
		theAction.setHoverImageDescriptor(DiagramUIPluginImages.DESC_BRING_FORWARD);
		return theAction;
	}

	/**
	 * Creates the Send to Back Action
	 * 
	 * @param workbenchPage
	 * @return ZOrderAction for Send to Back
	 */
	public static ZOrderAction createSendToBackAction(
			IWorkbenchPage workbenchPage) {
		ZOrderAction theAction = new ZOrderAction(workbenchPage);
		theAction.setId(ActionIds.ACTION_SEND_TO_BACK);
		theAction.setText(DiagramUIActionsMessages.ZOrderAction_SendToBack_ActionLabelText);
		theAction.setToolTipText(DiagramUIActionsMessages.ZOrderAction_SendToBack_ActionToolTipText);
		theAction.setImageDescriptor(DiagramUIPluginImages.DESC_SEND_TO_BACK);
		theAction.setHoverImageDescriptor(DiagramUIPluginImages.DESC_SEND_TO_BACK);
		return theAction;
	}

	/**
	 * Creates the Send Backward Action
	 * 
	 * @param workbenchPage
	 * @return ZOrderAction for Send Backward
	 */
	public static ZOrderAction createSendBackwardAction(
			IWorkbenchPage workbenchPage) {
		ZOrderAction theAction = new ZOrderAction(workbenchPage);
		theAction.setId(ActionIds.ACTION_SEND_BACKWARD);
		theAction.setText(DiagramUIActionsMessages.ZOrderAction_SendBackward_ActionLabelText);
		theAction.setToolTipText(DiagramUIActionsMessages.ZOrderAction_SendBackward_ActionToolTipText);
		theAction.setImageDescriptor(DiagramUIPluginImages.DESC_SEND_BACWARD);
		theAction.setHoverImageDescriptor(DiagramUIPluginImages.DESC_SEND_BACWARD);
		return theAction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return new ZOrderRequest(getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#updateTargetRequest()
	 */
	protected void updateTargetRequest() {
		ZOrderRequest theRequest = (ZOrderRequest) getTargetRequest();
		theRequest.setPartsToOrder(getOperationSet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#getCommand()
	 */
	protected Command getCommand() {

		if (getOperationSet().isEmpty()) {
			return UnexecutableCommand.INSTANCE;
		}

		EditPart editPart = (EditPart) getOperationSet().get(0);
		return editPart.getParent().getCommand(getTargetRequest());
	}

	/**
	 * Action is enabled if the operation set's parent has XYLayout and they all
	 * share the same parent
	 * 
	 * @see org.eclipse.gef.ui.actions.EditorPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {

		// If the selection list is empty
		if (getOperationSet().isEmpty()) {

			// disable this action
			return false;
		}

		// Get the first selected editpart
		EditPart editPart = (EditPart) getOperationSet().get(0);

		// Get the parent of the first selected editpart
		GraphicalEditPart parentEditPart = (GraphicalEditPart) editPart
			.getParent();

		if (parentEditPart == null)
			return false;

		// bugzilla 156733: disable this action if the parent is not editable
		if ((parentEditPart instanceof IEditableEditPart)
				&& !((IEditableEditPart) parentEditPart).isEditModeEnabled()) {
			return false;
		}
		
		// disable this action if the parent doesn't have an XYLayout
		if (!(parentEditPart.getContentPane().getLayoutManager() instanceof XYLayout))
			return false;

		// Iterate over all the selected edit parts
		for (Iterator iter = getOperationSet().iterator(); iter.hasNext();) {

			// Get the next selected editpart
			EditPart selectedEditPart = (EditPart) iter.next();

			// Verify that the editparts share the same parent
			if (parentEditPart != selectedEditPart.getParent()) {
				return false;
			}
			
			// bugzilla 156733: disable this action if the selected edit part is not editable
			if ((selectedEditPart instanceof IEditableEditPart)
					&& !((IEditableEditPart) selectedEditPart)
							.isEditModeEnabled()) {
				return false;
			}
		}

		// Enable this action
		return true;
	}
}
