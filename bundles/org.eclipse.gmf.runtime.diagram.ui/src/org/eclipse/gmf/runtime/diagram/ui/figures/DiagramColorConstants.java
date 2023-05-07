/******************************************************************************
 * Copyright (c) 2002, 2023 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

/**
 * Set of color constants that are commonly used.
 * <p>
 * This interface defines constants only, it is <EM>not</EM> intended to be
 * implemented by clients.
 * </p>
 * 
 * @author sshaw
 */
public interface DiagramColorConstants extends ColorConstants {

	/**
	 * Constant <code>Color</code> representing green.
	 */
	Color diagramGreen = new Color(null, DiagramColorPalette.diagramGreen);

	/**
	 * Constant <code>Color</code> representing light red.
	 */
	Color diagramLightRed = new Color(null, DiagramColorPalette.diagramLightRed);

	/**
	 * Constant <code>Color</code> representing red.
	 */
	Color diagramRed = new Color(DiagramColorPalette.diagramLightRed);

	/**
	 * Constant <code>Color</code> representing light blue.
	 */
	Color diagramLightBlue = new Color(DiagramColorPalette.diagramLightBlue);

	/**
	 * Constant <code>Color</code> representing blue.
	 */
	Color diagramBlue = new Color(DiagramColorPalette.diagramBlue);

	/**
	 * Constant <code>Color</code> representing light gray.
	 */
	Color diagramLightGray = new Color(DiagramColorPalette.diagramLightGray);

	/**
	 * Constant <code>Color</code> representing gray.
	 */
	Color diagramGray = new Color(DiagramColorPalette.diagramGray);

	/**
	 * Constant <code>Color</code> representing dark gray.
	 */
	Color diagramDarkGray = new Color(DiagramColorPalette.diagramDarkGray);

	/**
	 * Constant <code>Color</code> representing light yellow.
	 */
	Color diagramLightYellow = new Color(DiagramColorPalette.diagramLightYellow);

	/**
	 * Constant <code>Color</code> representing dark yellow.
	 */
	Color diagramDarkYellow = new Color(DiagramColorPalette.diagramDarkYellow);

	/**
	 * Constant <code>Color</code> representing a light gold yellow.
	 */
	Color diagramLightGoldYellow = new Color(DiagramColorPalette.diagramLightGoldYellow);

	/**
	 * Constant <code>Color</code> representing burgundy red.
	 */
	Color diagramBurgundyRed = new Color(DiagramColorPalette.diagramBurgundyRed);
}
