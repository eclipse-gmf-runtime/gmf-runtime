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