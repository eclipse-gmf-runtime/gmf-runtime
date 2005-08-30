/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005. 		 All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal.view.factories;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * @author mmostafa
 */
public interface DiagramFactory {
	
	/**
	 * @param semanticAdapter
	 * @param diagramKind
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 * @return
	 */
	public Diagram createDiagram(IAdaptable semanticAdapter,
							 	 String diagramKind, PreferencesHint preferencesHint);

}
