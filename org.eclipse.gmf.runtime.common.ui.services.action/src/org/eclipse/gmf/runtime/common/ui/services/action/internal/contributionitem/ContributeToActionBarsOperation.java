/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem;

import org.eclipse.ui.IActionBars;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.ContributionItemService;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;

/**
 * An operation to contribute to the action bars of a given part type
 * 
 * @see ContributionItemService
 * @see IContributionItemProvider
 * 
 * @author melaasar, cmahoney
 */
public class ContributeToActionBarsOperation implements IOperation {

	/** the part's action bars */
	private final IActionBars actionBars;
	/** the part's descriptor */
	private final IWorkbenchPartDescriptor workbenchPartDescriptor;

	/**
	 * Constructs a new <code>ContributeToActionBarsOperation</code> instance.
	 * 
	 * @param actionBars The part's action bars
	 * @param workbenchPartDescriptor The part's descriptor
	 */
	public ContributeToActionBarsOperation(
		IActionBars actionBars,
		IWorkbenchPartDescriptor workbenchPartDescriptor) {

		assert null != actionBars : "actionBars cannot be null"; //$NON-NLS-1$
		assert null != workbenchPartDescriptor : "workbenchPartDescriptor cannot be null"; //$NON-NLS-1$

		this.actionBars = actionBars;
		this.workbenchPartDescriptor = workbenchPartDescriptor;
	}

	/**
	 * Returns the action bars.
	 * 
	 * @return The action bars
	 */
	public IActionBars getActionBars() {
		return actionBars;
	}

	/**
	 * Returns the part's descriptor.
	 * 
	 * @return The part's descriptor
	 */
	public IWorkbenchPartDescriptor getWorkbenchPartDescriptor() {
		return workbenchPartDescriptor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		((IContributionItemProvider) provider).contributeToActionBars(
			getActionBars(),
			getWorkbenchPartDescriptor());
		return null;
	}

}
