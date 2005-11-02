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

import org.eclipse.gmf.runtime.common.ui.action.ActionMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author melaasar
 * @canBeSeenBy %level1
 *
 * The router menu manager. It contains all router-related actions
 */
public class RouterMenuManager extends ActionMenuManager {

	/**
	 * The router menu action containing the UI for the router menu manager
	 */
	private static class RouterMenuAction extends Action {
		public RouterMenuAction() {
			setText(DiagramActionsResourceManager.getI18NString("RouterActionMenu.LineStyleDropDownText")); //$NON-NLS-1$
			setToolTipText(DiagramActionsResourceManager.getI18NString("RouterActionMenu.LineStyleDropDownTooltip")); //$NON-NLS-1$
			
			ImageDescriptor enabledImage = DiagramActionsResourceManager
				.getInstance().getImageDescriptor(
					DiagramActionsResourceManager.IMAGE_CHANGEROUTER_GROUP);
			setImageDescriptor(enabledImage);
			setDisabledImageDescriptor(DiagramActionsResourceManager
				.getInstance()
				.getImageDescriptor(
					DiagramActionsResourceManager.IMAGE_CHANGEROUTER_GROUP_DISABLED));
			setHoverImageDescriptor(enabledImage);			
		}
	}

	/**
	 * Creates a new instance of the router menu manager
	 */
	public RouterMenuManager() {
		super(ActionIds.MENU_ROUTER, new RouterMenuAction(), true);
	}

}
