/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.dnd.drag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.dnd.DragSourceEvent;

/**
 * Abstract parent of all the drag source listeners
 * 
 * @author Vishy Ramaswamy
 */
public abstract class AbstractDragSourceListener
	implements IDragSourceListener {

	/**
	 * Attribute for the drag source context.
	 */
	private IDragSourceContext context = null;

	/**
	 * Attribute for the supporting transfer ids.
	 */
	private final String[] transferIds;

	/**
	 * Constructor for AbstractDragSourceListener.
	 * 
	 * @param transferIdArray
	 *            The transfer agent ids
	 */
	public AbstractDragSourceListener(String[] transferIdArray) {
		super();

		assert null != transferIdArray : "transferIdArray cannot be null"; //$NON-NLS-1$
		assert transferIdArray.length > 0 : "transferIdArray cannot be empty"; //$NON-NLS-1$

		this.transferIds = transferIdArray;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drag.IDragSourceListener#isDraggable(org.eclipse.gmf.runtime.common.ui.services.dnd.drag.IDragSourceContext)
	 */
	public final boolean isDraggable(IDragSourceContext cntxt) {
		/* Set the context */
		this.context = cntxt;

		/* Check if draggable */
		return isDraggable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drag.IDragSourceListener#getSupportingTransferIds()
	 */
	public final String[] getSupportingTransferIds() {
		return transferIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public final void dragStart(DragSourceEvent event) {
		event.doit = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragSetData(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragSetData(DragSourceEvent event) {
		/* method not implemented */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragFinished(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragFinished(DragSourceEvent event) {
		/* method not implemented */
	}

	/**
	 * Returns the context.
	 * 
	 * @return IDragSourceContext
	 */
	protected final IDragSourceContext getContext() {
		return context;
	}

	/**
	 * Checks if the selection contained in the context is draggable. Derived
	 * classes can get context using the <code>getContext()</code> method.
	 * 
	 * @return true if draggable, false otherwise
	 */
	protected abstract boolean isDraggable();

	/**
	 * Combines the arrays in the passed list into one array.
	 * 
	 * @param list
	 *            The List of arrays to combine together
	 * @param resultingArrayType
	 *            the Object[] used to define the type of the combined array
	 * 
	 * @return Object[] the combined array of the proper type
	 */
	protected final Object[] combineArraysInList(List list,
			Object[] resultingArrayType) {
		List listEntry = null;
		Iterator it = list.iterator();
		ArrayList results = new ArrayList();
		while (it.hasNext()) {
			listEntry = Arrays.asList((Object[]) it.next());
			results.addAll(listEntry);
		}

		return results.toArray(resultingArrayType);
	}
}