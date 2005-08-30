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
	protected CreateDiagramViewOperation(IAdaptable semanticAdapter, String diagramKindType, PreferencesHint preferencesHint) {
		super(semanticAdapter, diagramKindType, preferencesHint);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.services.view.CreateViewOperation#getViewKind()
	 */
	public Class getViewKind() {
		return Diagram.class;
	}
	
	/**
	 * @see com.ibm.xtools.common.service.IOperation#execute(IProvider)
	 */
	public Object execute(IProvider provider) {
		return ((IViewProvider) provider).createDiagramView(getSemanticAdapter(), getSemanticHint(), getPreferencesHint());
	}

}
