/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.preferences;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.LogicDiagramPlugin;
import org.eclipse.gmf.runtime.diagram.ui.preferences.AppearancePreferencePage;

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

}
