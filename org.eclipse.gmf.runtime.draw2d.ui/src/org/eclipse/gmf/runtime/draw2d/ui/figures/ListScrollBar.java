/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.figures;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.draw2d.ScrollBar;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.DropShadowButtonBorder;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.ImageFigureEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.GCUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.internal.l10n.Draw2dUIPluginImages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;


/**
 * 
 * @author sshaw
 *
 */
public class ListScrollBar extends ScrollBar {

	static private ImageConstants icons = new ImageConstants();
	private Image upIcon, upPressedIcon, upGrayedIcon, downIcon, downPressedIcon, downGrayedIcon;

	private ImageFigureEx upLabel;
	private ImageFigureEx downLabel;

	private static Border dropshadow = new DropShadowButtonBorder();

	/**
	 * Constructor
	 * 
	 * @param orientation int that is a define from <code>Orientable</code>
	 * @param insets the <code>Insets> that represents the white space buffer around the scroll bar in 
	 * logical coordinates.
	 * @param size the <code>Dimension</code> that is the size of the scroll bar end boxes in 
	 * logical coordinates
	 * @param stepInc the <code>int</code> space to jump when incrementing the scroll bar one step in
	 * logical coordinates
	 * @param pageInc the <code>int</code> space to jump when paging the scroll bar up or down in
	 * logical coordinates.
	 */
	public ListScrollBar(int orientation, Insets insets, Dimension size, int stepInc, int pageInc) {
		setOrientation(orientation);
		
		Border margin = new MarginBorder(insets.top, insets.left, insets.bottom, insets.right);
		
		setBorder(margin);
		setPreferredSize(size.width, size.height);
		setStepIncrement(stepInc);
		setPageIncrement(pageInc);

		upIcon = isHorizontal()? icons.left : icons.up;
		upPressedIcon = isHorizontal()? icons.leftPressed : icons.upPressed;
		upGrayedIcon = isHorizontal()? icons.leftGrayed : icons.upGrayed;
		downIcon = isHorizontal()? icons.right : icons.down;
		downPressedIcon = isHorizontal()? icons.rightPressed : icons.downPressed;
		downGrayedIcon = isHorizontal()? icons.rightGrayed : icons.downGrayed;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.ScrollBar#createDefaultDownButton()
	 */
	protected Clickable createDefaultDownButton() {
		downLabel = new ImageFigureEx(downIcon);
		downLabel.setOpaque(true);
		addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				updateDownLabel();
			}
		});
		Clickable button = new Clickable(downLabel);
		button.getModel().addChangeListener(new ChangeListener() {
			public void handleStateChanged(ChangeEvent event) {
				updateDownLabel();
			}
		});

		button.setFiringMethod(Clickable.REPEAT_FIRING);
		button.setRolloverEnabled(true);
		button.setBorder(dropshadow);
		button.setOpaque(false);
		return button;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.ScrollBar#createDefaultUpButton()
	 */
	protected Clickable createDefaultUpButton() {
		upLabel = new ImageFigureEx(upIcon);
		upLabel.setOpaque(true);
		addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				updateUpLabel();
			}
		});
		Clickable button = new Clickable(upLabel);
		button.getModel().addChangeListener(new ChangeListener() {
			public void handleStateChanged(ChangeEvent event) {
				updateUpLabel();
			}
		});

		button.setFiringMethod(Clickable.REPEAT_FIRING);
		button.setRolloverEnabled(true);
		button.setBorder(dropshadow);
		button.setOpaque(false);
		return button;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.ScrollBar#initialize()
	 */
	protected void initialize() {
		super.initialize();
		setPageUp(null);
		setPageDown(null);
		setOpaque(false);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.ScrollBar#createDefaultThumb()
	 */
	protected IFigure createDefaultThumb() {
		Panel thumb = new Panel() {

			public void paint(Graphics graphics) {
				if (GCUtilities.supportsAdvancedGraphics()){
					graphics.setAlpha(128);
				}
				super.paint(graphics);
			}
			
		};
		thumb.setMinimumSize(new Dimension(6, 6));
		thumb.setBackgroundColor(ColorConstants.button);

		thumb.setBorder(new SchemeBorder(SchemeBorder.SCHEMES.RIDGED));
		return thumb;
	}


	private void updateDownLabel() {
		Image icon = null;
		if (((Clickable) getButtonDown()).getModel().isPressed()
			|| !((Clickable) getButtonDown()).getModel().isMouseOver())
			icon = downPressedIcon;
		if (getValue() >= (getMaximum() - getExtent())) {
			icon = downGrayedIcon;
			getButtonDown().setEnabled(false);
		} else {
			getButtonDown().setEnabled(true);
			if (icon == null)
				icon = downIcon;
		}
		downLabel.setImage(icon);
	}

	private void updateUpLabel() {
		Image icon = null;
		if (((Clickable) getButtonUp()).getModel().isPressed()
			|| !((Clickable) getButtonUp()).getModel().isMouseOver())
			icon = upPressedIcon;
		if (getValue() <= getMinimum()) {
			icon = upGrayedIcon;
			getButtonUp().setEnabled(false);
		} else {
			getButtonUp().setEnabled(true);
			if (icon == null)
				icon = upIcon;
		}
		upLabel.setImage(icon);
	}

	/**
	 * @author sshaw
	 *
	 */
	static private class ImageConstants {

		private final Image up, upPressed, upGrayed,
						   down, downPressed, downGrayed,
						   left, leftPressed, leftGrayed,
						   right, rightPressed, rightGrayed;
		/**
		 * 
		 */
		public ImageConstants() {
			RGB[] palette2 = new RGB[] { FigureUtilities.integerToColor(new Integer(SWT.COLOR_LIST_BACKGROUND)).getRGB(), new RGB(0, 0, 0), new RGB(0, 0, 0), new RGB(0, 0, 0) };
			RGB[] palette1 = new RGB[] { FigureUtilities.integerToColor(new Integer(SWT.COLOR_LIST_FOREGROUND)).getRGB(), new RGB(0, 0, 0)};

			ImageData image;

			// normal images
			image = 
				Draw2dUIPluginImages.DESC_DOWN_ARROW.getImageData();
			down = convert(image, palette2);

			image = 
				Draw2dUIPluginImages.DESC_UP_ARROW.getImageData();
			up = convert(image, palette2);

			image = 
				Draw2dUIPluginImages.DESC_LEFT_ARROW.getImageData();
			left = convert(image, palette2);

			image = 
				Draw2dUIPluginImages.DESC_RIGHT_ARROW.getImageData();
			right = convert(image, palette2);

			// pressed images
			upPressed = Draw2dUIPluginImages
				.get(Draw2dUIPluginImages.IMG_UP_PRESSED_ARROW);

			downPressed = Draw2dUIPluginImages
				.get(Draw2dUIPluginImages.IMG_DOWN_PRESSED_ARROW);

			leftPressed = Draw2dUIPluginImages
				.get(Draw2dUIPluginImages.IMG_LEFT_PRESSED_ARROW);

			rightPressed = Draw2dUIPluginImages
				.get(Draw2dUIPluginImages.IMG_RIGHT_PRESSED_ARROW);

			// grayed images
			image = 
				Draw2dUIPluginImages.DESC_UP_GRAY_ARROW.getImageData();
			upGrayed = convert(image, palette1);

			image = 
				Draw2dUIPluginImages.DESC_DOWN_GRAY_ARROW.getImageData();
			downGrayed = convert(image, palette1);

			image = 
				Draw2dUIPluginImages.DESC_LEFT_GRAY_ARROW.getImageData();
			leftGrayed = convert(image, palette1);

			image = 
				Draw2dUIPluginImages.DESC_RIGHT_GRAY_ARROW.getImageData();
			rightGrayed = convert(image, palette1);
			
			palette1 = null;
			palette2 = null;
		}

		private Image convert(ImageData theData, RGB[] colors) {
			//theData.palette = new PaletteData(colors);
			return new Image(Display.getCurrent(), theData);
		}
	}
}
