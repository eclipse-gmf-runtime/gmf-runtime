/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
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
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Images;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;

/**
 * Action that will permit a user to snap back a label of a connector back
 * to its orginal position relative to the connector.  Use when the label connector parent
 * can't be located.
 * 
 */
/*
 * @canBeSeenBy %level1
 */
public class SnapBackAction extends PresentationAction {

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
		setText(DiagramActionsResourceManager.getI18NString("SnapBackAction.ActionLabelText")); //$NON-NLS-1$
		setId(ActionIds.ACTION_SNAP_BACK);
		setToolTipText(DiagramActionsResourceManager.getI18NString("SnapBackAction.ActionToolTipText")); //$NON-NLS-1$
		setImageDescriptor(Images.DESC_ACTION_SNAPBACK);
		setHoverImageDescriptor(getImageDescriptor());
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#createTargetRequest()
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

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isCommandStackListener()
	 */
	protected boolean isCommandStackListener() {
		return true;
	}

}
