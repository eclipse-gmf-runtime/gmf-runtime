/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.gmf.runtime.common.ui.action.ActionMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsPluginImages;
import org.eclipse.jface.action.Action;

/**
 * @author mboos
 * 
 * The connection label menu manager. It contains all connection label-related
 * actions
 */
public class ConnectionLabelMenuManager
	extends ActionMenuManager {

	/**
	 * The compartment menu action containing the UI for the connection label
	 * menu manager
	 */
	private static class ConnectionLabelMenuAction
		extends Action {

		public ConnectionLabelMenuAction() {
			setText(DiagramUIActionsMessages.ShowConnectionLabelsActionMenu_ShowConnectionLabelsText);
			setToolTipText(DiagramUIActionsMessages.ShowConnectionLabelsActionMenu_ShowConnectionLabelsTooltip);

			setImageDescriptor(DiagramUIActionsPluginImages.DESC_SHOW_HIDE_CONNECTION_LABELS_GROUP);
			setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_SHOW_HIDE_CONNECTION_LABELS_GROUP_DISABLED);
			setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_SHOW_HIDE_CONNECTION_LABELS_GROUP);
		}
	}

	/**
	 * Creates a new instance of the connection label menu manager
	 */
	public ConnectionLabelMenuManager() {
		super(ActionIds.MENU_CONNECTION_LABEL, new ConnectionLabelMenuAction(),
			true);
	}

}
