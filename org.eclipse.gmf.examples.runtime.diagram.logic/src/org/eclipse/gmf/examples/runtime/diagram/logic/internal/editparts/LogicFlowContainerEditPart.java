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

import org.eclipse.gef.EditPolicy;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.FigureFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author qili
 *
 * Holds the EditPart signifying a LogicFlowFigure
 */
public class LogicFlowContainerEditPart 
	extends ShapeNodeEditPart 
{
	/**
	 * @param view
	 */
	public LogicFlowContainerEditPart(View view) {
		super(view);
	}
	
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicy.NODE_ROLE, null);
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, null);
	}

	/**
	 * Overwrite createNodeFigure() in super class
	 */
	protected NodeFigure createNodeFigure() {
		NodeFigure newFigure =  FigureFactory.createNewFlowContainer();
		ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
		layout.setStretchMinorAxis(true);
		newFigure.setLayoutManager(layout);
		return newFigure;
	} 
}