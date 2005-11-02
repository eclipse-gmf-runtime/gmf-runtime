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
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.jface.action.Action;

/**
 * @author gsturov
 * @canBeSeenBy %level1
 */
public class ShowInMenuManager extends ActionMenuManager {
	/**
	 * The New menu action containing the UI for the new menu manager
	 */
	private static class ShowInMenuAction extends Action {
		public ShowInMenuAction() {			
			setText(DiagramActionsResourceManager.getI18NString("ShowInMenu.ShowInMenuText")); //$NON-NLS-1$
			setToolTipText(DiagramActionsResourceManager.getI18NString("ShowInMenu.ShowInMenuTooltip")); //$NON-NLS-1$
			//setImageDescriptor(Images.DESC_ACTION_SHOW_IN);
			//setHoverImageDescriptor(Images.DESC_ACTION_SHOW_IN);
		}
	}

	/**
	 * Creates a new instance of the filter menu manager
	 */
	public ShowInMenuManager() {
		super(ActionIds.MENU_SHOW_IN, new ShowInMenuAction(), true);
	}
}
