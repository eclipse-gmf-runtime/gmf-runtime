/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.jface.action.Action;

import org.eclipse.gmf.runtime.common.ui.action.ActionMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Images;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Messages;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;

/**
 * @author melaasar
 * @canBeSeenBy %level1
 *
 * The router menu manager. It contains all router-related actions
 */
public class RouterMenuManager extends ActionMenuManager {

	/**
	 * The router menu action containing the UI for the router menu manager
	 */
	private static class RouterMenuAction extends Action {
		public RouterMenuAction() {
			setText(Messages.getString("RouterActionMenu.LineStyleDropDownText")); //$NON-NLS-1$
			setToolTipText(Messages.getString("RouterActionMenu.LineStyleDropDownTooltip")); //$NON-NLS-1$
			setImageDescriptor(Images.DESC_ACTION_CHANGEROUTER_GROUP);
			setDisabledImageDescriptor(Images.DESC_ACTION_CHANGEROUTER_GROUP_DISABLED);
			setHoverImageDescriptor(Images.DESC_ACTION_CHANGEROUTER_GROUP);			
		}
	}

	/**
	 * Creates a new instance of the router menu manager
	 */
	public RouterMenuManager() {
		super(ActionIds.MENU_ROUTER, new RouterMenuAction(), true);
	}

}
