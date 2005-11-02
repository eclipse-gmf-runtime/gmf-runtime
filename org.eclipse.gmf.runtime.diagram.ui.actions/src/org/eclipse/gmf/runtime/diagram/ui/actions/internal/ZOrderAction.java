/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.requests.ZOrderRequest;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;

/**
 * This action is used to change the order of views within it's parent container
 */
/*
 * @canBeSeenBy %level1
 */
public class ZOrderAction
	extends DiagramAction {

	private static final String BRING_TO_FRONT_LABEL_TEXT = "ZOrderAction.BringToFront.ActionLabelText"; //$NON-NLS-1$

	private static final String BRING_TO_FRONT_TOOLTIP = "ZOrderAction.BringToFront.ActionToolTipText"; //$NON-NLS-1$

	private static final String BRING_FORWARD_LABEL_TEXT = "ZOrderAction.BringForward.ActionLabelText"; //$NON-NLS-1$

	private static final String BRING_FORWARD_TOOLTIP = "ZOrderAction.BringForward.ActionToolTipText"; //$NON-NLS-1$

	private static final String SEND_TO_BACK_LABEL_TEXT = "ZOrderAction.SendToBack.ActionLabelText"; //$NON-NLS-1$

	private static final String SEND_TO_BACK_TOOLTIP = "ZOrderAction.SendToBack.ActionToolTipText"; //$NON-NLS-1$

	private static final String SEND_BACKWARD_LABEL_TEXT = "ZOrderAction.SendBackward.ActionLabelText"; //$NON-NLS-1$

	private static final String SEND_BACKWARD_TOOLTIP = "ZOrderAction.SendBackward.ActionToolTipText"; //$NON-NLS-1$

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
		theAction.setText(DiagramActionsResourceManager.getI18NString(BRING_TO_FRONT_LABEL_TEXT));
		theAction.setToolTipText(DiagramActionsResourceManager.getI18NString(BRING_TO_FRONT_TOOLTIP));

		ImageDescriptor enabledImage = DiagramActionsResourceManager
			.getInstance().getImageDescriptor(
				DiagramActionsResourceManager.IMAGE_BRING_TO_FRONT);
		theAction.setImageDescriptor(enabledImage);
		theAction.setHoverImageDescriptor(enabledImage);
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
		theAction.setText(DiagramActionsResourceManager.getI18NString(BRING_FORWARD_LABEL_TEXT));
		theAction.setToolTipText(DiagramActionsResourceManager.getI18NString(BRING_FORWARD_TOOLTIP));
		
		ImageDescriptor enabledImage = DiagramResourceManager.getInstance()
			.getImageDescriptor(DiagramResourceManager.IMAGE_BRING_FORWARD);
		theAction.setImageDescriptor(enabledImage);
		theAction.setHoverImageDescriptor(enabledImage);
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
		theAction.setText(DiagramActionsResourceManager.getI18NString(SEND_TO_BACK_LABEL_TEXT));
		theAction.setToolTipText(DiagramActionsResourceManager.getI18NString(SEND_TO_BACK_TOOLTIP));
		ImageDescriptor enabledImage = DiagramResourceManager.getInstance()
			.getImageDescriptor(DiagramResourceManager.IMAGE_SEND_TO_BACK);
		theAction.setImageDescriptor(enabledImage);
		theAction.setHoverImageDescriptor(enabledImage);
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
		theAction.setText(DiagramActionsResourceManager.getI18NString(SEND_BACKWARD_LABEL_TEXT));
		theAction.setToolTipText(DiagramActionsResourceManager.getI18NString(SEND_BACKWARD_TOOLTIP));
		ImageDescriptor enabledImage = DiagramResourceManager.getInstance()
		.getImageDescriptor(DiagramResourceManager.IMAGE_SEND_BACWARD);
		theAction.setImageDescriptor(enabledImage);
		theAction.setHoverImageDescriptor(enabledImage);
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
		}

		// Enable this action
		return true;
	}
}
