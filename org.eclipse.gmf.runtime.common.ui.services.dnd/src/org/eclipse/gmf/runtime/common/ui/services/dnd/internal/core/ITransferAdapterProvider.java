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