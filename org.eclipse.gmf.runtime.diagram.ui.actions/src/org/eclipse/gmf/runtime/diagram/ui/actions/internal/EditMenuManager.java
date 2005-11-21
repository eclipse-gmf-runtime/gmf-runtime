/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.jface.action.Action;

/**
 * The edit menu manager. It contains all edit-related actions.
 *
 * @author chmahone
 * @canBeSeenBy %level1
 */
public class EditMenuManager extends ActionMenuManager {

	/**
	 * The edit menu action containing the UI for the edit menu manager
	 */
	private static class EditMenuAction extends Action {
		public EditMenuAction() {
			setText(DiagramActionsResourceManager.getI18NString("EditMenuManager.Edit.ActionLabelText")); //$NON-NLS-1$
			setToolTipText(DiagramActionsResourceManager.getI18NString("EditMenuManager.Edit.ActionToolTipText")); //$NON-NLS-1$
		}
	}

	/**
	 * Creates a new instance of the edit menu manager
	 */
	public EditMenuManager() {
		super(ActionIds.MENU_EDIT, new EditMenuAction(), true);
	}
}
