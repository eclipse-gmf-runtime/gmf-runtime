/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.gef.tools.ToolUtilities;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.DuplicateRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;

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
	 * @see org.eclipse.gef.tools.DragEditPartsTracker#isMove()
	 */
	protected boolean isMove() {
		if (!getOperationSet().contains(getSourceEditPart()))
			return false;
		return super.isMove();
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
    
}
