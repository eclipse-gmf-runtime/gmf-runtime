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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.AndGateFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.AndGateTerminalFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.OrGateFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.OrGateTerminalFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.OutputTerminalFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.XOrGateFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.AndGate;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.InputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.NonResizableEditPolicyEx;
import org.eclipse.gmf.runtime.diagram.ui.figures.GateFigure;
import org.eclipse.gmf.runtime.diagram.ui.util.DrawConstant;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
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
	
	/* (non-Javadoc)
	 * @see com.ibm.xtools.presentation.internal.editparts.ShapeEditPart#getPrimaryDragEditPolicy()
	 */
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
		if (eClazz == SemanticPackage.eINSTANCE.getOrGate())	
			newFigure = new OrGateFigure();
		else if (eClazz == SemanticPackage.eINSTANCE.getAndGate())
			newFigure = new AndGateFigure();
		else if (eClazz == SemanticPackage.eINSTANCE.getXORGate())
			newFigure = new XOrGateFigure();
		else
			newFigure = null;
		return newFigure;
	}
	
	/**
	 * Override to prevent change of bounds
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart#refreshBounds()
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
	
	/**
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.ITerminalOwnerEditPart#createBoundsMap()
	 */
	public Map createBoundsMap() {
		Map boundMap = new HashMap();
		if(getModel()==null || !(getModel() instanceof View))
			return boundMap;
		View view = (View)getModel();
		EObject semanticElement = view.getElement();
		EClass eClazz = semanticElement.eClass();
		
		if (eClazz == SemanticPackage.eINSTANCE.getOrGate()) {
			boundMap.put( "A", new Point(MapMode.DPtoLP(4), MapMode.DPtoLP(3))); //$NON-NLS-1$
			boundMap.put( "B", new Point(MapMode.DPtoLP(5), MapMode.DPtoLP(3))); //$NON-NLS-1$
			boundMap.put( "1", new Point(MapMode.DPtoLP(5), MapMode.DPtoLP(9))); //$NON-NLS-1$
		} else if (eClazz == SemanticPackage.eINSTANCE.getAndGate()) {
			boundMap.put( "A", new Point(MapMode.DPtoLP(4), MapMode.DPtoLP(3))); //$NON-NLS-1$
			boundMap.put( "B", new Point(MapMode.DPtoLP(9), MapMode.DPtoLP(3))); //$NON-NLS-1$
			boundMap.put( "1", new Point(MapMode.DPtoLP(6), MapMode.DPtoLP(9))); //$NON-NLS-1$
		} else {
			boundMap.put( "A", new Point(MapMode.DPtoLP(4), MapMode.DPtoLP(3))); //$NON-NLS-1$
			boundMap.put( "B", new Point(MapMode.DPtoLP(5), MapMode.DPtoLP(3))); //$NON-NLS-1$
			boundMap.put( "1", new Point(MapMode.DPtoLP(5), MapMode.DPtoLP(9))); //$NON-NLS-1$
		}
		
		return boundMap;
	}
	
	/**
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.ITerminalOwnerEditPart#createOwnedTerminalFigure(org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal)
	 */
	public NodeFigure createOwnedTerminalFigure(Terminal terminal) {
		GateFigure theFigure = null;
		
		if (terminal instanceof InputTerminal) {
			if (terminal.eContainer() instanceof AndGate) {
				theFigure = new AndGateTerminalFigure(DrawConstant.NORTH, terminal.getId());
			} else {
				theFigure = new OrGateTerminalFigure(DrawConstant.NORTH, terminal.getId());
			}
		}
		else {
			theFigure = new OutputTerminalFigure(DrawConstant.SOUTH, terminal.getId());
		}
		
		return theFigure;
	}
}
