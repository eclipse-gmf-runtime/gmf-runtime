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
@Deprecated
public class OriginalWrapLabelDelegate extends ILabelDelegate.Stub {

	private OriginalWrapLabel label;

	public OriginalWrapLabelDelegate(OriginalWrapLabel wrapLabel) {
		this.label = wrapLabel;
	}

	@Override
	public String getText() {
		return label.getText();
	}

	public void setIcon(Image image) {
		label.setIcon(image);
	}

	@Override
	public void setIcon(Image image, int index) {
		label.setIcon(image, index);
	}

	@Override
	public void setAlignment(int right) {
		label.setLabelAlignment(right);
	}

	@Override
	public void setText(String text) {
		label.setText(text);
	}

	@Override
	public void setTextStrikeThrough(boolean strikeThrough) {
		label.setTextStrikeThrough(strikeThrough);
	}

	@Override
	public void setTextUnderline(boolean underline) {
		label.setTextUnderline(underline);
	}

	@Override
	public int getTextJustification() {
		return label.getTextWrapAlignment();
	}

	@Override
	public void setTextJustification(int justification) {
		label.setTextWrapAlignment(justification);
	}

	@Override
	public void setFocus(boolean b) {
		label.setFocus(b);
	}

	@Override
	public void setSelected(boolean b) {
		label.setSelected(b);
	}

	@Override
	public void setTextAlignment(int alignment) {
		label.setTextAlignment(alignment);
	}

	@Override
	public void setIconAlignment(int alignment) {
		label.setIconAlignment(alignment);
	}

	@Override
	public boolean isTextWrapOn() {
		return label.isTextWrapped();
	}

	@Override
	public Rectangle getTextBounds() {
		Rectangle rect = label.getTextBounds();
		label.translateToAbsolute(rect);
		return rect;
	}

	@Override
	public void setTextPlacement(int placement) {
		label.setTextPlacement(placement);
	}

	@Override
	public void setTextWrapOn(boolean textWrappingOn) {
		label.setTextWrap(textWrappingOn);
	}

	@Override
	public Image getIcon(int index) {
		return label.getIcon(index);
	}

	@Override
	public int getIconAlignment() {
		return label.getIconAlignment();
	}

	@Override
	public int getTextAlignment() {
		return label.getTextAlignment();
	}

	@Override
	public int getTextPlacement() {
		return label.getTextPlacement();
	}

	@Override
	public boolean hasFocus() {
		return label.hasFocus();
	}

	@Override
	public boolean isSelected() {
		return label.isSelected();
	}

	@Override
	public boolean isTextStrikedThrough() {
		return label.isTextStrikedThrough();
	}

	@Override
	public boolean isTextUnderlined() {
		return label.isTextUnderlined();
	}

	@Override
	public int getAlignment() {
		return label.getLabelAlignment();
	}

}
