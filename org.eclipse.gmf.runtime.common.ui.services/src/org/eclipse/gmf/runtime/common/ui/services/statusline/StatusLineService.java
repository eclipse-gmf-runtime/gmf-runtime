/******************************************************************************
 * Copyright (c) 2009, 2022 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.statusline;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.common.ui.services.internal.CommonUIServicesPlugin;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.IWorkbenchPage;

/**
 * Service which handles status line contributions.
 * 
 * @author Paulina Masson
 * @author Anthony Hunter
 * @since 1.2
 */
public class StatusLineService extends Service implements
		IStatusLineContributionItemProvider {

	/**
	 * The singleton instance of the status line service.
	 */
	private final static StatusLineService service = new StatusLineService();

	static {
		service.configureProviders(CommonUIServicesPlugin.getPluginId(),
				"statusLineContributionItemProviders"); //$NON-NLS-1$
	}

	/**
	 * Retrieves the singleton instance of the status line service.
	 * 
	 * @return The StatusLineService singleton.
	 */
	public static StatusLineService getInstance() {
		return service;
	}

	public List<IContributionItem> getStatusLineContributionItems(
			IWorkbenchPage workbenchPage) {
		List<List<IContributionItem>> lists = execute(
				ExecutionStrategy.FORWARD,
				new GetStatusLineContributionOperation(workbenchPage));
		// execute returns a list of lists, so compact into a single list.
		List<IContributionItem> result = new ArrayList<IContributionItem>();
		for (List<IContributionItem> items : lists) {
			result.addAll(items);

		}
		return result;
	}

}
