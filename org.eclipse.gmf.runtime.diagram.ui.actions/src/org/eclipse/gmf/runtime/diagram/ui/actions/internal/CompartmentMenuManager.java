/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsPluginImages;
import org.eclipse.jface.action.Action;

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
			setText(DiagramUIActionsMessages.ShowResizableCompartmentActionMenu_ShowResizeableCompartmentsText);
			setToolTipText(DiagramUIActionsMessages.ShowResizableCompartmentActionMenu_ShowResizeableCompartmentsTooltip);

			setImageDescriptor(DiagramUIActionsPluginImages.DESC_SHOW_HIDE_COMPARTMENTS_GROUP);
			setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_SHOW_HIDE_COMPARTMENTS_GROUP_DISABLED);
			setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_SHOW_HIDE_COMPARTMENTS_GROUP);
		}
	}

	/**
	 * Creates a new instance of the compartment menu manager
	 */
	public CompartmentMenuManager() {
		super(ActionIds.MENU_COMPARTMENT, new CompartmentMenuAction(), true);
	}

}
