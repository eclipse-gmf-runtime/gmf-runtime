/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.action.ide.actions.global;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.MoveResourceAction;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.action.actions.global.ResourceGlobalActionHandler;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;

/**
 * Global action handler that moves resources using the Eclipse 
 * {@link org.eclipse.ui.actions.MoveResourceAction}.
 * 
 * @author ldamus
 */
public class MoveResourceGlobalActionHandler
	extends ResourceGlobalActionHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.core.global.AbstractGlobalActionHandler#getCommand()
	 */
	public ICommand getCommand(IGlobalActionContext cntxt) {
		MoveResourceAction moveAction =
			new MoveResourceAction(getShell(cntxt.getActivePart()));
		moveAction.selectionChanged(getResourceSelection((IStructuredSelection)cntxt.getSelection()));
		moveAction.run();
		return null;
	}
}
