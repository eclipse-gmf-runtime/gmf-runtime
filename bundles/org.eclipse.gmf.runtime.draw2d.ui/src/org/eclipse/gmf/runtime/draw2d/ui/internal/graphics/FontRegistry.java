/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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

	private Map<String, Font> fonts = null;

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
		Font font = null;
		String fontSignature = fd.toString();

		if(fonts == null) {
			fonts = new HashMap<String, Font>();
		} else {
			font = fonts.get(fontSignature);
		}
		
		if (font == null) {
			font = new Font(device, fd);
			fonts.put(fontSignature, font);
		}
		
		return font;
	}

	/**
	 * Removes all fonts currently in the cache and dispose of them
	 */
	public void clearFontCache() {
		if (fonts != null) {
			List<String> keys = new ArrayList<String>(fonts.keySet());
			Iterator<String> keyiter = keys.iterator();
			while (keyiter.hasNext()) {
				Font font = fonts.remove(keyiter.next());
				font.dispose();
			}
		}
	}
}
