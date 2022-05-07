/******************************************************************************
 * Copyright (c) 2004, 2 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.services.action.global.AbstractGlobalActionHandler;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;

/**
 * Proxy that instantiates the diagram action when necessary and destroys it
 * after use.
 * 
 * @author wdiu, Wayne Diu
 */
abstract public class AbstractDiagramGlobalActionProxy
	extends AbstractGlobalActionHandler {

	/**
	 * Instantiate the DiagramAction. You should call dispose() on the action
	 * when done with it. This is particularly important when the action is a
	 * selection listener.
	 * 
	 * Subclasses should return an instance of the DiagramAction
	 * 
	 * @param context
	 *            the <code>IGlobalActionContext</code> holding nevessary
	 *            context information for the action
	 * @return DiagramAction the newly instantiated action
	 */
	abstract protected DiagramAction instantiateAction(
			IGlobalActionContext context);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#getCommand(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public ICommand getCommand(IGlobalActionContext cntxt) {
		DiagramAction action = instantiateAction(cntxt);
		action.init();
		action.refresh();
		action.run();
		action.dispose();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#canHandle(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public boolean canHandle(IGlobalActionContext cntxt) {
		DiagramAction action = instantiateAction(cntxt);
		action.init();
		action.refresh();
		boolean isEnabled = action.isEnabled();
		action.dispose();
		return isEnabled;
	}
}