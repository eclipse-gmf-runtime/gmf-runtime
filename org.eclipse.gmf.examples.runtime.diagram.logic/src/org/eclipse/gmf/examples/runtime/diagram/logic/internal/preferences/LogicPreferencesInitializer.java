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

import org.eclipse.jface.preference.IPreferenceStore;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.LogicDiagramPlugin;
import org.eclipse.gmf.runtime.diagram.ui.preferences.DiagramPreferenceInitializer;

/**
 * Initializes the preferences pages for the Logic Diagram Editor.
 * 
 * @author cmahoney
 */
public class LogicPreferencesInitializer
	extends DiagramPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.preferences.DiagramPreferenceInitializer#getPreferenceStore()
	 */
	protected IPreferenceStore getPreferenceStore() {
		return LogicDiagramPlugin.getInstance().getPreferenceStore();
	}

}
