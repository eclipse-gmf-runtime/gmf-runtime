/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.internal.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

/**
 * Manages font resources.
 * 
 * @author cmahoney
 */
final class FontRegistry {

	/**
	 * Singleton instance for the font registry.
	 */
	private static FontRegistry singletonInstance = new FontRegistry();

	/**
	 * Return singleton instance of the font registry.
	 * 
	 * @return the font registry
	 */
	public static FontRegistry getInstance() {
		return singletonInstance;
	}

	private Map fonts = null;

	/**
	 * Private constructor.
	 */
	private FontRegistry() {
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

		Object value = fonts.get(fd.toString());
		if (value != null) {
			return (Font) value;
		}
		Font newFont = new Font(device, fd);
		fonts.put(fd.toString(), newFont);
		return newFont;
	}

	/**
	 * Removes all fonts currently in the cache and dispose of them
	 */
	public void clearFontCache() {
		if (fonts != null) {
			List keys = new ArrayList(fonts.keySet());
			Iterator keyiter = keys.iterator();
			while (keyiter.hasNext()) {
				Font font = (Font) fonts.remove(keyiter.next());
				font.dispose();
			}
		}
	}
}
