/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.diagram.ui.internal.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.preferences.PrintingPreferencePage;

/**
 * The printing preference page used for the Modeling preferences.
 * 
 * TODO: Move to Modeler / Visualizer common plugin.
 * 
 * @author cmahoney
 */
public class ModelingPrintingPreferencePage
	extends PrintingPreferencePage {

	/**
	 * Creates a new instance and initializes the preference store.
	 */
	public ModelingPrintingPreferencePage() {
		super(DiagramUIPlugin.getInstance().getPreferenceStore());
	}
	
	public ModelingPrintingPreferencePage(IPreferenceStore store) {
		super(store);
	}

}
