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

package org.eclipse.gmf.runtime.draw2d.ui.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.TextUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.draw2d.ui.text.TextUtilitiesEx;
import org.eclipse.swt.graphics.Image;

/**
 * GEF's Label extended for use in GMF with mapping mode support and the
 * following additional features:
 * <li>Provides selection and focus feedback.
 * <li>Provides support for underlined and striked-through text.
 * 
 * @author crevells
 * @since 2.1
 * 
 */
public class LabelEx
    extends Label {

    // reserve 1 bit
    private static int FLAG_SELECTED = Figure.MAX_FLAG << 1;

    private static int FLAG_HASFOCUS = Figure.MAX_FLAG << 2;

    private static int FLAG_UNDERLINED = Figure.MAX_FLAG << 3;

    private static int FLAG_STRIKEDTHROUGH = Figure.MAX_FLAG << 4;

    /**
     * The largest flag defined in this class. If subclasses define flags, they
     * should declare them as larger than this value and redefine MAX_FLAG to be
     * their largest flag value.
     * 
     * @see Figure#MAX_FLAG
     */
    @SuppressWarnings("hiding") 
    protected static final int MAX_FLAG = FLAG_STRIKEDTHROUGH;

    /**
     * cached mapmode used for measurement conversions
     */
    private IMapMode mapmode;
    
    private TextUtilities textUtilities;

    /**
     * Construct an empty Label.
     * 
     * @since 2.1
     */
    public LabelEx() {
        super();
    }

    /**
     * Construct a Label with passed String as its text.
     * 
     * @param s
     *            the label text
     * @since 2.1
     */
    public LabelEx(String s) {
        super(s);
    }

    /**
     * Construct a Label with passed Image as its icon.
     * 
     * @param i
     *            the label image
     * @since 2.1
     */
    public LabelEx(Image i) {
        super(i);
    }

    /**
     * Construct a Label with passed String as text and passed Image as its
     * icon.
     * 
     * @param s
     *            the label text
     * @param i
     *            the label image
     * @since 2.1
     */
    public LabelEx(String s, Image i) {
        super(s, i);
    }

    /**
     * Sets whether the label text should be striked-through
     * 
     * @param b
     *            Whether the label text should be striked-through
     */
    public void setTextStrikeThrough(boolean strikeThrough) {
        if (isTextStrikedThrough() == strikeThrough)
            return;
        setFlag(FLAG_STRIKEDTHROUGH, strikeThrough);
        repaint();
    }

    /**
     * @return whether the label text is striked-through
     */
    public boolean isTextStrikedThrough() {
        return (flags & FLAG_STRIKEDTHROUGH) != 0;
    }

    /**
     * Sets whether the label text should be underlined
     * 
     * @param b
     *            Whether the label text should be underlined
     */
    public void setTextUnderline(boolean underline) {
        if (isTextUnderlined() == underline)
            return;
        setFlag(FLAG_UNDERLINED, underline);
        repaint();
    }

    /**
     * @return whether the label text is underlined
     */
    public boolean isTextUnderlined() {
        return (flags & FLAG_UNDERLINED) != 0;
    }

    /**
     * Sets the selection state of this label
     * 
     * @param b
     *            true will cause the label to appear selected
     */
    public void setSelected(boolean b) {
        if (isSelected() == b)
            return;
        setFlag(FLAG_SELECTED, b);
        repaint();
    }

    /**
     * @return the selection state of this label
     */
    public boolean isSelected() {
        return (flags & FLAG_SELECTED) != 0;
    }

    /**
     * Sets the focus state of this label
     * 
     * @param b
     *            true will cause a focus rectangle to be drawn around the text
     *            of the Label
     */
    public void setFocus(boolean b) {
        if (hasFocus() == b)
            return;
        setFlag(FLAG_HASFOCUS, b);
        repaint();
    }

    /**
     * @return the focus state of this label
     */
    public boolean hasFocus() {
        return (flags & FLAG_HASFOCUS) != 0;
    }

    protected void paintFigure(Graphics graphics) {
        paintSelectionRectangle(graphics);
        paintFocusRectangle(graphics);

        super.paintFigure(graphics);

        Rectangle textBounds = getTextBounds();
        if (isTextUnderlined()) {
            int y = textBounds.getBottom().y + getMapMode().DPtoLP(1);

            graphics.drawLine(textBounds.x, y, textBounds.getRight().x, y);
        }
        if (isTextStrikedThrough()) {
            int y = textBounds.getCenter().y;
            graphics.drawLine(textBounds.x, y, textBounds.getRight().x, y);
        }
    }

    private void paintSelectionRectangle(Graphics g) {
        if (isSelected()) {
            g.pushState();
            g.setBackgroundColor(ColorConstants.menuBackgroundSelected);
            g.fillRectangle(getSelectionRectangle());
            g.popState();
            g.setForegroundColor(ColorConstants.white);
        }
    }

    private void paintFocusRectangle(Graphics g) {
        if (hasFocus()) {
            g.pushState();
            g.setXORMode(true);
            g.setForegroundColor(ColorConstants.menuBackgroundSelected);
            g.setBackgroundColor(ColorConstants.white);
            g.drawFocus(getSelectionRectangle().resize(-1, -1));
            g.popState();
        }
    }

    private Rectangle getSelectionRectangle() {
        // TODO
        Rectangle figBounds = getTextBounds();
        int expansion = getMapMode().DPtoLP(2);
        figBounds.resize(expansion, expansion);
        translateToParent(figBounds);
        figBounds.intersect(getBounds());
        return figBounds;

    }

    protected Dimension getIconSize() {
        Dimension iconSize = super.getIconSize();
        return new Dimension(getMapMode().DPtoLP(iconSize.width), getMapMode()
            .DPtoLP(iconSize.height));
    }

    public int getIconTextGap() {
        int gap = super.getIconTextGap();
        return getMapMode().DPtoLP(gap);
    }

    /**
     * Gets the mapmode to be used in pixel to logical unit conversions.
     * 
     * @return the mapmode
     */
    private IMapMode getMapMode() {
        if (mapmode == null) {
            mapmode = MapModeUtil.getMapMode(this);
        }
        return mapmode;
    }
    
    public TextUtilities getTextUtilities() {
        if (textUtilities == null) {
            textUtilities = new TextUtilitiesEx(getMapMode());
        }
        return textUtilities;
    }

}
