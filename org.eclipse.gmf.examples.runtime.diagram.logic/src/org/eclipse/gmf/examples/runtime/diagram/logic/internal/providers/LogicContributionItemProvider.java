/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers;

import org.eclipse.jface.action.IAction;

import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.actions.DeleteSemanticAction;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.actions.IncrementDecrementAction;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.actions.LogicActionIds;


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
		}

		return super.createAction(actionId, partDescriptor);
	}
}
