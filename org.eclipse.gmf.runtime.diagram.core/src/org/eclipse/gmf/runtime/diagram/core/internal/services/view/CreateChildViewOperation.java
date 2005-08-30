/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal.services.view;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import com.ibm.xtools.notation.View;

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
	public boolean getPersisted() { 
		return _persisted; 
	}
}
