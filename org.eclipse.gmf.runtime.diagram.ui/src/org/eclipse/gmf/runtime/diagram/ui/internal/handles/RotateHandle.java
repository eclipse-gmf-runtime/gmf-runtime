/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.handles;

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Locator;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.ResizeHandle;
import org.eclipse.gef.handles.SquareHandle;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.gmf.runtime.diagram.ui.internal.tools.RotateTracker;


/**
 * Rotate handle that provides support for figure rotations.
 * Looks exactly like resize handle, the only difference the cursor is "hand"
 * 
 * @author oboyko
 */
public class RotateHandle
	extends ResizeHandle {
	
	// direction/position where on the figure the handle is placed
	private int direction;
	
	/**
	 * Creates a new RotateHandle for the given GraphicalEditPart.
	 * <code>direction</code> is the relative direction from the 
	 * center of the owner figure.  For example, <code>SOUTH_EAST</code>
	 * would place the handle in the lower-right corner of its
	 * owner figure.  These direction constants can be found in
	 * {@link org.eclipse.draw2d.PositionConstants}.
	 * 
	 * @param owner the <code>GraphicalEditPart</code> that references the RotateHandle
	 * @param direction relative direction from the center of the owner figure
	 */
	public RotateHandle(GraphicalEditPart owner, int direction) {
		super(owner,direction);
		this.direction = direction; 
		setCursor(Cursors.HAND);
	}

	/**
	 * Creates a new ResizeHandle for the given GraphicalEditPart.
	 * 
	 * @see SquareHandle#SquareHandle(GraphicalEditPart, Locator, Cursor)
	 */
	public RotateHandle(GraphicalEditPart owner, Locator loc, Cursor c) {
		super(owner, loc, c);
	}

	/**
	 * Returns <code>null</code> for the DragTracker.
	 * 
	 * @return returns <code>null</code>
	*/
	protected DragTracker createDragTracker() {
		return new RotateTracker(getOwner(), direction);
	}


}

