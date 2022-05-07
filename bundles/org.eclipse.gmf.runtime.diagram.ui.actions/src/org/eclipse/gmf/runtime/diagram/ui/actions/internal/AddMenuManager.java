/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.jface.action.Action;

/**
 * @author schafe
 */
public class AddMenuManager extends ActionMenuManager {

	/**
	 * The arrange menu action containing the UI for the arrange menu manager
	 */
	private static class AddMenuAction extends Action {
		public AddMenuAction() {
			setText(DiagramUIActionsMessages.Add_menuItem);
			setToolTipText(DiagramUIActionsMessages.Add_menuItem);	
		}
	}
	
	/**
	 * Creates a new instance of the arrange menu manager
	 */
	public AddMenuManager( String menuId ) {
		super( menuId, new AddMenuAction(), true );
	}
}

