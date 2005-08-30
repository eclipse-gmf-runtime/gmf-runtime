/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.          	       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
 package org.eclipse.gmf.runtime.draw2d.ui.internal.graphics;



import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;

import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;



/**
 * A freeform layer that accepts figures in logical units and paints in device units.
 *
 * @author cmahoney
 */
public class MapModeFreeformLayer
	extends FreeformLayer {

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.IFigure#getClientArea(org.eclipse.draw2d.geometry.Rectangle)
	 */
	public Rectangle getClientArea(Rectangle rect) {
    	super.getClientArea(rect);
    	MapMode.translateToLP(rect);
    	return rect;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.IFigure#translateFromParent(org.eclipse.draw2d.geometry.Translatable)
	 */
	public void translateFromParent(Translatable t) {
    	super.translateFromParent( t );
    	MapMode.translateToLP(t);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.IFigure#translateToParent(org.eclipse.draw2d.geometry.Translatable)
	 */
	public void translateToParent(Translatable t) {
    	super.translateToParent(t);
    	MapMode.translateToDP(t);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paintClientArea(org.eclipse.draw2d.Graphics)
	 */
	protected void paintClientArea(Graphics graphics) {
    	// Create MapMode Graphics Object
        MapModeGraphics gMM = new MapModeGraphics(graphics);
    	gMM.pushState();

        super.paintClientArea(gMM);	
    
        gMM.popState();
	}
}
