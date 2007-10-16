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

package org.eclipse.gmf.runtime.diagram.ui.label;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.swt.graphics.Image;

/**
 * The label delegate for a {@link WrappingLabel}.
 * 
 * @since 2.1
 * @author crevells
 */
public class WrappingLabelDelegate
    extends ILabelDelegate.Stub {

    private WrappingLabel label;

    public WrappingLabelDelegate(WrappingLabel wrappingLabel) {
        this.label = wrappingLabel;
    }

    public void setText(String text) {
        label.setText(text);
    }
    
    public String getText() {
        return label.getText();
    }

    public void setIcon(Image image, int index) {
        label.setIcon(image, index);
    }
    
    public Image getIcon(int index) {
        return label.getIcon(index);
    }

    public void setAlignment(int right) {
        label.setAlignment(right);
    }

    public int getAlignment() {
        return label.getAlignment();
    }

    public void setTextStrikeThrough(boolean strikeThrough) {
        label.setTextStrikeThrough(strikeThrough);
    }

    public boolean isTextStrikedThrough() {
        return label.isTextStrikedThrough();
    }

    public void setTextUnderline(boolean underline) {
        label.setTextUnderline(underline);
    }

    public boolean isTextUnderlined() {
        return label.isTextUnderlined();
    }

    public void setTextJustification(int justification) {
        label.setTextJustification(justification);
    }

    public int getTextJustification() {
        return label.getTextJustification();
    }

    public void setFocus(boolean b) {
        label.setFocus(b);
    }

    public boolean hasFocus() {
        return label.hasFocus();
    }

    public void setSelected(boolean b) {
        label.setSelected(b);
    }

    public boolean isSelected() {
        return label.isSelected();
    }

    public void setTextAlignment(int alignment) {
        label.setTextAlignment(alignment);
    }

    public int getTextAlignment() {
        return label.getTextAlignment();
    }

    public void setIconAlignment(int alignment) {
        label.setIconAlignment(alignment);
    }

    public int getIconAlignment() {
        return label.getIconAlignment();
    }

    public void setTextWrapOn(boolean textWrappingOn) {
        label.setTextWrap(textWrappingOn);
    }
    
    public boolean isTextWrapOn() {
        return label.isTextWrapOn();
    }

    public Rectangle getTextBounds() {
        Rectangle rect = label.getTextBounds();
        label.translateToAbsolute(rect);
        return rect;
    }

    public void setTextPlacement(int placement) {
        label.setTextPlacement(placement);
    }

    public int getTextPlacement() {
        return label.getTextPlacement();
    }

}
