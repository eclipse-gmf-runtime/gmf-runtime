/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.ui.IWorkbenchPage;

/**
 * Action that will permit a user to snap back a label of a connection back
 * to its orginal position relative to the connection.  Use when the label connection parent
 * can't be located.
 * 
 */
public class SnapBackAction extends DiagramAction {

	/**
	 * @param page
	 */
	public SnapBackAction(IWorkbenchPage page) {
		super(page);
	}

	/**
	 * initializes the action with the correct action id ad image descriptors and tool text and text
	 * 
	 */
	public void init() {
		super.init();
		setText(DiagramUIActionsMessages.SnapBackAction_ActionLabelText);
		setId(ActionIds.ACTION_SNAP_BACK);
		setToolTipText(DiagramUIActionsMessages.SnapBackAction_ActionToolTipText);
		setImageDescriptor(DiagramUIActionsPluginImages.DESC_SNAPBACK);
		setHoverImageDescriptor(getImageDescriptor());
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return new Request(RequestConstants.REQ_SNAP_BACK);
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
    
    protected boolean isOperationHistoryListener() {
        return true;
    }

}
