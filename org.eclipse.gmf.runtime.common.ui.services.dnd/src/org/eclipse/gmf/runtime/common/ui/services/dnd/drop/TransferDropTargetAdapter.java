/******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.dnd.drop;

import java.lang.ref.WeakReference;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.dnd.core.DragDropListenerService;
import org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAgent;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.CommonUIServicesDNDDebugOptions;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.CommonUIServicesDNDPlugin;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.CommonUIServicesDNDStatusCodes;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core.DropListenerContext;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * Parent of all the transfer drop target adapters. Every transfer type drop
 * target adapter should override the methods in the
 * <code>DropTargetListener</code> interface.
 * 
 * @author Vishy Ramaswamy
 */
public class TransferDropTargetAdapter
	implements ITransferDropTargetListener {

	/**
	 * Attribute for the drop target listener.
	 */
	private IDropTargetListener listener;

	/**
	 * Attribute for the transfer agent.
	 */
	private final ITransferAgent transferAgent;

	/**
	 * Attribute for the drop target context.
	 */
	private WeakReference<IDropTargetContext> context;

	/**
	 * Attribute for the current event
	 */
	private final org.eclipse.gmf.runtime.common.ui.services.dnd.drop.DropTargetEvent currentEvent;

	/**
	 * Constructor for TransferDropTargetAdapter.
	 * 
	 * @param aTransferAgent
	 *            The transfer agent
	 */
	public TransferDropTargetAdapter(ITransferAgent aTransferAgent) {
		super();

		assert null != aTransferAgent : "aTransferAgent cannot be null"; //$NON-NLS-1$
		
		this.listener = null;
		this.transferAgent = aTransferAgent;
		this.context = null;
		this.currentEvent = new org.eclipse.gmf.runtime.common.ui.services.dnd.drop.DropTargetEvent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragEnter(DropTargetEvent event) {
		if (listener != null) {
			/* Send the event to the selected listener */
			listener.setFeedback(event);

			/* Send the event to the selected listener */
			listener.dragEnter(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragLeave(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragLeave(DropTargetEvent event) {
		/* Send the event to the selected listener */
		if (listener != null) {
			listener.dragLeave(event);
		}
		listener = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOperationChanged(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOperationChanged(DropTargetEvent event) {
		/* Send the event to the selected listener */
		if (listener != null) {
			listener.dragOperationChanged(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOver(DropTargetEvent event) {
		/* Send the event to the selected listener */
		if (listener != null) {
			/* Send the event to the selected listener */
			listener.setFeedback(event);

			/* Send the event to the selected listener */
			listener.dragOver(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public final void drop(DropTargetEvent event) {
		/* Send the event to the selected listener */
		if (listener != null) {
			/* Send the event to the selected listener */
			listener.drop(event);
		}
		listener = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dropAccept(DropTargetEvent event) {
		/* Set the event to the selected listener */
		if (listener != null) {
			listener.dropAccept(event);
		}
	}

	/**
	 * Returns the listener.
	 * 
	 * @return IDropTargetListener
	 */
	protected final IDropTargetListener getListener() {
		return listener;
	}

	/**
	 * Returns the context.
	 * 
	 * @return IDropTargetContext
	 */
	protected final IDropTargetContext getContext() {
		if(context == null) return null;
		return context.get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drop.ITransferDropTargetListener#init(org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetContext)
	 */
	public final void init(IDropTargetContext initialContext) {
		assert null != initialContext : "initialContext cannot be null"; //$NON-NLS-1$
		
		this.context = new WeakReference<IDropTargetContext>(initialContext);
	}

	/**
	 * Returns the transfer agent.
	 * 
	 * @return ITransferAgent
	 */
	public final ITransferAgent getTransferAgent() {
		return transferAgent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drop.ITransferDropTargetListener#canSupport(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public boolean canSupport(DropTargetEvent event) {
		if (Trace.shouldTrace(CommonUIServicesDNDPlugin.getDefault(),
			CommonUIServicesDNDDebugOptions.DND)) {
			Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDDebugOptions.DND,
				"canSupport::Adapter is " + getClass().getName()); //$NON-NLS-1$
		}

		/*
		 * Check if one of the transfer data can be supported by this transfer
		 * and if a supporting listener is available
		 */
		return canSupportTransferData(event) && isSupportingListenerAvailable();
	}

	/**
	 * Check if a supporting listener is available.It also updates the current
	 * listener cache.
	 * 
	 * @return boolean true or false
	 */
	protected final boolean isSupportingListenerAvailable() {
		/* Get the listeners if any for the adapter's transfer type */
		IDropTargetListener[] listeners = getListeners(false);

		/* Get a compatible one if no listener is found for a direct match */
		if (listeners == null || listeners.length == 0) {
			listeners = getListeners(true);
		}

		/* Choose the current listener */
		if (listeners != null) {
			CommonUIServicesDNDPlugin plugin = CommonUIServicesDNDPlugin
				.getDefault();
			boolean shouldTrace = Trace.shouldTrace(plugin,
				CommonUIServicesDNDDebugOptions.DND);

			for (int i = 0; i < listeners.length; ++i) {
				try {
					/* Update the listener */
					listener = listeners[i];
					if (listener.canSupport(getContext(), currentEvent,
						transferAgent)) {
						if (shouldTrace) {
							Trace
								.trace(
									plugin,
									CommonUIServicesDNDDebugOptions.DND,
									"isSupportingListenerAvailable::Chosen listener is " + listener.getClass().getName()); //$NON-NLS-1$
						}

						return true;
					}
				} catch (RuntimeException e) {
					if (shouldTrace) {
						Trace.catching(plugin,
							CommonUIServicesDNDDebugOptions.DND, getClass(),
							"isSupportingListenerAvailable", e); //$NON-NLS-1$
					}
					Log
						.warning(
							plugin,
							CommonUIServicesDNDStatusCodes.IGNORED_EXCEPTION_WARNING,
							"Drop Listener " + listeners[i].getClass().getName(), e); //$NON-NLS-1$
				}
			}
		}

		/* Set the listener back to null */
		listener = null;

		return false;
	}

	/**
	 * Checks if the transfer agent can support one of the transfer datas passed
	 * into the event. It also updates the internal current event cache and sets
	 * the current data type on the current event.
	 * 
	 * @param event
	 *            the drop target event
	 * @return boolean true or false
	 */
	protected final boolean canSupportTransferData(DropTargetEvent event) {
		/* Update the current event */
		currentEvent.setEvent(event);

		Transfer transfer = transferAgent.getTransfer();

		for (int i = 0; i < event.dataTypes.length; ++i) {
			TransferData dataType = event.dataTypes[i];

			if (transfer.isSupportedType(dataType)) {
				/* Set the current data type */
				event.currentDataType = dataType;
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the drop target listeners for the current context.
	 * 
	 * @param isCompatible
	 *            should compatible listeners be considered
	 * @return IDropTargetListener[] the drop target listeners
	 */
	private final IDropTargetListener[] getListeners(boolean isCompatible) {
		/* Get the drop target listeners */
		IDropTargetListener[] listeners = DragDropListenerService.getInstance()
			.getDropTargetListeners(
				new DropListenerContext(getContext().getActivePart(), getContext()
					.getCurrentTarget().getClass(), transferAgent
					.getTransferId(), isCompatible));

		return listeners;
	}
}