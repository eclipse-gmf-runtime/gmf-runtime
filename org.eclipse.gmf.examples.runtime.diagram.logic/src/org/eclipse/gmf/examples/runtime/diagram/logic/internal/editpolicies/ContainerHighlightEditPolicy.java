/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editpolicies;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.LogicColorConstants;
import org.eclipse.swt.graphics.Color;


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
