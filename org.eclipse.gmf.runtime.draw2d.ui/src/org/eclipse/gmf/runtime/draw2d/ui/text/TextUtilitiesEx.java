/******************************************************************************
 * Copyright (c) 2007, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.text;

import org.eclipse.draw2d.TextUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Shell;

/**
 * @author satif, crevells
 * @since 2.1
 */
public class TextUtilitiesEx
    extends TextUtilities {

    /**
     * The mapmode to be used for translating measurement units.
     */
    private IMapMode mapmode;

    /**
     * Private graphics context used to measure rendered text metrics    
     */
    private static GC gc;
    
    /**
     * Creates a new instance.
     * @param mapmode mapmode to be used for translating measurement units
     */
    public TextUtilitiesEx(IMapMode mapmode) {
        super();
        this.mapmode = mapmode;
    }

    /**
     *  Gets the ascent, converted by the mapmode
     */
    public int getAscent(Font font) {
        return mapmode.DPtoLP(super.getAscent(font));
    }

    /**
     *  Gets the descent, converted by the mapmode
     */
    public int getDescent(Font font) {
        return mapmode.DPtoLP(super.getDescent(font));
    }

    /**
     *  Gets a string's extents, converted by the mapmode
     */
    public Dimension getStringExtents(String s, Font f) {
    	Dimension extents = new Dimension(getStringDimension(s, f, true));
        applyItalicBugHack(s, f, extents);
        applyMapModeConversion(extents);
        return extents;
    }

    /**
     *  Gets text's extents, converted by the mapmode
     */
    public Dimension getTextExtents(String s, Font f) {
        Dimension extents = new Dimension(getTextDimension(s, f, true));
        applyItalicBugHack(s, f, extents);
        applyMapModeConversion(extents);
        return extents;
    }

    /**
     * Uses an offscreen GC to obtain text's rendered dimensions in pixels
     * 
     * @param s
     * @param f
     * @param advancedGraphics
     * @return
     */
    private static org.eclipse.swt.graphics.Point getTextDimension(String s, Font f, boolean advancedGraphics) {
    	return getGC(f, advancedGraphics).textExtent(s);
    }

    /**
     * Uses an offscreen GC to obtain a string's rendered dimensions in pixels
     * 
     * @param s
     * @param f
     * @param advancedGraphics
     * @return
     */
    private static org.eclipse.swt.graphics.Point getStringDimension(String s, Font f, boolean advancedGraphics) {
    	return getGC(f, advancedGraphics).stringExtent(s);
    }
    
    /**
     * Converts a dimension using mapmode.DPtoLP()
     * 
     * @param extents
     */
    private void applyMapModeConversion(Dimension extents)
    {
    	extents.width = mapmode.DPtoLP(extents.width);
    	extents.height = mapmode.DPtoLP(extents.height);
    }
    
    /**
     * If the font is in italics, this does not always return the correct
     * size and the text can get clipped. See
     * https://bugs.eclipse.org/bugs/show_bug.cgi?id=5190. Compensate for
     * this here until the bugzilla is fixed in SWT by adding an extra half
     * a character space here.
     * 
     * @deprecated
     * @param s the string
     * @param f the font
     * @param dimension the extents to be corrected
     */
    private static void applyItalicBugHack(String s, Font f, Dimension extents)
    {
        if ((f.getFontData()[0].getStyle() & SWT.ITALIC) != 0 &&
        		s.length() > 0) {
        	extents.width += (extents.width / s.length()) / 2;
        }
    }
    
    /**
     * Gets a singleton instance of a private off-screen graphics
     * context for measuring attributes of rendered text 
     * 
     * @param f font setting desired
     * @param advancedGraphics mode desired
     * @return
     */
    private static GC getGC(Font f, boolean advancedGraphics)
    {
    	if(gc == null) {
    		gc = new GC(new Shell());
    	}
    	gc.setFont(f);
    	gc.setAdvanced(advancedGraphics);
    	return gc;
    }
    
}
