/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.preferences;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.LogicDiagramPlugin;
import org.eclipse.gmf.runtime.diagram.ui.preferences.DiagramPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Initializes the preferences pages for the Logic Diagram Editor.
 * 
 * @author cmahoney
 */
public class LogicPreferencesInitializer
	extends DiagramPreferenceInitializer {

	public void initializeDefaultPreferences() {
		super.initializeDefaultPreferences();
		
		// Resetting appearance settings in the logic preference store 
		IPreferenceStore store = getPreferenceStore();	
		LogicAppearancePreferencePage.initDefaults(store);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.preferences.DiagramPreferenceInitializer#getPreferenceStore()
	 */
	protected IPreferenceStore getPreferenceStore() {
		return LogicDiagramPlugin.getInstance().getPreferenceStore();
	}

}
