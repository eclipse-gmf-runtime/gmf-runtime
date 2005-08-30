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
import org.eclipse.ui.actions.DeleteResourceAction;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.action.actions.global.ResourceGlobalActionHandler;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;

/**
 * Global action handler that delete resources using the Eclipse 
 * {@link org.eclipse.ui.actions.DeleteResourceAction}.
 * 
 * @author ldamus
 */
public class DeleteResourceGlobalActionHandler
	extends ResourceGlobalActionHandler {


	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#getCommand(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public ICommand getCommand(IGlobalActionContext cntxt) {
		DeleteResourceAction deleteAction =
			new DeleteResourceAction(getShell(cntxt.getActivePart()));
		deleteAction.selectionChanged(getResourceSelection((IStructuredSelection)cntxt.getSelection()));
		deleteAction.run();
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#canHandle(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public boolean canHandle(IGlobalActionContext cntxt) {
		DeleteResourceAction deleteAction = new DeleteResourceAction(getShell(cntxt.getActivePart()));
		deleteAction.selectionChanged(getResourceSelection((IStructuredSelection)cntxt.getSelection()));
		return super.canHandle(cntxt) && deleteAction.isEnabled();
	}
}
