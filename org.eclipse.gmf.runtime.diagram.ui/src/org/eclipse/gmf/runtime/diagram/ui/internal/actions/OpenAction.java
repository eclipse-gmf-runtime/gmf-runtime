/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.actions;

import org.eclipse.gef.Request;
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;




/**
 * @author tmacdoug
 * @canBeSeenBy %level1
 *
 * This action is used to handle open actions on a diagram
 *
 */
public class OpenAction
extends PresentationAction {
	private static final String ACTION_LABEL   = "Open.ActionLabelText"; //$NON-NLS-1$
	private static final String ACTION_TOOLTIP = "Open.ActionToolTipText"; //$NON-NLS-1$
	
	/**
	 * @param workbenchPage
	 */
	public OpenAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	public void init() {
		super.init();
		
		setId(ActionIds.OPEN);
		setText(PresentationResourceManager.getI18NString( ACTION_LABEL ) );
		setToolTipText(PresentationResourceManager.getI18NString( ACTION_TOOLTIP ) );
	}

	protected Request createTargetRequest() {		
		return new Request(RequestConstants.REQ_OPEN);
	}

	protected boolean isSelectionListener() {
		return true;
	}


}
