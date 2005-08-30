/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal.services.semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;

import org.eclipse.gmf.runtime.common.core.service.AbstractProviderConfiguration;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * A provider configuration for the SemanticService. It helps in filtering out
 * and delay loading unrelated providers.
 * 
 * @author melaasar
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.core.*
 *
 * @deprecated Use the Element Type API
 *             {@link org.eclipse.gmf.runtime.emf.type.core.IElementType#getEditCommand(IEditCommandRequest)}
 *             to get semantic commands.
 */
class SemanticProviderConfiguration extends AbstractProviderConfiguration{

	/** constants corresponding to different symbols in the extention schema */
	private static final String CONTEXT = "context"; //$NON-NLS-1$
	private static final String TYPE = "type"; //$NON-NLS-1$
	private static final String CONTEXTS = "contexts"; //$NON-NLS-1$

	/** a map of requests supported by this provider to their target contexts*/
	private List requests = new ArrayList();

	/**
	 * Builds a new provider contribution descriptor by parsing its configuration element
	 * 
	 * @param configElement A provider configuration element
	 * @return A provider XML contribution descriptor
	 */
	public static SemanticProviderConfiguration parse(IConfigurationElement configElement) {		
		assert null != configElement : "Null provider configuration element";//$NON-NLS-1$		
		return new SemanticProviderConfiguration(configElement);
	}

	/**
	 * Creates a new <code>ProviderContributionDescriptor</code> instance
	 * given a provider configuration element
	 * 
	 * @param configElement The provider XML configuration element
	 */
	private SemanticProviderConfiguration(IConfigurationElement configElement) {
		IConfigurationElement configChildren[];

		// read the object descriptors
		Map objects = new HashMap();
		configChildren = configElement.getChildren(OBJECT);
		for (int i = 0; i < configChildren.length; i++) {
			String id = configChildren[i].getAttribute(ID);
			if (id != null)
				objects.put(id, new ObjectDescriptor(configChildren[i]));
		}

		// read the context descriptors
		configChildren = configElement.getChildren(CONTEXT);
		for (int i = 0; i < configChildren.length; i++) {
			String requestType = configChildren[i].getAttribute(TYPE);
			List requestContexts =
				getObjectList(
					configChildren[i].getAttribute(CONTEXTS),
					objects, configElement);
			if (requestType != null || requestContexts != null)
				requests.add(
					new ContextDescriptor(requestType, requestContexts));
		}
	}

	/**
	 * Determines if the provider understands the given context
	 * The provider understands the request if:
	 * 1- There are no listed provider contexts
	 * 2- The context matches one of the provider contexts 
	 * 
	 * @param semanticRequest The semantic request
	 * @return boolean <code>true</code> if it supports; <code>false</code> otherwise
	 */
	public boolean supports(SemanticRequest semanticRequest) {
		if (requests.isEmpty())
			return true;

		Iterator iter = requests.iterator();
		while (iter.hasNext()) {
			ContextDescriptor request = (ContextDescriptor) iter.next();
			if (request.matches(semanticRequest))
				return true;
		}
		return false;
	}

	/**
	 * A descriptor for a provider context
	 */
	private static class ContextDescriptor {
		/** the request type */
		private final String type;
		/** the request contexts */
		private final List contexts;

		/**
		 * creates a new context descriptor from some context info
		 * 
		 * @param type The request type
		 * @param contexts The request contexts
		 */
		public ContextDescriptor(String type, List contexts) {
			this.type = type;
			this.contexts = contexts;
		}

		/**
		 * Determines if the context descriptor matches the given context
		 * 
		 * @param request the given semantic request
		 * @return boolean
		 */
		public boolean matches(SemanticRequest request) {
			if (type != null) {
				if (!type.equals(request.getRequestType()))
					return false;
			}
			if (contexts != null) {
				if (!objectMatches(request.getContext(), contexts))
					return false;
			}
			return true;
		}
	}

}
