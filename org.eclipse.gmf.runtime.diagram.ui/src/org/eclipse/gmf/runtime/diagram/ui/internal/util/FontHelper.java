/******************************************************************************
 * Copyright (c) 2004, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.gmf.runtime.common.ui.util.DisplayUtils;
import org.eclipse.swt.graphics.FontData;


/**
 * Helper for fonts.
 * 
 * For now, it contains a method to get the font names.
 * 
 * @author wdiu, Wayne Diu
 */
public class FontHelper {
	
	private static String[] FONT_NAMES;
	
	/**
	 * array of font sizes
	 */
	protected static final String[] FONT_SIZES = { "8", //$NON-NLS-1$
		"9", //$NON-NLS-1$
		"10", //$NON-NLS-1$
		"11", //$NON-NLS-1$
		"12", //$NON-NLS-1$
		"14", //$NON-NLS-1$
		"16", //$NON-NLS-1$
		"18", //$NON-NLS-1$
		"20", //$NON-NLS-1$
		"22", //$NON-NLS-1$
		"24", //$NON-NLS-1$
		"26", //$NON-NLS-1$
		"28", //$NON-NLS-1$
		"36", //$NON-NLS-1$
		"48", //$NON-NLS-1$
		"72" }; //$NON-NLS-1$
	
	/**
	 * Return the font names for the default display.
	 *  
	 * @return String array of font names as String objects for the default
	 * display. 
	 */
	static public String[] getFontNames() {
		if (FONT_NAMES != null)
			return FONT_NAMES;
		
		//add the names into a set to get a set of unique names
		Set<String> stringItems = new HashSet<String>();
		FontData[] fontDatas = DisplayUtils.getDisplay().getFontList(null, true);
		for (int i = 0; i < fontDatas.length; i++) {
			if (fontDatas[i].getName() != null) {
				stringItems.add(fontDatas[i].getName());
			}
		}
		
		//add strings into the array
		String strings[] = new String[stringItems.size()];
		int i = 0;
		for (String item : stringItems) {
			strings[i++] = item;
		}
		
		//sort the array
		Arrays.sort(strings);

		return FONT_NAMES = strings;
	}
	
	/**
	 * @return - array of fomt sizes
	 */
	public static final String[] getFontSizes(){
		return FONT_SIZES;
	}
	
}
