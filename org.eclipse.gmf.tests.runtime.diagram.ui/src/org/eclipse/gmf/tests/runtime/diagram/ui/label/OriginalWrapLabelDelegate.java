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

package org.eclipse.gmf.tests.runtime.diagram.ui.label;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.label.ILabelDelegate;
import org.eclipse.swt.graphics.Image;

/**
 * @since 2.1
 * @author crevells
 * @deprecated
 */
public class OriginalWrapLabelDelegate
    extends ILabelDelegate.Stub {

    private OriginalWrapLabel label;

    public OriginalWrapLabelDelegate(OriginalWrapLabel wrapLabel) {
        this.label = wrapLabel;
    }

    public String getText() {
        return label.getText();
    }

    public void setIcon(Image image) {
        label.setIcon(image);
    }

    public void setIcon(Image image, int index) {
        label.setIcon(image, index);
    }

    public void setAlignment(int right) {
        label.setLabelAlignment(right);
    }

    public void setText(String text) {
        label.setText(text);
    }

    public void setTextStrikeThrough(boolean strikeThrough) {
        label.setTextStrikeThrough(strikeThrough);
    }

    public void setTextUnderline(boolean underline) {
        label.setTextUnderline(underline);
    }

    public int getTextJustification() {
        return label.getTextWrapAlignment();
    }

    public void setTextJustification(int justification) {
        label.setTextWrapAlignment(justification);
    }

    public void setFocus(boolean b) {
        label.setFocus(b);
    }

    public void setSelected(boolean b) {
        label.setSelected(b);
    }

    public void setTextAlignment(int alignment) {
        label.setTextAlignment(alignment);
    }

    public void setIconAlignment(int alignment) {
        label.setIconAlignment(alignment);
    }

    public boolean isTextWrapOn() {
        return label.isTextWrapped();
    }

    public Rectangle getTextBounds() {
        Rectangle rect = label.getTextBounds();
        label.translateToAbsolute(rect);
        return rect;
    }

    public void setTextPlacement(int placement) {
        label.setTextPlacement(placement);
    }

    public void setTextWrapOn(boolean textWrappingOn) {
        label.setTextWrap(textWrappingOn);
    }
    
    public Image getIcon(int index) {
        return label.getIcon(index);
    }

    public int getIconAlignment() {
        return label.getIconAlignment();
    }

    public int getTextAlignment() {
        return label.getTextAlignment();
    }

    public int getTextPlacement() {
        return label.getTextPlacement();
    }

    public boolean hasFocus() {
        return label.hasFocus();
    }

    public boolean isSelected() {
        return label.isSelected();
    }

    public boolean isTextStrikedThrough() {
        return label.isTextStrikedThrough();
    }

    public boolean isTextUnderlined() {
        return label.isTextUnderlined();
    }

    public int getAlignment() {
        return label.getLabelAlignment();
    }
    
}
