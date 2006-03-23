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
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editpolicies.LEDEditPolicy;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.BottomTerminalFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.LEDFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.TerminalFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.TopTerminalFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.InputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.LED;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Terminal;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.NonResizableEditPolicyEx;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Color;

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
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#setFontColor(org.eclipse.swt.graphics.Color)
	 */
	protected void setFontColor(Color color) {
		((LEDFigure)getMainFigure()).setFontColor(color);
	}
	
	protected void handleNotificationEvent(Notification evt) {
		if (SemanticPackage.eINSTANCE.getLED_Value().equals(evt.getFeature()))
			refreshValue();
		else if (NotationPackage.eINSTANCE.getFontStyle_FontColor().equals(evt.getFeature())) {
			refreshFontColor();
		}
		else
			super.handleNotificationEvent(evt);
	}
	
	public EditPolicy getPrimaryDragEditPolicy() {
		return new NonResizableEditPolicyEx();
	}
	
	final private Dimension ledSizeDP = new Dimension(61, 44);
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.AbstractBorderedShapeEditPart#createMainFigure()
	 */
	protected NodeFigure createMainFigure() {
		Dimension ledSizeLP = new Dimension(ledSizeDP);
		getMapMode().DPtoLP(ledSizeLP);
		return new LEDFigure(ledSizeLP);
	}
	
	public Map createBoundsMap() {
		Map boundMap = new HashMap();
		
		IMapMode mm = getMapMode();
		boundMap.put( "A", new Point(mm.DPtoLP(51), mm.DPtoLP(1))); //$NON-NLS-1$
		boundMap.put( "B", new Point(mm.DPtoLP(36), mm.DPtoLP(1))); //$NON-NLS-1$
		boundMap.put( "C", new Point(mm.DPtoLP(21), mm.DPtoLP(1))); //$NON-NLS-1$
		boundMap.put( "D", new Point(mm.DPtoLP(6), mm.DPtoLP(1))); //$NON-NLS-1$
		boundMap.put( "1", new Point(mm.DPtoLP(51), mm.DPtoLP(44))); //$NON-NLS-1$
		boundMap.put( "2", new Point(mm.DPtoLP(36), mm.DPtoLP(44))); //$NON-NLS-1$
		boundMap.put( "3", new Point(mm.DPtoLP(21), mm.DPtoLP(44))); //$NON-NLS-1$
		boundMap.put( "4", new Point(mm.DPtoLP(6), mm.DPtoLP(44))); //$NON-NLS-1$
		
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
			theFigure = new TopTerminalFigure(terminal.getId(), 
								new Dimension(getMapMode().DPtoLP(6), getMapMode().DPtoLP(7)));
		} else {
			theFigure = new BottomTerminalFigure(terminal.getId(), 
								new Dimension(getMapMode().DPtoLP(6), getMapMode().DPtoLP(7)));
			side = PositionConstants.SOUTH;
		}
		
		Dimension ledSizeLP = new Dimension(ledSizeDP);
		getMapMode().DPtoLP(ledSizeLP);
		
		terminalEP.setLocator(new TerminalFigure.TerminalLocator(getFigure(), ledSizeLP, side));

		return theFigure;
	}
}
