/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.preferences.DiagramPreferenceInitializer;


/**
 * Initializes the preferences pages for the Modeling preferences.
 *
 * TODO: Move to Modeler / Visualizer common plugin.
 * 
 * @author schafe
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class ModelingDiagramPreferenceInitializer 
		extends DiagramPreferenceInitializer {
	
	/**
	 * Constructor.
	 */
	public ModelingDiagramPreferenceInitializer() {
		super();
	}

	/* (non-Javadoc)
	 * Implements the inherited method.
	 */
	public void initializeDefaultPreferences() {
		
		super.initializeDefaultPreferences();
		
		IPreferenceStore store = getPreferenceStore();
		
		// initialize defaults for preference pages
		ModelingDiagramsPreferencePage.initDefaults(store);		
		ModelingRulerGridPreferencePage.initDefaults(store);
		ModelingAppearancePreferencePage.initDefaults(store);
		ModelingPrintingPreferencePage.initDefaults(store);
		
		/*
		 * Default diagram creation and open preferences. These preferences are
		 * modified from a modeler preference page. For visualizer, they are not
		 * changed.
		 */
		store.setDefault(
			IPreferenceConstants.CREATE_DEFAULT_DIAGRAM_ON_PACKAGE_CREATION,
			true);
		store.setDefault(IPreferenceConstants.DEFAULT_DIAGRAM_TYPE_FOR_PACKAGE,
			0);
		store.setDefault(
			IPreferenceConstants.OPEN_DEFAULT_DIAGRAM_ON_NAMESPACE_OPEN, true);
		store.setDefault(
			IPreferenceConstants.OPEN_DEFAULT_DIAGRAM_ON_MODEL_OPEN, true);           
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.preferences.DiagramPreferenceInitializer#getPreferenceStore()
	 */
	protected IPreferenceStore getPreferenceStore() {
		return DiagramUIPlugin.getInstance().getPreferenceStore();
	}
}
