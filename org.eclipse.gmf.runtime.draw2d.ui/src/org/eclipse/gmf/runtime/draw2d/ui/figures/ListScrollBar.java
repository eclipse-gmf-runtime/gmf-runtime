/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.figures;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ScrollBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.DropShadowButtonBorder;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.ImageFigureEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.l10n.Draw2dResourceManager;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;


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

	private static Border margin = new MarginBorder(MapMode.DPtoLP(1), MapMode.DPtoLP(2),
		MapMode.DPtoLP(1), MapMode.DPtoLP(0));

	/**
	 * @param orientation
	 */
	public ListScrollBar(int orientation) {
		setOrientation(orientation);
		setBorder(margin);
		setPreferredSize(MapMode.DPtoLP(15), MapMode.DPtoLP(15));
		setStepIncrement(MapMode.DPtoLP(10));
		setPageIncrement(MapMode.DPtoLP(50));

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
		setThumb(null);
		setOpaque(false);
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
			image = Draw2dResourceManager.getInstance().getImageDescriptor(
				Draw2dResourceManager.DOWN_ARROW_IMAGE).getImageData();
			down = convert(image, palette2);

			image = Draw2dResourceManager.getInstance().getImageDescriptor(
				Draw2dResourceManager.UP_ARROW_IMAGE).getImageData();
			up = convert(image, palette2);

			image = Draw2dResourceManager.getInstance().getImageDescriptor(
				Draw2dResourceManager.LEFT_ARROW_IMAGE).getImageData();
			left = convert(image, palette2);

			image = Draw2dResourceManager.getInstance().getImageDescriptor(
				Draw2dResourceManager.RIGHT_ARROW_IMAGE).getImageData();
			right = convert(image, palette2);

			// pressed images
			upPressed = Draw2dResourceManager.getInstance().getImage(
				Draw2dResourceManager.UP_PRESSED_ARROW_IMAGE);

			downPressed = Draw2dResourceManager.getInstance().getImage(
				Draw2dResourceManager.DOWN_PRESSED_ARROW_IMAGE);

			leftPressed = Draw2dResourceManager.getInstance().getImage(
				Draw2dResourceManager.LEFT_PRESSED_ARROW_IMAGE);

			rightPressed = Draw2dResourceManager.getInstance().getImage(
				Draw2dResourceManager.RIGHT_PRESSED_ARROW_IMAGE);

			// grayed images
			image = Draw2dResourceManager.getInstance().getImageDescriptor(
				Draw2dResourceManager.UP_GRAY_ARROW_IMAGE).getImageData();
			upGrayed = convert(image, palette1);

			image = Draw2dResourceManager.getInstance().getImageDescriptor(
				Draw2dResourceManager.DOWN_GRAY_ARROW_IMAGE).getImageData();
			downGrayed = convert(image, palette1);

			image = Draw2dResourceManager.getInstance().getImageDescriptor(
				Draw2dResourceManager.LEFT_GRAY_ARROW_IMAGE).getImageData();
			leftGrayed = convert(image, palette1);

			image = Draw2dResourceManager.getInstance().getImageDescriptor(
				Draw2dResourceManager.RIGHT_GRAY_ARROW_IMAGE).getImageData();
			rightGrayed = convert(image, palette1);
			
			palette1 = null;
			palette2 = null;
		}

		private Image convert(ImageData theData, RGB[] colors) {
			theData.palette = new PaletteData(colors);
			return new Image(Display.getCurrent(), theData);
		}
	}
}
