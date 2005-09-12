/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;

/**
 * @author schafe
 * @canBeSeenBy %level1
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AddMenuManager extends ActionMenuManager {

	/**
	 * The arrange menu action containing the UI for the arrange menu manager
	 */
	private static class AddMenuAction extends Action {
		public AddMenuAction() {
			setText( DiagramActionsResourceManager.getInstance().getString( "Add.menuItem" ) ); //$NON-NLS-1$
			setToolTipText( DiagramActionsResourceManager.getInstance().getString( "Add.menuItem" ) ); //$NON-NLS-1$			
		}
	}
	
	/**
	 * Creates a new instance of the arrange menu manager
	 */
	public AddMenuManager( String menuId ) {
		super( menuId, new AddMenuAction(), true );
	}
}

