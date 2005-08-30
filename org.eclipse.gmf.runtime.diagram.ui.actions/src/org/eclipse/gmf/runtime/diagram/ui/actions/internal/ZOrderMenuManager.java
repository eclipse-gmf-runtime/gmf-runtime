/****************************************************************************
  Licensed Materials - Property of IBM
  (C) Copyright IBM Corp. 2004. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
*****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.jface.action.Action;

import org.eclipse.gmf.runtime.common.ui.action.ActionMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Messages;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;

/**
 * The ZOrder menu manager. It contains all Order-related actions
 * 
 * @author jschofie
 * @canBeSeenBy %level1
 */
public class ZOrderMenuManager extends ActionMenuManager {
	
	private static final String MENU_LABEL   = "ZOrderMenuManager.Order.ActionLabelText"; //$NON-NLS-1$
	private static final String MENU_TOOLTIP = "ZOrderMenuManager.Order.ActionToolTipText"; //$NON-NLS-1$

	/**
	 * The order menu action containing the UI for the zorder menu manager
	 */
	private static class ZOrderMenuAction extends Action {
		public ZOrderMenuAction() {
			setText(Messages.getString( MENU_LABEL ));
			setToolTipText(Messages.getString( MENU_TOOLTIP ));
		}
	}

	/**
	 * Creates a new instance of the arrange menu manager
	 */
	public ZOrderMenuManager() {
		super(ActionIds.MENU_ZORDER, new ZOrderMenuAction(), true);
	}
}
