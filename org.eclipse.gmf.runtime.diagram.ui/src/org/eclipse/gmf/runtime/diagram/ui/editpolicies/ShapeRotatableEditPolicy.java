/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import org.eclipse.gmf.runtime.gef.ui.internal.handles.RotateHandle;
import org.eclipse.gmf.runtime.gef.ui.internal.requests.RotateShapeRequest;
import org.eclipse.gmf.runtime.gef.ui.internal.tools.RotateTracker;

/**
 * A rotatable editpolicy for rotating fork and join itparts
 * It rotates the figure if diagonal handlers are dragged and
 * resizes the figure otherwise as defined by the superclass
 *  
 * @author oboyko
 */
public class ShapeRotatableEditPolicy extends ShapeResizableEditPolicy {
	
	// how much should be the mice moved to rotate the figure
	private final static int DEFAULT_TOLERANCE = 6;

	/*
	 * Create the selection handles for edit parts that have Rotatable Edit Policy
	 * installed on them, i.e. Forks and Joins from Activity and State Machine diagrams
	 *  (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#createSelectionHandles()
	 */
	protected List createSelectionHandles() {
		GraphicalEditPart part = (GraphicalEditPart) getHost();
		List selectionhandles = new ArrayList(9);
		MoveHandle moveHandle = new MoveHandle(part);
		moveHandle.setBorder(null);
		selectionhandles.add(moveHandle);
		selectionhandles.add(super.createHandle(part, PositionConstants.EAST));
		selectionhandles.add(createRotationHandle(part, PositionConstants.SOUTH_EAST));
		selectionhandles.add(super.createHandle(part, PositionConstants.SOUTH));
		selectionhandles.add(createRotationHandle(part, PositionConstants.SOUTH_WEST));
		selectionhandles.add(super.createHandle(part, PositionConstants.WEST));
		selectionhandles.add(createRotationHandle(part, PositionConstants.NORTH_WEST));
		selectionhandles.add(super.createHandle(part, PositionConstants.NORTH));
		selectionhandles.add(createRotationHandle(part, PositionConstants.NORTH_EAST));
		return selectionhandles;
	}
	
	/**
	 * Create rotate handle with a rotate tracker
	 * @param owner the owner edit part
	 * @param direction the handle direction
	 * @return the handle
	 */
	protected Handle createRotationHandle(GraphicalEditPart owner, int direction) {
		RotateHandle handle = new RotateHandle(owner, direction);
		handle.setDragTracker(
			new RotateTracker(owner, direction));
		return handle;
	}
	
	/**
	 * Shows or updates feedback for a change bounds request that is seen as rotation
	 * @param request the request
	*/
	protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {
		// If the figure is being rotated draw the rotation feedback
		if ( isRotationRequired(request)) {
			// Get current feedback
			IFigure feedback = getDragSourceFeedbackFigure();
			
			if (doRotation(request)) {
				// Get the absolute coordinates for rotated figure
				PrecisionRectangle rect = getAbsoluteRotatedBounds();
				// Draw the rotated figure in the feedback
				feedback.translateToRelative(rect);
				feedback.setBounds(rect);
			}
			else {
				// Get the absolute coordinates for initial and rotated figure
				PrecisionRectangle initFigure = getAbsoluteInitialBounds();
				// Draw the initial figure in the feedback
				feedback.translateToRelative(initFigure);
				feedback.setBounds(initFigure);
			}
		}
		else {
			// otherwise the figure is being resized
			super.showChangeBoundsFeedback(request);
		}
	}
	
	/*
	 * Returns PrecisionRectangle obtained from the rotation by 90 deg. of an argument rectangle
	 * with respect to it's geometrical centre
	 * @param Rectangle r
	 * @return PrecisionRectangle rect obtained from rotation of r
	 */
	private PrecisionRectangle rotateRectangle(Rectangle r) {
		PrecisionRectangle rect = new PrecisionRectangle(r);
		if (isVertical(r)) {
			rect.setX(rect.preciseX-rect.preciseHeight/2.0+rect.preciseWidth/2.0);
			rect.setY(rect.preciseY+rect.preciseHeight/2.0-rect.preciseWidth/2.0);
		}
		else {
			rect.setX(rect.preciseX+rect.preciseWidth/2.0-rect.preciseHeight/2.0);
			rect.setY(rect.preciseY-rect.preciseWidth/2.0+rect.preciseHeight/2.0);
		}
		transposePrecisionRectangleSize(rect);
		return rect;
	}
	
	/*
	 * Check whether the bar (or figure) is vertical or horizontal
	 * @param Rectangle - the bounds of the figure
	 * @return true if figure is vertical, fasle if figure is horizontal
	 */
	private boolean isVertical(Rectangle r) {
		return r.height>r.width;
	}
	
	/*
	 * Transposes PrecisionRectangle's size
	 * @param PrecisionRectangle
	 * @return PrecisionRectangle with transposed size  
	 */
	private void transposePrecisionRectangleSize(PrecisionRectangle r) {
		double height = r.preciseHeight;
		r.setHeight(r.preciseWidth);
		r.setWidth(height);
	}
	
	/*
	 * Returns if figure must be rotated based on the info in the request, i.e. 
	 * diagonal resize direction and rotatable edit parts are selected.
	 * @param change bounds request
	 * @return true if figure must be rotated
	 */
	private boolean isRotationRequired(ChangeBoundsRequest request) {
		return request instanceof RotateShapeRequest ?  ((RotateShapeRequest) request).shouldRotate() : false; 
	}
	
	/*
	 * Returns the command contribution for the given resize request. By default, the request
	 * is redispatched to the host's parent as a {@link
	 * org.eclipse.gef.RequestConstants#REQ_RESIZE_CHILDREN}.  The parent's editpolicies
	 * determine how to perform the resize based on the layout manager in use.
	 * @param request the resize request
	 * @return the command contribution obtained from the parent
	 * @see org.eclipse.gef.editpolicies.ResizableEditPolicy#getResizeCommand(org.eclipse.gef.requests.ChangeBoundsRequest)
	*/
	protected Command getResizeCommand(ChangeBoundsRequest request) {
		// if the figure needs to be rotated set the command with the proper data
		if (isRotationRequired(request)) {
			ChangeBoundsRequest req = new ChangeBoundsRequest(REQ_RESIZE_CHILDREN);
			req.setEditParts(getHost());

			// fake resizing and movement to resize the figure if mice is moved far enough
			if (doRotation(request)) {
					// Get the absolute coordinates for initial and rotated figure
					PrecisionRectangle rect = getAbsoluteRotatedBounds();
					PrecisionRectangle initFigure = getAbsoluteInitialBounds();
					req.setMoveDelta
						(new Point(rect.preciseX - initFigure.preciseX, rect.preciseY - initFigure.preciseY));
					req.setSizeDelta
						(new Dimension(rect.width - initFigure.width, rect.height - initFigure.height));
				}
				else {
					// otherwise SizeDelta and MoveDelta must be 0s
					req.setSizeDelta(new Dimension());
					req.setMoveDelta(new Point());
				}
			
			req.setLocation(request.getLocation());
			req.setExtendedData(request.getExtendedData());
			req.setResizeDirection(request.getResizeDirection());
			return getHost().getParent().getCommand(req);
		}
		else {
			// otherwise the figure is being resized
			return super.getResizeCommand(request);
		}
	}
	
	/*
	 * Based on the size delta from the request determines whether the EditPart must be rotated or
	 * remain as it is 
	 */
	private boolean doRotation(ChangeBoundsRequest request) {
		return Math.abs(request.getSizeDelta().width) > DEFAULT_TOLERANCE || Math.abs(request.getSizeDelta().height) > DEFAULT_TOLERANCE;
	}
	
	/*
	 * Returns the bounds of the initial figure in the absolute coordinates
	 */
	private PrecisionRectangle getAbsoluteInitialBounds() {
		// store the initial figure
		PrecisionRectangle initFigure = new PrecisionRectangle(getInitialFeedbackBounds().getCopy());
		getHostFigure().translateToAbsolute(initFigure);
		return initFigure;
	}
	
	/*
	 * Returns the bounds of the rotated initial figure with respect to its geometrical centre
	 * in absolute coordinates
	 */
	private PrecisionRectangle getAbsoluteRotatedBounds() {
		// store the rotated figure
		PrecisionRectangle rect = new PrecisionRectangle(rotateRectangle(getInitialFeedbackBounds().getCopy()));
		getHostFigure().translateToAbsolute(rect);
		return rect;
	}
}