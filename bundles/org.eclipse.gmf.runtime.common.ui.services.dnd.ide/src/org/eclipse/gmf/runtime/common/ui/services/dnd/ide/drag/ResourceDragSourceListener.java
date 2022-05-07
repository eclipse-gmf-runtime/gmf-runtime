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

package org.eclipse.gmf.runtime.common.ui.services.dnd.ide.drag;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drag.IDragSourceContext;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drag.IDragSourceListener;
import org.eclipse.gmf.runtime.common.ui.services.dnd.ide.core.IDETransferId;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.views.navigator.NavigatorDragAdapter;

/**
 * Concrete class that implements the IDragSourceListener. This class handles
 * dragging files and folder elements.
 * 
 * @author ldamus
 */
public class ResourceDragSourceListener
	extends NavigatorDragAdapter
	implements IDragSourceListener {

	/**
	 * Constructs a new drag listener.
	 * 
	 * @param provider
	 *            the selection provider
	 */
	public ResourceDragSourceListener(ISelectionProvider provider) {
		super(provider);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drag.IDragSourceListener#isDraggable(org.eclipse.gmf.runtime.common.ui.services.dnd.drag.IDragSourceContext)
	 */
	public boolean isDraggable(IDragSourceContext context) {
		ISelection selection = context.getSelection();
		if (!(selection instanceof IStructuredSelection)) {
			return false;
		}

		for (Iterator i = ((IStructuredSelection) selection).iterator(); i
			.hasNext();) {
			Object next = i.next();
			if (!(next instanceof IFile || next instanceof IFolder)) {
				if (next instanceof IAdaptable) {
					IResource resource = (IResource) ((IAdaptable) next)
						.getAdapter(IResource.class);
					if (resource != null
						&& (resource.getType() == IResource.FILE || resource
							.getType() == IResource.FOLDER)) {
						return true;
					}
				}
				return false;
			}
		}
		if (selection.isEmpty()) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drag.IDragSourceListener#getSupportingTransferIds()
	 */
	public String[] getSupportingTransferIds() {
		return new String[] {IDETransferId.NAV_SELECTION_TRANSFER};
	}
}