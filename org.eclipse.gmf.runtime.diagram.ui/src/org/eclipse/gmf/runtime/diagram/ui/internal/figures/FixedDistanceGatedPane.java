/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.internal.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;

import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableFixedLocator;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;


/**
 * Transitionary class.  We will eventually move GatedPaneFigures to this class.
 * 
 * @author jbruck
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
// TODO: Hide this class .. make private class of GatedPaneFigure
public class FixedDistanceGatedPane extends NodeFigure {
	
	// TODO: should be Inset for variable size
	private Dimension gateOffsets = null;   
	/**
	 * Constructor
	 */
	public FixedDistanceGatedPane() {
		super();
		setOpaque(false);
		//setBorder(new LineBorder(ColorConstants.blue));
	}
		
	
	// For now return the height of the lagest child.
	// TODO: eventually return insets.
	private Dimension getGateOffsets()	{
		Dimension gp = new Dimension(0,0);
		if( getChildren().size() > 0 )	{
			IFigure target = (IFigure)getChildren().get(0);
			ResizableFixedLocator currentConstraint = (ResizableFixedLocator)(getLayoutManager().getConstraint(target));
			if (currentConstraint != null && target.isVisible()) {
				Dimension cc = currentConstraint.getSize().getCopy();
				gp = new Dimension( Math.max(gp.width,cc.width), Math.max(gp.height,cc.height));
			}
		}
		return gp;
	}
	
	
	public Dimension getOffsets()	{
		if( gateOffsets == null ) {
			gateOffsets = getGateOffsets();
		}
		return gateOffsets;
	}
	
	/**
	 * @see org.eclipse.draw2d.IFigure#invalidate()
	 */
	public void invalidate() {
		super.invalidate();
		gateOffsets = null;
	}
	
	
}
