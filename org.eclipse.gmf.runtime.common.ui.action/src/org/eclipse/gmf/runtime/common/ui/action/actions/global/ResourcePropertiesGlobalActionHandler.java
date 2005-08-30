/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.action.actions.global;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.PropertyDialogAction;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;

/**
 * Global action handler that opens a property page on resources using the
 * Eclipse {@link org.eclipse.ui.dialogs.PropertyDialogAction}.
 * 
 * @author ldamus
 */
public class ResourcePropertiesGlobalActionHandler
	extends ResourceGlobalActionHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#getCommand(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public ICommand getCommand(IGlobalActionContext cntxt) {
		PropertyDialogAction propertyAction = new PropertyDialogAction(
			getShell(cntxt.getActivePart()), cntxt.getActivePart().getSite()
				.getSelectionProvider());
		propertyAction.selectionChanged(getResourceSelection((IStructuredSelection)cntxt.getSelection()));
		propertyAction.run();
		propertyAction.dispose();
		propertyAction = null;
		return null;
	}

}