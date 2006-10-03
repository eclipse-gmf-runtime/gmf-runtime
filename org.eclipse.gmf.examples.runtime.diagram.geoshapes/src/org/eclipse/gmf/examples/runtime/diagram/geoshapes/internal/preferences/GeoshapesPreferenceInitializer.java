/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
        store.setDefault(IPreferenceConstants.PREF_ENABLE_ANTIALIAS, false);
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
