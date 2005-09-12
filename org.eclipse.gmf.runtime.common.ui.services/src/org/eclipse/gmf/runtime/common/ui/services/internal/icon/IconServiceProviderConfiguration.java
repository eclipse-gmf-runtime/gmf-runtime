/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.internal.icon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;

import org.eclipse.gmf.runtime.common.core.service.AbstractProviderConfiguration;

/**
 * A provider configuration for the <code>IconService</code>. It helps in filtering out
 * and delay loading unrelated providers.
 * 
 * @author chmahone
 */
public class IconServiceProviderConfiguration extends AbstractProviderConfiguration {

	/** constants corresponding to different symbols in the extention schema */
	private static final String CONTEXT = "context"; //$NON-NLS-1$
	private static final String ELEMENTS = "elements"; //$NON-NLS-1$

	/** a map of requests supported by this provider to their target contexts*/
	private List requests = new ArrayList();

	/**
	 * Builds a new provider contribution descriptor by parsing its configuration element
	 * 
	 * @param configElement A provider configuration element
	 * @return A provider XML contribution descriptor
	 */
	public static IconServiceProviderConfiguration parse(IConfigurationElement configElement) {
		assert null != configElement : "null provider configuration element"; //$NON-NLS-1$
		return new IconServiceProviderConfiguration(configElement);
	}

	/**
	 * Creates a new <code>ProviderContributionDescriptor</code> instance
	 * given a provider configuration element
	 * 
	 * @param configElement The provider XML configuration element
	 */
	private IconServiceProviderConfiguration(IConfigurationElement configElement) {
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
			List elements =
				getObjectList(
					configChildren[i].getAttribute(ELEMENTS),
					objects,
					configElement);
			if (elements != null)
				requests.add(
					new ContextDescriptor(elements));
		}
	}

	/**
	 * Determines if the provider understands the given context
	 * The provider understands the request if:
	 * 1- There are no listed provider contexts
	 * 2- The context matches one of the provider contexts 
	 * 
	 * @param elementAdapter adaptable to <code>IElement</code>
	 * @return boolean <code>true</code> if it supports; <code>false</code> otherwise
	 */
	public boolean supports(IAdaptable elementAdapter) {
		if (requests.isEmpty())
			return true;

		Iterator iter = requests.iterator();
		while (iter.hasNext()) {
			ContextDescriptor request = (ContextDescriptor) iter.next();
			if (request.matches(elementAdapter))
				return true;
		}
		return false;
	}

	/**
	 * A descriptor for a provider context
	 */
	private static class ContextDescriptor {
		/** the element adapters */
		private final List elementAdapters;

		/**
		 * creates a new context descriptor from its context info
		 * 
		 * @param elementAdapters The element adapters
		 */
		public ContextDescriptor(List elementAdapters) {
			this.elementAdapters = elementAdapters;
		}

		/**
		 * Determines if the context descriptor matches the given context
		 * 
	 	 * @param elementAdapter adaptable to <code>IElement</code>
		 * @return boolean
		 */
		public boolean matches(IAdaptable elementAdapter) {
			return objectMatches(elementAdapter, elementAdapters);
		}
	}

}
