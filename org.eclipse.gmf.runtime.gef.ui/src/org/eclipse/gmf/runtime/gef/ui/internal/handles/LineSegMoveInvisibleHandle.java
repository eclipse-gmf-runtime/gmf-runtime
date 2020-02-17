/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal.handles;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.tools.ConnectionBendpointTracker;

import org.eclipse.gmf.runtime.gef.ui.internal.tools.ConnectionBendpointTrackerEx;

/**
 * @author sshaw
 */
public class LineSegMoveInvisibleHandle extends BendpointCreationInvisibleHandle {

	/**
	 * Creates a new BendpointCreationHandle, sets its owner to <code>owner</code>
	 * and its index to <code>index</code>, and sets its locator to a new
	 * {@link org.eclipse.draw2d.MidpointLocator}.
	 */
	public LineSegMoveInvisibleHandle(
		ConnectionEditPart owner,
		int index) {
		super(owner, index);
		
		PointList points = ((Connection)owner.getFigure()).getPoints();
		Point pt1 = points.getPoint(index);
		Point pt2 = points.getPoint(index + 1);
		if (Math.abs(pt1.x - pt2.x) < Math.abs(pt1.y - pt2.y)) {
			setCursor(SharedCursors.SIZEWE);
		} else {
			setCursor(SharedCursors.SIZENS);
		}
	}

	/**
	 * Creates and returns a new {@link ConnectionBendpointTracker}.
	 * @return the new ConnectionBendpointTracker
	 */
	protected DragTracker createDragTracker() {		
		ConnectionBendpointTrackerEx tracker;
		tracker = new ConnectionBendpointTrackerEx(
			(ConnectionEditPart)getOwner(),
			getIndex()) {
			
			/**
			 * Called once the drag has been interpreted.  This is where the real work of the drag is
			 * carried out.  By default, the current command is executed.
			 */
			protected void performDrag() {
				setCurrentCommand(getCommand());
				executeCurrentCommand();
			}
		};
		tracker.setType(RequestConstants.REQ_CREATE_BENDPOINT);
		tracker.setDefaultCursor(getCursor());
		return tracker;
	}

}
