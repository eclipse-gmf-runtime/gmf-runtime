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