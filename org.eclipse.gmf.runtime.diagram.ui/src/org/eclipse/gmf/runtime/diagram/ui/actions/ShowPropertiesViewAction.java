/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler;
import org.eclipse.gmf.runtime.common.ui.util.WorkbenchPartActivator;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.l10n.DiagramUIPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

/**
 * An action to show Eclipse's properties view
 * 
 * @author melaasar
 */
public class ShowPropertiesViewAction extends AbstractActionHandler {

	/**
	 * Constructor that takes a IWorkbenchPart.
	 * 
	 * @param workbenchPart the work bench associated with this action
	 */
	public ShowPropertiesViewAction(IWorkbenchPart workbenchPart) {
		super(workbenchPart);
		initialize();
	}
	
	/**
	 * Constructor the workbench page associated with this action
	 * @param workbenchPage
	 */
	public ShowPropertiesViewAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
		initialize();
	}
	
	/**
	 * Initialize the action
	 */
	private void initialize() {
		setId(ActionIds.ACTION_SHOW_PROPERTIES_VIEW);
		setText(DiagramUIMessages.ShowPropertiesViewAction_ActionLabelText);
		setToolTipText(DiagramUIMessages.ShowPropertiesViewAction_ActionToolTipText);
		setImageDescriptor(DiagramUIPluginImages.DESC_SHOW_PROPERTIES_VIEW);
		setHoverImageDescriptor(DiagramUIPluginImages.DESC_SHOW_PROPERTIES_VIEW);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#doRun(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void doRun(IProgressMonitor progressMonitor) {
		Trace.trace(DiagramUIPlugin.getInstance(), DiagramUIDebugOptions.METHODS_ENTERING, "ShowPropertiesView.doRun Entering"); //$NON-NLS-1$
		WorkbenchPartActivator.showPropertySheet();
		Trace.trace(DiagramUIPlugin.getInstance(), DiagramUIDebugOptions.METHODS_EXITING, "ShowPropertiesView.doRun Exiting"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.IRepeatableAction#refresh()
	 */
	public void refresh() {
		// null implementation
	}

}
