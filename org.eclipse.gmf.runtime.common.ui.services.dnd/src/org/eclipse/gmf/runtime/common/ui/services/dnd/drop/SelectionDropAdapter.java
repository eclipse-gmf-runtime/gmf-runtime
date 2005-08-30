/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.dnd.drop;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.dnd.DropTargetEvent;

import org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAgent;

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