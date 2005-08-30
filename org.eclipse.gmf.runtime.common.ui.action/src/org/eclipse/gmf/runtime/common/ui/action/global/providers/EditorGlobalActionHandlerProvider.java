/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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