/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editpolicies.ContainerHighlightEditPolicy;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.BottomTerminalFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.CircuitFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.LogicColorConstants;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.TerminalFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.TopTerminalFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.InputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Terminal;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
/**
 * Holds a circuit, which is a container capable of 
 * holding other LogicEditParts.
 */
/*
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class CircuitEditPart extends TerminalOwnerShapeEditPart
{
	/**
	 * @param view
	 */
	public CircuitEditPart(View view) {
		super(view);
	}
	
	//install ContainerHighlightEditPolicy to highlight circuit figure
	protected void createDefaultEditPolicies(){
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ContainerHighlightEditPolicy());
	}
	
	/**
	 * Creates a new Circuit Figure and returns it.
	 *
	 * @return  Figure representing the circuit.
	 */
	protected NodeFigure createMainFigure() {
		NodeFigure nf = new CircuitFigure(new Dimension(getMapMode().DPtoLP(100), getMapMode().DPtoLP(100)));
		ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
		nf.setLayoutManager(layout);
		return nf;
	}
	
	/**
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.ITerminalOwnerEditPart#createBoundsMap()
	 */
	public Map createBoundsMap() {
		Map posMap = new HashMap();
		
		IMapMode mm = getMapMode();
		posMap.put( "A", new Point(mm.DPtoLP(11), mm.DPtoLP(-1))); //$NON-NLS-1$
		posMap.put( "B", new Point(mm.DPtoLP(36), mm.DPtoLP(-1))); //$NON-NLS-1$
		posMap.put( "C", new Point(mm.DPtoLP(61), mm.DPtoLP(-1))); //$NON-NLS-1$
		posMap.put( "D", new Point(mm.DPtoLP(86), mm.DPtoLP(-1))); //$NON-NLS-1$
		
		posMap.put( "1", new Point(mm.DPtoLP(11), mm.DPtoLP(100))); //$NON-NLS-1$
		posMap.put( "2", new Point(mm.DPtoLP(36), mm.DPtoLP(100))); //$NON-NLS-1$
		posMap.put( "3", new Point(mm.DPtoLP(61), mm.DPtoLP(100))); //$NON-NLS-1$
		posMap.put( "4", new Point(mm.DPtoLP(86), mm.DPtoLP(100))); //$NON-NLS-1$
		
		return posMap;
	}
	
	/**
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.ITerminalOwnerEditPart#createOwnedTerminalFigure(TerminalEditPart)
	 */
	public NodeFigure createOwnedTerminalFigure(TerminalEditPart terminalEP) {
		Terminal terminal = (Terminal) ViewUtil
			.resolveSemanticElement((View) terminalEP.getModel());
		if (terminal == null) {
			return null;
		}

		NodeFigure theFigure = null;
		int side = PositionConstants.NORTH;
		if (terminal instanceof InputTerminal) {
			theFigure = new TopTerminalFigure(terminal.getId(), 
				new Dimension(getMapMode().DPtoLP(6), getMapMode().DPtoLP(7)));
		} else {
			theFigure = new BottomTerminalFigure(terminal.getId(),
				new Dimension(getMapMode().DPtoLP(6), getMapMode().DPtoLP(7)));
			side = PositionConstants.SOUTH;
		}
		
		terminalEP.setLocator(new TerminalFigure.TerminalLocator(
			getFigure(), new Dimension(getMapMode().DPtoLP(100), getMapMode()
				.DPtoLP(100)), side));		
		return theFigure;
	}
    
    public Object getPreferredValue(EStructuralFeature feature) {
        if (feature == NotationPackage.eINSTANCE.getFillStyle_FillColor()) {
            return FigureUtilities
                .colorToInteger(LogicColorConstants.logicGreen);
        } else if (feature == NotationPackage.eINSTANCE
            .getLineStyle_LineColor()) {
            return FigureUtilities
                .colorToInteger(LogicColorConstants.connectorGreen);
        }
        return super.getPreferredValue(feature);
    }
}
