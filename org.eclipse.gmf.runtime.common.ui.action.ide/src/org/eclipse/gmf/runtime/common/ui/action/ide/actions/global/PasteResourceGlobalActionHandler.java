/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.action.ide.actions.global;

import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.CopyFilesAndFoldersOperation;
import org.eclipse.ui.actions.CopyProjectOperation;
import org.eclipse.ui.part.ResourceTransfer;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.action.actions.global.ResourceGlobalActionHandler;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;

/**
 * Global action handler that pastes resources.
 * 
 * @author ldamus
 */
public class PasteResourceGlobalActionHandler
	extends ResourceGlobalActionHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#getCommand(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public ICommand getCommand(IGlobalActionContext cntxt) {

		ResourceTransfer resTransfer = ResourceTransfer.getInstance();
		Clipboard clipboard = new Clipboard(Display.getCurrent());
		IResource[] resourceData = (IResource[]) clipboard
			.getContents(resTransfer);
		clipboard.dispose();
		
		if (resourceData != null && resourceData.length > 0) {
			if (resourceData[0].getType() == IResource.PROJECT) {

				for (int i = 0; i < resourceData.length; i++) {
					CopyProjectOperation operation = new CopyProjectOperation(
						getShell(cntxt.getActivePart()));
					operation.copyProject((IProject) resourceData[i]);
				}
			} else {

				IContainer container = getContainer((IStructuredSelection)cntxt.getSelection());

				CopyFilesAndFoldersOperation operation = new CopyFilesAndFoldersOperation(
					getShell(cntxt.getActivePart()));
				operation.copyResources(resourceData, container);
			}
		}
		return null;
	}

	/**
	 * Returns the container to hold the pasted resources.
	 */
	private IContainer getContainer(IStructuredSelection sel) {
		List selection = getResourceSelection(sel).toList();
		if (selection.get(0) instanceof IFile) {
			return ((IFile) selection.get(0)).getParent();
		}
		return (IContainer) selection.get(0);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#canHandle(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public boolean canHandle(IGlobalActionContext cntxt) {

		if (super.canHandle(cntxt)) {
			
			ResourceTransfer resTransfer = ResourceTransfer.getInstance();
			Clipboard clipboard = new Clipboard(Display.getCurrent());
			IResource[] resourceData = (IResource[]) clipboard
				.getContents(resTransfer);

			clipboard.dispose();
			return resourceData != null && resourceData.length > 0;
		}
		return false;
	}

}