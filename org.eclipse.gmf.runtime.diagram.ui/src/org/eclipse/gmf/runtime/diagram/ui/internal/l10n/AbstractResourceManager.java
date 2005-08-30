/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.l10n;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

import org.eclipse.gmf.runtime.common.ui.l10n.AbstractUIResourceManager;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;

/**
 * This class adds the ability to retrieve ImageDescriptors which 
 * we need for Actions and the ability to cache Fonts.
 * 
 * @author chmahone
 * @canBeSeenBy %level1
 */
public abstract class AbstractResourceManager
	extends AbstractUIResourceManager {

	// Holds fonts.
	private Map fonts = null;

	// HashMap that holds onto Color instances
	private final static Map colorRegistry = new HashMap();

	/**
	 * Returns the Font based on the FontData given; creates a new Font
	 * (and caches it) if this is a new one being requested; otherwise, 
	 * returns a cached Font.
	 * 
	 * The FontRegistry from the parent AbstractResourceManager class
	 * could not be used because if the Font didn't exist it returns a 
	 * default font.
	 * 
	 * @param device the device to create the font on
	 * @param fd FontData from which to find or create a Font
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
	
	/**
	 * Returns the Color based on the id.
	 * If the color does not exist in the cache,
	 * creates a new one and caches.	 
	 * 
	 * @param id - the integer representation of a color
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
	 * Returns the Color based on the RGB.
	 * If the color does not exist in the cache,
	 * creates a new one and caches.	 
	 * 
	 * @param RGB 
	 * @return Color
	 */
	public Color getColor(RGB rgb) {
		Integer colorID = FigureUtilities.RGBToInteger(rgb);
		return getColor(colorID);
	}

}
