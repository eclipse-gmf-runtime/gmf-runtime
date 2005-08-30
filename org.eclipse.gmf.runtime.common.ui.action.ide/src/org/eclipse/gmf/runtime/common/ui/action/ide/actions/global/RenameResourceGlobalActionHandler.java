/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.action.ide.actions.global;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.actions.RenameResourceAction;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.action.actions.global.ResourceGlobalActionHandler;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;
import org.eclipse.gmf.runtime.common.ui.util.IInlineTextEditorPart;

/**
 * Global action handler that renames resources using an inline
 * editor on the active workbench part. The active part must adapt to 
 * {@link org.eclipse.gmf.runtime.common.ui.util.IInlineTextEditorPart}.
 * 
 * @author ldamus
 */
public class RenameResourceGlobalActionHandler
	extends ResourceGlobalActionHandler {


	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#getCommand(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public ICommand getCommand(IGlobalActionContext cntxt) {
		
		RenameResourceAction renameAction =
			new RenameResourceAction(getShell(cntxt.getActivePart()));

		IInlineTextEditorPart part =
			(IInlineTextEditorPart) cntxt.getActivePart().getAdapter(
				IInlineTextEditorPart.class);

		if (part != null) {
			StructuredViewer viewer = part.getViewer();
			if (viewer instanceof TreeViewer) {
				TreeViewer treeViewer = (TreeViewer) viewer;
				if (treeViewer != null) {
					renameAction =
						new RenameResourceAction(
							getShell(cntxt.getActivePart()),
							treeViewer.getTree());
				}
			}
		}

		renameAction.selectionChanged(getResourceSelection((IStructuredSelection)cntxt.getSelection()));
		renameAction.run();
		return null;
	}
}
