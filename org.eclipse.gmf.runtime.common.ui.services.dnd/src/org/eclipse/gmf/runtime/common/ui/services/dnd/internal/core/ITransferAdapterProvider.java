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