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
 * @author mboos
 * @canBeSeenBy %level1
 * 
 * The connector label menu manager. It contains all connector label-related
 * actions
 */
public class ConnectorLabelMenuManager
	extends ActionMenuManager {

	/**
	 * The compartment menu action containing the UI for the connector label
	 * menu manager
	 */
	private static class ConnectorLabelMenuAction
		extends Action {

		public ConnectorLabelMenuAction() {
			setText(Messages
				.getString("ShowConnectorLabelsActionMenu.ShowConnectorLabelsText")); //$NON-NLS-1$
			setToolTipText(Messages
				.getString("ShowConnectorLabelsActionMenu.ShowConnectorLabelsTooltip")); //$NON-NLS-1$
			setImageDescriptor(Images.DESC_ACTION_SHOW_HIDE_CONNECTOR_LABELS_GROUP);
			setDisabledImageDescriptor(Images.DESC_ACTION_SHOW_HIDE_CONNECTOR_LABELS_GROUP_DISABLED);
			setHoverImageDescriptor(Images.DESC_ACTION_SHOW_HIDE_CONNECTOR_LABELS_GROUP);
		}
	}

	/**
	 * Creates a new instance of the connector label menu manager
	 */
	public ConnectorLabelMenuManager() {
		super(ActionIds.MENU_CONNECTOR_LABEL, new ConnectorLabelMenuAction(),
			true);
	}

}