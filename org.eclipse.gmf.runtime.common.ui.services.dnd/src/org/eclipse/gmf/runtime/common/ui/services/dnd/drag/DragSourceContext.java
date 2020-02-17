/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.dnd.drag;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Concrete implementation of <code>IDragSourceContext</code>
 * 
 * @author Vishy Ramaswamy
 */
public class DragSourceContext
	implements IDragSourceContext {

	/**
	 * Attribute for the active part.
	 */
	private final IWorkbenchPart activePart;

	/**
	 * Constructor for DragSourceContext.
	 * 
	 * @param anActivePart
	 *            active IWorkbenchPart
	 */
	public DragSourceContext(IWorkbenchPart anActivePart) {
		super();

		assert null != anActivePart : "anActivePart cannot be null"; //$NON-NLS-1$
		this.activePart = anActivePart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drag.IDragSourceContext#getSelection()
	 */
	public ISelection getSelection() {
		return getActivePart().getSite().getSelectionProvider().getSelection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drag.IDragSourceContext#getActivePart()
	 */
	public final IWorkbenchPart getActivePart() {
		return activePart;
	}

	/**
	 * Sets the data of the given DragSourceEvent
	 * 
	 * @param event
	 *            the DragSourceEvent to have its data set
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragSetData(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	protected void dragSetData(DragSourceEvent event) {
		event.data = null;
	}

	/**
	 * Drag operation started.
	 * 
	 * @param event
	 *            the DragSouceEvent to be updated
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	protected void dragStart(DragSourceEvent event) {
		event.doit = false;
	}

	/**
	 * Drag operation finished.
	 * 
	 * @param event
	 *            the DragSouceEvent to be updated
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragFinished(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	protected void dragFinished(DragSourceEvent event) {
		/* method not implemented */
	}
}