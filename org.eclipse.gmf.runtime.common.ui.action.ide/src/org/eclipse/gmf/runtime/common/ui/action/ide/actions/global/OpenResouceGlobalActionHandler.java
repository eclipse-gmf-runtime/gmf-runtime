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

import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.OpenResourceAction;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.action.actions.global.ResourceGlobalActionHandler;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;

/**
 * Global action handler that opens resources using the Eclipse 
 * {@link org.eclipse.ui.actions.OpenResourceAction}.
 * 
 * @author ldamus
 */
public class OpenResouceGlobalActionHandler
	extends ResourceGlobalActionHandler {


	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#getCommand(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public ICommand getCommand(IGlobalActionContext cntxt) {
		OpenResourceAction openAction =
			new OpenResourceAction(getShell(cntxt.getActivePart()));
		openAction.selectionChanged(getResourceSelection((IStructuredSelection)cntxt.getSelection()));
		openAction.run();

		
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
					|| ((IProject) nextResource).isOpen()) {
					return false;
				}
			}
		}
		return super.canHandle(cntxt);
	}
}
