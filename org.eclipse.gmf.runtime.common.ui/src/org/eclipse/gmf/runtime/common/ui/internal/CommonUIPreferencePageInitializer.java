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
