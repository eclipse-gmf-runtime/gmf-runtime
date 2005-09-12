/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.part.ISetSelectionTarget;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.util.WorkbenchPartActivator;
import org.eclipse.gmf.runtime.diagram.ui.actions.AbstractPresentationModelActionDelegate;

/**
 * Action delegate which handles showing one or more items selected in a
 * diagram in the model explorer.
 * 
 * @author ldamus
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.actions.*
 */
public class ShowInViewActionDelegate
	extends AbstractPresentationModelActionDelegate
	implements IObjectActionDelegate {

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionDelegate#doRun(IProgressMonitor)
	 */
	protected void doRun(IProgressMonitor progressMonitor) {

		Trace.trace(DiagramActionsPlugin.getInstance(), DiagramActionsDebugOptions.METHODS_ENTERING, "ShowInViewActionDelegate.doRun Entering"); //$NON-NLS-1$

		List elements = getElements(getStructuredSelection());

		if (elements.isEmpty())
			return;

		IViewPart view = WorkbenchPartActivator.showView(getAction().getId());

		if ((view != null) && (view instanceof ISetSelectionTarget)) {
			ISelection selection = new StructuredSelection(elements);
			((ISetSelectionTarget) view).selectReveal(selection);
		}
		Trace.trace(DiagramActionsPlugin.getInstance(), DiagramActionsDebugOptions.METHODS_EXITING, "ShowInViewActionDelegate.doRun Exiting"); //$NON-NLS-1$
	}
}
