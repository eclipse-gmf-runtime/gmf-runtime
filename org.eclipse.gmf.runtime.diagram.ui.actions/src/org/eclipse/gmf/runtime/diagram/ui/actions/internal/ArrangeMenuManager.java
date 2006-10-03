/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler;
import org.eclipse.gmf.runtime.common.ui.action.ActionMenuManager;
import org.eclipse.gmf.runtime.common.ui.util.IPartSelector;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPart;

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
			setText(DiagramUIActionsMessages.ArrangeMenuManager_Arrange_ActionLabelText);
			setToolTipText(DiagramUIActionsMessages.ArrangeMenuManager_Arrange_ActionToolTipText);
			
			setImageDescriptor(DiagramUIActionsPluginImages.DESC_ARRANGE_ALL);
			setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_ARRANGE_ALL_DISABLED);
			setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_ARRANGE_ALL);
		}
	}

    /**
     * Creates a new instance of the arrange menu manager
     */
    public ArrangeMenuManager() {
        super(ActionIds.MENU_ARRANGE, new ArrangeMenuAction(), true);
    }    
    
	/**
	 * Creates a new instance of the arrange menu manager
     * @param action default action associated with this menu manager (should not be null)
	 */
	public ArrangeMenuManager(IAction action) {
		super(ActionIds.MENU_ARRANGE, action, true);

        // If the action is null then use the original menu action
        if (action == null) {
            action = new ArrangeMenuAction();
        }
        
       ((AbstractActionHandler) getDefaultAction()).setPartSelector(new IPartSelector() {
            public boolean selects(IWorkbenchPart p) {
                return p instanceof IDiagramWorkbenchPart;
            }
        });
    }    
}
