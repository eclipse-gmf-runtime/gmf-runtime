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

import org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAgent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.dnd.DropTargetEvent;

/**
 * Concrete implementation of the transfer drop target adapter for the selection
 * transfer type.
 * 
 * @author Vishy Ramaswamy
 */
public class SelectionDropAdapter
	extends TransferDropTargetAdapter {

	/**
	 * Constructor for SelectionDropAdapter.
	 * 
	 * @param transferAgent
	 *            ITransferAgent to use
	 */
	public SelectionDropAdapter(ITransferAgent transferAgent) {
		super(transferAgent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drop.ITransferDropTargetListener#canSupport(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public boolean canSupport(DropTargetEvent event) {
		/* Check if the transfer data is supported */
		if (canSupportTransferData(event)) {
			/* Get the selection */
			ISelection selection = getTransferAgent().getSelection(
				event.currentDataType);
			return selection != null && !selection.isEmpty()
				&& isSupportingListenerAvailable();
		}

		return false;
	}
}