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
import com.ibm.xtools.notation.View;

/**
 * @author melaasar
 *
 * This operation is used to determine if there is a provider that can
 * create a view for the given element descriptor in the given container
 * 
 * The operation is "not" meant to be executed and is only passed to view
 * providers in their <code>provides</code> method for the above mentioned reason
 * To properly create a view, refer to <code>CreateViewOperation</code>
 * 
 */
public class CreateViewForKindOperation extends CreateChildViewOperation {
	
	private final Class viewKind;

	/**
	 * @param viewKind
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @param index
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 */
	public CreateViewForKindOperation(
		Class viewKind,
		IAdaptable semanticAdapter,
		View containerView,		
		String semanticHint,
		int index, PreferencesHint preferencesHint) {
		super(
			semanticAdapter,
			containerView,
			semanticHint,
			index,
			true, preferencesHint);
		this.viewKind = viewKind;
	}

	/**
	 * @return
	 */
	public Class getViewKind() {
		return viewKind;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(IProvider)
	 */
	public Object execute(IProvider provider) {
		throw new UnsupportedOperationException("This operation cannot be executed. It is used to check if there is a view provider for the given ElementDescriptor"); //$NON-NLS-1$
	}

}
