/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.emf.ui.preferences;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

import org.eclipse.gmf.runtime.emf.ui.internal.MslUIPlugin;


/**
 * Default preferences initializer for the MSL UI plug-in.
 *
 * @author Christian W. Damus (cdamus)
 * @deprecated
 */
public class PreferenceInitializer
	extends AbstractPreferenceInitializer {

	/**
	 * Initializes me.
	 */
	public PreferenceInitializer() {
		super();
	}

	/* (non-Javadoc)
	 * Implements the inherited method.
	 */
	public void initializeDefaultPreferences() {
		Preferences prefs = MslUIPlugin.getDefault().getPluginPreferences();
		
		//validation preference defaults
        prefs.setDefault(IPreferenceConstants.VALIDATION_LIVE_PROBLEMS_DISPLAY, 0);
        prefs.setDefault(IPreferenceConstants.VALIDATION_LIVE_WARNINGS_IN_DIALOG, true);
        prefs.setDefault(IPreferenceConstants.VALIDATION_LIVE_SHOW_CONSOLE, false);
	}

}
