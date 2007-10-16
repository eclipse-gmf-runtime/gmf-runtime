/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
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
     * Creates a new instance.
     * @param mapmode mapmode to be used for translating measurement units
     */
    public TextUtilitiesEx(IMapMode mapmode) {
        super();
        this.mapmode = mapmode;
    }

    public int getAscent(Font font) {
        int ascent = super.getAscent(font);
        return mapmode.DPtoLP(ascent);
    }

    public int getDescent(Font font) {
        int descent = super.getDescent(font);
        return mapmode.DPtoLP(descent);
    }

    public Dimension getStringExtents(String s, Font f) {
        Dimension dimension = super.getStringExtents(s, f);

        // If the font is in italics, this does not always return the correct
        // size and the text can get clipped. See
        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=5190. Compensate for
        // this here until the bugzilla is fixed in SWT by adding an extra half
        // a character space here.
        if ((f.getFontData()[0].getStyle() & SWT.ITALIC) != 0) {
            dimension.width += (dimension.width / s.length()) / 2;
        }

        return new Dimension(mapmode.DPtoLP(dimension.width), mapmode
            .DPtoLP(dimension.height));
    }

    public Dimension getTextExtents(String s, Font f) {
        Dimension dimension = super.getTextExtents(s, f);

        // If the font is in italics, this does not always return the correct
        // size and the text can get clipped. See
        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=5190. Compensate for
        // this here until the bugzilla is fixed in SWT by adding an extra half
        // a character space here.
        if ((f.getFontData()[0].getStyle() & SWT.ITALIC) != 0 && s.length() > 0) {
            dimension.width += (dimension.width / s.length()) / 2;
        }

        return new Dimension(mapmode.DPtoLP(dimension.width), mapmode
            .DPtoLP(dimension.height));
    }

}
