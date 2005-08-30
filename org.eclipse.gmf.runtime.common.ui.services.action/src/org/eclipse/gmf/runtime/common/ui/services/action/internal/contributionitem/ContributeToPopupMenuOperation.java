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

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.ContributionItemService;

/**
 * An operation to contribute to a popup menu of a given part type.
 * 
 * @see ContributionItemService
 * @see IContributionItemProvider
 * 
 * @author melaasar
 */
public final class ContributeToPopupMenuOperation implements IOperation {

	/** the popup menu manager */
	private final IMenuManager popupMenu;
	/** the workbench part */
	private final IWorkbenchPart workbenchPart;

	/**
	 * Constructs a new <code>ContributeToPopupMenuOperation</code> instance.
	 * 
	 * @param popupMenu The popup menu manager
	 * @param workbenchPart The workbench part
	*/
	public ContributeToPopupMenuOperation(
		IMenuManager popupMenu,
		IWorkbenchPart workbenchPart) {
		
		assert null != popupMenu : "popupMenu cannot be null"; //$NON-NLS-1$
		assert null != workbenchPart : "workbenchPart cannot be null"; //$NON-NLS-1$

		this.popupMenu = popupMenu;
		this.workbenchPart = workbenchPart;
	}

	/**
	 * Retuns the popup menu manager.
	 * 
	 * @return The popup menu manager
	 */
	public IMenuManager getPopupMenu() {
		return popupMenu;
	}

	/**
	 * Returns the workbench part.
	 * 
	 * @return The workbench part
	 */
	public IWorkbenchPart getWorkbenchPart() {
		return workbenchPart;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		((IContributionItemProvider) provider).contributeToPopupMenu(
			getPopupMenu(),
			getWorkbenchPart());
		return null;
	}

}
