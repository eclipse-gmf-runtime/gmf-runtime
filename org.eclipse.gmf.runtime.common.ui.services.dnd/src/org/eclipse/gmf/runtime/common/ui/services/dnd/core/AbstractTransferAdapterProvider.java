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

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drag.ITransferDragSourceListener;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.ITransferDropTargetListener;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core.GetTransferAdapterOperation;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core.ITransferAdapterProvider;

/**
 * The abstract parent of all concrete transfer adapter providers. A concrete
 * provider needs to override the <code>getTransferDragSourceAdapter()</code>
 * and <code>getTransferDropTargetAdapter</code> methods. The
 * <code>provides</code> method has a default implementation. The
 * <code>provides()</code> method is already handled by the proxy for the
 * provider (<code>TransferAdapterService.ProviderDescriptor</code>). The
 * proxy contains all the information necessary for the <code>provides()</code>
 * method.
 * 
 * @author Vishy Ramaswamy
 */
public class AbstractTransferAdapterProvider
	extends AbstractProvider
	implements ITransferAdapterProvider {

	/**
	 * Constructor for AbstractTransferAdapterProvider.
	 */
	public AbstractTransferAdapterProvider() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAdapterProvider#getTransferDragSourceAdapter(java.lang.String)
	 */
	public ITransferDragSourceListener getTransferDragSourceAdapter(
			String transferId) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAdapterProvider#getTransferDropTargetAdapter(java.lang.String)
	 */
	public ITransferDropTargetListener getTransferDropTargetAdapter(
			String transferId) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public final boolean provides(IOperation operation) {
		if (operation instanceof GetTransferAdapterOperation) {
			return true;
		}

		return false;
	}
}