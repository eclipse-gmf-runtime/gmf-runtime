/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.internal.graphics;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.swt.SWT;

import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;


/**
 * @author jcorchis / sshaw / jschofie
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.*
 *
 * Override for supporting anti-aliasing lines
 */
public class ScalableFreeformLayeredPane
	extends org.eclipse.draw2d.ScalableFreeformLayeredPane {

	boolean antiAlias = true;
	
	public void setAntiAlias(boolean antiAliasValue) {
		antiAlias = antiAliasValue;
		super.repaint();
	}
	
    /**
    * 
    * @see org.eclipse.draw2d.Figure#paintClientArea(Graphics)
    */
    protected void paintClientArea(Graphics graphics) {
    	// Use Anti-Aliasing
    	if (graphics instanceof SWTGraphics) {
			graphics.setAntialias(SWT.ON);			
		}

    	// Create MapMode Graphics Object
        MapModeGraphics gMM = new MapModeGraphics(graphics);
    	gMM.pushState();
  	
    	if (getChildren().isEmpty()) {
    		gMM.popState();
    		return;
    	}
    	if (getScale() == 1.0) {
    		super.paintClientArea(gMM);
    	} else {
    		ScaledGraphics g = new ScaledGraphics(gMM);
    		boolean optimizeClip = getBorder() == null || getBorder().isOpaque();
    		if (!optimizeClip)
    			g.clipRect(getBounds().getCropped(getInsets()));
    		g.scale(getScale());
    		g.pushState();
    		paintChildren(g);
    		g.dispose();
    		gMM.restoreState();
    	}
    	
    	gMM.popState();
    }

    /**
     * @see org.eclipse.draw2d.Figure#getClientArea()
     */
    public Rectangle getClientArea(Rectangle rect) {
    	
    	super.getClientArea(rect);
    	MapMode.translateToLP(rect);
    	return rect;
    }
    
    /**
     * @see org.eclipse.draw2d.Figure#translateToParent(Translatable)
     */
    public void translateToParent(Translatable t) {
    	super.translateToParent(t);
    	MapMode.translateToDP(t);
    }

    /**
     * @see org.eclipse.draw2d.Figure#translateFromParent(Translatable)
     */
    public void translateFromParent(Translatable t) {
    	super.translateFromParent( t );
    	MapMode.translateToLP(t);
    }

}
