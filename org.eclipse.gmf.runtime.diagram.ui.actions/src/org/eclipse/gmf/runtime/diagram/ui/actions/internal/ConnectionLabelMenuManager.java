/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.gmf.runtime.common.ui.action.ActionMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author mboos
 * @canBeSeenBy %level1
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
			setText(DiagramActionsResourceManager
				.getI18NString("ShowConnectionLabelsActionMenu.ShowConnectionLabelsText")); //$NON-NLS-1$
			setToolTipText(DiagramActionsResourceManager
				.getI18NString("ShowConnectionLabelsActionMenu.ShowConnectionLabelsTooltip")); //$NON-NLS-1$

			ImageDescriptor enabledImage = DiagramActionsResourceManager
				.getInstance()
				.getImageDescriptor(
					DiagramActionsResourceManager.IMAGE_SHOW_HIDE_CONNECTION_LABELS_GROUP);
			setImageDescriptor(enabledImage);
			setDisabledImageDescriptor(DiagramActionsResourceManager
				.getInstance()
				.getImageDescriptor(
					DiagramActionsResourceManager.IMAGE_SHOW_HIDE_CONNECTION_LABELS_GROUP_DISABLED));
			setHoverImageDescriptor(enabledImage);
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