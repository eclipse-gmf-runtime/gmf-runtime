/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.providers.ide.providers;

import java.util.Iterator;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.action.ide.global.IDEGlobalActionId;
import org.eclipse.gmf.runtime.common.ui.services.action.global.AbstractGlobalActionHandler;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IPrimaryEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.resources.AddBookmarkHelper;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The global action handler for diagram global actions with IDE dependencies. 
 * 
 * @author Wayne Diu, wdiu
 */
public class DiagramIDEGlobalActionHandler
	extends AbstractGlobalActionHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#getCommand(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public ICommand getCommand(IGlobalActionContext cntxt) {
		String actionId = cntxt.getActionId();

		IWorkbenchPart part = cntxt.getActivePart();
		if (!(part instanceof IDiagramWorkbenchPart)) {
			return null;
		}

		IDiagramWorkbenchPart diagramPart = (IDiagramWorkbenchPart) part;

		ICommand command = null;

		if (actionId.equals(IDEGlobalActionId.BOOKMARK)) {
			AddBookmarkHelper.addBookmark(diagramPart);
		}

		return command;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractGlobalActionHandler#canHandle(IGlobalActionContext)
	 */
	public boolean canHandle(final IGlobalActionContext cntxt) {

		boolean result = false;

		String actionId = cntxt.getActionId();

		if (actionId.equals(IDEGlobalActionId.BOOKMARK)) {
			result = canBookmark(cntxt);
		}

		return result;
	}

	/**
	 * Checks if the selection can be bookmarked
	 * @param cntxt the <code>IGlobalActionContext</code> holding the necessary information needed by this action handler
	 * @return boolean <code>true</code> if the selection can be bookmarked,
	 *         otherwise <code>false</code>
	 */
	private boolean canBookmark(IGlobalActionContext cntxt) {
		// Check if the active part is a IDiagramEditorPart
		if (!(cntxt.getActivePart() instanceof IDiagramWorkbenchPart)) {
			return false;
		}

		// Check each selected object
		IStructuredSelection selected = (IStructuredSelection) cntxt
			.getSelection();
		for (Iterator i = selected.toList().iterator(); i.hasNext();) {
			Object selectedElement = i.next();
			if (!(selectedElement instanceof EditPart)) {
				return false;
			}

			// Check that primary view (i.e. shape view or connection view) is
			// selected
			View view = (View) ((EditPart) selectedElement)
			.getAdapter(View.class);
			if (!((EditPart) selectedElement instanceof IPrimaryEditPart) || view == null
					||view.eResource()==null) {
				return false;
			}
		}

		return true;
	}
}