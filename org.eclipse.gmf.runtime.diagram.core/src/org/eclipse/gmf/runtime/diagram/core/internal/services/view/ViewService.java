/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.internal.services.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.diagram.core.internal.DiagramPlugin;
import org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.CreateElementRequest;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.providers.IViewProvider;
import org.eclipse.gmf.runtime.diagram.core.providers.ViewProviderConfiguration;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

/**
 *
 * A service for manipulating the notational models
 * @author melaasar, mmostafa
 */
final public class ViewService
	extends Service
	implements IViewProvider {

	/**
	 * A descriptor for <code>ISemanticProvider</code> defined
	 * by a configuration element.
	 */
	protected static class ProviderDescriptor
		extends Service.ProviderDescriptor {

		/** the provider configuration parsed from XML */
		private ViewProviderConfiguration providerConfiguration;

		/**
		 * Constructs a <code>ISemanticProvider</code> descriptor for
		 * the specified configuration element.
		 * 
		 * @param element The configuration element describing the provider.
		 */
		public ProviderDescriptor(IConfigurationElement element) {
			super(element);

			this.providerConfiguration = ViewProviderConfiguration
				.parse(element);
			assert null != providerConfiguration : "Null providerConfiguration in ProviderDescriptor";//$NON-NLS-1$
		}

		/**
		 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
		 */
		public boolean provides(IOperation operation) {
			if (getPolicy() != null)
				return getPolicy().provides(operation);
			if (provider == null) {
				if (isSupportedInExtention(operation)) {
					providerConfiguration = null;
					return getProvider().provides(operation);
				}
				return false;
			}
			return getProvider().provides(operation);
		}

		/**
		 * Cheks if the operation is supported by the XML extension
		 * @param operation
		 * @return
		 */
		private boolean isSupportedInExtention(IOperation operation) {
			if (operation instanceof CreateViewOperation) {
				CreateViewOperation o = (CreateViewOperation) operation;
				Class viewKind = o.getViewKind();
				IAdaptable semanticAdapter = o.getSemanticAdapter();
				String semanticHint = o.getSemanticHint();
				View containerView = null;
				if (o instanceof CreateChildViewOperation) {
					CreateChildViewOperation cvo = (CreateChildViewOperation) o;
					containerView = cvo.getContainerView();
				}

				return providerConfiguration.supports(viewKind,
					semanticAdapter, containerView, semanticHint);
			}
			return false;
		}

		/** 
		 * the default implementation is overriden here to make it easier to debug
		 * XML providers, now when you select the ProviderDescriptor in the debug
		 * window the provider class name will be displayed
		 * @return the provider class name
		 */
		public String toString() {
			return getElement().getAttribute("class"); 	 //$NON-NLS-1$
			//return (super.toString();
		}

	}

	/**
	 * The singleton instance of the notation service.
	 */
	private final static ViewService instance = new ViewService();

	static {
		instance.configureProviders(DiagramPlugin.getPluginId(), "viewProviders"); //$NON-NLS-1$
	}
	
	/**
	 * Retrieves the singleton instance of the notation service.
	 * 
	 * @return The notation service singleton.
	 */
	public static ViewService getInstance() {
		return instance;
	}

	/**
	 * 
	 */
	protected ViewService() {
		super(true, false);
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.Service#newProviderDescriptor(org.eclipse.core.runtime.IConfigurationElement)
	 */
	protected Service.ProviderDescriptor newProviderDescriptor(
		IConfigurationElement element) {
		return new ProviderDescriptor(element);
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.Service#createPriorityCache()
	 */
	protected Map createPriorityCache() {
		return new HashMap();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.Service#getCacheKey(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	protected Object getCachingKey(IOperation operation) {
		return ((CreateViewOperation) operation).getCachingKey();
	}

	/**
	 * Executes the specified operation using the FIRST execution
	 * strategy.
	 * @return The result of executing the model operation.
	 * @param operation The model operation to be executed.
	 */
	private Object execute(IOperation operation) {
		List results = execute(ExecutionStrategy.FIRST, operation);
		return results.isEmpty() ? null : results.get(0);
	}

	/**
	 * A convenience method to determine whether there is a provider 
	 * that can create a view with the given parameters
	 * @param viewKind
	 * @param semanticAdapter adapts to either <code>Integer<code> or <code>IReference</code>
	 * @param containerView
	 * @param semanticHint
	 * @param index
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 * @return boolean
	 */
	public final boolean provides(Class viewKind, IAdaptable semanticAdapter,
		View containerView, String semanticHint, int index,
		boolean persisted, PreferencesHint preferencesHint) {
		
		assert (viewKind == Diagram.class ||
				viewKind == Edge.class ||
				viewKind == Node.class ) :
				"The default View service does not support " + viewKind.getName() + " as a view kind";//$NON-NLS-1$ //$NON-NLS-2$

		/* if the semantic adapter adapters to the semantic kind */
		if (semanticAdapter != null) {
			if (semanticAdapter.getAdapter(CreateElementRequest.class) != null) {
				return providerExistsFor(new CreateViewForKindOperation(
					viewKind, semanticAdapter, containerView, semanticHint,
					index, preferencesHint));
			}
		}
		if (viewKind == Diagram.class)
			return providerExistsFor(new CreateDiagramViewOperation(
				semanticAdapter, semanticHint, preferencesHint));
		else if (viewKind == Edge.class)
			return providerExistsFor(new CreateConnectorViewOperation(
				semanticAdapter, containerView, semanticHint, index, persisted, preferencesHint));
		else if (viewKind == Node.class)
			return providerExistsFor(new CreateNodeViewOperation(
				semanticAdapter, containerView, semanticHint, index, persisted, preferencesHint));
		return false;
	}

	private boolean providerExistsFor(IOperation operation) {
		return provides(operation);
	}

	/**
	 * A convenience method to create a view with the given parameters
	 * @param viewKind
	 * @param semanticAdapter adapts to <code>IReference<code>
	 * @param containerView
	 * @param semanticHint
	 * @param index
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 * @return IView
	 */
	public final View createView(Class viewKind, IAdaptable semanticAdapter,
		View containerView, String semanticHint, int index,
		boolean persisted, PreferencesHint preferencesHint) {
		
		assert (viewKind == Diagram.class ||
				viewKind == Edge.class ||
				viewKind == Node.class ) :
			    "The default View service does not support " + viewKind.getName() + " as a view kind";//$NON-NLS-1$ //$NON-NLS-2$

		if (viewKind == Diagram.class)
			return createDiagram(semanticAdapter, semanticHint, preferencesHint);
		else if (viewKind == Edge.class)
			return createEdge(semanticAdapter, containerView,
				semanticHint, index, persisted, preferencesHint);
		else if (viewKind == Node.class)
			return createNode(semanticAdapter, containerView, semanticHint,
				index, persisted, preferencesHint);
		return null;
	}

	public final Diagram createDiagram(IAdaptable semanticAdapter,
		String diagramKindType, PreferencesHint preferencesHint) {
		Diagram view = (Diagram) execute(new CreateDiagramViewOperation(
			semanticAdapter, diagramKindType, preferencesHint));
		return view;
	}

	public final Edge createEdge(IAdaptable semanticAdapter,
		View containerView, String semanticHint, int index,
		boolean persisted, PreferencesHint preferencesHint) {
		Edge edge = (Edge) execute(new CreateConnectorViewOperation(
			semanticAdapter, containerView, semanticHint, index, persisted, preferencesHint));
		return edge;
	}

	/**
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @param index
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 * @return
	 */
	public final View createConnectorView(IAdaptable semanticAdapter,
		View containerView, String semanticHint, int index, PreferencesHint preferencesHint) {
		return createEdge(semanticAdapter, containerView,
			semanticHint, index, true, preferencesHint);
	}

	/**
	 * creates a persisted Node
	 * @param semanticElement
	 * @param containerView
	 * @param semanticHint
	 * @param index
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 * @return the created node
	 */
	public final Node createNode(IAdaptable semanticElement,
		View containerView, String semanticHint, int index, PreferencesHint preferencesHint) {
		return createNode(semanticElement, containerView, semanticHint,
			index, true, preferencesHint);
	}

	/**
	 * creates a Node
	 * @param semanticElement
	 * @param containerView
	 * @param semanticHint
	 * @param persisted
	 * @param index
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 * @return the created node
	 */
	public Node createNode(IAdaptable semanticAdapter,
			View containerView, String semanticHint, int index,
			boolean persisted, PreferencesHint preferencesHint) {
		Node node = (Node) execute(new CreateNodeViewOperation(
			semanticAdapter, containerView, semanticHint, index, persisted, preferencesHint));
		return node;
	}
}