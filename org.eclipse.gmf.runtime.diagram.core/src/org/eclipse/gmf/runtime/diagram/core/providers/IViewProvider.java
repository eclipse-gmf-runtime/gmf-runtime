/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.providers;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author melaasar, mmostafa
 * 
 * An interface for manipulating the notational model Defines the factory
 * methods for creating the various view elements.
 * <p>
 * This interface is <EM>not</EM> intended to be implemented by clients as new
 * methods may be added in the future. Extend {@link AbstractViewProvider}
 * instead.
 * </p>
 */
public interface IViewProvider extends IProvider {

	/**
	 * create an <code>Diagram</code> element.
	 * @param IAdaptable (for semantic element)
	 * @param diagramKind  indicates the diagram type 
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 *  @return the created <code>Diagram</code>
	 */
	public Diagram createDiagram(
		IAdaptable semanticAdapter,
		String diagramKind, PreferencesHint preferencesHint);

	/** 
	 * create an <code>Edge</code> element.
	 * @param IAdaptable (for semantic element)
	 * @param containerView the container view that will contain the created view.
	 * @param indicates the type of the Edge, it could be used later to determine the edge type
	 * @param index position in the container view's list of children views.
	 * @param persisted indicates if the created edge will be persisted or not
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 * @return the created <code>Edge</code>
	 */
	public Edge createEdge(
		IAdaptable semanticAdapter,
		View containerView,
		String semanticHint,
		int index,
		boolean persisted, PreferencesHint preferencesHint);
		
	/** 
	 * create a <code>Node</code>.
	 * @param IAdaptable (for semantic element)
	 * @param containerView the container view that will contain the created view.
	 * @param indicates the type of the view, it could be used later to determine the view type
	 * @param index position in the container view's list of children views.
	 * @param persisted indicates if the created view will be persisted or not
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 * @return the created <code>Node</code>
	 */
	public Node createNode(
		IAdaptable semanticAdapter,
		View containerView,
		String semanticHint,
		int index,
		boolean persisted, PreferencesHint preferencesHint);
}
