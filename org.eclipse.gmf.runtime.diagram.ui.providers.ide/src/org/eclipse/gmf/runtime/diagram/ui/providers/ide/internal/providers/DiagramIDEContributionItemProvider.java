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

package org.eclipse.gmf.runtime.diagram.ui.providers.ide.internal.providers;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.common.ui.action.ide.actions.global.IDEGlobalActionManager;
import org.eclipse.gmf.runtime.common.ui.action.ide.global.IDEGlobalActionId;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;

/**
 * The contribution item provider for diagram contributions with IDE
 * dependencies. 
 * 
 * @author Wayne Diu, wdiu
 */
public class DiagramIDEContributionItemProvider
	extends AbstractContributionItemProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider#createAction(java.lang.String, org.eclipse.gmf.runtime.common.ui.services.contributionitem.IWorkbenchPartDescriptor)
	 */
	protected IAction createAction(String actionId,
			IWorkbenchPartDescriptor partDescriptor) {

		IWorkbenchPage workbenchPage = partDescriptor.getPartPage();

		if (actionId.equals(org.eclipse.ui.ide.IDEActionFactory.BOOKMARK
			.getId()))
			return IDEGlobalActionManager.getInstance().createActionHandler(
				workbenchPage, IDEGlobalActionId.BOOKMARK);

		return super.createAction(actionId, partDescriptor);
	}

}