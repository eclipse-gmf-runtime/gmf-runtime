/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Images;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Messages;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;

/**
 * @author melaasar
 * @canBeSeenBy %level1
 *
 * The arrange menu manager. It contains all arrange-related actions
 */
public class ArrangeMenuManager extends ActionMenuManager {

	/**
	 * The arrange menu action containing the UI for the arrange menu manager
	 */
	private static class ArrangeMenuAction extends Action {
		public ArrangeMenuAction() {
			setText(Messages.getString("ArrangeMenuManager.Arrange.ActionLabelText")); //$NON-NLS-1$
			setToolTipText(Messages.getString("ArrangeMenuManager.Arrange.ActionToolTipText")); //$NON-NLS-1$
			setImageDescriptor(Images.DESC_ACTION_ARRANGE_ALL);
			setDisabledImageDescriptor(Images.DESC_ACTION_ARRANGE_ALL_DISABLED);
			setHoverImageDescriptor(Images.DESC_ACTION_ARRANGE_ALL);
		}
	}

	/**
	 * Creates a new instance of the arrange menu manager
	 */
	public ArrangeMenuManager() {
		super(ActionIds.MENU_ARRANGE, new ArrangeMenuAction(), true);
	}

}
