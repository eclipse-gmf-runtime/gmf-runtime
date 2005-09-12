/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;

import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GateEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.GateFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A Connection Point can sit on a border of a logic shape or on the
 * interior.
 * 
 * @author qili
 */
public class TerminalEditPart extends GateEditPart {
	
	/**
	 * @param view
	 */
	public TerminalEditPart(View view) {
		super(view);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart#createNodeFigure()
	 */
	protected NodeFigure createNodeFigure() {
		
		Terminal terminal = (Terminal)ViewUtil.resolveSemanticElement((View)getModel());
		EditPart host = getParent();
		if (host instanceof ITerminalOwnerEditPart && terminal != null) {
			return ((ITerminalOwnerEditPart)host).createOwnedTerminalFigure(terminal);
		}
		
		return null;
	}

	/**
	 * Return null indicating that this shape cannot be moved.
	 * 
	 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker(Request request) {
		return null;
	}
	
	/**
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate() {
		super.activate();
		Insets parentInset = new Insets(0);
		IFigure fig =  ((GateFigure)getFigure()).getBoundaryFigure();
		if( fig != null ){
			parentInset = fig.getInsets();
		}
		Rectangle rBounds = ((NodeFigure) getFigure()).getHandleBounds();
		((GateFigure) getFigure()).setGateOffset(new Dimension(rBounds.width
			/ 2 + parentInset.getWidth() / 2, rBounds.height / 2 + parentInset.getHeight() / 2));
	}
}