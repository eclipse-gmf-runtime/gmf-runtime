/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

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

