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
 * The select menu manager. It contains all select-related actions
 */
public class SelectMenuManager extends ActionMenuManager {

	/**
	 * The select menu action containing the UI for the select menu manager
	 */
	private static class SelectMenuAction extends Action {
		public SelectMenuAction() {
			setText(Messages.getString("SelectActionMenu.SelectDropDownText")); //$NON-NLS-1$
			setToolTipText(Messages.getString("SelectActionMenu.SelectDropDownTooltip")); //$NON-NLS-1$
			setImageDescriptor(Images.DESC_ACTION_SELECTALL);
			setDisabledImageDescriptor(Images.DESC_ACTION_SELECTALL_DISABLED);
			setHoverImageDescriptor(Images.DESC_ACTION_SELECTALL);
		}
	}

	/**
	 * Creates a new instance of the select menu manager
	 */
	public SelectMenuManager() {
		super(ActionIds.MENU_SELECT, new SelectMenuAction(), true);
	}

}
