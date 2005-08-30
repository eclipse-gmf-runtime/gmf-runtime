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
 * The compartment menu manager. It contains all compartment-related actions
 */
public class CompartmentMenuManager extends ActionMenuManager {

	/**
	 * The compartment menu action containing the UI for the compartment menu manager
	 */
	private static class CompartmentMenuAction extends Action {
		public CompartmentMenuAction() {
			setText(Messages.getString("ShowResizableCompartmentActionMenu.ShowResizeableCompartmentsText")); //$NON-NLS-1$
			setToolTipText(Messages.getString("ShowResizableCompartmentActionMenu.ShowResizeableCompartmentsTooltip")); //$NON-NLS-1$
			setImageDescriptor(Images.DESC_ACTION_SHOW_HIDE_COMPARTMENTS_GROUP);
			setDisabledImageDescriptor(Images.DESC_ACTION_SHOW_HIDE_COMPARTMENTS_GROUP_DISABLED);
			setHoverImageDescriptor(Images.DESC_ACTION_SHOW_HIDE_COMPARTMENTS_GROUP);
		}
	}

	/**
	 * Creates a new instance of the compartment menu manager
	 */
	public CompartmentMenuManager() {
		super(ActionIds.MENU_COMPARTMENT, new CompartmentMenuAction(), true);
	}

}
