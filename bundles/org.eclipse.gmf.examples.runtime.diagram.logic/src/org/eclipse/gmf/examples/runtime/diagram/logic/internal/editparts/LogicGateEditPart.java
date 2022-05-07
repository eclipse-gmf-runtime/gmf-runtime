/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
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

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
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
            terminalEP.setLocator(new GateTerminalLocator(getFigure(), true));
        } else {
            theFigure = new OutputTerminalFigure(terminal.getId(),
                new Dimension(getMapMode().DPtoLP(4), getMapMode().DPtoLP(5)));
            terminalEP.setLocator(new GateTerminalLocator(getFigure(), false));
        }

        return theFigure;
    }
    
    private class GateTerminalLocator extends BorderItemLocator {
        /**
         * @param parentFigure
         * @param isInputTerminal
         */
        public GateTerminalLocator (IFigure parentFigure, boolean isInputTerminal) {
            super(parentFigure);
            if (isInputTerminal)
                setPreferredSideOfParent(PositionConstants.NORTH);
            else
                setPreferredSideOfParent(PositionConstants.SOUTH);
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemLocator#relocate(org.eclipse.draw2d.IFigure)
         */
        public void relocate(IFigure borderItem) {
            Rectangle bounds = getParentFigure().getBounds();
            Point pos = new Point();
            
            if (getPreferredSideOfParent() == PositionConstants.NORTH) {
                int gap = (bounds.width - (2 * borderItem.getBounds().width)) / 3;
                
                pos.x = bounds.x + gap;
                pos.y = bounds.y;
                
                if (conflicts(pos,borderItem))
                    pos.x += borderItem.getBounds().width + gap - 25; //25 is offset.
            }
            else if (getPreferredSideOfParent() == PositionConstants.SOUTH) {
                pos.x = bounds.x + ((bounds.width - borderItem.getBounds().width) / 2);
                pos.y = bounds.y + bounds.height;
            }
            
            borderItem.setBounds(new Rectangle(pos, borderItem.getSize()));
        }

        /**
         * Determine if the the given point conflicts with the position of an
         * existing borderItemFigure.
         * 
         * @param recommendedLocation
         * @return <code>ture</code> or <code>false</code>
         */
        private boolean conflicts(Point recommendedLocation,
                IFigure targetBorderItem) {
            Rectangle recommendedRect = new Rectangle(recommendedLocation,
                targetBorderItem.getSize());
            List borderItems = targetBorderItem.getParent().getChildren();
            ListIterator iterator = borderItems.listIterator();
            while (iterator.hasNext()) {
                IFigure borderItem = (IFigure) iterator.next();
                if (borderItem.isVisible()) {
                    Rectangle rect = borderItem.getBounds().getCopy();
                    if (borderItem != targetBorderItem
                        && rect.intersects(recommendedRect)
                        && !rect.getLocation().equals(targetBorderItem.getParent().getBounds().getLocation())) {
                        return true;
                    }
                }
            }
            return false;
        }
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
