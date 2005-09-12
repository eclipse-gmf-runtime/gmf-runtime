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

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.ISelection;

import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.ContributionItemService;

/**
 * A policy interface for popup menu contributions. It inspects a
 * given selection and determines whether the contribution should
 * be applied to the popup menu given that selection or not.
 * 
 * @see IContributionItemProvider
 * @see ContributionItemService
 * 
 * @author melaasar
 */
public interface IPopupMenuContributionPolicy {

	/**
	 * Tests whether the policy applies to the given selection.
	 * 
	 * @param selection The given context of the popup menu
	 * @param configuration The policy's configuration (could be used to parse extra params)
	 * @return Whether the policy applies to the selection or not
	 */
	public boolean appliesTo(
		ISelection selection,
		IConfigurationElement configuration);

}
