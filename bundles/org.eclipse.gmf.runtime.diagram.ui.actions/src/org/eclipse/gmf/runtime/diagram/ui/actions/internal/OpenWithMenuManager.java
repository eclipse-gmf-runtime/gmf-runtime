/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
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
import org.eclipse.jface.action.Action;

/**
 * @author gsturov
 */
public class OpenWithMenuManager extends ActionMenuManager {
	/**
	 * The New menu action containing the UI for the new menu manager
	 */
	private static class OpenWithMenuAction extends Action {
		public OpenWithMenuAction() {			
			setText(DiagramUIActionsMessages.OpenWithMenu_OpenWithMenuText);
			setToolTipText(DiagramUIActionsMessages.OpenWithMenu_OpenWithMenuTooltip);
			//setImageDescriptor(Images.DESC_ACTION_SHOW_IN);
			//setHoverImageDescriptor(Images.DESC_ACTION_SHOW_IN);
		}
	}

	/**
	 * Creates a new instance of the filter menu manager
	 */
	public OpenWithMenuManager() {
		super(ActionIds.MENU_OPEN_WITH, new OpenWithMenuAction(), true);
	}
}
