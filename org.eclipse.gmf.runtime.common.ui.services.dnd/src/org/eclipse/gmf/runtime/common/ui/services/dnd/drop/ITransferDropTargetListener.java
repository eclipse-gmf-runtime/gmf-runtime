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

package org.eclipse.gmf.runtime.common.ui.services.dnd.drop;

import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;

import org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAgent;

/**
 * Interface to be implemented by providers of transfer agents that are used by
 * the drop target within drag/drop operations.
 * 
 * @author Vishy Ramaswamy
 */
public interface ITransferDropTargetListener
	extends DropTargetListener {

	/**
	 * Returns the transfer agent associated with this transfer listener.
	 * 
	 * @return ITransferAgent associated with this transfer listener
	 */
	public ITransferAgent getTransferAgent();

	/**
	 * Initializes this transfer drop target listener with the drop target
	 * context that provides the target and location information during the
	 * drag/drop operation.
	 * 
	 * @param context
	 *            the drop target context
	 */
	public void init(IDropTargetContext context);

	/**
	 * Returns whether the drop operation for the current drop event is
	 * supported by this listener. This listener will have access to the
	 * <code>IDropActionContext</code> and the current drop target listener
	 * when this method is invoked.
	 * 
	 * @param event
	 *            The drop target event
	 * @return true if can support, false otherwise
	 */
	public boolean canSupport(DropTargetEvent event);
}