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
 * The file menu manager. It contains all file-related actions.
 *
 * @author chmahone
 * @canBeSeenBy %level1
 */
public class FileMenuManager extends ActionMenuManager {

	/**
	 * The file menu action containing the UI for the file menu manager
	 */
	private static class FileMenuAction extends Action {
		public FileMenuAction() {
			setText(Messages.getString("FileMenuManager.File.ActionLabelText")); //$NON-NLS-1$
			setToolTipText(Messages.getString("FileMenuManager.File.ActionToolTipText")); //$NON-NLS-1$
		}
	}

	/**
	 * Creates a new instance of the file menu manager
	 */
	public FileMenuManager() {
		super(ActionIds.MENU_FILE, new FileMenuAction(), false);
	}

}
