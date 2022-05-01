/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.services.view;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.providers.IViewProvider;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;


/**
 * Node view creation operation
 * @author melaasar
 */
public final class CreateNodeViewOperation extends CreateChildViewOperation 
{
	/**
	 * Constructor for CreateNodeViewOperation.
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
	public CreateNodeViewOperation(
		IAdaptable semanticAdapter,
		View containerView,
		String semanticHint,
		int index,
		boolean persisted, PreferencesHint preferencesHint) {
		super(
			semanticAdapter,
			containerView,
			semanticHint,
			index,
			persisted, preferencesHint);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.services.view.CreateViewOperation#getViewKind()
	 */
	public final Class getViewKind() {
		return Node.class;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		return ((IViewProvider) provider).createNode(
			getSemanticAdapter(),
			getContainerView(),
			getSemanticHint(),
			getIndex(),
			getPersisted(), getPreferencesHint());
	}

}
