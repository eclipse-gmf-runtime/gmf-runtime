/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
