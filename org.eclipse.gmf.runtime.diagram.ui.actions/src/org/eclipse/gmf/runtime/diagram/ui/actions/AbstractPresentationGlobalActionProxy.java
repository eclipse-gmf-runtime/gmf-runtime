/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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