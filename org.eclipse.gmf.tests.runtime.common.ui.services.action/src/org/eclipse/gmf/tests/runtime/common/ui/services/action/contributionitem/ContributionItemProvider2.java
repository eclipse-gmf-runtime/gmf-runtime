/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.common.ui.services.action.contributionitem;

import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;

/**
 * A contribution item provider used to test the contribution item service. The
 * plugin.xml defines where the contributions are made.
 * 
 * @author cmahoney
 */
public class ContributionItemProvider2
	extends AbstractContributionItemProvider {

	public static final String ACTION2 = "action2"; //$NON-NLS-1$

	public static final String MENU2 = "menu2"; //$NON-NLS-1$

	protected IAction createAction(String actionId,
			IWorkbenchPartDescriptor partDescriptor) {
		if (actionId.equals(ACTION2)) {
			return new MyAction(ACTION2);
		}
		return null;
	}

	protected IMenuManager createMenuManager(String menuId,
			IWorkbenchPartDescriptor partDescriptor) {
		if (menuId.equals(MENU2)) {
			return new MenuManager(MENU2, MENU2);
		}
		return null;
	}
}
