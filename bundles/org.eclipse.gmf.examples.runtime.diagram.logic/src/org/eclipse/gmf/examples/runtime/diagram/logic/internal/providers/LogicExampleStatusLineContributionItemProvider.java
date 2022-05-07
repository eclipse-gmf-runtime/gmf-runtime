/******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.actions.LogicActionIds;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.LEDEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.ui.parts.LogicNotationEditor;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.statusline.AbstractStatusLineContributionItemProvider;
import org.eclipse.gmf.runtime.common.ui.services.statusline.GetStatusLineContributionOperation;
import org.eclipse.gmf.runtime.common.ui.services.statusline.StatusLineMessageContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;

/**
 * A status line contribution item provider for the logic example.
 * 
 * @author Anthony Hunter
 */
public class LogicExampleStatusLineContributionItemProvider extends
		AbstractStatusLineContributionItemProvider implements LogicActionIds {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.statusline.
	 * AbstractStatusLineContributionItemProvider
	 * #getStatusLineContributionItems(org.eclipse.ui.IWorkbenchPage)
	 */
	public List<IContributionItem> getStatusLineContributionItems(
			IWorkbenchPage workbenchPage) {
		List<IContributionItem> contrItemList = new ArrayList<IContributionItem>();

		// add the icon and message contribution
		contrItemList.add(new StatusLineMessageContributionItem());

		// add the increment and decrement contribution if the selected element
		// is an LED
		ISelection selection = workbenchPage.getActiveEditor().getSite()
				.getSelectionProvider().getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (!structuredSelection.isEmpty()) {
				if (structuredSelection.size() == 1
						&& structuredSelection.getFirstElement() instanceof LEDEditPart) {
					contrItemList.add(new IncrementDecrementContributionItem(
							workbenchPage, ACTION_INCREMENT_VALUE));
					contrItemList.add(new IncrementDecrementContributionItem(
							workbenchPage, ACTION_DECREMENT_VALUE));
				}
			}
		}
		return contrItemList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.statusline.
	 * AbstractStatusLineContributionItemProvider
	 * #provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public boolean provides(IOperation operation) {
		if (operation instanceof GetStatusLineContributionOperation) {
			IWorkbenchPage workbenchPage = ((GetStatusLineContributionOperation) operation)
					.getWorkbenchPage();
			if (workbenchPage.getActiveEditor() instanceof LogicNotationEditor) {
				return true;
			}
		}
		return false;
	}

}
