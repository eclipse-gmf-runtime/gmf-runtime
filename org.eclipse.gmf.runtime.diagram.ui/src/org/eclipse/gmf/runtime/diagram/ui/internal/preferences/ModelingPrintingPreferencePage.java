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

import org.eclipse.jface.preference.IPreferenceStore;

import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.preferences.PrintingPreferencePage;

/**
 * The printing preference page used for the Modeling preferences.
 * 
 * @deprecated These preferences are not supported in the diagram layer.
 * To be deleted after Dec 21, 2005.
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
