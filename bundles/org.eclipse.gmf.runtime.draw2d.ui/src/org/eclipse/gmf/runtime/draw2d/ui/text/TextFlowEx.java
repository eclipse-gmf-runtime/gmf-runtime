/******************************************************************************
 * Copyright (c) 2007, 2010 IBM Corporation and others.
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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.draw2d.text.TextFragmentBox;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 * A <code>TextFlow</code> with the following additional capabilities: <br>
 * <UL>
 * <LI>text can be underlined or have a strike-through
 * <LI>truncated with ... when the full text doesn't fit vertically
 * </UL>
 * 
 * @since 2.1
 * @author satif, crevells
 * 
 */
public class TextFlowEx
    extends TextFlow {

    // reserve 1 bit
    private static int FLAG_UNDERLINED = Figure.MAX_FLAG << 1;

    private static int FLAG_STRIKEDTHROUGH = Figure.MAX_FLAG << 2;

    /**
     * The highest reserved flag used by this class.
     * 
     * @see Figure#MAX_FLAG
     */
    protected static final int MAX_FLAG = FLAG_STRIKEDTHROUGH;

    private String truncationString = "..."; //$NON-NLS-1$

    /** the FlowUtilities instance that is dependent on the mapmode */
    private FlowUtilitiesEx flowUtilities;

    /** the TextUtilities instance that is dependent on the mapmode */
    private TextUtilitiesEx textUtilities;

    private int nDPtoLP_1 = -1;

    /**
     * Constructs a new TextFlow with the empty String.
     */
    public TextFlowEx() {
        super();
    }

    /**
     * Constructs a new TextFlow with the specified String.
     * 
     * @param text
     *            the string
     */
    public TextFlowEx(String text) {
        super(text);
    }

    /**
     * Gets the truncation string. The default is an ellipsis "...".
     * 
     * @return the truncation string
     */
    protected String getTruncationString() {
        return truncationString;
    }

    /**
     * Sets the truncation string. The default is an ellipsis "...".
     * 
     * @param truncationString
     *            the new truncation string
     */
    public void setTruncationString(String truncationString) {
        this.truncationString = truncationString;
    }

    /**
     * @return whether the label text is striked-through
     */
    public boolean isTextStrikedThrough() {
        return (flags & FLAG_STRIKEDTHROUGH) != 0;
    }

    /**
     * Sets whether the label text should be striked-through
     * 
     * @param strikeThrough
     *            Whether the label text should be striked-through
     */
    public void setTextStrikeThrough(boolean strikeThrough) {
        if (isTextStrikedThrough() == strikeThrough)
            return;
        setFlag(FLAG_STRIKEDTHROUGH, strikeThrough);
        repaint();
    }

    /**
     * @return whether the label text is underlined
     */
    public boolean isTextUnderlined() {
        return (flags & FLAG_UNDERLINED) != 0;
    }

    /**
     * Sets whether the label text should be underlined
     * 
     * @param underline
     *            Whether the label text should be underlined
     */
    public void setTextUnderline(boolean underline) {
        if (isTextUnderlined() == underline)
            return;
        setFlag(FLAG_UNDERLINED, underline);
        repaint();
    }

    protected void paintFigure(Graphics g) {
        TextFragmentBox frag;
        g.getClip(Rectangle.SINGLETON);
        int yStart = Rectangle.SINGLETON.y;
        int yEnd = Rectangle.SINGLETON.bottom();
        Rectangle maxBounds = getVisibleBounds();

        for (int i = 0; i < getFragments().size(); i++) {
            frag = (TextFragmentBox) getFragments().get(i);
            if (frag.offset == -1)
                continue;
            // Loop until first visible fragment
            if (yStart > getVisibleBottom(frag) + 1)// The + 1
                // is for
                // disabled
                // text
                continue;
            // Break loop at first non-visible fragment
            if (yEnd < getVisibleTop(frag))
                break;

            String draw = getBidiSubstring(frag, i);

            // ///////////////////////////////////////////
            // If the next fragment will not be completely visible, then
            // truncate this fragment.
            boolean truncate = frag.isTruncated();
            if (i + 1 < getFragments().size()
                && maxBounds.bottom() < getVisibleBottom((TextFragmentBox) getFragments()
                    .get(i + 1))) {

                draw = truncateText(draw);
                truncate = true;

                // increment the counter so no further fragments will be
                // processed
                i = getFragments().size();
            }

            if (truncate)
                draw += getTruncationString();
            // ///////////////////////////////////////////

            if (!isEnabled()) {
                Color cachedfgColor = g.getForegroundColor();
                g.setForegroundColor(ColorConstants.buttonLightest);
                paintText(g, draw, frag.getX() + getDPtoLP1(), frag
                    .getBaseline()
                    - frag.getAscent() + getDPtoLP1(), frag.getBidiLevel());
                g.setForegroundColor(ColorConstants.buttonDarker);
                paintText(g, draw, frag.getX(), frag.getBaseline()
                    - frag.getAscent(), frag.getBidiLevel());
                g.setForegroundColor(cachedfgColor);
            } else {
                paintText(g, draw, frag.getX(), frag.getBaseline()
                    - frag.getAscent(), frag.getBidiLevel());
            }

            drawTextAdornments(g, frag);
        }
    }

    private int getDPtoLP1() {
        if (nDPtoLP_1 == -1) {
            nDPtoLP_1 = MapModeUtil.getMapMode(this).DPtoLP(1);
        }
        return nDPtoLP_1;
    }

    private void drawTextAdornments(Graphics g, TextFragmentBox fragment) {
        int baseline = fragment.getBaseline();
        if (isTextUnderlined()) {
            baseline += getDPtoLP1();

            g.drawLine(fragment.getX(), baseline, fragment.getWidth()
                + fragment.getX(), baseline);
        }
        if (isTextStrikedThrough()) {
            int y = fragment.getBaseline()
                - fragment.getAscent()
                + ((fragment.getAscent() + fragment.getDescent() + getDPtoLP1()) / 2);
            g.drawLine(fragment.getX(), y, fragment.getWidth()
                + fragment.getX(), y);
        }
    }

    /**
     * Gets the y-value representing the top of the visible text.
     * 
     * @param fragment
     *            the text fragment
     * @return the top value
     */
    private int getVisibleTop(TextFragmentBox fragment) {
        return fragment.getBaseline() - fragment.getAscent();
    }

    /**
     * Gets the y-value representing the bottom of the visible text.
     * 
     * @param fragment
     *            the text fragment
     * @return the bottom value
     */
    private int getVisibleBottom(TextFragmentBox fragment) {
        return fragment.getBaseline() + fragment.getDescent();
    }

    /**
     * Adds an ellipsis to the text passed in if this will fit in the width of
     * this figure, otherwise first truncates the text as required and then adds
     * the ellipsis.
     * 
     * @param text
     *            the full text
     * @return a new string with the text ending in an ellipsis
     */
    protected String truncateText(String text) {
        int maxWidth = getVisibleBounds().width;
        Font currentFont = getFont();

        int ellipsisWidth = getTextUtilities().getTextExtents(
            getTruncationString(), currentFont).width;

        if (maxWidth < ellipsisWidth) {
            maxWidth = ellipsisWidth;
        }

        int subStringLength = getTextUtilities().getLargestSubstringConfinedTo(
            text, currentFont, maxWidth - ellipsisWidth);

        return new String(text.substring(0, subStringLength));
    }

    public FlowUtilitiesEx getFlowUtilities() {
        if (flowUtilities == null) {
            flowUtilities = new FlowUtilitiesEx(MapModeUtil.getMapMode(this));
        }
        return flowUtilities;
    }

    public TextUtilitiesEx getTextUtilities() {
        if (textUtilities == null) {
            textUtilities = new TextUtilitiesEx(MapModeUtil.getMapMode(this));
        }
        return textUtilities;
    }

    /**
     * Gets the area that will be visible to know where to truncate.
     * 
     * @return the visible bounds
     */
    private Rectangle getVisibleBounds() {
        // Not the best idea to depend on the parent, but it will have to do for
        // now.
        return getParent().getClientArea();
    }

}
