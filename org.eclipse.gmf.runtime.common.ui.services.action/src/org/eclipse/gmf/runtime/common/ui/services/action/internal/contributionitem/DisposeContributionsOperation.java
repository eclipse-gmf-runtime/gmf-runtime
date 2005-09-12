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

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;

/**
 * An operation to dispose of and clear the cache of contributions
 * made to a part with a certain type.
 * 
 * @author melaasar
 */
public final class DisposeContributionsOperation implements IOperation {

	/** the part's descriptor */
	private final IWorkbenchPartDescriptor workbenchPartDescriptor;

	/**
	 * Constructs an instance of <code>DisposeContributionsOperation</code>.
	 * 
	 * @param workbenchPartDescriptor the part's descriptor
	 */
	public DisposeContributionsOperation(IWorkbenchPartDescriptor workbenchPartDescriptor) {

		assert null != workbenchPartDescriptor : "workbenchPartDescriptos cannot be null"; //$NON-NLS-1$

		this.workbenchPartDescriptor = workbenchPartDescriptor;
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
		((IContributionItemProvider) provider).disposeContributions(
			getWorkbenchPartDescriptor());
		return null;
	}

}
