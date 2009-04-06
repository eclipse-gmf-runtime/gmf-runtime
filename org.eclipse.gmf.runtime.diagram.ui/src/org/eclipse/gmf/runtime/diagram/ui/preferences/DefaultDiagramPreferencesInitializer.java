/******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.preferences;

import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Populates default diagram preferences
 * 
 * @author aboyko
 *
 */
public class DefaultDiagramPreferencesInitializer extends
		DiagramPreferenceInitializer {

	@Override
	protected IPreferenceStore getPreferenceStore() {
		return (IPreferenceStore) PreferencesHint.USE_DEFAULTS.getPreferenceStore();
	}

}
