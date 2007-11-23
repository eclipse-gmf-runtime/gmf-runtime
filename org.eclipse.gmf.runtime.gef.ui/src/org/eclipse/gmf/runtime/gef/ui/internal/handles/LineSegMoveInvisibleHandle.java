/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal.handles;

import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.tools.ConnectionBendpointTracker;

import org.eclipse.gmf.runtime.gef.ui.internal.l10n.Cursors;
import org.eclipse.gmf.runtime.gef.ui.internal.tools.ConnectionBendpointTrackerEx;

/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.gef.ui.*
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
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
		
		setCursor(Cursors.CURSOR_SEG_MOVE);
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
