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
 * @deprecated Renamed to {@link org.eclipse.gmf.runtime.diagram.ui.actions.AbstractDiagramGlobalActionProxy}
 */
abstract public class AbstractPresentationGlobalActionProxy extends AbstractGlobalActionHandler {
	
	/**
	 * Instantiate the DiagramAction.  You should call dispose() on the
	 * action when done with it.  This is particularly important when the
	 * action is a selection listener.
	 * 
	 * Subclasses should return an instance of the DiagramAction
	 * 
	 * @param context the <code>IGlobalActionContext</code> holding nevessary context information for the action 
	 * @return DiagramAction the newly instantiated action
	 */	
	abstract protected DiagramAction instantiateAction(IGlobalActionContext context);
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#getCommand(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public ICommand getCommand(IGlobalActionContext cntxt) {
		DiagramAction presentationAction =
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
		DiagramAction presentationAction = instantiateAction(cntxt);
		presentationAction.init();
		presentationAction.refresh();
		boolean isEnabled = presentationAction.isEnabled();
		presentationAction.dispose();
		return isEnabled;
	}
}