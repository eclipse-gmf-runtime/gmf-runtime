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
public class ContributionItemProvider1
	extends AbstractContributionItemProvider {

	public static final String ACTION1 = "action1"; //$NON-NLS-1$

	public static final String MENU1 = "menu1"; //$NON-NLS-1$

	protected IAction createAction(String actionId,
			IWorkbenchPartDescriptor partDescriptor) {
		if (actionId.equals(ACTION1)) {
			return new MyAction(ACTION1);
		}
		return null;
	}

	protected IMenuManager createMenuManager(String menuId,
			IWorkbenchPartDescriptor partDescriptor) {
		if (menuId.equals(MENU1)) {
			return new MenuManager(MENU1, MENU1);
		}
		return null;
	}

}
