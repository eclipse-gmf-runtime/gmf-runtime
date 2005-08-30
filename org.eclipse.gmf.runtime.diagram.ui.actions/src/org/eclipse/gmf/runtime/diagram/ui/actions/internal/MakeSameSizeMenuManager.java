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
public class MakeSameSizeMenuManager extends ActionMenuManager {
	
	private static final String MENU_LABEL   = "SameSizeMenuManager.MakeSameSize.ActionLabelText"; //$NON-NLS-1$
	private static final String MENU_TOOLTIP = "SameSizeMenuManager.MakeSameSize.ActionToolTipText"; //$NON-NLS-1$

	/**
	 * The order menu action containing the UI for the zorder menu manager
	 */
	private static class MakeSameMenuAction extends Action {
		public MakeSameMenuAction() {
			setText(Messages.getString( MENU_LABEL ));
			setToolTipText(Messages.getString( MENU_TOOLTIP ));
		}
	}

	/**
	 * Creates a new instance of the arrange menu manager
	 */
	public MakeSameSizeMenuManager() {
		super(ActionIds.MENU_MAKE_SAME_SIZE, new MakeSameMenuAction(), true);
	}
}
