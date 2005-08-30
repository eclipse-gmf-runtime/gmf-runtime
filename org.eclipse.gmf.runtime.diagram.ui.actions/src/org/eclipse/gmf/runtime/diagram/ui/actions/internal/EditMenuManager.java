/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
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
 * The edit menu manager. It contains all edit-related actions.
 *
 * @author chmahone
 * @canBeSeenBy %level1
 */
public class EditMenuManager extends ActionMenuManager {

	/**
	 * The edit menu action containing the UI for the edit menu manager
	 */
	private static class EditMenuAction extends Action {
		public EditMenuAction() {
			setText(Messages.getString("EditMenuManager.Edit.ActionLabelText")); //$NON-NLS-1$
			setToolTipText(Messages.getString("EditMenuManager.Edit.ActionToolTipText")); //$NON-NLS-1$
		}
	}

	/**
	 * Creates a new instance of the edit menu manager
	 */
	public EditMenuManager() {
		super(ActionIds.MENU_EDIT, new EditMenuAction(), true);
	}
}
