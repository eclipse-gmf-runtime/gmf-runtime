/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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