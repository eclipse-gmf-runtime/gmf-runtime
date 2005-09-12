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

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;

/**
 * Used to identify an editpart as having preference support. A root editpart
 * created in the EditPart Service
 * {@link org.eclipse.gmf.runtime.diagram.ui.services.editpart.EditPartService#createRootEditPart() 
 * should implement this interface if the diagram is to support the common
 * diagram preferences.
 * 
 * @author cmahoney
 */
public interface IDiagramPreferenceSupport {

	/**
	 * Sets the preferences hint that is to be used to find the appropriate
	 * preference store from which to retrieve diagram preference values. The
	 * preference hint is mapped to a preference store in the preference
	 * registry <@link DiagramPreferencesRegistry>.
	 * 
	 * @param preferencesHint the preferences hint
	 */
	public void setPreferencesHint(PreferencesHint preferenceHint);

	/**
	 * Gets the preferences hint that is to be used to find the appropriate
	 * preference store from which to retrieve diagram preference values. The
	 * preference hint is mapped to a preference store in the preference
	 * registry <@link DiagramPreferencesRegistry>.
	 * 
	 * @return the preferences hint
	 */
	public PreferencesHint getPreferencesHint();
}
