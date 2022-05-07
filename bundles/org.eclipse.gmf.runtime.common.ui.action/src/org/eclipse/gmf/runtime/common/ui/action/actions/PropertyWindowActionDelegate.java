/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.action.actions;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.action.AbstractActionDelegate;
import org.eclipse.gmf.runtime.common.ui.action.internal.CommonUIActionDebugOptions;
import org.eclipse.gmf.runtime.common.ui.action.internal.CommonUIActionPlugin;
import org.eclipse.gmf.runtime.common.ui.util.WorkbenchPartActivator;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IViewActionDelegate;

/**
 * Action delegate to show the properties window or give it focus if it's
 * already visible.
 * 
 * @author ldamus
 */
public class PropertyWindowActionDelegate
	extends AbstractActionDelegate
	implements IEditorActionDelegate, IViewActionDelegate, IActionDelegate2 {

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionDelegate#doRun(IProgressMonitor)
	 */
	protected void doRun(IProgressMonitor progressMonitor) {
		Trace.trace(CommonUIActionPlugin.getDefault(), CommonUIActionDebugOptions.METHODS_ENTERING, "PropertyWindowActionDelegate.doRun Entering"); //$NON-NLS-1$
		WorkbenchPartActivator.showPropertySheet();
		Trace.trace(CommonUIActionPlugin.getDefault(), CommonUIActionDebugOptions.METHODS_EXITING, "PropertyWindowActionDelegate.doRun Exiting"); //$NON-NLS-1$
	}

}
