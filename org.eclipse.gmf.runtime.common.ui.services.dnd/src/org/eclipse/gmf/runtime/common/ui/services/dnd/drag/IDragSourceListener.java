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

/**
 * Interface to be implemented by providers to handle drag source events
 * 
 * @author Vishy Ramaswamy
 */
public interface IDragSourceListener
	extends DragSourceListener {

	/**
	 * Determines if the selection in the context is draggable. This method is
	 * invoked when the drag starts
	 * 
	 * @param context
	 *            The context associated with the drag source
	 * @return true if the drag allowed and false otherwise.
	 */
	public boolean isDraggable(IDragSourceContext context);

	/**
	 * Returns the supporting transfer agent ids.
	 * 
	 * @return return the supporting transfer agent ids
	 */
	public String[] getSupportingTransferIds();
}