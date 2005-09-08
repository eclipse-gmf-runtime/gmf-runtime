/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005. 		 All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.view.factories;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * Interface defining the basic Diagram Factory APIs; a diagram factory is responsible for creating a diagram.
 * The <code>createDiagram</code> will be called by the <code>ViewService<code> (using reflection) during
 * a diagram creation process. The Diagram Factory implementation class is provided to the <code>ViewService</code>
 * by the <code>CreateDiagramViewOperation<code>
 * @see org.eclipse.gmf.runtime.diagram.core.internal.services.view.ViewService
 * @see org.eclipse.gmf.runtime.diagram.core.internal.services.view.CreateDiagramViewOperation
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
