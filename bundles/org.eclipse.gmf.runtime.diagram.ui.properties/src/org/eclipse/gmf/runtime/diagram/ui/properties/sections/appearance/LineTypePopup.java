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
import org.eclipse.gmf.runtime.notation.LineType;
import org.eclipse.swt.widgets.Shell;

/**
 * The menu-like pop-up widget that allows the user to select a line type.
 * 
 * @author Anthony Hunter
 * @since 2.1
 */
public class LineTypePopup extends LineStylesPopup {

	/**
	 * Constructor for LineTypePopup.
	 * 
	 * @param parent
	 *            the parent shell.
	 */
	public LineTypePopup(Shell parent) {
		super(parent);
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.properties.sections.appearance.LineStylesPopup#initializeImageMap()
	 */
	protected void initializeImageMap() {
		imageMap.put(LineType.SOLID_LITERAL, DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_LINE_SOLID));
		imageMap.put(LineType.DASH_LITERAL, DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_LINE_DASH));
		imageMap.put(LineType.DOT_LITERAL, DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_LINE_DOT));
		imageMap.put(LineType.DASH_DOT_LITERAL, DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_LINE_DASH_DOT));
		imageMap.put(LineType.DASH_DOT_DOT_LITERAL, DiagramUIPropertiesImages
				.get(DiagramUIPropertiesImages.IMG_LINE_DASH_DOT_DOT));
	}

	/**
	 * Gets the line type the user selected. Could return null as the user may
	 * cancel the gesture.
	 * 
	 * @return the selected line type or null.
	 */
	public LineType getSelectedLineType() {
		if (getSelectedItem() == null) {
			return null;
		} else {
			LineType selectedLineType = (LineType) getSelectedItem();
			return selectedLineType;
		}
	}

}
