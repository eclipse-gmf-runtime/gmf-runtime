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


package org.eclipse.gmf.runtime.diagram.ui.internal.preferences;

import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.preferences.RulerGridPreferencePage;

/**
 * The ruler/grid preference page used for the Modeling preferences.
 * 
 * @deprecated These preferences are not supported in the diagram layer.
 * To be deleted after Dec 21, 2005.
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
