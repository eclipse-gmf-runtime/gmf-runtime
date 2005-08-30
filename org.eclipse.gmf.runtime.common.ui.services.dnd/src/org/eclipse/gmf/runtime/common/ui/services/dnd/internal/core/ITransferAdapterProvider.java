/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core;

import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drag.ITransferDragSourceListener;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.ITransferDropTargetListener;

/**
 * Interface that needs to be implemented by the client who provides transfer
 * adapters for transfer ids in drag and drop operations
 * 
 * @author Vishy Ramaswamy
 */
public interface ITransferAdapterProvider
	extends IProvider {

	/**
	 * Returns a <code>ITransferDragSourceListener</code> for the given
	 * transfer id
	 * 
	 * @param transferId
	 *            the transfer id
	 * @return Returns a <code>ITransferDragSourceListener</code>
	 */
	public ITransferDragSourceListener getTransferDragSourceAdapter(
			String transferId);

	/**
	 * Returns a <code>ITransferDropTargetListener</code> for the given
	 * transfer id
	 * 
	 * @param transferId
	 *            the transfer id
	 * @return Returns a <code>ITransferDropTargetListener</code>
	 */
	public ITransferDropTargetListener getTransferDropTargetAdapter(
			String transferId);
}