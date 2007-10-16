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

package org.eclipse.gmf.runtime.draw2d.ui.figures;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Image;

import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.util.StringTokenizer;

/**
 * An extended label that has the following extra features:
 * 
 * 1- It is capable of showing selection and focus feedback (primary or
 * secondary) 2- It is capable of optionally underlining the label's text 3- It
 * is capable of wrapping the label's text at a given width with a given
 * alignment 4- It is capable of supporting multiple label icons (temporary
 * feature)
 * 
 * This class was originally deriving off Draw2d's <code>Label</code> class
 * but with the introduction of the auto-wrapping feature, a copy had to be made
 * overriding was not straightforward. Hopefully, this extended version can be
 * pushed to opensource
 * 
 * <p>
 * Code taken from Eclipse reference bugzilla #98820
 * 
 * @author melaasar, crevells
 * @deprecated This class has been deprecated and may be removed in the future.
 *             Use <code>WrappingLabel</code> instead. This class now extends
 *             from <code>WrappingLabel</code> so the behavior should be the
 *             same. If client code is calling one of the methods on
 *             <code>WrapLabel</code> that no longer exists, evaluate whether
 *             this is necessary or not.
 */
public class WrapLabel
    extends WrappingLabel {

    /** the label's text used in painting after applying required styles */
    private String subStringText;

    /**
     * Construct an empty Label.
     * 
     * @since 2.0
     */
    public WrapLabel() {
        super();
    }

    /**
     * Construct a Label with passed String as its text.
     * 
     * @param s
     *            the label text
     * @since 2.0
     */
    public WrapLabel(String s) {
        super(s);
 
        // Compensate for the fact that the original wraplabel never called the
        // default constructor to set the default layout values.
        setTextWrapAlignment(CENTER);
    }

    /**
     * Construct a Label with passed Image as its icon.
     * 
     * @param i
     *            the label image
     * @since 2.0
     */
    public WrapLabel(Image i) {
        super(i);
        
        // Compensate for the fact that the original wraplabel never called the
        // default constructor to set the default layout values.
        setTextWrapAlignment(CENTER);
    }

    /**
     * Construct a Label with passed String as text and passed Image as its
     * icon.
     * 
     * @param s
     *            the label text
     * @param i
     *            the label image
     * @since 2.0
     */
    public WrapLabel(String s, Image i) {
        super(s, i);
        
        // Compensate for the fact that the original wraplabel never called the
        // default constructor to set the default layout values.
        setTextWrapAlignment(CENTER);
    }

    /**
     * Calculates the size of the Label's text size. The text size calculated
     * takes into consideration if the Label's text is currently truncated. If
     * text size without considering current truncation is desired, use
     * {@link #calculateTextSize(int, int)}.
     * 
     * @return the size of the label's text, taking into account truncation
     * @since 2.0
     * @deprecated If this behavior is required then a request can be made (with
     *             justification) for the WrappingLabel.
     */
    protected Dimension calculateSubStringTextSize() {
        Font f = getFont();
		return getTextExtents(getSubStringText(), f, getFigureMapMode().DPtoLP(FigureUtilities.getFontMetrics(f).getHeight())); 
    }

    /**
     * Calculates and returns the size of the Label's text. Note that this
     * Dimension is calculated using the Label's full text, regardless of
     * whether or not its text is currently truncated. If text size considering
     * current truncation is desired, use {@link #calculateSubStringTextSize()}.
     * 
	 * @param wHint a width hint
	 * @param hHint a height hint
     * @return the size of the label's text, ignoring truncation
     * @since 2.0
     * @deprecated If this behavior is required then a request can be made (with
     *             justification) for the WrappingLabel.
     */
    protected Dimension calculateTextSize(int wHint, int hHint) {
        return getTextFlow().getPreferredSize(wHint, hHint);
    }

    /**
     * Returns the bounds of the Label's icon.
     * 
     * @return the icon's bounds
     * @since 2.0
     * @deprecated The icon location can be retrieved with
     *             {@link #getIconLocation()} and the icon(s) size can be
     *             retrieved with {@link #getTotalIconSize()}.
     */
    public Rectangle getIconBounds() {
        if (hasIcons()) {
            return new Rectangle(getBounds().getLocation().translate(
                getIconLocation()), getTotalIconSize());
        }
        return new Rectangle(0, 0, 0, 0);
    }

    /**
     * Calculates the amount of the Label's current text will fit in the Label,
     * including an elipsis "..." if truncation is required.
     * 
     * @return the substring
     * @since 2.0
     * @deprecated If this behavior is required then a request can be made (with
     *             justification) for the WrappingLabel.
     */
    public String getSubStringText() {
        if (subStringText != null)
            return subStringText;

        String theText = getText();
        int textLen = theText.length();
        if (textLen == 0) {
            return subStringText = "";//$NON-NLS-1$;;
        }
        Dimension size = getSize();
        Dimension shrink = getPreferredSize(size.width, size.height)
            .getDifference(size);
        Dimension effectiveSize = getTextSize().getExpanded(-shrink.width,
            -shrink.height);

        if (effectiveSize.height == 0) {
            return subStringText = "";//$NON-NLS-1$;
        }

        Font f = getFont();
        FontMetrics metrics = FigureUtilities.getFontMetrics(f);
        IMapMode mm = getFigureMapMode();
        int fontHeight = mm.DPtoLP(metrics.getHeight());
        int charAverageWidth = mm.DPtoLP(metrics.getAverageCharWidth());
        int maxLines = (int) (effectiveSize.height / (double) fontHeight);
        if (maxLines == 0) {
            return subStringText = "";//$NON-NLS-1$
        }

        StringBuffer accumlatedText = new StringBuffer();
        StringBuffer remainingText = new StringBuffer(theText);

        int effectiveSizeWidth = effectiveSize.width;
        int widthHint = Math.max(effectiveSizeWidth
            - getTruncationStringSize().width, 0);
        int i = 0, j = 0;
        while (remainingText.length() > 0 && j++ < maxLines) {
            i = getLineWrapPosition(remainingText.toString(), f,
                effectiveSizeWidth, fontHeight);

            if (accumlatedText.length() > 0)
                accumlatedText.append('\n');

            if (i == 0 || (remainingText.length() > i && j == maxLines)) {
                i = getLargestSubstringConfinedTo(remainingText.toString(), f,
                    widthHint, fontHeight, charAverageWidth);
                accumlatedText.append(remainingText.substring(0, i));
                accumlatedText.append(getEllipse());
            } else
                accumlatedText.append(remainingText.substring(0, i));
            remainingText.delete(0, i);
        }
        return subStringText = accumlatedText.toString();
    }

    /**
     * Returns the size of the Label's current text. If the text is currently
     * truncated, the truncated text with its ellipsis is used to calculate the
     * size.
     * 
     * @return the size of this label's text, taking into account truncation
     * @since 2.0 
     * @deprecated If this behavior is required then a request can be made (with
     *             justification) for the WrappingLabel.
     */
    protected Dimension getSubStringTextSize() {
        return calculateSubStringTextSize();
    }

    /**
     * Returns the location of the label's text relative to the label.
     * 
     * @return the text location
     * @since 2.0
     * @deprecated Use <code>getTextBounds().getLocation()</code> instead.
     */
    protected Point getTextLocation() {
        return getTextBounds().getLocation();
    }

    /**
     * Returns the size of the label's complete text. Note that the text used to
     * make this calculation is the label's full text, regardless of whether the
     * label's text is currently being truncated and is displaying an ellipsis.
     * If the size considering current truncation is desired, call
     * {@link #getSubStringTextSize()}.
     * 
     * @param wHint
     *            a width hint
     * @param hHint
     *            a height hint
     * @return the size of this label's complete text
     * @since 2.0
     * @deprecated If this behavior is required then a request can be made (with
     *             justification) for the WrappingLabel.
     */
    protected Dimension getTextSize(int wHint, int hHint) {
        return getTextFlow().getPreferredSize(wHint, hHint);
    }

    /**
     * Gets the text size given the current size as a width hint
     */
    private final Dimension getTextSize() {
        Rectangle r = getBounds();
        return getTextSize(r.width, r.height);
    }

    /**
     * @see IFigure#invalidate()
     */
    public void invalidate() {
        subStringText = null;
        super.invalidate();
    }

    /**
     * Returns <code>true</code> if the label's text is currently truncated
     * and is displaying an ellipsis, <code>false</code> otherwise.
     * 
     * @return <code>true</code> if the label's text is truncated
     * @since 2.0
     * @deprecated If this behavior is required then a request can be made (with
     *             justification) for the WrappingLabel.
     */
    public boolean isTextTruncated() {
        return !getSubStringTextSize().equals(getTextSize());
    }

    /**
     * Return the ellipse string.
     * 
     * @return the <code>String</code> that represents the fact that the text
     *         has been truncated and that more text is available but hidden.
     *         Usually this is represented by "...".
     *         @deprecated Renamed to {@link #getTruncationString()}
     */
    protected String getEllipse() {
        return ELLIPSIS;
    }
    
    protected String getTruncationString() {
        if (getEllipse() != null) {
            return getEllipse();
        }
        return ELLIPSIS;
    }

    /**
     * @return whether the label text wrap is on
     * @deprecated Use {@link #isTextWrapOn()} instead. This method was renamed
     *             because it never indicated if the text was actually wrapped,
     *             but whether text wrapping was turned on in the label.
     */
    public boolean isTextWrapped() {
        return isTextWrapOn();
    }

    /**
     * Sets the wrapping width of the label text. This is only valid if text
     * wrapping is turned on
     * 
     * @param i
     *            The label text wrapping width
     * @deprecated this method was empty and never called
     */
    public void setTextWrapWidth(int i) {
        // do nothing
    }

    /**
     * Sets the wrapping width of the label text. This is only valid if text
     * wrapping is turned on
     * 
     * @param i
     *            The label text wrapping width
     * @deprecated Call {@link #setTextJustification(int)} and
     *             {@link #setAlignment(int)} instead. This method was somewhat
     *             controlling text justification and label alignment, but they
     *             are really two independent settings. Previously,
     *             setTextWrapAlignment(CENTER) would not only center-justifies
     *             the text, but also put the label in the center. Now, you need
     *             to call {@link #setTextJustification(int)} to justify the
     *             text (this only affects text when it is wrapped) and
     *             {@link #setAlignment(int)} to position the text correctly in
     *             the label. If you want the text in the center of the label
     *             than call <code>setAlignment(PositionConstants.CENTER)</code>.
     *             Look at the implementation of this method to see how your
     *             code needs to be migrated.
     */
    public void setTextWrapAlignment(int i) {
        setTextJustification(i);

        // The old WrapLabel's Text Wrap Alignment (i.e. justification) and
        // Label Alignment did not work properly. They worked together
        // previously so we need to compensate for this here.
        switch (i) {
            case LEFT:
                 setAlignment(TOP | LEFT);
                break;
            case CENTER:
                setAlignment(TOP);
                break;
            case RIGHT:
                setAlignment(TOP | RIGHT);
                break;
            default:
                break;
        }
    }

    /**
     * @deprecated This never worked properly anyways. Call
     *             {@link #setAlignment(int)} instead to position the icon and
     *             text within the label.
     */
    public void setLabelAlignment(int alignment) {
        // setLabelAlignment() never worked properly instead the label alignment
        // seemed to be based on the text justification. Therefore, if it was
        // set it will be ignored.
    }

    /**
     * @return the label text wrapping width
     * @deprecated Renamed to {@link #getTextJustification()}
     */
    public int getTextWrapAlignment() {
        return getTextJustification();
    }

    /**
     * returns the position of last character within the supplied text that will
     * fit within the supplied width.
     * 
     * @param s
     *            a text string
     * @param f
     *            font used to draw the text string
     * @param w
     *            width in pixles.
     * @param fontHeight
     *            int <b>mapped already to logical units</b>.
     */
    private int getLineWrapPosition(String s, Font f, int w, int fontHeight) {
        if (getTextExtents(s, f, fontHeight).width <= w) {
            return s.length();
        }
        // create an iterator for line breaking positions
        BreakIterator iter = BreakIterator.getLineInstance();
        iter.setText(s);
        int start = iter.first();
        int end = iter.next();

        // if the first line segment does not fit in the width,
        // determine the position within it where we need to cut
        if (getTextExtents(s.substring(start, end), f, fontHeight).width > w) {
            iter = BreakIterator.getCharacterInstance();
            iter.setText(s);
            start = iter.first();
        }

        // keep iterating as long as width permits
        do
            end = iter.next();
        while (end != BreakIterator.DONE
            && getTextExtents(s.substring(start, end), f, fontHeight).width <= w);
        return (end == BreakIterator.DONE) ? iter.last()
            : iter.previous();
    }

    /**
     * Returns the largest substring of <i>s </i> in Font <i>f </i> that can be
     * confined to the number of pixels in <i>availableWidth <i>.
     * 
     * @param s
     *            the original string
     * @param f
     *            the font
     * @param w
     *            the available width
     * @param fontHeight
     *            int <b>mapped already to logical units</b>.
     * @param charAverageWidth
     *            int <b>mapped already to logical units</b>.
     * @return the largest substring that fits in the given width
     * @since 2.0
     */
    private int getLargestSubstringConfinedTo(String s, Font f, int w,
            int fontHeight, int charAverageWidth) {
        float avg = charAverageWidth;
        int min = 0;
        int max = s.length() + 1;

        // The size of the current guess
        int guess = 0, guessSize = 0;
        while ((max - min) > 1) {
            // Pick a new guess size
            // New guess is the last guess plus the missing width in pixels
            // divided by the average character size in pixels
            guess = guess + (int) ((w - guessSize) / avg);

            if (guess >= max)
                guess = max - 1;
            if (guess <= min)
                guess = min + 1;

            // Measure the current guess
            guessSize = getTextExtents(s.substring(0, guess), f, fontHeight).width;

            if (guessSize < w)
                // We did not use the available width
                min = guess;
            else
                // We exceeded the available width
                max = guess;
        }
        return min;
    }

    /**
     * Gets the tex extent scaled to the mapping mode
     */
    private Dimension getTextExtents(String s, Font f, int fontHeight) {
        if (s.length() == 0) {
            return getMapModeConstants().dimension_nDPtoLP_0;
        } else {
            // height should be set using the font height and the number of
            // lines in the string
            Dimension d = FigureUtilities.getTextExtents(s, f);
            IMapMode mapMode = getFigureMapMode();
            d.width = mapMode.DPtoLP(d.width);
            d.height = fontHeight * new StringTokenizer(s, "\n").countTokens();//$NON-NLS-1$
            return d;
        }
    }

}