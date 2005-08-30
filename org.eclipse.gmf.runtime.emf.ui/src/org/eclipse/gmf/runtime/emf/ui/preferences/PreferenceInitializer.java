/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.emf.ui.preferences;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

import org.eclipse.gmf.runtime.emf.ui.internal.MslUIPlugin;


/**
 * Default preferences initializer for the MSL UI plug-in.
 *
 * @author Christian W. Damus (cdamus)
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
