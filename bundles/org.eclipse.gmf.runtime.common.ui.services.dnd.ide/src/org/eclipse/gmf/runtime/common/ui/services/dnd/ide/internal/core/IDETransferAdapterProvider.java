/******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.dnd.ide.internal.core;

import java.util.HashMap;
import java.util.Objects;

import org.eclipse.gmf.runtime.common.ui.services.dnd.core.AbstractTransferAdapterProvider;
import org.eclipse.gmf.runtime.common.ui.services.dnd.core.TransferAgent;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drag.ITransferDragSourceListener;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drag.SelectionDragAdapter;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drag.TransferDragSourceAdapter;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.ITransferDropTargetListener;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.SelectionDropAdapter;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.TransferDropTargetAdapter;
import org.eclipse.gmf.runtime.common.ui.services.dnd.ide.core.IDETransferId;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.part.MarkerTransfer;
import org.eclipse.ui.part.ResourceTransfer;

/**
 * Concrete implementation for common transfer adapters
 *
 * @author Wayne Diu, wdiu, based on
 *         org.eclipse.gmf.runtime.common.ui.services.dnd.core.TransferAdapterProvider
 */
public final class IDETransferAdapterProvider extends AbstractTransferAdapterProvider {

	/**
	 * Attribute for the table of transfer ids and their drag adapters
	 */
	private HashMap<Integer, ITransferDragSourceListener> transferDragSourceTable = new HashMap<>();

	/**
	 * Attribute for the table of transfer ids and their drop adapters
	 */
	private HashMap<Integer, ITransferDropTargetListener> transferDropTargetTable = new HashMap<>();

	/**
	 * Constructor for TransferAdapterProvider.
	 */
	public IDETransferAdapterProvider() {

		/*
		 * Initialize the table of transfer Ids and their drag adapters for pre-defined
		 * ids
		 */
		transferDragSourceTable.put(Integer.valueOf(IDETransferId.RESOURCE_TRANSFER.hashCode()),
				new TransferDragSourceAdapter(
						new TransferAgent(IDETransferId.RESOURCE_TRANSFER, ResourceTransfer.getInstance(), false)));

		transferDragSourceTable.put(Integer.valueOf(IDETransferId.MARKER_TRANSFER.hashCode()),
				new TransferDragSourceAdapter(
						new TransferAgent(IDETransferId.MARKER_TRANSFER, MarkerTransfer.getInstance(), false)));
		transferDragSourceTable.put(Integer.valueOf(IDETransferId.NAV_SELECTION_TRANSFER.hashCode()),
				new SelectionDragAdapter(new TransferAgent(IDETransferId.NAV_SELECTION_TRANSFER,
						LocalSelectionTransfer.getTransfer(), true) {

					@Override
					public ISelection getSelection(TransferData transferData) {
						return LocalSelectionTransfer.getTransfer().getSelection();
					}

					@Override
					public void setSelection(ISelection selection) {
						LocalSelectionTransfer.getTransfer().setSelection(selection);
					}
				}));
		/*
		 * Initialize the table of transfer Ids and their drop adapters for pre-defined
		 * ids
		 */
		transferDropTargetTable.put(Integer.valueOf(IDETransferId.RESOURCE_TRANSFER.hashCode()),
				new TransferDropTargetAdapter(
						new TransferAgent(IDETransferId.RESOURCE_TRANSFER, ResourceTransfer.getInstance(), false)));

		transferDropTargetTable.put(Integer.valueOf(IDETransferId.MARKER_TRANSFER.hashCode()),
				new TransferDropTargetAdapter(
						new TransferAgent(IDETransferId.MARKER_TRANSFER, MarkerTransfer.getInstance(), false)));

		transferDropTargetTable.put(Integer.valueOf(IDETransferId.NAV_SELECTION_TRANSFER.hashCode()),
				new SelectionDropAdapter(new TransferAgent(IDETransferId.NAV_SELECTION_TRANSFER,
						LocalSelectionTransfer.getTransfer(), true) {

					@Override
					public ISelection getSelection(TransferData transferData) {
						return LocalSelectionTransfer.getTransfer().getSelection();
					}
				}));
	}

	@Override
	public ITransferDragSourceListener getTransferDragSourceAdapter(String transferId) {
		return transferDragSourceTable.get(Integer.valueOf(Objects.requireNonNull(transferId, "transferId cannot be null").hashCode())); //$NON-NLS-1$
	}

	@Override
	public ITransferDropTargetListener getTransferDropTargetAdapter(String transferId) {
		return transferDropTargetTable.get(Integer.valueOf(Objects.requireNonNull(transferId, "transferId cannot be null").hashCode())); //$NON-NLS-1$
	}

}