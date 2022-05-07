/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.ui.services.modelingassistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gmf.runtime.common.core.service.AbstractProviderConfiguration;

/**
 * A provider configuration for the ModelingAssistantService.  It helps in
 * filtering out and delay loading unrelated providers.
 * 
 * @author cmahoney
 */
public class ModelingAssistantProviderConfiguration
	extends AbstractProviderConfiguration {

	/** constants corresponding to different symbols in the extention schema */
	private static final String CONTEXT = "context"; //$NON-NLS-1$

	private static final String OPERATION_ID = "operationId"; //$NON-NLS-1$

	private static final String ELEMENTS = "elements"; //$NON-NLS-1$

	/** a map of context descriptors supported by this provider to their target contexts */
	private List contextDescriptors = new ArrayList();

	/**
	 * Builds a new provider contribution descriptor by parsing its configuration element
	 * 
	 * @param configElement A provider configuration element
	 * @return A provider XML contribution descriptor
	 */
	public static ModelingAssistantProviderConfiguration parse(
		IConfigurationElement configElement) {
		assert configElement != null: "null provider configuration element"; //$NON-NLS-1$
		return new ModelingAssistantProviderConfiguration(configElement);
	}

	/**
	 * Creates a new <code>ModelingAssistantProviderConfiguration</code> instance
	 * given a provider configuration element
	 * 
	 * @param configElement The provider XML configuration element
	 */
	private ModelingAssistantProviderConfiguration(
		IConfigurationElement configElement) {
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
			String operationType = configChildren[i].getAttribute(OPERATION_ID);
			List elements = getObjectList(configChildren[i]
				.getAttribute(ELEMENTS), objects, configElement);
			if (operationType != null || elements != null)
				contextDescriptors.add(new ContextDescriptor(operationType,
					elements));
		}
	}

	/**
	 * Determines if the provider understands the given context
	 * The provider understands the request if:
	 * 1- There are no listed provider contexts
	 * 2- The context matches one of the provider contexts 
	 * 
	 * @param operationType The operation type
	 * @param context  The element context
	 * @return boolean <code>true</code> if it supports; <code>false</code> otherwise
	 */
	public boolean supports(String operationType, IAdaptable context) {
		if (contextDescriptors.isEmpty())
			return true;

		Iterator iter = contextDescriptors.iterator();
		while (iter.hasNext()) {
			ContextDescriptor descriptor = (ContextDescriptor) iter.next();
			if (descriptor.matches(operationType, context))
				return true;
		}
		return false;
	}

	/**
	 * A descriptor for a provider context
	 */
	private static class ContextDescriptor {

		/** the operation type */
		private final String operationType;

		/** the elements */
		private final List elements;

		/**
		 * creates a new context descriptor from its context info
		 * 
		 * @param operationType The operation type
		 * @param elements The elements
		 */
		public ContextDescriptor(String operationType, List elements) {
			this.operationType = operationType;
			this.elements = elements;
		}

		/**
		 * Determines if the context descriptor matches the given context
		 * 
		 * @param anOperationType The operation type
		 * @param context The element context
		 * @return boolean
		 */
		public boolean matches(String anOperationType, IAdaptable context) {
			if (operationType != null) {
				if (!operationType.equals(anOperationType))
					return false;
			}
			if (elements != null) {
				if (!objectMatches(context, elements))
					return false;
			}
			return true;
		}
	}

}
