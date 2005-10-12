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

package org.eclipse.gmf.runtime.common.ui.l10n;

import java.text.MessageFormat;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreStatusCodes;
import org.eclipse.gmf.runtime.common.core.util.Log;

/**
 * Class represents a font registry that is always empty. It is designed as a 
 * default value to be returned whenever users try to load a regsitry that does
 * not exist. An empty font resitry will always return a default font for all 
 * queries and log all attempts to access it to the error log.
 * 
 * @author Natalia Balaba
 * @canBeSeenBy %partners
 */
public final class EmptyFontRegistry extends FontRegistry {

	// --------------------------------------------------------------------//
	// ------------  STATIC VARIABLES BEGIN -------------------------------//
	// --------------------------------------------------------------------//

	/**
	 * error message
	 */
	private static String MISSING_REGISTRY_MESSAGE = "Font registry ({0}) is missing."; //$NON-NLS-1$

	/**
	 * error message
	 */
	private static String INVALID_ACCESS_MESSAGE = "Attempt to access font in missing registry ({0})."; //$NON-NLS-1$

	// --------------------------------------------------------------------//
	// ------------  STATIC VARIABLES END -----------------------------//
	// --------------------------------------------------------------------//	

	// --------------------------------------------------------------------//
	// ------------  INSTANCE VARIABLES BEGIN -----------------------------//
	// --------------------------------------------------------------------//

	/**
	 *  name of the font regsitry that was not accessible
	 */
	private String registryName = null;

	// --------------------------------------------------------------------//
	// ------------  INSTANCE VARIABLES END -------------------------------//
	// --------------------------------------------------------------------//    

	// --------------------------------------------------------------------//
	// ------------  CONSTRUCTORS BEGIN -----------------------------------//
	// --------------------------------------------------------------------//
	/**
	 * Constructor for EmptyFontRegistry.
	 * 
	 * @param registryName empty <code>FontRegistry</code>
	 */
	public EmptyFontRegistry(String registryName) {
		super();

		this.registryName = registryName;

		Log.warning(
			CommonCorePlugin.getDefault(),
			CommonCoreStatusCodes.L10N_FAILURE,
			MessageFormat.format(
				MISSING_REGISTRY_MESSAGE,
				new Object[] { getRegistryName()}));
	}

	// --------------------------------------------------------------------//
	// ------------  CONSTRUCTORS END -------------------------------------//
	// --------------------------------------------------------------------//

	// --------------------------------------------------------------------//
	// ------------  INSTANCE METHODS BEGIN -------------------------------//
	// --------------------------------------------------------------------//    
	/**
	 * Method getRegistryName.
	 * @return name of the missing registry
	 */
	private String getRegistryName() {
		return registryName;
	}

	/* Do nothing - the registry is always empty
	 * @see org.eclipse.jface.resource.FontRegistry#put(String, FontData[])
	 */
	public void put(String symbolicName, FontData[] fontData) {
		/* empty method body */
	}

	/**
	 * Returns the default font data . Logs error to the log
	 *
	 * @param symbolicName symbolic font name
	 * @return the font with the given symbolic name
	 */
	public FontData[] getFontData(String symbolicName) {
		Log.warning(
			CommonCorePlugin.getDefault(),
			CommonCoreStatusCodes.L10N_FAILURE,
			MessageFormat.format(
				INVALID_ACCESS_MESSAGE,
				new Object[] { getRegistryName()}));

		return super.getFontData(symbolicName);
	}
	
    /**
     * @see org.eclipse.jface.resource.FontRegistry#bestDataArray(org.eclipse.swt.graphics.FontData[], org.eclipse.swt.widgets.Display)
     */
    public FontData[] bestDataArray(FontData[] fonts, Display display) {
		Log.warning(
			CommonCorePlugin.getDefault(),
			CommonCoreStatusCodes.L10N_FAILURE,
			MessageFormat.format(
				INVALID_ACCESS_MESSAGE,
				new Object[] { getRegistryName()}));

        return super.bestDataArray(fonts, display);
    }

	// --------------------------------------------------------------------//
	// ------------  INSTANCE MEHTODS END ---------------------------------//
	// --------------------------------------------------------------------//
}
