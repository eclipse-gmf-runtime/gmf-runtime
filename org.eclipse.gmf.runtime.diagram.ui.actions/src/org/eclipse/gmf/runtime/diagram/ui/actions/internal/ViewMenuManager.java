/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.jface.action.Action;

import org.eclipse.gmf.runtime.common.ui.action.ActionMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Messages;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;

/**
 * The View Menu Manager contains all View related actions
 * 
 * @author jschofie
 * @canBeSeenBy %level1
 */
public class ViewMenuManager extends ActionMenuManager {

	private static final String MENU_LABEL   = "ViewMenuManager.View.ActionLabelText"; //$NON-NLS-1$
	private static final String MENU_TOOLTIP = "ViewMenuManager.View.ActionToolTipText"; //$NON-NLS-1$

	/**
	 * The align menu action containing the UI for the align menu manager
	 */
	private static class ViewMenuAction extends Action {
		public ViewMenuAction() {
			setText(Messages.getString( MENU_LABEL ));
			setToolTipText(Messages.getString( MENU_TOOLTIP ));
		}
	}

	/**
	 * Creates a new instance of the align menu manager
	 */
	public ViewMenuManager() {
		super(ActionIds.MENU_VIEW, new ViewMenuAction(), true);
	}
}
