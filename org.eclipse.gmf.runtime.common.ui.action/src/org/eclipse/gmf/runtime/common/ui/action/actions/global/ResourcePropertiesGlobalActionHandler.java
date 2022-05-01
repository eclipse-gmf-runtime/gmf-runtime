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

package org.eclipse.gmf.runtime.common.ui.action.actions.global;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.PropertyDialogAction;

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
			cntxt.getActivePart().getSite(), cntxt.getActivePart().getSite()
				.getSelectionProvider());
		propertyAction.selectionChanged(getResourceSelection((IStructuredSelection)cntxt.getSelection()));
		propertyAction.run();
		propertyAction.dispose();
		propertyAction = null;
		return null;
	}

}