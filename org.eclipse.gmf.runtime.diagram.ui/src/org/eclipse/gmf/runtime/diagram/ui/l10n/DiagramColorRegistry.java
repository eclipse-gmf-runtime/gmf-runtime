/******************************************************************************
 * Copyright (c) 2005 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.l10n;

import org.eclipse.gmf.runtime.draw2d.ui.graphics.ColorRegistry;
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
		return ColorRegistry.getInstance().getColor(id);
	}

	/**
	 * Returns the Color based on the RGB. If the color does not exist in the
	 * cache, creates a new one and caches.
	 * 
	 * @param RGB
	 * @return Color
	 */
	public Color getColor(RGB rgb) {
		return ColorRegistry.getInstance().getColor(rgb);
	}

}
