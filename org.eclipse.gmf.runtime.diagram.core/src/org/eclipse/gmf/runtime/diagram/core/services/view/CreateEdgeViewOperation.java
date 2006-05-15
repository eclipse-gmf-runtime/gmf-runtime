/******************************************************************************
 * Copyright (c) 2002, 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.services.view;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.providers.IViewProvider;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;

/**
 * 
 * Edge view creation operation
 * @author melaasar
 */
public final class CreateEdgeViewOperation extends CreateChildViewOperation {

	/**
	 * Creates a new instance.
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @param index
	 * @param persisted
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 */
	public CreateEdgeViewOperation(
		IAdaptable semanticAdapter,
		View containerView,
		String semanticHint,
		int index,
		boolean persisted, PreferencesHint preferencesHint) {
		super(semanticAdapter, containerView, semanticHint, index, persisted, preferencesHint);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.services.view.CreateViewOperation#getViewKind()
	 */
	public final Class getViewKind() {
		return Edge.class;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		return ((IViewProvider) provider).createEdge(
			getSemanticAdapter(),
			getContainerView(),
			getSemanticHint(),
			getIndex(),
			getPersisted(), getPreferencesHint());
	}

}
