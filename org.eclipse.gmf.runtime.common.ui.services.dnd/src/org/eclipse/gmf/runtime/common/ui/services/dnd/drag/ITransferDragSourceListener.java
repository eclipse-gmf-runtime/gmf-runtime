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

package org.eclipse.gmf.runtime.common.ui.services.dnd.drag;

import org.eclipse.swt.dnd.DragSourceListener;

import org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAgent;

/**
 * Interface to be implemented by providers of transfer agents that are used by
 * the drag source within drag/drop operations.
 * 
 * @author Vishy Ramaswamy
 */
public interface ITransferDragSourceListener
	extends DragSourceListener {

	/**
	 * Returns the transfer agent associated with this transfer listener.
	 * 
	 * @return ITransferAgent associated with this transfer listener
	 */
	public ITransferAgent getTransferAgent();

	/**
	 * Initializes this transfer drag source listener with the drag source
	 * listeners, registered against this transfer agent, and the drag source
	 * context
	 * 
	 * @param listeners
	 *            the drag source listeners
	 * @param context
	 *            the drag source context
	 */
	public void init(IDragSourceListener[] listeners, IDragSourceContext context);
}