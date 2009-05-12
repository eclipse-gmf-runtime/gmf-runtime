/******************************************************************************
 * Copyright (c) 2004, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.services.decorator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gmf.runtime.common.core.service.AbstractProviderConfiguration;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;


/**
 * A provider configuration for the Decorator Service. It helps in filtering out
 * and delay the loading of unrelated providers.
 * 
 * @see AbstractProviderConfiguration
 * 
 * @author cmahoney
 */
public class DecoratorProviderConfiguration
	extends AbstractProviderConfiguration {

	/** constants corresponding to different symbols in the extension schema */
	private static final String CONTEXT = "context"; //$NON-NLS-1$
	private static final String DECORATOR_TARGETS = "decoratorTargets"; //$NON-NLS-1$

	/** a list of object descriptors supported by this provider */
	private List contextDescriptors = new ArrayList();

	/**
	 * Creates and builds a new provider contribution descriptor (<code>DecoratorProviderConfiguration</code>) by parsing its configuration element.
	 * 
	 * @param configElement A provider XML configuration element
	 * @return A provider XML contribution descriptor
	 */
	public static DecoratorProviderConfiguration parse(IConfigurationElement configElement) {
		Assert.isNotNull(configElement, "null provider configuration element"); //$NON-NLS-1$
		return new DecoratorProviderConfiguration(configElement);
	}

	/**
	 * Creates a new <code>ProviderContributionDescriptor</code> instance
	 * by parsing its configuration element.
	 * 
	 * @param configElement The provider XML configuration element
	 */
	private DecoratorProviderConfiguration(IConfigurationElement configElement) {
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
			List decoratorTargets =
				getObjectList(
					configChildren[i].getAttribute(DECORATOR_TARGETS),
					objects, configElement);
			if (decoratorTargets != null)
				contextDescriptors.add(new ContextDescriptor(decoratorTargets));
		}
	}

	/**
	 * Determines if the provider understands the given context
	 * The provider understands the request if:
	 * 1- There are no listed provider contexts
	 * 2- The context matches one of the provider contexts 
	 * 
	 * @param decoratorTarget the decoratorTarget
	 * @return boolean <code>true</code> if it supports; <code>false</code> otherwise
	 */
	public boolean supports(IDecoratorTarget decoratorTarget) {
		if (contextDescriptors.isEmpty())
			return true;

		Iterator iter = contextDescriptors.iterator();
		while (iter.hasNext()) {
			ContextDescriptor descriptor = (ContextDescriptor) iter.next();
			if (descriptor.matches(decoratorTarget))
				return true;
		}
		return false;
	}

	/**
	 * A descriptor for a provider context
	 */
	private static class ContextDescriptor {

		/** the decoratorTargets */
		private final List decoratorTargets;

		/**
		 * creates a new context descriptor from some context info
		 * 
		 * @param decoratorTargets the list of decoratorTargets
		 */
		public ContextDescriptor(List decoratorTargets) {
			this.decoratorTargets = decoratorTargets;
		}

		/**
		 * Determines if the context descriptor matches the given context
		 * 
		 * @param decoratorTarget the decoratorTarget
		 * @return boolean
		 */
		public boolean matches(IDecoratorTarget decoratorTarget) {
			if (decoratorTargets != null) {
				if (!objectMatches(decoratorTarget, decoratorTargets))
					return false;
			}
			return true;
		}
	}

}
