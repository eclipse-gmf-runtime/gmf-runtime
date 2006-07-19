/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.dnd.ide.internal.core;

import java.util.Hashtable;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.part.MarkerTransfer;
import org.eclipse.ui.part.ResourceTransfer;

import org.eclipse.gmf.runtime.common.ui.services.dnd.core.AbstractTransferAdapterProvider;
import org.eclipse.gmf.runtime.common.ui.services.dnd.core.TransferAgent;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drag.ITransferDragSourceListener;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drag.SelectionDragAdapter;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drag.TransferDragSourceAdapter;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.ITransferDropTargetListener;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.SelectionDropAdapter;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.TransferDropTargetAdapter;
import org.eclipse.gmf.runtime.common.ui.services.dnd.ide.core.IDETransferId;

/**
 * Concrete implementation for common transfer adapters
 * 
 * @author Wayne Diu, wdiu, based on
 * org.eclipse.gmf.runtime.common.ui.services.dnd.core.TransferAdapterProvider
 */
public final class IDETransferAdapterProvider
	extends AbstractTransferAdapterProvider {

	/**
	 * Attribute for the table of transfer ids and their drag adapters
	 */
	private Hashtable transferDragSourceTable = new Hashtable();

	/**
	 * Attribute for the table of transfer ids and their drop adapters
	 */
	private Hashtable transferDropTargetTable = new Hashtable();

	/**
	 * Constructor for TransferAdapterProvider.
	 */
	public IDETransferAdapterProvider() {
		super();

		/*
		 * Initialize the table of transfer Ids and their drag adapters for
		 * pre-defined ids
		 */
		getTransferDragSourceTable().put(
			new Integer(IDETransferId.RESOURCE_TRANSFER.hashCode()),
			new TransferDragSourceAdapter(new TransferAgent(
				IDETransferId.RESOURCE_TRANSFER, ResourceTransfer.getInstance(),
				false)));

		getTransferDragSourceTable().put(
			new Integer(IDETransferId.MARKER_TRANSFER.hashCode()),
			new TransferDragSourceAdapter(
				new TransferAgent(IDETransferId.MARKER_TRANSFER, MarkerTransfer
					.getInstance(), false)));
		getTransferDragSourceTable().put(
			new Integer(IDETransferId.NAV_SELECTION_TRANSFER.hashCode()),
			new SelectionDragAdapter(new TransferAgent(
				IDETransferId.NAV_SELECTION_TRANSFER, LocalSelectionTransfer
					.getTransfer(), true) {

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAgent#getSelection(org.eclipse.swt.dnd.TransferData)
				 */
				public ISelection getSelection(TransferData transferData) {
					return LocalSelectionTransfer.getTransfer().getSelection();
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAgent#setSelection(org.eclipse.jface.viewers.ISelection)
				 */
				public void setSelection(ISelection selection) {
					LocalSelectionTransfer.getTransfer()
						.setSelection(selection);
				}
			}));
		/*
		 * Initialize the table of transfer Ids and their drop adapters for
		 * pre-defined ids
		 */
		getTransferDropTargetTable().put(
			new Integer(IDETransferId.RESOURCE_TRANSFER.hashCode()),
			new TransferDropTargetAdapter(new TransferAgent(
				IDETransferId.RESOURCE_TRANSFER, ResourceTransfer.getInstance(),
				false)));

		getTransferDropTargetTable().put(
			new Integer(IDETransferId.MARKER_TRANSFER.hashCode()),
			new TransferDropTargetAdapter(
				new TransferAgent(IDETransferId.MARKER_TRANSFER, MarkerTransfer
					.getInstance(), false)));

		getTransferDropTargetTable().put(
			new Integer(IDETransferId.NAV_SELECTION_TRANSFER.hashCode()),
			new SelectionDropAdapter(new TransferAgent(
				IDETransferId.NAV_SELECTION_TRANSFER, LocalSelectionTransfer
					.getTransfer(), true) {

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAgent#getSelection(org.eclipse.swt.dnd.TransferData)
				 */
				public ISelection getSelection(TransferData transferData) {
					return LocalSelectionTransfer.getTransfer().getSelection();
				}
			}));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAdapterProvider#getTransferDragSourceAdapter(java.lang.String)
	 */
	public ITransferDragSourceListener getTransferDragSourceAdapter(
			String transferId) {
		assert null != transferId : "transferId cannot be null";; //$NON-NLS-1$

		ITransferDragSourceListener listener = null;
		listener = (ITransferDragSourceListener) getTransferDragSourceTable()
			.get(new Integer(transferId.hashCode()));
		return listener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAdapterProvider#getTransferDropTargetAdapter(java.lang.String)
	 */
	public ITransferDropTargetListener getTransferDropTargetAdapter(
			String transferId) {
		assert null != transferId : "transferId cannot be null"; //$NON-NLS-1$

		ITransferDropTargetListener listener = null;
		listener = (ITransferDropTargetListener) getTransferDropTargetTable()
			.get(new Integer(transferId.hashCode()));
		return listener;
	}

	/**
	 * Return table of transfer drag source adapters
	 * 
	 * @return Hashtable
	 */
	private Hashtable getTransferDragSourceTable() {
		return transferDragSourceTable;
	}

	/**
	 * Return table of transfer drop target adapters
	 * 
	 * @return Hashtable
	 */
	private Hashtable getTransferDropTargetTable() {
		return transferDropTargetTable;
	}
}