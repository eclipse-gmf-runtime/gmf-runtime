/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.tools;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.gef.tools.ToolUtilities;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GroupEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.ruler.SnapToHelperUtil;
import org.eclipse.gmf.runtime.diagram.ui.requests.DuplicateRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Control;

/**
 * A dervied DragEditPartsTRacker that sends REQ_DRAG instead of REQ_ORPHAN
 * and REQ_DROP instead of REQ_ADD
 * 
 * @author melaasar
 */
public class DragEditPartsTrackerEx extends DragEditPartsTracker {

    /**
	 * @param sourceEditPart
	 */
	public DragEditPartsTrackerEx(EditPart sourceEditPart) {
		super(sourceEditPart);
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#getCommand()
	 */
	protected Command getCommand() {
		if (!isMove()) {
			CompoundCommand command = new CompoundCommand();
			addSourceCommands(false, command); 
			if (getTargetEditPart()!=null){
				command.add(getTargetEditPart().getCommand(getTargetRequest()));
				if (command.canExecute())
					return command;
			} 
			
			
		}
		CompoundCommand command = new CompoundCommand();
		addSourceCommands(true, command);
		return command;
	}

	/**
	 * Collects the move/drag commands from the operation set
	 * @param isMove
	 * @param command
	 */
	protected void addSourceCommands(boolean isMove, CompoundCommand command) {
        Request request = getTargetRequest();

        if (isCloneActive()) {

            // do not use operation set in this case as connections will get
            // filtered out
            List editparts = ToolUtilities
                .getSelectionWithoutDependants(getCurrentViewer());

            DuplicateRequest duplicateRequest = new DuplicateRequest();
            duplicateRequest.setEditParts(editparts);
            duplicateRequest.setExtendedData(request.getExtendedData());
            if (request instanceof ChangeBoundsRequest) {
                Point delta = ((ChangeBoundsRequest) request).getMoveDelta();
                MapModeUtil.getMapMode(
                    ((IGraphicalEditPart) getTargetEditPart()).getFigure())
                    .DPtoLP(delta);

                duplicateRequest.setOffset(delta);
            }
            command.add(getTargetEditPart().getCommand(duplicateRequest));
        } else {
            request.setType(isMove ? REQ_MOVE
                : RequestConstants.REQ_DRAG);
            Iterator iter = getOperationSet().iterator();
            while (iter.hasNext()) {
                EditPart editPart = (EditPart) iter.next();
                command.add(editPart.getCommand(request));
            }
        }

        request.setType(RequestConstants.REQ_DROP);
    }

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#getCommandName()
	 */
	protected String getCommandName() {
		if (!isMove())
			return RequestConstants.REQ_DROP;
		return super.getCommandName();
	}

	/**
	 * If the source is not in the operation set, it is not a move
	 * @see org.eclipse.gef.tool s.DragEditPartsTracker#isMove()
	 */
	protected boolean isMove() {		
		for (int i = 0 ; i < getOperationSet().size(); i++){
			if (getOperationSet().get(i).equals(getSourceEditPart())){
				return super.isMove();
			}
			//additional case for GroupEditPart, check the children
			//this is for snap to geometry			
			if (getOperationSet().get(i) instanceof GroupEditPart){
				GroupEditPart currPart = (GroupEditPart)getOperationSet().get(i);
				for (int j = 0 ; j < currPart.getChildren().size() ; j++){
					if (currPart.getChildren().get(j).equals(getSourceEditPart())){
						return super.isMove();
					}
				}
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#executeCurrentCommand()
	 */
	protected void executeCurrentCommand() {
		super.executeCurrentCommand();
		if (isActive()) {
			if (getOperationSet().size() > 0) {
				if (getOperationSet().get(0) instanceof IGraphicalEditPart) {
					IGraphicalEditPart editpart = (IGraphicalEditPart) getOperationSet()
						.get(0);
					if ((editpart.getFigure() == null)
						|| (editpart.getFigure().getParent() == null)
						|| (editpart.getFigure().getParent().getLayoutManager() == null))
						return;
					editpart.getFigure().getParent().getLayoutManager().layout(
						editpart.getFigure().getParent());
					reveal(editpart);
				}
			}
		}
	}
	
	/**
	 * @param editpart
	 */
	protected void reveal(EditPart editpart){
		editpart.getViewer().reveal(editpart);
	}
   
    protected boolean handleDragInProgress() {
        boolean returnValue = super.handleDragInProgress();
        if (isInState(STATE_DRAG_IN_PROGRESS)
            || isInState(STATE_ACCESSIBLE_DRAG_IN_PROGRESS)) {
            
            // Expose the diagram as the user scrolls in the area handled by the
            // autoexpose helper.
            updateAutoexposeHelper();
        }
        return returnValue;
    }

    protected Cursor calculateCursor() {
        if (isInState(STATE_DRAG_IN_PROGRESS)
            || isInState(STATE_ACCESSIBLE_DRAG_IN_PROGRESS)) {

            // Give some feedback so the user knows the area where autoscrolling
            // will occur.
            if (getAutoexposeHelper() != null) {
                return SharedCursors.HAND;
            } else {

                // Give some feedback so the user knows that they can't drag
                // outside the viewport.
                Control control = getCurrentViewer().getControl();
                if (control instanceof FigureCanvas) {
                    Viewport viewport = ((FigureCanvas) control).getViewport();
                    Rectangle rect = Rectangle.SINGLETON;
                    viewport.getClientArea(rect);
                    viewport.translateToParent(rect);
                    viewport.translateToAbsolute(rect);

                    if (!rect.contains(getLocation())) {
                        return getDisabledCursor();
                    }
                }
            }
        }
        return super.calculateCursor();
    }
    
    protected boolean handleButtonDown(int button) {

        // If the group is selected, and the user clicks on a shape, defer the
        // selection of the shape until the mouse button is released instead of
        // selecting on mouse down because if the user does a drag they will
        // move the entire group and not the shape.
        if (button == 1
            && getSourceEditPart().getParent() instanceof GroupEditPart
            && getSourceEditPart().getParent().getSelected() != EditPart.SELECTED_NONE) {

            stateTransition(STATE_INITIAL, STATE_DRAG);
            return true;
        }

        return super.handleButtonDown(button);
    }

    protected boolean handleDoubleClick(int button) {
        // If the user double-clicks a shape in a group and the shape is not
        // selected, select the shape.
        if (getSourceEditPart().getParent() instanceof GroupEditPart
            && getSourceEditPart().getSelected() == EditPart.SELECTED_NONE) {
            performSelection();
            return true;
        } else {
            return super.handleDoubleClick(button);
        }
    }

    protected void performSelection() {
        super.performSelection();

        // If the new selection is a child of a group, we want to deselect the group.
        if (getSourceEditPart().getParent() instanceof GroupEditPart
            && getSourceEditPart().getParent().getSelected() != EditPart.SELECTED_NONE) {
            getCurrentViewer().deselect(getSourceEditPart().getParent());
        }
    }
    
    /**
     * Overridden to add extended data to the request to restrict snapping to
     * specific directions based on the move delta.
     */
    protected void snapPoint(ChangeBoundsRequest request) {
        Point moveDelta = request.getMoveDelta();
        if (getState() == STATE_ACCESSIBLE_DRAG_IN_PROGRESS) {
            int restrictedDirection = 0;

            if (moveDelta.x > 0) {
                restrictedDirection = restrictedDirection
                    | PositionConstants.EAST;
            } else if (moveDelta.x < 0) {
                restrictedDirection = restrictedDirection
                    | PositionConstants.WEST;
            }

            if (moveDelta.y > 0) {
                restrictedDirection = restrictedDirection
                    | PositionConstants.SOUTH;
            } else if (moveDelta.y < 0) {
                restrictedDirection = restrictedDirection
                    | PositionConstants.NORTH;
            }

            request.getExtendedData().put(SnapToHelperUtil.RESTRICTED_DIRECTIONS,
                restrictedDirection);
        }

        super.snapPoint(request);
    }
    
    protected boolean handleKeyDown(KeyEvent e) {
        if (acceptArrowKey(e)) {
            if (isInState(STATE_INITIAL)) {
                IGraphicalEditPart ep = (IGraphicalEditPart) getSourceEditPart();
                if (ep != null) {
                    Point location = ep.getFigure().getBounds().getCenter();
                    ep.getFigure().translateToAbsolute(location);
                    placeMouseInViewer(location);       
                    getCurrentInput().setMouseLocation(location.x, location.y);
                }
            }
        }
        return super.handleKeyDown(e);
    }

}
