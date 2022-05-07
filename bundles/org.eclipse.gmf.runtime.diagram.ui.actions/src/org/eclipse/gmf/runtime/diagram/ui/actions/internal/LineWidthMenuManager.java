/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsPluginImages;
import org.eclipse.jface.action.Action;

/**
 * The menu manager for line width.
 * 
 * @author Anthony Hunter
 */
public class LineWidthMenuManager extends ActionMenuManager {

	/**
	 * The line width menu action containing the UI for the line width menu manager
	 */
	private static class LineWidthMenuAction extends Action {
		public LineWidthMenuAction() {
			setText(DiagramUIActionsMessages.LineWidthActionMenu_lineWidthText);
			setToolTipText(DiagramUIActionsMessages.LineWidthActionMenu_lineWidthToolTip);
	        setImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_WIDTH);
	        setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_WIDTH_DISABLED);
	        setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_WIDTH);
		}
	}

	/**
	 * Constructor for a LineWidthMenuManager.
	 */
	public LineWidthMenuManager() {
		super(ActionIds.MENU_LINE_WIDTH, new LineWidthMenuAction(), true);
	}

}
