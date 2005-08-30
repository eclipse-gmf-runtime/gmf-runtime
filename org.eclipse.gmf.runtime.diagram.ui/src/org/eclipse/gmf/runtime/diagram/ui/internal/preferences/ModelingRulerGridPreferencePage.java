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

import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.preferences.RulerGridPreferencePage;

/**
 * The ruler/grid preference page used for the Modeling preferences.
 * 
 * TODO: Move to Modeler / Visualizer common plugin.
 * 
 * @author cmahoney
 */
public class ModelingRulerGridPreferencePage
	extends RulerGridPreferencePage {

	/**
	 * Creates a new instance and initializes the preference store.
	 */
	public ModelingRulerGridPreferencePage() {
		super();
		setPreferenceStore(DiagramUIPlugin.getInstance().getPreferenceStore());
	}

}
