/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
        MapModeGraphics gMM = createMapModeGraphics(graphics);
    	gMM.pushState();
  	
    	if (getChildren().isEmpty()) {
    		gMM.popState();
    		return;
    	}
    	if (getScale() == 1.0) {
    		super.paintClientArea(gMM);
    	} else {
    		ScaledGraphics g = createScaledGraphics(gMM);
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
	 * @param graphics <code>MapModeGraphics</code> object to wrap with a <code>ScaledGraphics</code>
	 * @return <code>ScaledGraphics</code>
	 */
	protected ScaledGraphics createScaledGraphics(MapModeGraphics gMM) {
		ScaledGraphics g = new ScaledGraphics(gMM);
		return g;
	}

	/**
	 * @param graphics <code>Graphics</code> object to wrap with a <code>MapModeGraphics</code>
	 * @return <code>MapModeGraphics</code>
	 */
	protected MapModeGraphics createMapModeGraphics(Graphics graphics) {
		MapModeGraphics gMM = new MapModeGraphics(graphics);
		return gMM;
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
