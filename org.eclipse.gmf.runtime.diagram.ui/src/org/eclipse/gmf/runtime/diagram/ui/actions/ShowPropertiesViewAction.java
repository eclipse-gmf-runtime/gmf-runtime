/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.actions;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler;
import org.eclipse.gmf.runtime.common.ui.util.WorkbenchPartActivator;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.l10n.Images;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;

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
		setText(PresentationResourceManager.getI18NString("ShowPropertiesViewAction.ActionLabelText")); //$NON-NLS-1$
		setToolTipText(PresentationResourceManager.getI18NString("ShowPropertiesViewAction.ActionToolTipText")); //$NON-NLS-1$
		setImageDescriptor(Images.DESC_ACTION_SHOW_PROPERTIES_VIEW);
		setHoverImageDescriptor(Images.DESC_ACTION_SHOW_PROPERTIES_VIEW);
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
