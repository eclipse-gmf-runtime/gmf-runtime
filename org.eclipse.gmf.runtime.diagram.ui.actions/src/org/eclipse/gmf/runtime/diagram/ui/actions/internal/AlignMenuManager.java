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
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Images;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Messages;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;

/**
 * @author melaasar
 * @canBeSeenBy %level1
 *
 * The align menu manager. It contains all align-related actions
 */
public class AlignMenuManager extends ActionMenuManager {

	/**
	 * The align menu action containing the UI for the align menu manager
	 */
	private static class AlignMenuAction extends Action {
		public AlignMenuAction() {
			setText(Messages.getString("AlignActionMenu.AlignDropDownText")); //$NON-NLS-1$
			setToolTipText(Messages.getString("AlignActionMenu.AlignDropDownTooltip")); //$NON-NLS-1$
			setImageDescriptor(Images.DESC_ACTION_ALIGN);
			setHoverImageDescriptor(Images.DESC_ACTION_ALIGN);
		}
	}

	/**
	 * Creates a new instance of the align menu manager
	 */
	public AlignMenuManager() {
		super(ActionIds.MENU_ALIGN, new AlignMenuAction(), true);
	}

}
