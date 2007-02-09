/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.internal.graphics;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.draw2d.ui.graphics.GCUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.internal.Draw2dPlugin;
import org.eclipse.gmf.runtime.draw2d.ui.internal.mapmode.IMapModeHolder;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;


/**
 * @author jcorchis / sshaw / jschofie
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.*
 *
 * Override for supporting anti-aliasing lines
 */
public class ScalableFreeformLayeredPane
	extends org.eclipse.draw2d.ScalableFreeformLayeredPane implements IMapModeHolder {

	boolean antiAlias = true;
	private IMapMode mm;
	
	/**
	 * @return <code>IMapMode</code> that is used to map coordinate coordinates
	 * from device to logical.
	 */
	public IMapMode getMapMode() {
		return mm;
	}

	public ScalableFreeformLayeredPane(IMapMode mm) {
		super();
		this.mm = mm;
	}

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
    	if ((graphics instanceof SWTGraphics) && antiAlias && 
    		GCUtilities.supportsAdvancedGraphics()) {
            graphics.setAntialias(SWT.ON);			
		} else if (graphics.getAntialias()==SWT.ON){
			graphics.setAntialias(SWT.OFF);		
        }
        
        
    	// Create MapMode Graphics Object
        MapModeGraphics gMM = createMapModeGraphics(graphics);
    	gMM.pushState();
  	
    	if (getChildren().isEmpty()) {
    		gMM.popState();
    		return;
    	}
    	try {
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
		} catch (SWTException e) {
			Log.error(Draw2dPlugin.getInstance(), IStatus.ERROR,e.getMessage(), e);
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
		MapModeGraphics gMM = new MapModeGraphics(graphics, getMapMode());
		return gMM;
	}

    /**
     * @see org.eclipse.draw2d.Figure#getClientArea()
     */
    public Rectangle getClientArea(Rectangle rect) {
    	
    	super.getClientArea(rect);
    	getMapMode().DPtoLP(rect);
    	return rect;
    }
    
    /**
     * @see org.eclipse.draw2d.Figure#translateToParent(Translatable)
     */
    public void translateToParent(Translatable t) {
    	super.translateToParent(t);
    	getMapMode().LPtoDP(t);
    }

    /**
     * @see org.eclipse.draw2d.Figure#translateFromParent(Translatable)
     */
    public void translateFromParent(Translatable t) {
    	super.translateFromParent( t );
    	getMapMode().DPtoLP(t);
    }

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode#DPtoLP(int)
	 */
	public int DPtoLP(int deviceUnit) {
		return getMapMode().DPtoLP(deviceUnit);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode#DPtoLP(org.eclipse.draw2d.geometry.Translatable)
	 */
	public Translatable DPtoLP(Translatable t) {
		return getMapMode().DPtoLP(t);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode#LPtoDP(int)
	 */
	public int LPtoDP(int logicalUnit) {
		return getMapMode().LPtoDP(logicalUnit);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode#LPtoDP(org.eclipse.draw2d.geometry.Translatable)
	 */
	public Translatable LPtoDP(Translatable t) {
		return getMapMode().LPtoDP(t);
	}

}
