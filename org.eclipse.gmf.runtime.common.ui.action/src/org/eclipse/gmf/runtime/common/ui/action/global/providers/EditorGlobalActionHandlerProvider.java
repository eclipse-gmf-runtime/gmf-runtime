/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.action.global.providers;

import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.services.action.global.AbstractGlobalActionHandlerProvider;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandlerContext;

/**
 * Provides common action handlers for global actions on editors.
 * 
 * @author ldamus
 */
public class EditorGlobalActionHandlerProvider
	extends AbstractGlobalActionHandlerProvider {

	/**
	 * My REVERT action handler.
	 */
	IGlobalActionHandler revertHandler = new RevertGlobalActionHandler();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.internal.services.action.global.AbstractGlobalActionHandlerProvider#getGlobalActionHandler(org.eclipse.gmf.runtime.common.ui.internal.services.action.global.IGlobalActionHandlerContext)
	 */
	public IGlobalActionHandler getGlobalActionHandler(
			IGlobalActionHandlerContext context) {

		String actionId = context.getActionId();

		if (actionId != null && GlobalActionId.REVERT.equals(actionId)) {
			return revertHandler;
		}

		return null;
	}
}