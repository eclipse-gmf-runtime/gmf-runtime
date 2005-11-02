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
 * The filters menu manager. It contains all filters-related actions.
 *
 * @author chmahone
 * @canBeSeenBy %level1
 */
public class FiltersMenuManager extends ActionMenuManager {

	/**
	 * The filters menu action containing the UI for the filters menu manager
	 */
	private static class FiltersMenuAction extends Action {
		public FiltersMenuAction() {
			setText(DiagramActionsResourceManager.getI18NString("FiltersMenuManager.Filters.ActionLabelText")); //$NON-NLS-1$
			setToolTipText(DiagramActionsResourceManager.getI18NString("FiltersMenuManager.Filters.ActionToolTipText")); //$NON-NLS-1$
		}
	}

	/**
	 * Creates a new instance of the filters menu manager
	 */
	public FiltersMenuManager() {
		super(ActionIds.MENU_FILTERS, new FiltersMenuAction(), true);
	}

}
