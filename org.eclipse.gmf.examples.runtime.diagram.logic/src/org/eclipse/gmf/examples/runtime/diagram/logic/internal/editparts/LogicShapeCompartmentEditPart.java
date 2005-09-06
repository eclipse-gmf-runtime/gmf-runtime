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

import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.notation.View;

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
	
	/* 
	 * Overridden to turn off support for drag selection of children.
	 */
	protected boolean supportsDragSelection() {
		return false;
	}
}
