/******************************************************************************
 * Copyright (c) 2023 Obeo.
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

import org.eclipse.swt.graphics.RGB;

/**
 * RGB values for the standard DiagramColorConstants.
 * 
 * @author pcdavid
 */
public interface DiagramColorPalette {
	/**
	 * Green.
	 */
	RGB diagramGreen = new RGB(40, 100, 70);

	/**
	 * Light red.
	 */
	RGB diagramLightRed = new RGB(255, 203, 203);

	/**
	 * Red.
	 */
	RGB diagramRed = new RGB(255, 128, 128);

	/**
	 * Light blue.
	 */
	RGB diagramLightBlue = new RGB(202, 203, 255);

	/**
	 * Blue.
	 */
	RGB diagramBlue = new RGB(128, 128, 255);

	/**
	 * Light gray.
	 */
	RGB diagramLightGray = new RGB(250, 250, 254);

	/**
	 * Gray.
	 */
	RGB diagramGray = new RGB(176, 176, 176);

	/**
	 * Dark gray.
	 */
	RGB diagramDarkGray = new RGB(131, 122, 133);

	/**
	 * Light yellow.
	 */
	RGB diagramLightYellow = new RGB(255, 255, 203);

	/**
	 * Dark yellow.
	 */
	RGB diagramDarkYellow = new RGB(255, 204, 102);

	/**
	 * Light gold yellow.
	 */
	RGB diagramLightGoldYellow = new RGB(255, 255, 204);

	/**
	 * Burgundy red.
	 */
	RGB diagramBurgundyRed = new RGB(153, 0, 51);
}
