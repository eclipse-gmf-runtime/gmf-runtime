/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * The initializer for the generic diagram preferences. Clients that have their
 * own diagram preferences should subclass this so that they inherit default
 * values for the preferences that they do not provide preference pages for.
 * This will ensure that the default values are set for all available diagram
 * preferences.
 * 
 * @author cmahoney
 */
public abstract class DiagramPreferenceInitializer
	extends AbstractPreferenceInitializer {

	/**
	 * Initializes all the generic diagram preferences with their default
	 * values. Override to initialize new preferences added.
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = getPreferenceStore();

		DiagramsPreferencePage.initDefaults(store);
		RulerGridPreferencePage.initDefaults(store);
		AppearancePreferencePage.initDefaults(store);
		ConnectorsPreferencePage.initDefaults(store);
		PrintingPreferencePage.initDefaults(store);

	}

	/**
	 * Gets the preference store to be initialized.
	 * 
	 * @return the preference store to be initialized
	 */
	protected abstract IPreferenceStore getPreferenceStore();

}
