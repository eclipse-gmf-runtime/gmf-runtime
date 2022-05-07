/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.diagram.ui.properties.sections.appearance;

import org.eclipse.gmf.runtime.diagram.ui.properties.internal.l10n.DiagramUIPropertiesImages;
import org.eclipse.swt.widgets.Shell;

/**
 * The menu-like pop-up widget that allows the user to select a line width.
 * 
 * @author Anthony Hunter
 * @since 2.1
 */
public class LineWidthPopup extends LineStylesPopup {

	private static class LineWidth {
		private int width;

		public LineWidth(int width) {
			this.width = width;
		}

		public int getLineWidth() {
			return width;
		}
	}

	private static final LineWidth LINE_WIDTH_ONE = new LineWidth(1);

	private static final LineWidth LINE_WIDTH_TWO = new LineWidth(2);

	private static final LineWidth LINE_WIDTH_THREE = new LineWidth(3);

	private static final LineWidth LINE_WIDTH_FOUR = new LineWidth(4);

	private static final LineWidth LINE_WIDTH_FIVE = new LineWidth(5);

	/**
	 * Constructor for LineWidthPopup.
	 * 
	 * @param parent
	 *            the parent shell.
	 */
	public LineWidthPopup(Shell parent) {
		super(parent);
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.properties.sections.appearance.LineStylesPopup#initializeImageMap()
	 */
	protected void initializeImageMap() {
		imageMap.put(LINE_WIDTH_ONE, DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_LINE_WIDTH_ONE));
		imageMap.put(LINE_WIDTH_TWO, DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_LINE_WIDTH_TWO));
		imageMap.put(LINE_WIDTH_THREE, DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_LINE_WIDTH_THREE));
		imageMap.put(LINE_WIDTH_FOUR, DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_LINE_WIDTH_FOUR));
		imageMap.put(LINE_WIDTH_FIVE, DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_LINE_WIDTH_FIVE));
	}

	/**
	 * Gets the line width the user selected. Could return -1 as the user may cancel the
	 * gesture.
	 * 
	 * @return the selected line width or -1.
	 */
	public int getSelectedLineWidth() {
		if (getSelectedItem() == null) {
			return -1;
		} else {
			LineWidth selectedLineWidth = (LineWidth)getSelectedItem();
			return selectedLineWidth.getLineWidth();
		}
	}

}
