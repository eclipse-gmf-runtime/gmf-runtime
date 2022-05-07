/******************************************************************************
 * Copyright (c) 2002, 2003, 2006 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author melaasar
 *
 * Base of child view creation operations
 */
public abstract class CreateChildViewOperation extends CreateViewOperation {

	/** containerView view */
	private final View containerView;

	/** index of child in containerView children collection */
	private final int index;

	/** cached persisted flag. */
	private boolean _persisted = true;
	
	/**
	 * Method CreateChildViewOperation.
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
	protected CreateChildViewOperation(
		IAdaptable semanticAdapter,
		View containerView,
		String semanticHint,
		int index,
		boolean persisted, PreferencesHint preferencesHint) {
		super(semanticAdapter, semanticHint, preferencesHint);

		
		assert null != containerView : "Null containerView in CreateChildViewOperation";//$NON-NLS-1$		
		assert index >= ViewUtil.APPEND : "Invalid index in CreateChildViewOperation";//$NON-NLS-1$

		this.containerView = containerView;
		this.index = index;
		_persisted = persisted;	
	}

	/**
	 * Method getParent.
	 * @return IContainerView
	 */
	public final View getContainerView() {
		return containerView;
	}

	/**
	 * Method getIndex.
	 * @return int
	 */
	public final int getIndex() {
		return index;
	}

	/** Return the <i>persisted</i> paramter value. */
	public final boolean getPersisted() { 
		return _persisted; 
	}
}
