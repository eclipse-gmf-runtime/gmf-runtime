/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
