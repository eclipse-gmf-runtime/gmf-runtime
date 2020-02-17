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

package org.eclipse.gmf.runtime.common.ui.services.dnd.core;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * Utility class that contains the transfer id and the associated transfer
 * 
 * @author Vishy Ramasamy
 */
public class TransferAgent
	implements ITransferAgent {

	/**
	 * Attribute for the transfer id
	 */
	private final String transferId;

	/**
	 * Attribute for the transfer
	 */
	private final Transfer transfer;

	/**
	 * Attribute to check if selection type
	 */
	private final boolean isSelectionType;

	/**
	 * Constructor for the transfer agent
	 * 
	 * @param aTransferId
	 *            the unique transfer ID for this transfer agent
	 * @param aTransfer
	 *            instance of a Transfer class that corresponds to the type of
	 *            data to be transferred
	 * @param anIsSelectionType
	 *            true if transfer data a selection type transfer, false if
	 *            transfer data is not a selection type transfer
	 */
	public TransferAgent(String aTransferId, Transfer aTransfer,
			boolean anIsSelectionType) {
		assert null != aTransferId : "aTransferId cannot be null"; //$NON-NLS-1$
		assert null != aTransfer : "aTransfer cannot be null"; //$NON-NLS-1$

		this.transferId = aTransferId;
		this.transfer = aTransfer;
		this.isSelectionType = anIsSelectionType;
	}

	/**
	 * Return the transfer id
	 * 
	 * @return transferId
	 */
	public String getTransferId() {
		return transferId;
	}

	/**
	 * Return the transfer
	 * 
	 * @return transfer
	 */
	public Transfer getTransfer() {
		return transfer;
	}

	/**
	 * Set the selection if the transfer is a selection transfer
	 * 
	 * @param selection
	 *            The selection to be set
	 */
	public void setSelection(ISelection selection) {
		/* method not implemented */
	}

	/**
	 * Return the selection if the transfer is a selection transfer
	 * 
	 * @param transferData
	 *            the platform specific representation of the data to be
	 *            converted
	 * @return ISelection the selection
	 */
	public ISelection getSelection(TransferData transferData) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAgent#isSelectionType()
	 */
	public boolean isSelectionType() {
		return isSelectionType;
	}
}