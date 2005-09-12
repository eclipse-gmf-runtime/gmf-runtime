/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.services.action.global.AbstractGlobalActionHandler;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;

/**
 * Proxy that instantiates the presentation action when necessary
 * and destroys it after use.
 * 
 * @author wdiu, Wayne Diu
 */
abstract public class AbstractPresentationGlobalActionProxy extends AbstractGlobalActionHandler {
	
	/**
	 * Instantiate the PresentationAction.  You should call dispose() on the
	 * action when done with it.  This is particularly important when the
	 * action is a selection listener.
	 * 
	 * Subclasses should return an instance of the PresentationAction
	 * 
	 * @param context the <code>IGlobalActionContext</code> holding nevessary context information for the action 
	 * @return PresentationAction the newly instantiated action
	 */	
	abstract protected PresentationAction instantiateAction(IGlobalActionContext context);
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#getCommand(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public ICommand getCommand(IGlobalActionContext cntxt) {
		PresentationAction presentationAction =
			instantiateAction(cntxt);
		presentationAction.init();
		presentationAction.refresh();
		presentationAction.run();
		presentationAction.dispose();
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#canHandle(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public boolean canHandle(IGlobalActionContext cntxt) {
		PresentationAction presentationAction = instantiateAction(cntxt);
		presentationAction.init();
		presentationAction.refresh();
		boolean isEnabled = presentationAction.isEnabled();
		presentationAction.dispose();
		return isEnabled;
	}
}