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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editpolicies.ContainerHighlightEditPolicy;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.BottomTerminalFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.CircuitFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.FigureFactory;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.TerminalFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.TopTerminalFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.InputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal;
import org.eclipse.gmf.runtime.diagram.ui.figures.GateFigure;
import org.eclipse.gmf.runtime.diagram.ui.util.DrawConstant;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
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
		NodeFigure nf =  FigureFactory.createNewCircuit();
		ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
		nf.setLayoutManager(layout);
		return nf;
	}
	
	/**
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.ITerminalOwnerEditPart#createBoundsMap()
	 */
	public Map createBoundsMap() {
		Map posMap = new HashMap();
		
		posMap.put( "A", new Point(MapMode.DPtoLP(11), MapMode.DPtoLP(-1))); //$NON-NLS-1$
		posMap.put( "B", new Point(MapMode.DPtoLP(36), MapMode.DPtoLP(-1))); //$NON-NLS-1$
		posMap.put( "C", new Point(MapMode.DPtoLP(61), MapMode.DPtoLP(-1))); //$NON-NLS-1$
		posMap.put( "D", new Point(MapMode.DPtoLP(86), MapMode.DPtoLP(-1))); //$NON-NLS-1$
		
		posMap.put( "1", new Point(MapMode.DPtoLP(11), MapMode.DPtoLP(100))); //$NON-NLS-1$
		posMap.put( "2", new Point(MapMode.DPtoLP(36), MapMode.DPtoLP(100))); //$NON-NLS-1$
		posMap.put( "3", new Point(MapMode.DPtoLP(61), MapMode.DPtoLP(100))); //$NON-NLS-1$
		posMap.put( "4", new Point(MapMode.DPtoLP(86), MapMode.DPtoLP(100))); //$NON-NLS-1$
		
		return posMap;
	}
	
	/**
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.ITerminalOwnerEditPart#createOwnedTerminalFigure(org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal)
	 */
	public NodeFigure createOwnedTerminalFigure(Terminal terminal) {
		GateFigure theFigure = null;
		if (terminal instanceof InputTerminal) {
			theFigure = new TopTerminalFigure(DrawConstant.NORTH, terminal.getId());
		} else {
			theFigure = new BottomTerminalFigure(DrawConstant.SOUTH, terminal.getId());
		}
		
		theFigure.setLocator(new TerminalFigure.FixedGateLocation(theFigure, getFigure(), CircuitFigure.SIZE));
		return theFigure;
	}
}
