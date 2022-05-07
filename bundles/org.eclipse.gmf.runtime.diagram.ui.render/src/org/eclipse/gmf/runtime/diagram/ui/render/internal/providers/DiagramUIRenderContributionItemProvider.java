/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.render.internal.providers;

import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.render.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.render.actions.CopyToImageAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPage;

/**
 * Contribution item provider for the Diagram UI Render plugin.
 * 
 * @author cmahoney
 */
public class DiagramUIRenderContributionItemProvider
	extends AbstractContributionItemProvider {

	protected IAction createAction(String actionId,
			IWorkbenchPartDescriptor partDescriptor) {

		IWorkbenchPage workbenchPage = partDescriptor.getPartPage();

		if (actionId.equals(ActionIds.ACTION_COPY_TO_IMAGE))
			return new CopyToImageAction(workbenchPage);

		return super.createAction(actionId, partDescriptor);
	}

}
