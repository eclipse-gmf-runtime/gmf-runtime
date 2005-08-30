/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.render.internal.graphics;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.swt.SWT;

import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;

/**
 * @author jcorchis / sshaw / jschofie
 *
 * Set anti-aliasing
 */
public class RenderedScalableFreeformLayeredPane
	extends org.eclipse.draw2d.ScalableFreeformLayeredPane {
    
	boolean antiAlias = true;
	
	public void setAntiAlias(boolean antiAliasValue) {
		antiAlias = antiAliasValue;
		super.repaint();
	}
	
	
    /* 
     * (non-Javadoc)
     * @see org.eclipse.draw2d.Figure#paintClientArea(org.eclipse.draw2d.Graphics)
     */
		
    protected void paintClientArea(Graphics graphics) {
    	    	
    	// Use Anti-Aliasing
    	if ((graphics instanceof SWTGraphics) && (antiAlias)) {
			graphics.setAntialias(SWT.ON);
		}
    	//RenderedGraphicsAALines gAA = new RenderedGraphicsAALines(graphics, true);

    	// Create MapMode Graphics Object
    	RenderedMapModeGraphics gMM = new RenderedMapModeGraphics(graphics);
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
