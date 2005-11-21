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
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

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
			setText(DiagramActionsResourceManager.getI18NString("ArrangeMenuManager.Arrange.ActionLabelText")); //$NON-NLS-1$
			setToolTipText(DiagramActionsResourceManager.getI18NString("ArrangeMenuManager.Arrange.ActionToolTipText")); //$NON-NLS-1$
			
			ImageDescriptor enabledImage = DiagramActionsResourceManager
				.getInstance().getImageDescriptor(
					DiagramActionsResourceManager.IMAGE_ARRANGE_ALL);
			setImageDescriptor(enabledImage);
			setDisabledImageDescriptor(DiagramActionsResourceManager
				.getInstance().getImageDescriptor(
					DiagramActionsResourceManager.IMAGE_ARRANGE_ALL_DISABLED));
			setHoverImageDescriptor(enabledImage);
		}
	}

	/**
	 * Creates a new instance of the arrange menu manager
	 */
	public ArrangeMenuManager() {
		super(ActionIds.MENU_ARRANGE, new ArrangeMenuAction(), true);
	}

}
