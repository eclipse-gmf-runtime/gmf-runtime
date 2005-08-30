/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.tools;

import java.util.Iterator;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.tools.DragEditPartsTracker;

import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;

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
		Iterator iter = getOperationSet().iterator();
		Request request = getTargetRequest();
		request.setType(isMove ? REQ_MOVE : RequestConstants.REQ_DRAG);
		while (iter.hasNext()) {
			EditPart editPart = (EditPart) iter.next();
			command.add(editPart.getCommand(request));
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
