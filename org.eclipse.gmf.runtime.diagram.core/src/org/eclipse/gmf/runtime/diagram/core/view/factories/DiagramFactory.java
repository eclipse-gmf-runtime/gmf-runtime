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

package org.eclipse.gmf.runtime.diagram.core.view.factories;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * Interface defining the basic Diagram Factory APIs; a diagram factory is responsible for creating a diagram.
 * The <code>createDiagram</code> will be called by the <code>ViewService<code> (using reflection) during
 * a diagram creation process. The Diagram Factory implementation class is provided to the <code>ViewService</code>
 * by the <code>CreateDiagramViewOperation<code>
 * @see org.eclipse.gmf.runtime.diagram.core.services.ViewService
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
