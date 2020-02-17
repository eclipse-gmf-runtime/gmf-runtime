/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.dnd.core.DragDropListenerService;
import org.eclipse.gmf.runtime.common.ui.services.dnd.core.IListenerContext;
import org.eclipse.gmf.runtime.common.ui.services.dnd.core.TransferAdapterService;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.CommonUIServicesDNDDebugOptions;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.CommonUIServicesDNDPlugin;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.CommonUIServicesDNDStatusCodes;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core.DragListenerContext;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.l10n.CommonUIServicesDNDMessages;

/**
 * This class delegates all the drag source events to the transfer drag source
 * adapters. The transfer adapters delegate to the registered listeners
 * 
 * @author Vishy Ramaswamy
 */
public final class DelegatingDragSourceAdapter
	implements DragSourceListener {

	/**
	 * Attribute for the all transfer drag source adapters mapped to their
	 * transfer id.
	 */
	private final Hashtable allAdapters;

	/**
	 * Attribute for the current transfer drag source adapters.
	 */
	private ITransferDragSourceListener[] currentAdapters = null;

	/**
	 * Attribute for the chosen transfer drag source adapter.
	 */
	private ITransferDragSourceListener chosenAdapter = null;

	/**
	 * Attribute for the drag source context.
	 */
	private final DragSourceContext context;

	/**
	 * Constructor for DelegatingDragSourceAdapter.
	 * 
	 * @param aContext
	 *            the drag source context
	 */
	public DelegatingDragSourceAdapter(DragSourceContext aContext) {
		super();

		assert null != aContext : "aContext cannot be null"; //$NON-NLS-1$
		this.context = aContext;

		/* Set the adapters */
		allAdapters = getAllTransferAdapters();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragStart(DragSourceEvent event) {
		try {
			if (Trace.shouldTrace(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDDebugOptions.DND)) {
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND, "dragStart entered"); //$NON-NLS-1$
			}

			/* Save the provided event information */
			boolean saveDoit = event.doit;
			Object saveData = event.data;

			/* Clear the previous collection of adapters */
			setCurrentAdapters(null);

			/* Clear the previous chosen adapter */
			setChosenAdapter(null);

			/* Set the adapters */
			setCurrentAdapters(getAdapters(getListeners()));

			/* Iterate through the adapters */
			boolean doIt = false;
			ArrayList listOfActiveAdapters = new ArrayList();
			ArrayList listOfActiveTransferAgents = new ArrayList();
			if (getCurrentAdapters() != null && getCurrentAdapters().length > 0) {
				for (int i = 0; i < getCurrentAdapters().length; i++) {
					/* Set the do it flag */
					event.doit = saveDoit;

					/* Invoke the drag start */
					getCurrentAdapters()[i].dragStart(event);

					/* If ok to drag add the transfer to the list */
					if (event.doit) {
						listOfActiveAdapters.add(getCurrentAdapters()[i]);
						listOfActiveTransferAgents.add(getCurrentAdapters()[i]
							.getTransferAgent().getTransfer());
					}

					/* Ok to drag if anyone is ready to handle */
					doIt = doIt || event.doit;
				}

				/* Set the transfers and the updated adapters */
				if (doIt) {
					/* Set the transfers */
					((DragSource) event.widget)
						.setTransfer((Transfer[]) listOfActiveTransferAgents
							.toArray(new Transfer[listOfActiveTransferAgents
								.size()]));

					/* Set the adapters */
					setCurrentAdapters((org.eclipse.gmf.runtime.common.ui.services.dnd.drag.ITransferDragSourceListener[]) listOfActiveAdapters
						.toArray(new ITransferDragSourceListener[listOfActiveAdapters
							.size()]));
				}

				if (Trace.shouldTrace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND)) {
					String result = doIt ? "true" : "false"; //$NON-NLS-1$ //$NON-NLS-2$
					Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
						CommonUIServicesDNDDebugOptions.DND,
						"DragStart::Event do it flag is " + result); //$NON-NLS-1$

					/* Write the transfer adapters */
					for (int i = 0; i < getCurrentAdapters().length; i++) {
						Trace
							.trace(
								CommonUIServicesDNDPlugin.getDefault(),
								CommonUIServicesDNDDebugOptions.DND,
								"DragStart::Transfer Adapter is " + getCurrentAdapters()[i].getClass().getName()); //$NON-NLS-1$
					}
				}

				/* Set the event information */
				event.data = saveData;
				event.doit = doIt;
			} else {
				/* Delegate to the drag source */
				getContext().dragStart(event);
			}
		} catch (Throwable e) {
			event.doit = false;
			handle(e, false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragSetData(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragSetData(DragSourceEvent event) {
		try {
			/* Get the corresponding chosen adapter */
			setChosenAdapter(getAdapter(event.dataType));

			if (Trace.shouldTrace(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDDebugOptions.DND)) {
				String adapterChosen = getChosenAdapter() != null ? getChosenAdapter()
					.getClass().getName()
					: "No Adapter Chosen"; //$NON-NLS-1$
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"DragSetData::Chosen Adapter is " + adapterChosen); //$NON-NLS-1$
			}

			if (getChosenAdapter() != null) {
				getChosenAdapter().dragSetData(event);
			} else {
				/* Delegate to the drag source */
				getContext().dragSetData(event);
			}
		} catch (Throwable e) {
			event.data = null;
			handle(e, false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragFinished(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragFinished(DragSourceEvent event) {
		try {
			if (Trace.shouldTrace(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDDebugOptions.DND)) {
				String adapterChosen = getChosenAdapter() != null ? getChosenAdapter()
					.getClass().getName()
					: "No Adapter Chosen"; //$NON-NLS-1$
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"DragFinished::Chosen Adapter is " + adapterChosen); //$NON-NLS-1$
			}

			/* Get the corresponding chosen adapter */
			if (getChosenAdapter() != null) {
				getChosenAdapter().dragFinished(event);
			} else {
				setChosenAdapter(getAdapter(event.dataType));
				if (getChosenAdapter() != null) {
					getChosenAdapter().dragFinished(event);
				} else {
					/* Delegate to the drag source */
					getContext().dragFinished(event);
				}
			}
		} catch (Throwable e) {
			handle(e, false);
		} finally {
			setChosenAdapter(null);
			setCurrentAdapters(null);
		}
	}

	/**
	 * Returns the current adapters.
	 * 
	 * @return ITransferDragSourceListener[]
	 */
	private ITransferDragSourceListener[] getCurrentAdapters() {
		return currentAdapters;
	}

	/**
	 * Returns the drag source context.
	 * 
	 * @return DragSourceContext
	 */
	private DragSourceContext getContext() {
		return context;
	}

	/**
	 * Returns the set of registered listeners. The listeners are acquired
	 * through the <code>DragDropListenerService</code>.
	 * 
	 * @return IDragSourceListener[] an array of listeners
	 */
	private IDragSourceListener[] getListeners() {
		/* Get the structured selection */
		ISelection selection = getContext().getSelection();

		/* Check the selection */
		if (selection == null || selection.isEmpty()
			|| !(selection instanceof IStructuredSelection)) {
			return null;
		}

		/* Get the selection as an object array */
		Object[] array = ((IStructuredSelection) selection).toArray();

		/* Create a unique list of element types */
		ArrayList listOfElementTypes = new ArrayList();
		for (int i = 0; i < array.length; i++) {
			if (!listOfElementTypes.contains(array[i].getClass())) {
				listOfElementTypes.add(array[i].getClass());
			}
		}

		/* Get the drag source listeners */
		ArrayList listOfListeners = new ArrayList();
		Iterator iterator = listOfElementTypes.iterator();
		while (iterator.hasNext()) {
			/* Get the element type */
			Class clazz = (Class) iterator.next();

			/* Create a drag listener context */
			DragListenerContext aContext = new DragListenerContext(getContext()
				.getActivePart(), clazz, IListenerContext.ALL_TRANSFERS, false);

			/* Get the drag source listeners */
			IDragSourceListener[] listeners = DragDropListenerService
				.getInstance().getDragSourceListeners(aContext);

			/* Get a compatible one if no listener is found for a direct match */
			if (listeners == null || listeners.length == 0) {
				/* Create a drag listener context */
				aContext = new DragListenerContext(
					getContext().getActivePart(), clazz,
					IListenerContext.ALL_TRANSFERS, true);

				/* Get the drag source listener */
				listeners = DragDropListenerService.getInstance()
					.getDragSourceListeners(aContext);
			}

			/* Add to the list */
			if (listeners != null && listeners.length != 0) {
				for (int i = 0; i < listeners.length; i++) {
					if (!listOfListeners.contains(listeners[i])) {
						listOfListeners.add(listeners[i]);
					}
				}
			}
		}

		/* Combine all the listeners into one array */
		return !listOfListeners.isEmpty() ? (IDragSourceListener[]) listOfListeners
			.toArray(new IDragSourceListener[listOfListeners.size()])
			: null;
	}

	/**
	 * Returns the set of transfer drag source adapters. The adapters are
	 * created based on the transfers supported by the listeners
	 * 
	 * @param listeners
	 *            The registered list of <code>IDragSourceListener</code>
	 * @return ITransferDragSourceListener[] an array of transfer adapters
	 */
	private ITransferDragSourceListener[] getAdapters(
			IDragSourceListener[] listeners) {
		/* Return if no listeners or no selection */
		if (listeners == null || listeners.length == 0) {
			return null;
		}

		/* Create placeholder for active listeners */
		Hashtable listOfUniqueTransferIds = new Hashtable();

		/* Check if the listeners can handle the drag */
		for (int i = 0; i < listeners.length; i++) {
			/* Check if the selection is draggable */
			if (!listeners[i].isDraggable(getContext())) {
				continue;
			}

			/* Get the transfer ids */
			String[] transferIds = listeners[i].getSupportingTransferIds();
			if (transferIds == null || transferIds.length == 0) {
				continue;
			}

			/* Iterate through the ids */
			for (int y = 0; y < transferIds.length; y++) {
				/* Placeholder for listeners */
				ArrayList listOfListeners = null;

				if (!listOfUniqueTransferIds.containsKey(transferIds[y])) {
					/* Create a new list */
					listOfListeners = new ArrayList();
				} else {
					/* Get the list of listeners and add to it */
					listOfListeners = (ArrayList) listOfUniqueTransferIds
						.get(transferIds[y]);
				}

				/* Update the list */
				if (listOfListeners != null) {
					listOfListeners.add(listeners[i]);
					listOfUniqueTransferIds
						.put(transferIds[y], listOfListeners);
				}
			}
		}

		/* Return if no active transfer agents */
		if (listOfUniqueTransferIds.isEmpty()) {
			return null;
		}

		/* Get the adapters */
		ArrayList listOfAdapters = new ArrayList();
		Enumeration enumeration = listOfUniqueTransferIds.keys();
		while (enumeration.hasMoreElements()) {
			/* Get the next id */
			String transferId = (String) enumeration.nextElement();

			/* Get the listeners */
			ArrayList supportingListeners = (ArrayList) listOfUniqueTransferIds
				.get(transferId);

			/* Get the adapter */
			ITransferDragSourceListener adapter = (ITransferDragSourceListener) getAllAdapters()
				.get(Integer.valueOf(transferId.hashCode()));
			if (adapter != null) {
				/* Initialize the adapter */
				adapter.init(
					(IDragSourceListener[]) supportingListeners
						.toArray(new IDragSourceListener[supportingListeners
							.size()]), getContext());

				/* Add to the list */
				listOfAdapters.add(adapter);
			}
		}

		/* Return the list of adapters */
		return !listOfAdapters.isEmpty() ? (ITransferDragSourceListener[]) listOfAdapters
			.toArray(new ITransferDragSourceListener[listOfAdapters.size()])
			: null;
	}

	/**
	 * Sets the current adapters.
	 * 
	 * @param currentAdapterArray
	 *            The adapters to set
	 */
	private void setCurrentAdapters(
			ITransferDragSourceListener[] currentAdapterArray) {
		this.currentAdapters = currentAdapterArray;
	}

	/**
	 * Returns the chosen adapter
	 * 
	 * @param type
	 *            The transfer data type
	 * @return The transfer drag source adapter chosen
	 */
	private ITransferDragSourceListener getAdapter(TransferData type) {
		if (type != null) {
			for (int i = 0; i < getCurrentAdapters().length; i++) {
				if (getCurrentAdapters()[i].getTransferAgent().getTransfer()
					.isSupportedType(type)) {
					return getCurrentAdapters()[i];
				}
			}
		}

		return null;
	}

	/**
	 * Returns the chosenAdapter.
	 * 
	 * @return ITransferDragSourceListener
	 */
	private ITransferDragSourceListener getChosenAdapter() {
		return chosenAdapter;
	}

	/**
	 * Sets the chosenAdapter.
	 * 
	 * @param aChosenAdapter
	 *            The chosenAdapter to set
	 */
	private void setChosenAdapter(ITransferDragSourceListener aChosenAdapter) {
		this.chosenAdapter = aChosenAdapter;
	}

	/**
	 * Returns the set of transfer drag source adapters. The adapters are
	 * created based on the provided transfer ids determined from the drag and
	 * drop service.
	 * 
	 * @return Hashtable a table of transfer adapters
	 */
	private Hashtable getAllTransferAdapters() {
		/* Create placeholder for possible adapters */
		Hashtable listOfSupportedAdapters = new Hashtable();

		/* Get the ids from the drag and drop service */
		String[] transferIds = DragDropListenerService.getInstance()
			.getAllTransferIds(getContext().getActivePart().getSite().getId(),
				IListenerContext.DRAG);

		/* Iterate through the ids */
		if (transferIds == null || transferIds.length == 0) {
			return null;
		}

		/* Create the adapters */
		for (int i = 0; i < transferIds.length; i++) {
			ITransferDragSourceListener adapter = TransferAdapterService
				.getInstance().getTransferDragSourceAdapter(transferIds[i]);

			if (adapter != null) {
				listOfSupportedAdapters.put(Integer.valueOf(transferIds[i]
					.hashCode()), adapter);
			}
		}

		/* Return the adapters */
		return listOfSupportedAdapters;
	}

	/**
	 * Handles the specified exception.
	 * 
	 * @param exception
	 *            The exception to be handled.
	 * @param displayDialog
	 *            boolean to indicate whether to display dialog
	 */
	private void handle(Throwable exception, boolean displayDialog) {
		if (Trace.shouldTrace(CommonUIServicesDNDPlugin.getDefault(),
			CommonUIServicesDNDDebugOptions.EXCEPTIONS_CATCHING)) {
			Trace.catching(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDDebugOptions.EXCEPTIONS_CATCHING,
				getClass(), "handle", exception); //$NON-NLS-1$
		}

		IStatus status = new Status(IStatus.ERROR, CommonUIServicesDNDPlugin
			.getPluginId(), CommonUIServicesDNDStatusCodes.DRAG_ACTION_FAILURE,
			CommonUIServicesDNDMessages.DelegatingDragSourceAdapter__ERROR__errorMessage, exception);

		Log.log(CommonUIServicesDNDPlugin.getDefault(), status);

		if (displayDialog) {

			status = new Status(IStatus.ERROR, CommonUIServicesDNDPlugin
				.getPluginId(),
				CommonUIServicesDNDStatusCodes.DRAG_ACTION_FAILURE, 
				CommonUIServicesDNDMessages.DelegatingDragSourceAdapter__ERROR__errorMessage,
				exception);

			ErrorDialog.openError(getContext().getActivePart().getSite()
				.getShell(), null, null, status);
		}
	}

	/**
	 * Returns the adapters.
	 * 
	 * @return Hashtable
	 */
	private Hashtable getAllAdapters() {
		return allAdapters;
	}
}