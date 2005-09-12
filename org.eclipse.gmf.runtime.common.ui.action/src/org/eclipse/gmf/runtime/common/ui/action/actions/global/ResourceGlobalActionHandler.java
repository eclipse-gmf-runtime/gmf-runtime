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

package org.eclipse.gmf.runtime.common.ui.action.actions.global;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;

import org.eclipse.gmf.runtime.common.ui.services.action.global.AbstractGlobalActionHandler;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;

/**
 * Global action handler that works with the resources in the context selection.
 * 
 * @author ldamus
 */
public abstract class ResourceGlobalActionHandler extends AbstractGlobalActionHandler {


	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandler#canHandle(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	public boolean canHandle(IGlobalActionContext cntxt) {
		IStructuredSelection selection =
			(IStructuredSelection) cntxt.getSelection();
		return getResourceSelection((IStructuredSelection)cntxt.getSelection()).size() == selection.size();
	}

	/**
	 * Gets a structured selection containing all of the IResources in the
	 * context selection. Selected elements may either by IResources or may
	 * adapt to IResource.
	 * 
	 * @param selection the context selection for which <code>IStructuredSelection</code> will be retrieved
	 * @return the selection of file resources
	 */
	protected IStructuredSelection getResourceSelection(IStructuredSelection selection) {

		List result = new ArrayList();

		for (Iterator i = selection.iterator(); i.hasNext();) {
			Object nextSelected = i.next();

			if (nextSelected instanceof IResource) {
				result.add(nextSelected);

			} else if (nextSelected instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) nextSelected;
				IResource resource =
					(IResource) adaptable.getAdapter(IResource.class);
				if (resource != null) {
					result.add(resource);
				}
			}
		}
		return new StructuredSelection(result);
	}
	
	/**
	 * Gets the shell from the global action context.
	 * 
	 * @param part the <code>IWorkbenchPart</code> for which a <code>Shell</code> will be retrieved
	 * @return the shell
	 */
	protected Shell getShell(IWorkbenchPart part) {
		if (part != null) {
			IWorkbenchPartSite site = part.getSite();
			if (site != null) {
				return site.getShell();
			}
		}
		return null;
	}
}
