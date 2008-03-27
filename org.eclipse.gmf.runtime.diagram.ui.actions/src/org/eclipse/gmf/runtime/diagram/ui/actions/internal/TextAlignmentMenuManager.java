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
import org.eclipse.jface.action.Action;

/**
 * The menu manager for text alignment.
 * 
 * @author Anthony Hunter
 */
public class TextAlignmentMenuManager extends ActionMenuManager {

	/**
	 * The text alignment menu action containing the UI for the text alignment menu manager
	 */
	private static class TextAlignmentMenuAction extends Action {
		public TextAlignmentMenuAction() {
			setText(DiagramUIActionsMessages.TextAlignmentActionMenu_textAlignmentText);
			setToolTipText(DiagramUIActionsMessages.TextAlignmentActionMenu_textAlignmentToolTip);
		}
	}

	/**
	 * Creates a new instance of the text alignment menu manager
	 */
	public TextAlignmentMenuManager() {
		super(ActionIds.MENU_TEXT_ALIGNMENT, new TextAlignmentMenuAction(), true);
	}
}
