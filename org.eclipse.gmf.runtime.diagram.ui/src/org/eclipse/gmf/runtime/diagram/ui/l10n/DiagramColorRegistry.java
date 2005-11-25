/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.l10n;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * Manages color resources.
 * 
 * @author cmahoney
 */
public class DiagramColorRegistry {

	/**
	 * Singleton instance for the color registry.
	 */
	private static DiagramColorRegistry singletonInstance = new DiagramColorRegistry();

	/**
	 * Return singleton instance of the color registry.
	 * 
	 * @return the color registry
	 */
	public static DiagramColorRegistry getInstance() {
		return singletonInstance;
	}

	// HashMap that holds onto Color instances
	private final static Map colorRegistry = new HashMap();

	/**
	 * Private constructor.
	 */
	private DiagramColorRegistry() {
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
