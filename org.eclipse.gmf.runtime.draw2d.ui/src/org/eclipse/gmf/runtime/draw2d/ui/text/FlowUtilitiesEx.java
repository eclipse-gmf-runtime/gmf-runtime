/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.text;

import org.eclipse.draw2d.TextUtilities;
import org.eclipse.draw2d.text.FlowUtilities;
import org.eclipse.draw2d.text.TextFragmentBox;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;

/**
 * This class extends GEF's <code>FlowUtilities</code> class to provide
 * mapmode support.
 * 
 * @since 2.1
 * @author satif, crevells
 */
public class FlowUtilitiesEx
    extends FlowUtilities {

    /**
     * The mapmode to be used for translating measurement units.
     */
    private IMapMode mapmode;

    private TextUtilities textUtilities;

    /**
     * Creates a new instance.
     * 
     * @param mapmode
     *            mapmode to be used for translating measurement units
     */
    public FlowUtilitiesEx(IMapMode mapmode) {
        super();
        this.mapmode = mapmode;
    }

    public float getAverageCharWidth(TextFragmentBox fragment, Font font) {
        return mapmode.DPtoLP((int) super.getAverageCharWidth(fragment, font));
    }

    protected Rectangle getTextLayoutBounds(String s, Font f, int start, int end) {
        Rectangle rect = super.getTextLayoutBounds(s, f, start, end);
        return new Rectangle(mapmode.DPtoLP(rect.x), mapmode.DPtoLP(rect.y),
            mapmode.DPtoLP(rect.width), mapmode.DPtoLP(rect.height));
    }

    protected TextUtilities getTextUtilities() {
        if (textUtilities == null) {
            textUtilities = new TextUtilitiesEx(mapmode);
        }
        return textUtilities;
    }

    /**
     * Make public.
     * 
     * @see #setupFragment(TextFragmentBox, Font, String)
     */
    public void setupFragmentEx(TextFragmentBox fragment, Font font,
            String string) {
        super.setupFragment(fragment, font, string);
    }
}
