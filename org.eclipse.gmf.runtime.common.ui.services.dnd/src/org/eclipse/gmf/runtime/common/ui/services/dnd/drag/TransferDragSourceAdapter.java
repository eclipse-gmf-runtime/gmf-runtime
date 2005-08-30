/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.dnd.drag;

import org.eclipse.swt.dnd.DragSourceEvent;

import org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAgent;

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
	private IDragSourceContext context;

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
		return context;
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
		this.context = dragSourceContext;
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