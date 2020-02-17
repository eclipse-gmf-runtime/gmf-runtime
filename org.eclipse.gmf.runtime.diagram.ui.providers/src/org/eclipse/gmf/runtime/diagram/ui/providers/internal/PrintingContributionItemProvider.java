/******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import org.eclipse.jface.action.IAction;

import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.PageSetupAction;

/**
 * Add the page setup action.
 * 
 * @author Wayne Diu, wdiu
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
