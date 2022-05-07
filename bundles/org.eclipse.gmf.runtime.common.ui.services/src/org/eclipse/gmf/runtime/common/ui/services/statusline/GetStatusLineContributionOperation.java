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

package org.eclipse.gmf.runtime.common.ui.services.statusline;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.ui.IWorkbenchPage;

/**
 * The parent of all status line items.
 * 
 * @author Paulina Masson
 * @author Anthony Hunter
 * @since 1.2
 */
public class GetStatusLineContributionOperation implements IOperation {

	private final IWorkbenchPage workbenchPage;

	/**
	 * Constructor for a GetStatusLineContributionOperation.
	 * 
	 * @param part
	 *            the workbench part.
	 */
	public GetStatusLineContributionOperation(IWorkbenchPage workbenchPage) {
		super();
		this.workbenchPage = workbenchPage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse
	 * .gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		return ((IStatusLineContributionItemProvider) provider).getStatusLineContributionItems(workbenchPage);
	}

	/**
	 * Gets the workbench page as a hint.
	 * 
	 * @return the workbench page.
	 */
	public IWorkbenchPage getWorkbenchPage() {
		return workbenchPage;
	}
}
