/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.tools;


import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.tools.ResizeTracker;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.gmf.runtime.diagram.ui.editparts.IRotatableEditPart;
import org.eclipse.gmf.runtime.gef.ui.internal.requests.RotateShapeRequest;

/**
 * Rotate tracker provides support for figure rotations.
 * Creates and passes off RotateShapeRequests to the EditPolicies
 * The request specifies whether the EditPart can be rotated
 * 
 * @author oboyko
 * 
 */
public class RotateTracker extends ResizeTracker {
	
	// We need to remember which of the handles has been used for rotations
	private int direction;

	/**
	 * Constructs a resize tracker that resizes in the specified direction.  The direction is
	 * specified using {@link PositionConstants#NORTH}, {@link PositionConstants#NORTH_EAST},
	 * etc.
	 * 
	 * @param direction the direction of the resize gesture
	 * @param owner of the resize handle which returned this tracker
	 */
	public RotateTracker(GraphicalEditPart owner, int direction) {
		super(owner, direction);
		this.direction = direction;
	}
	
	/*
	 * Determines whether the selected EditPart can be rotated
	 */
	private boolean isRotationRequired() {
		boolean result = true;
		// check if the selected edit parts implement rotatable interface and 
		// if they are check if they are rotatable
		for (int i=0; i<getOperationSet().size() && result; i++) {
			result = getOperationSet().get(i) instanceof IRotatableEditPart ?  
			((IRotatableEditPart) getOperationSet().get(i)).isRotatable() : false; 
		}
		return result;
	}
	
	/**
	 * This method must be overriden to give the RotateShapeRequest information on
	 * whether the rotation is for the EditPart is permited or not   
	 */
	protected void updateSourceRequest() {
		super.updateSourceRequest();
		RotateShapeRequest request = (RotateShapeRequest) getSourceRequest();
		request.setRotate(isRotationRequired());
	}
	
	/**
	 * Creates the new RotateShapeRequest for which the rotation p-ermission is
	 * evaluated later on
	 */
	protected Request createSourceRequest() {
		RotateShapeRequest request;
		request = new RotateShapeRequest(REQ_RESIZE);
		request.setResizeDirection(getResizeDirection());
		return request;
	}
	
	/**
	 * If rotation is not permited for the selected EditParts then by dragging a rotate
	 * handle the rotate tracker becomes resize tracker and the selected figures are being
	 * resized, hence the cursor dispalyed will be an arrow corresponding to the dragging 
	 * direction. Otherwise, the cursor returned is "hand"
	 */
	protected Cursor getDefaultCursor() {
		return isRotationRequired() ? Cursors.HAND : Cursors.getDirectionalCursor(direction);
	}
}

