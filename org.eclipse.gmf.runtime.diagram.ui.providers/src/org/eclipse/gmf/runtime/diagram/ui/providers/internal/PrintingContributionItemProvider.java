/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import org.eclipse.jface.action.IAction;

import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.PageSetupAction;

/**
 * Add the page setup action.
 * 
 * @author Wayne Diu, wdiu
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.providers.*
 */
public class PrintingContributionItemProvider
	extends AbstractContributionItemProvider {

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider#createAction(java.lang.String, org.eclipse.gmf.runtime.common.ui.services.contributionitem.IWorkbenchPartDescriptor)
	 */
	protected IAction createAction(
		String actionId,
		IWorkbenchPartDescriptor partDescriptor) {

		if (actionId.equals(PageSetupAction.ID))
			return new PageSetupAction();

		return super.createAction(actionId, partDescriptor);
	}
}
