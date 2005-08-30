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

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swt.graphics.Color;

import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editpolicies.LEDEditPolicy;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.BottomTerminalFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.FigureFactory;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.LEDFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.TerminalFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.TopTerminalFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.InputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.LED;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.NonResizableEditPolicyEx;
import org.eclipse.gmf.runtime.diagram.ui.figures.GateFigure;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.util.DrawConstant;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import org.eclipse.gmf.runtime.emf.core.util.MetaModelUtil;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;

/**
 * qili
 * 
 * Holds the EditPart signifying an LED.
 */
/*
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class LEDEditPart extends TerminalOwnerShapeEditPart
{	
	/**
	 * @param view
	 */
	public LEDEditPart(View view) {
		super(view);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#createDefaultEditPolicies()
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new LEDEditPolicy());
	}
	
	/**
	 * Apart from the usual visual update, it also
	 * updates the numeric contents of the LED.
	 */
	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshValue();
		refreshFontColor();
	}

	/**
	 * Refreshes the LED value property
	 */
	protected void refreshValue(){		
		LED LEDStyle = (LED) ViewUtil.resolveSemanticElement((View)getModel());
		if(LEDStyle != null)
			((LEDFigure)getMainFigure()).setValue(LEDStyle.getValue());
	}
	
	/**
	 * Override to prevent change of bounds
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart#refreshBounds()
	 */
	protected void refreshBounds() {
		Dimension size = getFigure().getSize();
		int x = ((Integer) getPropertyValue(Properties.ID_POSITIONX)).intValue();
		int y = ((Integer) getPropertyValue(Properties.ID_POSITIONY)).intValue();
		Point loc = new Point(x, y);
		((GraphicalEditPart) getParent()).setLayoutConstraint(
			this,
			getFigure(),
			new Rectangle(loc, size));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#setFontColor(org.eclipse.swt.graphics.Color)
	 */
	protected void setFontColor(Color color) {
		((LEDFigure)getMainFigure()).setFontColor(color);
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#handlePropertyChangeEvent(java.beans.PropertyChangeEvent)
	 */
	protected void handlePropertyChangeEvent(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(MetaModelUtil.getID(SemanticPackage.eINSTANCE.getLED_Value())))
			refreshValue();
		else if (evt.getPropertyName().equals(Properties.ID_FONTCOLOR)) {
			refreshFontColor();
		}
		else
			super.handlePropertyChangeEvent(evt);
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.xtools.presentation.internal.editparts.ShapeEditPart#getPrimaryDragEditPolicy()
	 */
	public EditPolicy getPrimaryDragEditPolicy() {
		return new NonResizableEditPolicyEx();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GatedShapeEditPart#createMainFigure()
	 */
	protected NodeFigure createMainFigure() {
		return FigureFactory.createNewLED();
	}
	
	/**
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.ITerminalOwnerEditPart#createBoundsMap()
	 */
	public Map createBoundsMap() {
		Map boundMap = new HashMap();
		
		boundMap.put( "A", new Point(MapMode.DPtoLP(51), MapMode.DPtoLP(1))); //$NON-NLS-1$
		boundMap.put( "B", new Point(MapMode.DPtoLP(36), MapMode.DPtoLP(1))); //$NON-NLS-1$
		boundMap.put( "C", new Point(MapMode.DPtoLP(21), MapMode.DPtoLP(1))); //$NON-NLS-1$
		boundMap.put( "D", new Point(MapMode.DPtoLP(6), MapMode.DPtoLP(1))); //$NON-NLS-1$
		boundMap.put( "1", new Point(MapMode.DPtoLP(51), MapMode.DPtoLP(44))); //$NON-NLS-1$
		boundMap.put( "2", new Point(MapMode.DPtoLP(36), MapMode.DPtoLP(44))); //$NON-NLS-1$
		boundMap.put( "3", new Point(MapMode.DPtoLP(21), MapMode.DPtoLP(44))); //$NON-NLS-1$
		boundMap.put( "4", new Point(MapMode.DPtoLP(6), MapMode.DPtoLP(44))); //$NON-NLS-1$
		
		return boundMap;
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
		
		theFigure.setLocator(new TerminalFigure.FixedGateLocation(theFigure, getFigure(), LEDFigure.SIZE));
		return theFigure;
	}
}
