/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editpolicies;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.swt.graphics.Color;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.LogicColorConstants;


/**
 * @author qili
 *
 * To highlight the container when it is selected
 */
public class ContainerHighlightEditPolicy extends GraphicalEditPolicy{
	private Color revertColor;

	public void eraseTargetFeedback(Request request){
		if (revertColor != null){
			setContainerBackground(revertColor);
			revertColor = null;
		}
	}

	private Color getContainerBackground(){
		return getContainerFigure().getBackgroundColor();
	}

	private IFigure getContainerFigure(){
		return ((GraphicalEditPart)getHost()).getFigure();
	}
	
	public EditPart getTargetEditPart(Request request){
		return request.getType().equals(RequestConstants.REQ_SELECTION_HOVER) ?
			getHost() : null;
	}

	private void setContainerBackground(Color c){
		getContainerFigure().setBackgroundColor(c);
	}
	
	protected void showHighlight(){
		if (revertColor == null){
			revertColor = getContainerBackground();
			setContainerBackground(LogicColorConstants.logicBackgroundBlue);
		}
	}
	
	//Highlight the background when the following requests are created
	public void showTargetFeedback(Request request){
		if(request.getType().equals(RequestConstants.REQ_MOVE) ||
			request.getType().equals(RequestConstants.REQ_ADD) ||
			request.getType().equals(RequestConstants.REQ_CLONE) ||
			request.getType().equals(RequestConstants.REQ_CONNECTION_START) ||
			request.getType().equals(RequestConstants.REQ_CONNECTION_END) ||
			request.getType().equals(RequestConstants.REQ_CREATE)
		)
			showHighlight();
	}
}
