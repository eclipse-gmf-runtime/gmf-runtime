/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.AndGateFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.AndGateTerminalFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.LogicColorConstants;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.OrGateFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.OrGateTerminalFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.OutputTerminalFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.XOrGateFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.AndGate;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.InputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Terminal;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.NonResizableEditPolicyEx;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemLocator;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * EditPart for holding gates in the Logic Example.
 */
/*
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class LogicGateEditPart extends TerminalOwnerShapeEditPart
{	
	/** 
	 * Create an instance.
	 * @param view the editpart's model.
	 */
	public LogicGateEditPart(View view) {
		super(view);
	}
	
	public EditPolicy getPrimaryDragEditPolicy() {
		return new NonResizableEditPolicyEx();
	}
	
	/**
	 * Returns a newly created Figure of this.
	 *
	 * @return A new Figure of this.
	 */
	protected NodeFigure createMainFigure() {
		NodeFigure newFigure;
		if (getModel() == null)
			return null;
		View view = (View)getModel();
		EObject semanticElement = view.getElement();
		EClass eClazz = semanticElement.eClass();
		Dimension gateSize = new Dimension(getMapMode().DPtoLP(15), getMapMode().DPtoLP(17));
		if (eClazz == SemanticPackage.eINSTANCE.getOrGate())	
			newFigure = new OrGateFigure(gateSize);
		else if (eClazz == SemanticPackage.eINSTANCE.getAndGate())
			newFigure = new AndGateFigure(gateSize);
		else if (eClazz == SemanticPackage.eINSTANCE.getXORGate())
			newFigure = new XOrGateFigure(gateSize);
		else
			newFigure = null;
		return newFigure;
	}
	
	/**
	 * Override to prevent change of bounds
	 */
	protected void refreshBounds() {
		Dimension size = getFigure().getSize();
		int x = ((Integer) getStructuralFeatureValue(NotationPackage.eINSTANCE.getLocation_X())).intValue();
		int y = ((Integer) getStructuralFeatureValue(NotationPackage.eINSTANCE.getLocation_Y())).intValue();
		Point loc = new Point(x, y);
		((GraphicalEditPart) getParent()).setLayoutConstraint(
			this,
			getFigure(),
			new Rectangle(loc, size));
	}
	
	public Map createBoundsMap() {
		Map boundMap = new HashMap();
		if(getModel()==null || !(getModel() instanceof View))
			return boundMap;
		View view = (View)getModel();
		EObject semanticElement = view.getElement();
		EClass eClazz = semanticElement.eClass();
		
		IMapMode mm = getMapMode();
		
		if (eClazz == SemanticPackage.eINSTANCE.getOrGate()) {
			boundMap.put( "A", new Point(mm.DPtoLP(4), mm.DPtoLP(3))); //$NON-NLS-1$
			boundMap.put( "B", new Point(mm.DPtoLP(5), mm.DPtoLP(3))); //$NON-NLS-1$
			boundMap.put( "1", new Point(mm.DPtoLP(5), mm.DPtoLP(9))); //$NON-NLS-1$
		} else if (eClazz == SemanticPackage.eINSTANCE.getAndGate()) {
			boundMap.put( "A", new Point(mm.DPtoLP(4), mm.DPtoLP(3))); //$NON-NLS-1$
			boundMap.put( "B", new Point(mm.DPtoLP(9), mm.DPtoLP(3))); //$NON-NLS-1$
			boundMap.put( "1", new Point(mm.DPtoLP(6), mm.DPtoLP(9))); //$NON-NLS-1$
		} else {
			boundMap.put( "A", new Point(mm.DPtoLP(4), mm.DPtoLP(3))); //$NON-NLS-1$
			boundMap.put( "B", new Point(mm.DPtoLP(5), mm.DPtoLP(3))); //$NON-NLS-1$
			boundMap.put( "1", new Point(mm.DPtoLP(5), mm.DPtoLP(9))); //$NON-NLS-1$
		}
		
		return boundMap;
	}
	
	public NodeFigure createOwnedTerminalFigure(TerminalEditPart terminalEP) {
		Terminal terminal = (Terminal) ViewUtil
			.resolveSemanticElement((View) terminalEP.getModel());
		if (terminal == null) {
			return null;
		}
		
		NodeFigure theFigure = null;

		int side = PositionConstants.NORTH;
		if (terminal instanceof InputTerminal) {
			if (terminal.eContainer() instanceof AndGate) {
				theFigure = new AndGateTerminalFigure(terminal.getId(),
					new Dimension(getMapMode().DPtoLP(4), getMapMode()
						.DPtoLP(2)));
			} else {
				theFigure = new OrGateTerminalFigure(terminal.getId(),
					new Dimension(getMapMode().DPtoLP(4), getMapMode()
						.DPtoLP(4)));
			}
		} else {
			theFigure = new OutputTerminalFigure(terminal.getId(),
				new Dimension(getMapMode().DPtoLP(4), getMapMode().DPtoLP(5)));
			side = PositionConstants.SOUTH;
		}

		terminalEP.setLocator(new BorderItemLocator(getFigure(),
			side));

		return theFigure;
	}

     public Object getPreferredValue(EStructuralFeature feature) {
        if (feature == NotationPackage.eINSTANCE.getFillStyle_FillColor()) {

            View view = (View) getModel();
            EObject semanticElement = view.getElement();
            EClass eClazz = semanticElement.eClass();

            if (eClazz == SemanticPackage.eINSTANCE.getOrGate()) {
                return FigureUtilities
                    .colorToInteger(LogicColorConstants.andGate);
            } else if (eClazz == SemanticPackage.eINSTANCE.getAndGate()) {
                return FigureUtilities
                    .colorToInteger(LogicColorConstants.orGate);
            } else if (eClazz == SemanticPackage.eINSTANCE.getXORGate()) {
                return FigureUtilities
                    .colorToInteger(LogicColorConstants.xorGate);
            }
        } else if (feature == NotationPackage.eINSTANCE
            .getLineStyle_LineColor()) {
            return FigureUtilities
                .colorToInteger(LogicColorConstants.logicBlack);
        }
        return super.getPreferredValue(feature);
    }
    
    
}
