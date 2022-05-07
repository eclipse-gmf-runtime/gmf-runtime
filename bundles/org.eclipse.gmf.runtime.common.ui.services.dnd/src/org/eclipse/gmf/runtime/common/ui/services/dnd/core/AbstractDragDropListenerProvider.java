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
import org.eclipse.gmf.runtime.common.ui.services.dnd.drag.IDragSourceListener;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetListener;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core.ListenerOperation;

/**
 * The abstract parent of all concrete drag and drop listener providers. A
 * concrete provider needs to override the <code>getDragSourceListener()</code>
 * and <code>getDropTargetListener</code> methods. The <code>provides</code>
 * method has a default implementation. The <code>provides()</code> method is
 * already handled by the proxy for the provider (
 * <code>DragDropListenerService.ProviderDescriptor</code>). The proxy
 * contains all the information necessary for the <code>provides()</code>
 * method.
 * 
 * @author Vishy Ramaswamy
 */
public abstract class AbstractDragDropListenerProvider
	extends AbstractProvider
	implements IDragDropListenerProvider {

	/**
	 * Constructor for AbstractDragDropListenerProvider.
	 */
	public AbstractDragDropListenerProvider() {
		super();
	}

	/**
	 * Returns <code>true</code> if the provider can handle the
	 * <code>ListenerOperation</code> operation
	 * 
	 * @param operation
	 *            An operation
	 * 
	 * @return Returns <code>true</code> if the provider can handle the
	 *         <code>ListenerOperation</code> operation. Otherwise returns
	 *         <code>false</code>
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public final boolean provides(IOperation operation) {

		if (operation instanceof ListenerOperation) {
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.IDragDropListenerProvider#getDragSourceListeners(org.eclipse.gmf.runtime.common.ui.services.dnd.core.IDragListenerContext)
	 */
	public IDragSourceListener[] getDragSourceListeners(
			IDragListenerContext context) {
		return new IDragSourceListener[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.IDragDropListenerProvider#getDropTargetListeners(org.eclipse.gmf.runtime.common.ui.services.dnd.core.IDropListenerContext)
	 */
	public IDropTargetListener[] getDropTargetListeners(
			IDropListenerContext context) {
		return new IDropTargetListener[0];
	}
}