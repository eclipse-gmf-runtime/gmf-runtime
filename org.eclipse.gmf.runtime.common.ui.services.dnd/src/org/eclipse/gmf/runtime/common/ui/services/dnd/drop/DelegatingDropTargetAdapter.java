/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.dnd.drop;

import java.util.ArrayList;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
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
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.l10n.CommonUIServicesDNDMessages;

/**
 * This class delegates all the drop target events to the transfer drop target
 * adapter. The transfer adapter delegates to the registered listeners
 * 
 * @author Vishy Ramaswamy
 */
public class DelegatingDropTargetAdapter
	implements DropTargetListener {

	/**
	 * Attribute for the error message
	 */
	//private static final String ERROR_MSG = ResourceManager
	//	.getI18NString("DelegatingDropTargetAdapter.errorMessage"); //$NON-NLS-1$

	/**
	 * Attribute for the error message for logging
	 */
	//private static final String ERROR_MSG_LOG = ResourceManager
	//	.getI18NString("DelegatingDropTargetAdapter._ERROR_.errorMessage"); //$NON-NLS-1$

	/**
	 * Attribute for the transfer drop target adapters.
	 */
	private ITransferDropTargetListener[] adapters = null;

	/**
	 * Attribute for the current transfer drop target adapter.
	 */
	private ITransferDropTargetListener chosenAdapter = null;

	/**
	 * Attribute for the drop target context.
	 */
	private final DropTargetContext context;

	/**
	 * Attribute for the original drop event type.
	 */
	private int origDropType = DND.DROP_NONE;

	/**
	 * Attribute for the current drop event type.
	 */
	private int currentDropType = DND.DROP_NONE;

	/**
	 * Constructor for DelegatingDropTargetAdapter.
	 * 
	 * @param aContext
	 *            context information for the drop target
	 */
	public DelegatingDropTargetAdapter(DropTargetContext aContext) {
		super();

		assert null != aContext : "context information for the drop target cannot be null"; //$NON-NLS-1$
		this.context = aContext;

		/* Set the adapters */
		adapters = getAllTransferAdapters();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragEnter(DropTargetEvent event) {
		try {
			/* Set the original drop type */
			setOrigDropType(event.detail);

			/* Set the current target and location */
			getContext().setCurrentTargetAndLocation(event);

			/*
			 * Re-Initialize the Transfer Adapters with dynamically with the
			 * correct context
			 */
			initAdapters();

			/* Set the chosen adapter */
			setChosenAdapter(event);

			/* Check if a chosen adapter exists */
			if (getChosenAdapter() != null) {
				getChosenAdapter().dragEnter(event);
			} else {
				/* Delegate to the drop target */
				getContext().dragEnter(event);
			}

			if (Trace.shouldTrace(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDDebugOptions.DND)) {
				String adapterChosen = getChosenAdapter() != null ? getChosenAdapter()
					.getClass().getName()
					: "No Adapter"; //$NON-NLS-1$ 
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"DragEnter::Event detail is " + event.detail); //$NON-NLS-1$
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"DragEnter::Chosen adapter is " + adapterChosen); //$NON-NLS-1$
			}
		} catch (Throwable e) {
			event.detail = DND.DROP_NONE;
			handle(e, false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragLeave(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragLeave(DropTargetEvent event) {
		try {
			/* Check if a chosen adapter exists */
			if (getChosenAdapter() != null) {
				getChosenAdapter().dragLeave(event);
			} else {
				/* Delegate to the drop target */
				getContext().dragLeave(event);
			}

			if (Trace.shouldTrace(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDDebugOptions.DND)) {
				String adapterChosen = getChosenAdapter() != null ? getChosenAdapter()
					.getClass().getName()
					: "No Adapter"; //$NON-NLS-1$ 
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"DragLeave::Event detail is " + event.detail); //$NON-NLS-1$
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"DragLeave::Chosen adapter is " + adapterChosen); //$NON-NLS-1$
			}
		} catch (Throwable e) {
			handle(e, false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOperationChanged(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOperationChanged(DropTargetEvent event) {
		try {
			/* Set the original drop type */
			setOrigDropType(event.detail);

			/* Set the current target and location */
			getContext().setCurrentTargetAndLocation(event);

			/* Set the chosen adapter */
			setChosenAdapter(event);

			/* Check if a chosen adapter exists */
			if (getChosenAdapter() != null) {
				getChosenAdapter().dragOperationChanged(event);
			} else {
				/* Delegate to the drop target */
				getContext().dragOperationChanged(event);
			}

			if (Trace.shouldTrace(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDDebugOptions.DND)) {
				String adapterChosen = getChosenAdapter() != null ? getChosenAdapter()
					.getClass().getName()
					: "No Adapter"; //$NON-NLS-1$ 
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"DragOperationChanged::Event detail is " + event.detail); //$NON-NLS-1$
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"DragOperationChanged::Chosen adapter is " + adapterChosen); //$NON-NLS-1$
			}
		} catch (Throwable e) {
			event.detail = DND.DROP_NONE;
			handle(e, false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOver(DropTargetEvent event) {
		try {
			/* Set the current target and location */
			getContext().setCurrentTargetAndLocation(event);

			/* Set the event detail as the original drop type */
			event.detail = getOrigDropType();

			/* Set the chosen adapter */
			setChosenAdapter(event);

			/* Check if a chosen adapter exists */
			if (getChosenAdapter() != null) {
				getChosenAdapter().dragOver(event);

				/* Check the event detail */
				if (event.detail != DND.DROP_NONE) {
					setCurrentDropType(event.detail);
				}
			} else {
				/* Delegate to the drop target */
				getContext().dragOver(event);
			}

			if (Trace.shouldTrace(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDDebugOptions.DND)) {
				String adapterChosen = getChosenAdapter() != null ? getChosenAdapter()
					.getClass().getName()
					: "No Adapter"; //$NON-NLS-1$ 
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"DragOver::Event detail is " + event.detail); //$NON-NLS-1$
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"DragOver::Chosen adapter is " + adapterChosen); //$NON-NLS-1$
			}
		} catch (Throwable e) {
			event.detail = DND.DROP_NONE;
			handle(e, false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void drop(DropTargetEvent event) {
		try {
			/*
			 * Save the event detail just in case the event has to be delegated
			 * to the drop target
			 */
			int oldDetail = event.detail;

			/* Set the current target and location */
			getContext().setCurrentTargetAndLocation(event);

			/* Set the event detail as the current drop type */
			event.detail = getCurrentDropType();

			/* Set the chosen adapter */
			setChosenAdapter(event);

			/* Check if a chosen adapter exists */
			if (getChosenAdapter() != null) {
				getChosenAdapter().drop(event);
			} else {
				event.detail = oldDetail;

				/* Delegate to the drop target */
				getContext().drop(event);
			}

			if (Trace.shouldTrace(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDDebugOptions.DND)) {
				String adapterChosen = getChosenAdapter() != null ? getChosenAdapter()
					.getClass().getName()
					: "No Adapter"; //$NON-NLS-1$ 
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"Drop::Event detail is " + event.detail); //$NON-NLS-1$
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"Drop::Chosen adapter is " + adapterChosen); //$NON-NLS-1$
			}
		} catch (Throwable e) {
			event.detail = DND.DROP_NONE;
			handle(e, true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dropAccept(DropTargetEvent event) {
		try {
			/*
			 * Save the event detail just in case the event has to be delegated
			 * to the drop target
			 */
			int oldDetail = event.detail;

			/* Set the current target and location */
			getContext().setCurrentTargetAndLocation(event);

			/* Set the event detail as the original drop type */
			event.detail = getCurrentDropType();

			/* Set the chosen adapter */
			setChosenAdapter(event);

			/* Check if a chosen adapter exists */
			if (getChosenAdapter() != null) {
				getChosenAdapter().dropAccept(event);

				/* Check the event detail */
				if (event.detail != getCurrentDropType()) {
					setCurrentDropType(event.detail);
				}
			} else {
				event.detail = oldDetail;

				/* Delegate to the drop target */
				getContext().dropAccept(event);
			}

			if (Trace.shouldTrace(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDDebugOptions.DND)) {
				String adapterChosen = getChosenAdapter() != null ? getChosenAdapter()
					.getClass().getName()
					: "No Adapter"; //$NON-NLS-1$ 
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"DropAccept::Event detail is " + event.detail); //$NON-NLS-1$
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"DropAccept::Chosen adapter is " + adapterChosen); //$NON-NLS-1$
			}
		} catch (Throwable e) {
			event.detail = DND.DROP_NONE;
			handle(e, false);
		}
	}

	/**
	 * Returns the adapters.
	 * 
	 * @return ITransferDropTargetListener[]
	 */
	private ITransferDropTargetListener[] getAdapters() {
		return adapters;
	}

	/**
	 * Returns the drop target context.
	 * 
	 * @return DropTargetContext
	 */
	private DropTargetContext getContext() {
		return context;
	}

	/**
	 * Returns the set of registered transfer agents. This is determined from
	 * the drop target service.
	 * 
	 * @return Transfer[] an array of transfer agents
	 */
	public Transfer[] getAllTransferAgents() {
		/* Return null if no registered agents */
		if (getAdapters() == null || getAdapters().length == 0) {
			return null;
		}

		/* Get the transfers */
		ArrayList list = new ArrayList();
		for (int i = 0; i < getAdapters().length; i++) {
			list.add(getAdapters()[i].getTransferAgent().getTransfer());
		}

		return (Transfer[]) list.toArray(new Transfer[list.size()]);
	}

	/**
	 * Returns the set of transfer drop target adapters. The adapters are
	 * created based on the provided transfer ids determined from the drag and
	 * drop service.
	 * 
	 * @return ITransferDropTargetListener[] an array of transfer adapters
	 */
	private ITransferDropTargetListener[] getAllTransferAdapters() {
		/* Create placeholder for possible adapters */
		ArrayList listOfSupportedAdapters = new ArrayList();

		/* Get the ids from the drag and drop service */
		String[] transferIds = DragDropListenerService.getInstance()
			.getAllTransferIds(getContext().getActivePart().getSite().getId(),
				IListenerContext.DROP);

		/* Iterate through the ids */
		if (transferIds == null || transferIds.length == 0) {
			return null;
		}

		/* Create the adapters */
		for (int i = 0; i < transferIds.length; i++) {
			ITransferDropTargetListener adapter = TransferAdapterService
				.getInstance().getTransferDropTargetAdapter(transferIds[i]);

			if (adapter != null) {
				listOfSupportedAdapters.add(adapter);
			}
		}

		/* Return the adapters */
		return !listOfSupportedAdapters.isEmpty() ? (ITransferDropTargetListener[]) listOfSupportedAdapters
			.toArray(new ITransferDropTargetListener[listOfSupportedAdapters
				.size()])
			: null;
	}

	/**
	 * Set the chosen adapter.
	 * 
	 * @param event
	 *            the event
	 */
	private void setChosenAdapter(DropTargetEvent event) {
		/* Set the chosen adapter to null */
		chosenAdapter = null;

		/* Get the data types */
		TransferData[] dataTypes = event.dataTypes;
		if (dataTypes == null || dataTypes.length == 0) {
			if (Trace.shouldTrace(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDDebugOptions.DND)) {
				Trace
					.trace(CommonUIServicesDNDPlugin.getDefault(),
						CommonUIServicesDNDDebugOptions.DND,
						"setChosenAdapter::No transfer types available in the event"); //$NON-NLS-1$
			}

			return;
		}

		/* Check if there are any adapters */
		if (getAdapters() == null) {
			if (Trace.shouldTrace(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDDebugOptions.DND)) {
				Trace
					.trace(CommonUIServicesDNDPlugin.getDefault(),
						CommonUIServicesDNDDebugOptions.DND,
						"setChosenAdapter::No adapters registered with this target"); //$NON-NLS-1$
			}
			return;
		}

		/* Check if the target is null */
		if (getContext().getCurrentTarget() == null) {
			if (Trace.shouldTrace(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDDebugOptions.DND)) {
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"setChosenAdapter::Current target is null"); //$NON-NLS-1$
			}

			return;
		}

		/*
		 * Preferably use the selection transfer for in-process drag and drop
		 * operations. Iterate through the adapters
		 */
		for (int i = 0; i < getAdapters().length; i++) {
			if (getAdapters()[i].getTransferAgent().isSelectionType()) {
				/* Check if the adapter can handle */
				if (getAdapters()[i].canSupport(event)) {
					/* Set the adapter */
					chosenAdapter = getAdapters()[i];
					return;
				}
			}
		}

		/* Iterate through the rest of the adapters */
		for (int i = 0; i < getAdapters().length; i++) {
			if (!getAdapters()[i].getTransferAgent().isSelectionType()) {
				/* Check if the adapter can handle */
				if (getAdapters()[i].canSupport(event)) {
					/* Set the adapter */
					chosenAdapter = getAdapters()[i];
					return;
				}
			}
		}
	}

	/**
	 * Returns the chosenAdapter.
	 * 
	 * @return ITransferDropTargetListener
	 */
	private ITransferDropTargetListener getChosenAdapter() {
		return chosenAdapter;
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
			.getPluginId(), CommonUIServicesDNDStatusCodes.DROP_ACTION_FAILURE,
			CommonUIServicesDNDMessages.DelegatingDropTargetAdapter__ERROR__errorMessage, exception);

		Log.log(CommonUIServicesDNDPlugin.getDefault(), status);

		if (displayDialog) {

			status = new Status(IStatus.ERROR, CommonUIServicesDNDPlugin
				.getPluginId(),
				CommonUIServicesDNDStatusCodes.DROP_ACTION_FAILURE, 
				CommonUIServicesDNDMessages.DelegatingDropTargetAdapter_errorMessage,
				exception);

			ErrorDialog.openError(getContext().getActivePart().getSite()
				.getShell(), null, null, status);
		}
	}

	/**
	 * Return the original drop type
	 * 
	 * @return int the drop type
	 */
	private int getOrigDropType() {
		return origDropType;
	}

	/**
	 * Set the original drop type
	 * 
	 * @param anOrigDropType
	 *            the drop type
	 */
	private void setOrigDropType(int anOrigDropType) {
		this.origDropType = anOrigDropType;
	}

	/**
	 * Return the current drop type
	 * 
	 * @return int the drop type
	 */
	private int getCurrentDropType() {
		return currentDropType;
	}

	/**
	 * Set the current drop type
	 * 
	 * @param aCurrentDropType
	 *            the drop type
	 */
	private void setCurrentDropType(int aCurrentDropType) {
		this.currentDropType = aCurrentDropType;
	}

	/**
	 * Inititializes the transfer adapters' context
	 */
	private void initAdapters() {
		/* Iterate through the adapters */

		if (getAdapters() != null)
			for (int i = 0; i < getAdapters().length; i++) {

				/* Initialize the adapter */
				getAdapters()[i].init(getContext());

			}

	}
}