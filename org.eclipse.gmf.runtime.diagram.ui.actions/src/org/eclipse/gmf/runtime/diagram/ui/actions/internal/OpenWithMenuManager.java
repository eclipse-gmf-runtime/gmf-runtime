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

import org.eclipse.jface.action.Action;

import org.eclipse.gmf.runtime.common.ui.action.ActionMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Messages;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;

/**
 * @author gsturov
 * @canBeSeenBy %level1
 */
public class OpenWithMenuManager extends ActionMenuManager {
	/**
	 * The New menu action containing the UI for the new menu manager
	 */
	private static class OpenWithMenuAction extends Action {
		public OpenWithMenuAction() {			
			setText(Messages.getString("OpenWithMenu.OpenWithMenuText")); //$NON-NLS-1$
			setToolTipText(Messages.getString("OpenWithMenu.OpenWithMenuTooltip")); //$NON-NLS-1$
			//setImageDescriptor(Images.DESC_ACTION_SHOW_IN);
			//setHoverImageDescriptor(Images.DESC_ACTION_SHOW_IN);
		}
	}

	/**
	 * Creates a new instance of the filter menu manager
	 */
	public OpenWithMenuManager() {
		super(ActionIds.MENU_OPEN_WITH, new OpenWithMenuAction(), true);
	}
}
