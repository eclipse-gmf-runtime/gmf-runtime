/******************************************************************************
 * Copyright (c) 2002, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.dnd.drag;

import java.lang.ref.WeakReference;

import org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAgent;
import org.eclipse.swt.dnd.DragSourceEvent;

/**
 * Parent of all the transfer drag source adapters. Every transfer type drag
 * source adapter should override the methods in the
 * <code>DragSourceListener</code> interface.
 * 
 * @author Vishy Ramaswamy
 */
public class TransferDragSourceAdapter
	implements ITransferDragSourceListener {

	/**
	 * Attribute for the drag source listeners.
	 */
	private IDragSourceListener[] listeners;

	/**
	 * Attribute for the transfer agent.
	 */
	private final ITransferAgent transferAgent;

	/**
	 * Attribute for the drag source context.
	 */
	private WeakReference<IDragSourceContext> context;

	/**
	 * Constructor for TransferDragSourceAdapter.
	 * 
	 * @param aTransferAgent
	 *            ITransferAgent that this class is an adpater for
	 */
	public TransferDragSourceAdapter(ITransferAgent aTransferAgent) {
		super();

		assert null != aTransferAgent : "aTransferAgent cannot be null"; //$NON-NLS-1$
		this.transferAgent = aTransferAgent;
	}

	/**
	 * Returns the listeners.
	 * 
	 * @return IDragSourceListener[]
	 */
	protected final IDragSourceListener[] getListeners() {
		return listeners;
	}

	/**
	 * Returns the context.
	 * 
	 * @return IDragSourceContext
	 */
	protected final IDragSourceContext getContext() {
		if (context == null) return null;
		return context.get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragFinished(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragFinished(DragSourceEvent event) {
		/* Set the event to all the listeners */
		for (int i = 0; i < getListeners().length; i++) {
			getListeners()[i].dragFinished(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragSetData(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragSetData(DragSourceEvent event) {
		/* Set the event to all the listeners */
		for (int i = 0; i < getListeners().length; i++) {
			getListeners()[i].dragSetData(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragStart(DragSourceEvent event) {
		/* Set the event to all the listeners */
		for (int i = 0; i < getListeners().length; i++) {
			getListeners()[i].dragStart(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drag.ITransferDragSourceListener#init(org.eclipse.gmf.runtime.common.ui.services.dnd.drag.IDragSourceListener[],
	 *      org.eclipse.gmf.runtime.common.ui.services.dnd.drag.IDragSourceContext)
	 */
	public final void init(IDragSourceListener[] dragSourceListeners,
			IDragSourceContext dragSourceContext) {
		assert null != dragSourceListeners : "list of dragSourceListeners cannot be null"; //$NON-NLS-1$
		assert null != dragSourceContext : "dragSourceContext cannot be null"; //$NON-NLS-1$

		this.listeners = dragSourceListeners;
		this.context = new WeakReference<IDragSourceContext>(dragSourceContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drag.ITransferDragSourceListener#getTransferAgent()
	 */
	public final ITransferAgent getTransferAgent() {
		return transferAgent;
	}
}