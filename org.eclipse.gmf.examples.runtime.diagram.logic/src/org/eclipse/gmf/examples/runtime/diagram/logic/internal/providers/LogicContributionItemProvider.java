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

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.actions.DeleteSemanticAction;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.actions.IncrementDecrementAction;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.actions.LogicActionIds;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.printing.actions.PrintPreviewAction;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.actions.EnhancedPrintActionHelper;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.internal.printpreview.RenderedPrintPreviewHelper;
import org.eclipse.jface.action.IAction;

/**
 * @author qili
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 *
 * Collects all the actions that are provided by the logic diagram plug-in.
 */
public class LogicContributionItemProvider 
	extends AbstractContributionItemProvider
	implements LogicActionIds {
	
	/**
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider#createAction(java.lang.String, org.eclipse.gmf.runtime.common.ui.internal.util.IWorkbenchPartDescriptor)
	 */
	protected IAction createAction(
			String actionId,
			IWorkbenchPartDescriptor partDescriptor) {
		
		if (actionId.equals(ACTION_INCREMENT_VALUE) || actionId.equals(ACTION_DECREMENT_VALUE)) {
			return new IncrementDecrementAction(partDescriptor.getPartPage(), actionId);
		} else if (actionId.equals(DELETE_SEMANTIC_VALUE)) {
			return new DeleteSemanticAction(partDescriptor.getPartPage());
		} else if (actionId.equals(PrintPreviewAction.ID)) {
			return new PrintPreviewAction(new EnhancedPrintActionHelper(),
				new RenderedPrintPreviewHelper());
		}

		return super.createAction(actionId, partDescriptor);
	}
}
