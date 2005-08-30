/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.internal.dialogs.PageSetupDialog;

/**
 * This is the action for page setup.  It shows the page setup dialog.
 * 
 * @author Wayne Diu, wdiu
 * @canBeSeenBy %level1
 */
public class PageSetupAction
	extends Action
	implements IWorkbenchWindowActionDelegate {

	/**
	 * ID for this page setup action
	 */
	public static final String ID = "pageSetupAction";//$NON-NLS-1$

	/**
	 * Constructor sets the id and label that is displayed in the
	 * menu bar.
	 */
	public PageSetupAction() {
		setId(ID);
		setText(DiagramActionsResourceManager.getI18NString("PageSetupAction.Label")); //$NON-NLS-1$
	}

	/**
	 * The run method does the real run action.
	 * From IAction.
	 */
	public void run() {
		//IPreferenceStore p = null;

		//IWorkbenchPart page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
		//if (page instanceof IDiagramWorkbenchPart) {
		//	IDiagramGraphicalViewer viewer = ((IDiagramWorkbenchPart)page).getDiagramGraphicalViewer();
		//	if (viewer instanceof DiagramGraphicalViewer) {
		//		p = ((DiagramGraphicalViewer)viewer).getWorkspaceViewerPreferenceStore();
		//	}
		//}
		
		//new PageSetupDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), p).open();
		new PageSetupDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()).open();
	}

	/**
	 * The run method does the real run action.
	 * From IActionDelegate
	 */
	public void run(IAction action) {
		run();
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose() {
		//do nothing
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
		//do nothing
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		//do nothing
	}
}
