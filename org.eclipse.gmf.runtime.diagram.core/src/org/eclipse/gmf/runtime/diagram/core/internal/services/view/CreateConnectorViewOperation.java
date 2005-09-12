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

import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.providers.IViewProvider;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;

/**
 * 
 * Connector view creation operation
 * @author melaasar
 */
public class CreateConnectorViewOperation extends CreateChildViewOperation {

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
	protected CreateConnectorViewOperation(
		IAdaptable semanticAdapter,
		View containerView,
		String semanticHint,
		int index,
		boolean persisted, PreferencesHint preferencesHint) {
		super(semanticAdapter, containerView, semanticHint, index, persisted, preferencesHint);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.services.view.CreateViewOperation#getViewKind()
	 */
	public Class getViewKind() {
		return Edge.class;
	}


	/**
	 * @see com.ibm.xtools.common.service.IOperation#execute(IProvider)
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
