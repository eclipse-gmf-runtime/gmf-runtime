/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.action.filter;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.filter.IActionFilterProvider;

/**
 * The parent of all action filter providers. Defines useful constants and
 * behavior for retrieving the current workbench selection.
 * 
 * @author khussey
 *
 */
public abstract class AbstractActionFilterProvider
	extends AbstractProvider
	implements IActionFilterProvider {

	/**
	 * The attribute value indicating an enablement test.
	 * 
	 */
	protected static final String ENABLEMENT_VALUE = "enablement"; //$NON-NLS-1$

	/**
	 * The attribute value indicating a visibility test.
	 * 
	 */
	protected static final String VISIBILITY_VALUE = "visibility"; //$NON-NLS-1$

	/**
	 * Constructs a new action filter provider.
	 */
	protected AbstractActionFilterProvider() {
		super();
	}

	/**
	 * Retrieves the selection from selection service of the active workbench
	 * window.
	 * 
	 * @return The current workbench selection.
	 * 
	 */
	protected ISelection getSelection() {

		ISelection selection = null;
		
		IWorkbenchWindow window =
			PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		if (null != window) {
			selection = window.getSelectionService().getSelection();
		}
		return selection == null ? StructuredSelection.EMPTY : selection;
	}

	/**
	 * Retrieves the selection as a structured selection.
	 * 
	 * @return The current workbench selection if it is a structured selection;
	 *          an empty structured selection otherwise.
	 * 
	 */
	protected IStructuredSelection getStructuredSelection() {
		ISelection selection = getSelection();
		if (selection instanceof IStructuredSelection) {
			return (IStructuredSelection) getSelection();
		} else {
			return StructuredSelection.EMPTY;
		}
	}

}
