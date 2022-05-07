/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.dialogs.sortfilter;

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Content Provider for the sort filter dialog.
 * 
 * @author jcorchis
 */
public class SortFilterContentProvider implements IStructuredContentProvider {

	/**
	 * Convience method to return an Object[] given content <code>List</code>.
	 * @return an array of objects give the content <code>List</code>
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object sortFilterElements) {
		if (sortFilterElements == null) {
			return null;
		}

		return ((Collection)sortFilterElements).toArray();
	}

	/**
	 * Unimplemented.
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
	    // Not implemented
	}

	/**
	 * Unimplemented
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	    // Not implemented
	}

}
