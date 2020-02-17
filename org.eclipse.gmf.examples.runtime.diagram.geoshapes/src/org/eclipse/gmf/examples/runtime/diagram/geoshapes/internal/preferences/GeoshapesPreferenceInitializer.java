/******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.preferences;

import org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.DiagramPlugin;
import org.eclipse.gmf.runtime.diagram.ui.preferences.DiagramPreferenceInitializer;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;

/**
/**
 * Initializes the preference values for the geo shapes Diagram Editor.
 * @author mmostafa
 */

public class GeoshapesPreferenceInitializer
    extends DiagramPreferenceInitializer {

    public void initializeDefaultPreferences() {
        super.initializeDefaultPreferences();
        // Resetting AntiAliase settings in the geo shapes preference store 
        IPreferenceStore store = getPreferenceStore(); 
        // Anti Aliasing was turned off in fix Bugzilla 137155 
        // Since Bugzilla 204012 is resolved, we can turn on again.
        // This class should be removed, but leaving in case we need it again.
        store.setDefault(IPreferenceConstants.PREF_ENABLE_ANTIALIAS, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.diagram.ui.internal.preferences.DiagramPreferenceInitializer#getPreferenceStore()
     */
    protected IPreferenceStore getPreferenceStore() {
        return DiagramPlugin.getInstance().getPreferenceStore();
    }

}
