/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal.services.view;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import com.ibm.xtools.notation.Diagram;
import com.ibm.xtools.notation.View;

/**
 * @author melaasar
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.core.*
 *
 * An interface for manipulating the notational model
 */
/**
 * Defines the factory methods for creating the various view elements.
 */
public interface IViewProvider extends IProvider {

	/**
	 * create an <code>IDiagramView</code> element.
	 * @param sematicAdapter Adapts to Integer (semanticKind) and possibly IAdaptable (for semantic element)
	 * @param diagramKind 
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 */
	public Diagram createDiagramView(
		IAdaptable semanticAdapter,
		String diagramKind, PreferencesHint preferencesHint);

	/** 
	 * create an <code>IConnectorView</code> element.
	 * @param sematicAdapter Adapts to <code>IReference</code> for semantic references
	 * @param containerView the container view that will contain the created view.
	 * @param semanticHint may be used by implements to determine the type of view to create.
	 * @param index position in the container view's list of children views.
	 * @param persisted persisted view flag.
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 */
	public View createConnectorView(
		IAdaptable semanticAdapter,
		View containerView,
		String semanticHint,
		int index,
		boolean persisted, PreferencesHint preferencesHint);
		
	/** 
	 * create an <code>INodeView</code> element.
	 * @param sematicAdapter Adapts to <code>IReference</code> for semantic references
	 * @param containerView the container view that will contain the created view.
	 * @param semanticHint may be used by implements to determine the type of view to create.
	 * @param index position in the container view's list of children views.
	 * @param persisted persisted view flag.
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 */
	public View createNodeView(
		IAdaptable semanticAdapter,
		View containerView,
		String semanticHint,
		int index,
		boolean persisted, PreferencesHint preferencesHint);

	/** 
	 * incarnate an instance of the view mapped to the supplied model element
	 * @param viewElement a reference to the actual model element to be mapped to the 
	 * created view.
	 * @return IView
	 */
	//public IView incarnateView(View viewElement);
}
