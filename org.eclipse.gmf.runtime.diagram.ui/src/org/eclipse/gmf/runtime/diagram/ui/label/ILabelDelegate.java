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

package org.eclipse.gmf.runtime.diagram.ui.label;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

/**
 * The purpose of this interface is to provide a generic way for editparts,
 * editpolicies, text direct edit managers, etc. to deal with label figures
 * (i.e. figures containing text and/or an image). This way, if a client would
 * like to provide their own type of label figure they may not need to override
 * all the pieces that interact with the label figure.
 * <p>
 * The methods in this interface are documented in a way that they were intended
 * to be used; however, if the client is overriding the pieces of GMF that
 * interact with the label (e.g. editpart, editpolicies, text direct edit
 * manager) and creating their own label figure, they are free to use this API
 * as they see fit. For this reason, the supported alignment and placement
 * values are not documented here. These would be determined by the the client's
 * label figure being used.
 * <p>
 * IMPORTANT: This interface is <EM>not</EM> intended to be implemented by
 * clients. Clients should inherit from {@link ILabelDelegate.Stub}. New
 * methods may be added to this interface in the future.
 * </p>
 * 
 * @since 2.1
 * @author crevells
 * 
 */
public interface ILabelDelegate {

    /**
     * Sets the focus state of the label. Implementors may want to react by
     * drawing a focus rectangle around the text in the label.
     * 
     * @param focus
     *            the focus state
     */
    public void setFocus(boolean focus);

    /**
     * Gets the focus state of the label.
     * 
     * @return the focus state of the label
     */
    public boolean hasFocus();

    /**
     * Sets the selected state of the label. Implementors may want to react by
     * drawing a selection rectangle around the text in the label.
     * 
     * @param selected
     *            the selected state
     */
    public void setSelected(boolean selected);

    /**
     * Gets the selected state of the label.
     * 
     * @return the selected state of the label
     */
    public boolean isSelected();

    /**
     * Sets the text in the label. This will be called when the text has changed
     * and the label figure should be updated.
     * 
     * @param text
     *            the new text
     */
    public void setText(String text);

    /**
     * Returns the text of the label. Unless clients require customized
     * behavior, implementors should generally return the complete text of the
     * label, regardless of whether it is currently being truncated.
     * 
     * @return the text in the label
     */
    public String getText();

    /**
     * Sets the label's icon at given index. 
     * 
     * @param image
     *            The icon image or null to remove the icon
     * @param index
     *            The icon index
     */
    public void setIcon(Image image, int index);

    /**
     * Gets the label's icon at given index.
     * 
     * @param index
     *            the icon index
     * @return the image
     */
    public Image getIcon(int index);

    /**
     * Sets whether the label's text should be striked-through
     * 
     * @param strikeThrough
     *            whether the label's text should be striked-through
     */
    public void setTextStrikeThrough(boolean strikeThrough);

    /**
     * Should the label's text should be striked-through?
     * 
     * @return true if the label's text should be striked-through; false
     *         otherwise
     */
    public boolean isTextStrikedThrough();

    /**
     * Sets whether the label's text should be underlined
     * 
     * @param underline
     *            whether the label's text should be underlined
     */
    public void setTextUnderline(boolean underline);

    /**
     * Should the label's text should be underlined?
     * 
     * @return true if the label's text should be underlined; false otherwise
     */
    public boolean isTextUnderlined();

    /**
     * Sets the alignment of the label (icon and text) within the figure. If
     * this figure's bounds are larger than the size needed to display the
     * label, the label will be aligned accordingly.
     * 
     * @param alignment
     *            label alignment
     */
    public void setAlignment(int alignment);

    /**
     * Gets the alignment of the label (icon and text) within the figure.
     * 
     * @return the alignment
     */
    public int getAlignment();

    /**
     * Sets the current placement of the label's text relative to its icon. If
     * the text placement is set to {@link PositionConstants#EAST}, then the
     * text would be placed on the right of the icon. Similarly, if text
     * placement is set to {@link PositionConstants#WEST}, the text will be
     * placed on the left of the icon; {@link PositionConstants#NORTH} would put
     * the text above the icon; and {@link PositionConstants#SOUTH} would place
     * the text below the icon.
     * 
     * @param placement
     *            the text placement relative to the icon
     */
    public void setTextPlacement(int placement);

    /**
     * Gets the placement of the label's text relative to its icon.
     * 
     * @return the placement
     */
    public int getTextPlacement();

    /**
     * Sets the alignment of the label's icon relative to the label's text
     * bounds. This is only relevant if the icon's width or height (depending on
     * the location of the icon relative to the text) is smaller than the text's
     * width or height.
     * 
     * @param alignment
     *            the icon alignment relative to the text bounds
     */
    public void setIconAlignment(int alignment);

    /**
     * Gets the alignment of the label's icon relative to the label's text
     * bounds.
     * 
     * @return the alignment
     */
    public int getIconAlignment();

    /**
     * Sets the alignment of the label's text relative to the label's icon
     * bounds. This is only relevant if the text's width or height (depending on
     * the location of the text relative to the icon) is smaller than the icon's
     * width or height.
     * 
     * @param alignment
     *            the text alignment relative to the icon bounds
     */
    public void setTextAlignment(int alignment);

    /**
     * Gets the alignment of the label's text relative to the label's icon
     * bounds.
     * 
     * @return the alignment
     */
    public int getTextAlignment();

    /**
     * Sets the text justification of the label's text.
     * 
     * @param justification
     *            the text justification
     */
    public void setTextJustification(int justification);

    /**
     * Gets the text justification of the label's text.
     * 
     * @return the text justification
     */
    public int getTextJustification();

    /**
     * Returns true if the label's text wrapping feature is turned on; false
     * otherwise.
     * 
     * @return true if the label's text wrapping feature is turned on; false
     *         otherwise
     */
    public boolean isTextWrapOn();

    /**
     * Turns the label's text wrapping feature on or off.
     * 
     * @param textWrapOn
     *            true if the label's text wrapping feature is to be turned on;
     *            false otherwise
     */
    public void setTextWrapOn(boolean textWrapOn);

    /**
     * Returns the bounds of the label's complete text in absolute coordinates.
     * One use of this method is by the text direct edit manager to determine
     * the size and location of the cell editor popup.
     * 
     * @return the bounds of the label's complete text in absolute coordinates
     */
    public Rectangle getTextBounds();

    /**
     * This is a stub implementation of the <code>ILabelDelegate</code>
     * interface. Clients should subclass this stub to avoid any breakage if API
     * is added to the ILabelDelegate in the future. Clients only need to
     * override methods in this stub that our applicable to their label. For
     * example, if the client's label does not support complicated label
     * alignment then there is no need to override such methods. All methods do
     * nothing by default or return some default value.
     * 
     * @since 2.1
     * @author crevells
     */
    public class Stub
        implements ILabelDelegate {

        public void setFocus(boolean b) {
            // do nothing by default, clients may override if desired
        }

        public boolean hasFocus() {
            return false;
        }

        public void setSelected(boolean b) {
            // do nothing by default, clients may override if desired
        }

        public boolean isSelected() {
            return false;
        }

        public int getTextJustification() {
            return PositionConstants.LEFT;
        }

        public void setTextJustification(int justification) {
            // do nothing by default, clients may override if desired
        }

        public void setIcon(Image image, int index) {
            // do nothing by default, clients may override if desired
        }

        public Image getIcon(int index) {
            return null;
        }

        public String getText() {
            return ""; //$NON-NLS-1$
        }

        public void setText(String text) {
            // do nothing by default, clients may override if desired
        }

        public void setAlignment(int alignment) {
            // do nothing by default, clients may override if desired
        }

        public int getAlignment() {
            return PositionConstants.CENTER;
        }

        public void setTextPlacement(int placement) {
            // do nothing by default, clients may override if desired
        }

        public int getTextPlacement() {
            return PositionConstants.CENTER;
        }

        public void setTextAlignment(int alignment) {
            // do nothing by default, clients may override if desired
        }

        public int getTextAlignment() {
            return PositionConstants.CENTER;
        }

        public void setIconAlignment(int alignment) {
            // do nothing by default, clients may override if desired
        }

        public int getIconAlignment() {
            return PositionConstants.CENTER;
        }

        public void setTextStrikeThrough(boolean strikeThrough) {
            // do nothing by default, clients may override if desired
        }

        public boolean isTextStrikedThrough() {
            return false;
        }

        public void setTextUnderline(boolean underline) {
            // do nothing by default, clients may override if desired
        }

        public boolean isTextUnderlined() {
            return false;
        }

        public void setTextWrapOn(boolean textWrappingOn) {
            // do nothing by default, clients may override if desired
        }

        public boolean isTextWrapOn() {
            return false;
        }

        public Rectangle getTextBounds() {
            return new Rectangle();
        }

    }

}
