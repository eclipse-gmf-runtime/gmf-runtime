/******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.graphics;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * Manages color resources.
 * 
 * @author cmahoney, ldamus
 * @since 1.4
 */
public class ColorRegistry {

	/**
	 * Singleton instance for the color registry.
	 */
	private static ColorRegistry singletonInstance = new ColorRegistry();

	/**
	 * Return singleton instance of the color registry.
	 * 
	 * @return the color registry
	 */
	public static ColorRegistry getInstance() {
		return singletonInstance;
	}

	// HashMap that holds onto Color instances
	private final static Map colorRegistry = new HashMap();

	/**
	 * Private constructor.
	 */
	private ColorRegistry() {
		super();
	}

	/**
	 * Returns the Color based on the id. If the color does not exist in the
	 * cache, creates a new one and caches.
	 * 
	 * @param id -
	 *            the integer representation of a color
	 * @return Color
	 */
	public Color getColor(Integer id) {
		Object value = colorRegistry.get(id);
		if (value != null) {
			return (Color) value;
		}
		Color newColor = FigureUtilities.integerToColor(id);
		colorRegistry.put(id, newColor);
		return newColor;
	}

	/**
	 * Returns the Color based on the RGB. If the color does not exist in the
	 * cache, creates a new one and caches.
	 * 
	 * @param RGB
	 * @return Color
	 */
	public Color getColor(RGB rgb) {
		Integer colorID = FigureUtilities.RGBToInteger(rgb);
		return getColor(colorID);
	}

}
