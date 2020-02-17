/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.action.ide.actions.global;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.actions.OpenFileAction;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.services.action.global.AbstractGlobalActionHandler;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;

/**
 * Global action handler that opens files using the Eclipse 
 * {@link org.eclipse.ui.actions.OpenFileAction}.
 * 
 * @author ldamus
 */
public class OpenFileGlobalActionHandler extends AbstractGlobalActionHandler {


	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#getCommand(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public ICommand getCommand(IGlobalActionContext cntxt) {
		OpenFileAction openAction =
			new OpenFileAction(
				cntxt.getActivePart().getSite().getPage());
		openAction.selectionChanged(getResourceSelection((IStructuredSelection) cntxt.getSelection()));
		openAction.run();
		return null;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#canHandle(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public boolean canHandle(IGlobalActionContext cntxt) {
		IStructuredSelection selection =
			(IStructuredSelection) cntxt.getSelection();
		return getResourceSelection((IStructuredSelection) cntxt.getSelection()).size() == selection.size();
	}
	
	/**
	 * Gets a structured selection containing all of the IFiles in the
	 * context selection. Selected elements may either by IFiles or may
	 * adapt to IFile.
	 * 
	 * @param selection the context selection for which to retrieve a <code>IStructuredSelection</code>
	 * @return the selection of file resources
	 */
	protected IStructuredSelection getResourceSelection(IStructuredSelection selection) {
		
		List result = new ArrayList();

		for (Iterator i = selection.iterator(); i.hasNext();) {
			Object nextSelected = i.next();

			if (nextSelected instanceof IFile) {
				result.add(nextSelected);
				
			} else if (nextSelected instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) nextSelected;
				IFile file = (IFile) adaptable.getAdapter(IFile.class);
				if (file != null) {
					result.add(file);
				}
			}
		}
		return new StructuredSelection(result);
	}

}
