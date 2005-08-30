/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;

import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.gef.ui.tools.DelegatingDragEditPartsTracker;

/**
 * @author qili
 *
 * Holds the EditPart signifying a ShapeCompartmentFigure
 */
public class LogicShapeCompartmentEditPart extends ShapeCompartmentEditPart{
	
	/**
	 * Constructor for LogicShapeCompartmentEditPart.
	 * @param view the view <code>controlled</code> by this editpart.
	 */
	public LogicShapeCompartmentEditPart(View view) {
		super(view);
	}
	
	/**
	 * We want to override the drag tracker to make it easier to move the circuit shape.
	 * The ability to marquee select inside the shape compartment is not important in this
	 * shape.
	 * 
	 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker(Request request) {
		return new DelegatingDragEditPartsTracker(
			this,
			getTopGraphicEditPart());
	}
}
