/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.action.ide.actions.global;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.action.actions.global.ResourceGlobalActionHandler;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.DeleteResourceAction;

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
			new DeleteResourceAction(cntxt.getActivePart().getSite());
		deleteAction.selectionChanged(getResourceSelection((IStructuredSelection)cntxt.getSelection()));
		deleteAction.run();
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#canHandle(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public boolean canHandle(IGlobalActionContext cntxt) {
		DeleteResourceAction deleteAction = new DeleteResourceAction(cntxt.getActivePart().getSite());
		deleteAction.selectionChanged(getResourceSelection((IStructuredSelection)cntxt.getSelection()));
		return super.canHandle(cntxt) && deleteAction.isEnabled();
	}
}
