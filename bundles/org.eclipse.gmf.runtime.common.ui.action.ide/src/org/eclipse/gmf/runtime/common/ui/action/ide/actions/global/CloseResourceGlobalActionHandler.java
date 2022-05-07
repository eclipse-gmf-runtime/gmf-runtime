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

import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.action.actions.global.ResourceGlobalActionHandler;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.CloseResourceAction;

/**
 * Global action handler that closes resources using the Eclipse
 * {@link org.eclipse.ui.actions.CloseResourceAction}.
 * 
 * @author ldamus
 */
public class CloseResourceGlobalActionHandler
	extends ResourceGlobalActionHandler {
	

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#getCommand(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public ICommand getCommand(IGlobalActionContext cntxt) {
		CloseResourceAction closeAction = new CloseResourceAction(cntxt.getActivePart().getSite());
		closeAction.selectionChanged(getResourceSelection((IStructuredSelection)cntxt.getSelection()));
		closeAction.run();
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#canHandle(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public boolean canHandle(IGlobalActionContext cntxt) {
		
		if (super.canHandle(cntxt)) {
			for (Iterator i = getResourceSelection((IStructuredSelection)cntxt.getSelection()).iterator(); i.hasNext();) {
				IResource nextResource = (IResource) i.next();
				if (nextResource.getType() != IResource.PROJECT
					|| !((IProject) nextResource).isOpen()) {
					return false;
				}
			}
		}
		return true;
	}

}