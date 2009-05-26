/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal.handles;

import org.eclipse.draw2d.BendpointLocator;
import org.eclipse.draw2d.Locator;

import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.handles.BendpointMoveHandle;
import org.eclipse.gmf.runtime.gef.ui.internal.tools.ConnectionBendpointTrackerEx;

/**
 * A BendpointHandle that is used to move an existing bendpoint.
 * 
 * @author carson_li
 */
public class BendpointMoveHandleEx extends BendpointMoveHandle {

	/**
	 * Creates a new BendpointMoveHandle, sets its owner to <code>owner</code>
	 * and its index to <code>index</code>, and sets its locator to a new
	 * {@link BendpointLocator}.
	 * 
	 * @param owner
	 *            the ConnectionEditPart owner
	 * @param index
	 *            the index
	 */
	public BendpointMoveHandleEx(ConnectionEditPart owner, int index) {
		super(owner, index);
	}

	/**
	 * Creates a new BendpointMoveHandle, sets its owner to <code>owner</code>
	 * and its index to <code>index</code>, and sets its locator to a new
	 * {@link BendpointLocator} with the given <code>locatorIndex</code>.
	 * 
	 * @param owner
	 *            the ConnectionEditPart owner
	 * @param index
	 *            the index
	 * @param locatorIndex
	 *            the index to use for the locator
	 */
	public BendpointMoveHandleEx(ConnectionEditPart owner, int index,
			int locatorIndex) {
		super(owner, index, locatorIndex);
	}

	/**
	 * Creates a new BendpointMoveHandle and sets its owner to
	 * <code>owner</code>, sets its index to <code>index</code>, and sets
	 * its locator to <code>locator</code>.
	 * 
	 * @param owner
	 *            the ConnectionEditPart owner
	 * @param index
	 *            the index
	 * @param locator
	 *            the Locator
	 */
	public BendpointMoveHandleEx(ConnectionEditPart owner, int index,
			Locator locator) {
		super(owner, index, locator);
	}

	/**
	 * Creates and returns a new {@link ConnectionBendpointTrackerEx}.
	 * 
	 * @return the new ConnectionBendpointTrackerEx
	 */
	protected DragTracker createDragTracker() {
		ConnectionBendpointTrackerEx tracker;
		tracker = new ConnectionBendpointTrackerEx(
				(ConnectionEditPart) getOwner(), getIndex());
		tracker.setType(RequestConstants.REQ_MOVE_BENDPOINT);
		tracker.setDefaultCursor(getCursor());
		return tracker;
	}

}
