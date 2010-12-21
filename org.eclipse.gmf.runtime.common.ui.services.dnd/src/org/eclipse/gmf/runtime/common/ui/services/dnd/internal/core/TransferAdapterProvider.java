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

package org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core;

import java.util.Hashtable;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.part.PluginTransfer;

import org.eclipse.gmf.runtime.common.ui.services.dnd.core.AbstractTransferAdapterProvider;
import org.eclipse.gmf.runtime.common.ui.services.dnd.core.SelectionTransfer;
import org.eclipse.gmf.runtime.common.ui.services.dnd.core.TransferAgent;
import org.eclipse.gmf.runtime.common.ui.services.dnd.core.TransferId;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drag.ITransferDragSourceListener;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drag.SelectionDragAdapter;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drag.TransferDragSourceAdapter;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.ITransferDropTargetListener;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.SelectionDropAdapter;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.TransferDropTargetAdapter;
import org.eclipse.gmf.runtime.common.ui.util.CustomDataTransfer;

/**
 * Concrete implementation for common transfer adapters
 * 
 * @author Vishy Ramaswamy
 */
public final class TransferAdapterProvider
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
	public TransferAdapterProvider() {
		super();
		
		/*
		 * Initialize the table of transfer Ids and their drag adapters for
		 * pre-defined ids
		 */
		getTransferDragSourceTable().put(
				Integer.valueOf(TransferId.CUSTOM_DATA_TRANSFER.hashCode()),
			new TransferDragSourceAdapter(new TransferAgent(
				TransferId.CUSTOM_DATA_TRANSFER, CustomDataTransfer
					.getInstance(), false)));
		getTransferDragSourceTable().put(
				Integer.valueOf(TransferId.FILE_TRANSFER.hashCode()),
			new TransferDragSourceAdapter(new TransferAgent(
				TransferId.FILE_TRANSFER, FileTransfer.getInstance(), false)));
		getTransferDragSourceTable().put(
				Integer.valueOf(TransferId.PLUGIN_TRANSFER.hashCode()),
			new TransferDragSourceAdapter(
				new TransferAgent(TransferId.PLUGIN_TRANSFER, PluginTransfer
					.getInstance(), false)));
		getTransferDragSourceTable().put(
				Integer.valueOf(TransferId.SELECTION_TRANSFER.hashCode()),
			new SelectionDragAdapter(new TransferAgent(
				TransferId.SELECTION_TRANSFER, SelectionTransfer.getInstance(),
				true) {

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAgent#getSelection(org.eclipse.swt.dnd.TransferData)
				 */
				public ISelection getSelection(TransferData transferData) {
					return SelectionTransfer.getInstance().getSelection();
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAgent#setSelection(org.eclipse.jface.viewers.ISelection)
				 */
				public void setSelection(ISelection selection) {
					SelectionTransfer.getInstance().setSelection(selection);
				}
			}));
		getTransferDragSourceTable().put(
				Integer.valueOf(TransferId.TEXT_TRANSFER.hashCode()),
			new TransferDragSourceAdapter(new TransferAgent(
				TransferId.TEXT_TRANSFER, TextTransfer.getInstance(), false)));
		getTransferDragSourceTable().put(
				Integer.valueOf(TransferId.RTF_TRANSFER.hashCode()),
			new TransferDragSourceAdapter(new TransferAgent(
				TransferId.RTF_TRANSFER, RTFTransfer.getInstance(), false)));

		/*
		 * Initialize the table of transfer Ids and their drop adapters for
		 * pre-defined ids
		 */
		getTransferDropTargetTable().put(
				Integer.valueOf(TransferId.CUSTOM_DATA_TRANSFER.hashCode()),
			new TransferDropTargetAdapter(new TransferAgent(
				TransferId.CUSTOM_DATA_TRANSFER, CustomDataTransfer
					.getInstance(), false)));
		getTransferDropTargetTable().put(
				Integer.valueOf(TransferId.FILE_TRANSFER.hashCode()),
			new TransferDropTargetAdapter(new TransferAgent(
				TransferId.FILE_TRANSFER, FileTransfer.getInstance(), false)));
		getTransferDropTargetTable().put(
				Integer.valueOf(TransferId.PLUGIN_TRANSFER.hashCode()),
			new TransferDropTargetAdapter(
				new TransferAgent(TransferId.PLUGIN_TRANSFER, PluginTransfer
					.getInstance(), false)));
		getTransferDropTargetTable().put(
				Integer.valueOf(TransferId.SELECTION_TRANSFER.hashCode()),
			new SelectionDropAdapter(new TransferAgent(
				TransferId.SELECTION_TRANSFER, SelectionTransfer.getInstance(),
				true) {

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAgent#getSelection(org.eclipse.swt.dnd.TransferData)
				 */
				public ISelection getSelection(TransferData transferData) {
					return SelectionTransfer.getInstance().getSelection();
				}
			}));
		getTransferDropTargetTable().put(
				Integer.valueOf(TransferId.TEXT_TRANSFER.hashCode()),
			new TransferDropTargetAdapter(new TransferAgent(
				TransferId.TEXT_TRANSFER, TextTransfer.getInstance(), false)));
		getTransferDropTargetTable().put(
				Integer.valueOf(TransferId.RTF_TRANSFER.hashCode()),
			new TransferDropTargetAdapter(new TransferAgent(
				TransferId.RTF_TRANSFER, RTFTransfer.getInstance(), false)));
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
			.get(Integer.valueOf(transferId.hashCode()));
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
			.get(Integer.valueOf(transferId.hashCode()));
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