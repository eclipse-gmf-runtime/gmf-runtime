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
