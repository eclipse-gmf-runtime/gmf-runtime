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

package org.eclipse.gmf.runtime.diagram.ui.internal.l10n;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

/**
 * Manages font resources.
 * 
 * @author cmahoney
 */
public class DiagramFontRegistry {

	/**
	 * Singleton instance for the font registry.
	 */
	private static DiagramFontRegistry singletonInstance = new DiagramFontRegistry();

	/**
	 * Return singleton instance of the font registry.
	 * 
	 * @return the font registry
	 */
	public static DiagramFontRegistry getInstance() {
		return singletonInstance;
	}

	// Holds fonts.
	private Map fonts = null;

	/**
	 * Private constructor.
	 */
	private DiagramFontRegistry() {
		super();
	}

	/**
	 * Returns the Font based on the FontData given; creates a new Font (and
	 * caches it) if this is a new one being requested; otherwise, returns a
	 * cached Font.
	 * 
	 * The FontRegistry from the parent AbstractResourceManager class could not
	 * be used because if the Font didn't exist it returns a default font.
	 * 
	 * @param device
	 *            the device to create the font on
	 * @param fd
	 *            FontData from which to find or create a Font
	 * @return the Font
	 */
	public Font getFont(Device device, FontData fd) {
		if (fonts == null)
			fonts = new HashMap();

		Object value = fonts.get(fd);
		if (value != null) {
			return (Font) value;
		}
		Font newFont = new Font(device, fd);
		fonts.put(fd, newFont);
		return newFont;
	}
}
