/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.internal.services.view;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.providers.IViewProvider;
import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * @author melaasar
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.core.*
 *
 * Diagram view creation operation
 */
public class CreateDiagramViewOperation extends CreateViewOperation {
	
	/**
	 * Creates a new instance.
	 * @param semanticAdapter
	 * @param diagramKindType
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 */
	public CreateDiagramViewOperation(IAdaptable semanticAdapter, String diagramKindType, PreferencesHint preferencesHint) {
		super(semanticAdapter, diagramKindType, preferencesHint);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.services.view.CreateViewOperation#getViewKind()
	 */
	public Class getViewKind() {
		return Diagram.class;
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		return ((IViewProvider) provider).createDiagram(getSemanticAdapter(), getSemanticHint(), getPreferencesHint());
	}

}
