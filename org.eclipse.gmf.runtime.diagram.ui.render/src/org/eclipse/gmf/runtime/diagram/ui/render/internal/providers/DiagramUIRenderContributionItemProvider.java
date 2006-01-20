/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
