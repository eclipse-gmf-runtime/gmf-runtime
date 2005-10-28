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

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.preferences;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.LogicDiagramPlugin;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.LogicColorConstants;
import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.figures.DiagramColorConstants;
import org.eclipse.gmf.runtime.diagram.ui.preferences.AppearancePreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.Color;

/**
 * The Appearance preference page used for the Logic Diagram preferences.
 *
 * @author cmahoney
 */
public class LogicAppearancePreferencePage
	extends AppearancePreferencePage {

	/**
	 * Creates a new instance and initializes the preference store.
	 */
	public LogicAppearancePreferencePage() {
		super();
		setPreferenceStore(LogicDiagramPlugin.getInstance().getPreferenceStore());
	}

	/**
     * Initializes the default preference values 
     * for this preference store.
     * 
     * @param store
     */
    public static void initDefaults(IPreferenceStore store) {

    	setDefaultFontPreference(store);

        Color fontColor = ColorConstants.black;
    	PreferenceConverter.setDefault(
            store,
            IPreferenceConstants.PREF_FONT_COLOR,
            fontColor.getRGB());

        Color fillColor = LogicColorConstants.logicGreen;
        PreferenceConverter.setDefault(
            store,
            IPreferenceConstants.PREF_FILL_COLOR,
            fillColor.getRGB());

        Color lineColor = LogicColorConstants.connectorGreen;
        PreferenceConverter.setDefault(
            store,
            IPreferenceConstants.PREF_LINE_COLOR,
            lineColor.getRGB());

        Color noteFillColor = DiagramColorConstants.diagramLightYellow;
        PreferenceConverter.setDefault(
            store,
            IPreferenceConstants.PREF_NOTE_FILL_COLOR,
			noteFillColor.getRGB());

        Color noteLineColor = DiagramColorConstants.diagramDarkYellow;
        PreferenceConverter.setDefault(
            store,
            IPreferenceConstants.PREF_NOTE_LINE_COLOR,
			noteLineColor.getRGB());
    }	
	
}
