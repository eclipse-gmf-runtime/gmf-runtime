/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.action.actions;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IViewActionDelegate;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.action.AbstractActionDelegate;
import org.eclipse.gmf.runtime.common.ui.action.internal.CommonUIActionDebugOptions;
import org.eclipse.gmf.runtime.common.ui.action.internal.CommonUIActionPlugin;
import org.eclipse.gmf.runtime.common.ui.util.WorkbenchPartActivator;

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
