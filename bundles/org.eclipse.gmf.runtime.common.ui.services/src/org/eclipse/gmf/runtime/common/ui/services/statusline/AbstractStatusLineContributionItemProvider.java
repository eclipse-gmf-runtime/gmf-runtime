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

import java.util.Collections;
import java.util.List;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.IWorkbenchPage;

/**
 * Abstract implementation of a status line contribution item provider.
 * 
 * @author Anthony Hunter
 * @since 1.2
 */
public class AbstractStatusLineContributionItemProvider extends AbstractProvider implements
		IStatusLineContributionItemProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gmf.runtime.common.ui.services.statusline.IStatusLineContributionItemProvider
	 * #getStatusLineItems(org.eclipse.ui.part.WorkbenchPart)
	 */
	public List<IContributionItem> getStatusLineContributionItems(IWorkbenchPage workbenchPage) {
		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse
	 * .gmf.runtime.common.core.service.IOperation)
	 */
	public boolean provides(IOperation operation) {
		return false;
	}

}
