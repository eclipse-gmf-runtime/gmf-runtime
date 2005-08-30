/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.internal;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import org.eclipse.gmf.runtime.common.ui.preferences.ModelingPreferencePage;

/**
 * Initializes the Common UI preferences to their default values.
 * 
 * @author cmahoney
 */
public class CommonUIPreferencePageInitializer
	extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = CommonUIPlugin.getDefault()
			.getPreferenceStore();
		ModelingPreferencePage.initDefaults(store);
	}
}
