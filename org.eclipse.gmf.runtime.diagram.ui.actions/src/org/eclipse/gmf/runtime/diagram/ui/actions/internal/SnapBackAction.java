/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
