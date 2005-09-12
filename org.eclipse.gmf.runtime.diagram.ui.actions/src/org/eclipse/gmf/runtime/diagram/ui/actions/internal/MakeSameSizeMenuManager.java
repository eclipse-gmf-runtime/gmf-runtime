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
 * The ZOrder menu manager. It contains all Order-related actions
 * 
 * @author jschofie
 * @canBeSeenBy %level1
 */
public class MakeSameSizeMenuManager extends ActionMenuManager {
	
	private static final String MENU_LABEL   = "SameSizeMenuManager.MakeSameSize.ActionLabelText"; //$NON-NLS-1$
	private static final String MENU_TOOLTIP = "SameSizeMenuManager.MakeSameSize.ActionToolTipText"; //$NON-NLS-1$

	/**
	 * The order menu action containing the UI for the zorder menu manager
	 */
	private static class MakeSameMenuAction extends Action {
		public MakeSameMenuAction() {
			setText(Messages.getString( MENU_LABEL ));
			setToolTipText(Messages.getString( MENU_TOOLTIP ));
		}
	}

	/**
	 * Creates a new instance of the arrange menu manager
	 */
	public MakeSameSizeMenuManager() {
		super(ActionIds.MENU_MAKE_SAME_SIZE, new MakeSameMenuAction(), true);
	}
}
