/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.dnd.core;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * Interface to be implemented by providers of new transfer agents for drag and
 * drop operations
 * 
 * @author Vishy Ramaswamy
 */
public interface ITransferAgent {

	/**
	 * Returns the unique id for this transfer agent
	 * 
	 * @return String the transfer id
	 */
	public String getTransferId();

	/**
	 * Returns the transfer agent
	 * 
	 * @return Transfer the transfer
	 */
	public Transfer getTransfer();

	/**
	 * Returns a boolean indicating whether the transfer agent is selection
	 * transfer
	 * 
	 * @return boolean true or false
	 */
	public boolean isSelectionType();

	/**
	 * Set the selection if the transfer is a selection transfer
	 * 
	 * @param selection
	 *            The selection to be set
	 */
	public void setSelection(ISelection selection);

	/**
	 * Return the selection if the transfer is a selection transfer
	 * 
	 * @param transferData
	 *            the platform specific representation of the data to be
	 *            converted
	 * @return ISelection the selection
	 */
	public ISelection getSelection(TransferData transferData);
}