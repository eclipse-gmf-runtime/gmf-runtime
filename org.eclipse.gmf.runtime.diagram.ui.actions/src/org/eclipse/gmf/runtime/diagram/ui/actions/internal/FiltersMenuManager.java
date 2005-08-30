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
 * The filters menu manager. It contains all filters-related actions.
 *
 * @author chmahone
 * @canBeSeenBy %level1
 */
public class FiltersMenuManager extends ActionMenuManager {

	/**
	 * The filters menu action containing the UI for the filters menu manager
	 */
	private static class FiltersMenuAction extends Action {
		public FiltersMenuAction() {
			setText(Messages.getString("FiltersMenuManager.Filters.ActionLabelText")); //$NON-NLS-1$
			setToolTipText(Messages.getString("FiltersMenuManager.Filters.ActionToolTipText")); //$NON-NLS-1$
		}
	}

	/**
	 * Creates a new instance of the filters menu manager
	 */
	public FiltersMenuManager() {
		super(ActionIds.MENU_FILTERS, new FiltersMenuAction(), true);
	}

}
