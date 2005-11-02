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

import org.eclipse.gmf.runtime.common.ui.action.ActionMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.jface.action.Action;

/**
 * The ZOrder menu manager. It contains all Order-related actions
 * 
 * @author jschofie
 * @canBeSeenBy %level1
 */
public class ZOrderMenuManager extends ActionMenuManager {
	
	private static final String MENU_LABEL   = "ZOrderMenuManager.Order.ActionLabelText"; //$NON-NLS-1$
	private static final String MENU_TOOLTIP = "ZOrderMenuManager.Order.ActionToolTipText"; //$NON-NLS-1$

	/**
	 * The order menu action containing the UI for the zorder menu manager
	 */
	private static class ZOrderMenuAction extends Action {
		public ZOrderMenuAction() {
			setText(DiagramActionsResourceManager.getI18NString( MENU_LABEL ));
			setToolTipText(DiagramActionsResourceManager.getI18NString( MENU_TOOLTIP ));
		}
	}

	/**
	 * Creates a new instance of the arrange menu manager
	 */
	public ZOrderMenuManager() {
		super(ActionIds.MENU_ZORDER, new ZOrderMenuAction(), true);
	}
}
