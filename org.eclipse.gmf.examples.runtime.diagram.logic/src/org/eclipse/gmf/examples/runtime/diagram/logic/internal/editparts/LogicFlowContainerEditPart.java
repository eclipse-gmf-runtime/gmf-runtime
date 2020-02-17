/******************************************************************************
 * Copyright (c) 2004, 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.LogicColorConstants;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.LogicFlowBorder;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.LogicFlowFigure;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.NotationPackage;
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
		LogicFlowFigure logicFlowFigure = new LogicFlowFigure(new Dimension(getMapMode().DPtoLP(100), getMapMode().DPtoLP(100)));
        Dimension borderSize = new Dimension(getMapMode()
                .DPtoLP(20), getMapMode().DPtoLP(18));
        logicFlowFigure.setBorder(new LogicFlowBorder(borderSize));
		ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
		layout.setStretchMinorAxis(true);
		logicFlowFigure.setLayoutManager(layout);
		return logicFlowFigure;
	} 
    
    public Object getPreferredValue(EStructuralFeature feature) {
        if (feature == NotationPackage.eINSTANCE.getFillStyle_FillColor()) {
            return FigureUtilities
                .colorToInteger(LogicColorConstants.logicGreen);
        } else if (feature == NotationPackage.eINSTANCE
            .getLineStyle_LineColor()) {
            return FigureUtilities
                .colorToInteger(LogicColorConstants.logicBlack);
        }
        return super.getPreferredValue(feature);
    }
}