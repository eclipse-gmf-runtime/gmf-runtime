/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
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
 * The menu manager for arrow type.
 * 
 * @author Anthony Hunter
 */
public class ArrowTypeMenuManager extends ActionMenuManager {

	/**
	 * The arrow type menu action containing the UI for the arrow type menu
	 * manager
	 */
	private static class ArrowTypeMenuAction extends Action {
		public ArrowTypeMenuAction() {
			setText(DiagramUIActionsMessages.ArrowTypeActionMenu_arrowTypeText);
			setToolTipText(DiagramUIActionsMessages.ArrowTypeActionMenu_arrowTypeToolTip);
			setImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE);
			setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE_DISABLED);
			setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE);
		}
	}

	/**
	 * Constructor for an ArrowTypeMenuManager.
	 */
	public ArrowTypeMenuManager() {
		super(ActionIds.MENU_ARROW_TYPE, new ArrowTypeMenuAction(), true);
	}

}