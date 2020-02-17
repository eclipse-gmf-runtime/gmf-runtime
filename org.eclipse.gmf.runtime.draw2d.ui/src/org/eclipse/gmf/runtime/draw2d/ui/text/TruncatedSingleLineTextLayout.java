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

import java.util.List;

import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.draw2d.text.TextFragmentBox;
import org.eclipse.swt.graphics.Font;

/**
 * A single-line text layout (i.e. no word wrapping), that truncates the text if
 * it does not all fit in the width available.
 * 
 * @author satif, crevells
 * @since 2.1
 * 
 */
public class TruncatedSingleLineTextLayout
    extends org.eclipse.draw2d.text.TextLayout {

    private String truncationString;

    /**
     * Creates a new SimpleTextLayout with the given TextFlow
     * 
     * @param flow
     *            the TextFlow
     * @param truncationString
     *            the string to be used to show truncation (normally "...")
     */
    public TruncatedSingleLineTextLayout(TextFlowEx flow,
            String truncationString) {
        super(flow);
        this.truncationString = truncationString;
    }

    /**
     * Gets the string to be used to show truncation (normally "...").
     * 
     * @return the truncation string
     */
    protected String getTruncationString() {
        return truncationString;
    }

    private FlowUtilitiesEx getFlowUtilities() {
        return ((TextFlowEx) getFlowFigure()).getFlowUtilities();
    }

    private TextUtilitiesEx getTextUtilities() {
        return ((TextFlowEx) getFlowFigure()).getTextUtilities();
    }

    protected void layout() {
        TextFlow textFlow = (TextFlow) getFlowFigure();
        String text = textFlow.getText();
        List fragments = textFlow.getFragments();
        Font font = textFlow.getFont();

        int ellipsisWidth = getTextUtilities().getTextExtents(truncationString,
            font).width;

        TextFragmentBox fragment = getFragment(0, fragments);
        fragment.length = text.length();
        fragment.offset = 0;
        fragment.setWidth(-1);
        fragment.setTruncated(false);
        getFlowUtilities().setupFragmentEx(fragment, font, text);

        int remainingLineWidth = getContext().getRemainingLineWidth();

        // if the text should be truncated...
        if (remainingLineWidth > 0 && remainingLineWidth < fragment.getWidth()) {

            remainingLineWidth -= ellipsisWidth;
            if (remainingLineWidth > 0) {
                int subStringLength = getTextUtilities()
                    .getLargestSubstringConfinedTo(text, font,
                        remainingLineWidth);
                fragment.length = subStringLength;
            } else {
                fragment.length = 0;
            }
            fragment.setTruncated(true);
            fragment.setWidth(-1);
            getFlowUtilities().setupFragmentEx(fragment, font, text);
        } else {
            fragment.setTruncated(false);
        }

        getContext().addToCurrentLine(fragment);
        getContext().endLine();

        // Remove the remaining unused fragments.
        int i = 1;
        while (i < fragments.size())
            fragments.remove(i++);
    }

}
