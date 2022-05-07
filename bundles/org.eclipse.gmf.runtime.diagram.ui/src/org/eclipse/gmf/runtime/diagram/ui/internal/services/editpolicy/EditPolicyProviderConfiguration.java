/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.services.editpolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.core.service.AbstractProviderConfiguration;

/**
 * A provider configuration for the EditPolicy Service. It helps in filtering out
 * and delay loading unrelated providers.
 * 
 * @author chmahone
 */
public class EditPolicyProviderConfiguration
	extends AbstractProviderConfiguration {

	/** constants corresponding to different symbols in the extention schema */
	private static final String CONTEXT = "context"; //$NON-NLS-1$
	private static final String EDITPARTS = "editparts"; //$NON-NLS-1$

	/** a map of requests supported by this provider to their target contexts */
	private List contextDescriptors = new ArrayList();

	/**
	 * Builds a new provider contribution descriptor by parsing its configuration element
	 * 
	 * @param configElement A provider configuration element
	 * @return A provider XML contribution descriptor
	 */
	public static EditPolicyProviderConfiguration parse(IConfigurationElement configElement) {
		Assert.isNotNull(configElement, "null provider configuration element"); //$NON-NLS-1$
		return new EditPolicyProviderConfiguration(configElement);
	}

	/**
	 * Creates a new <code>ProviderContributionDescriptor</code> instance
	 * given a provider configuration element
	 * 
	 * @param configElement The provider XML configuration element
	 */
	private EditPolicyProviderConfiguration(IConfigurationElement configElement) {
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
			List editParts =
				getObjectList(
					configChildren[i].getAttribute(EDITPARTS),
					objects, configElement);
			if (editParts != null)
				contextDescriptors.add(new ContextDescriptor(editParts));
		}
	}

	/**
	 * Determines if the provider understands the given context
	 * The provider understands the request if:
	 * 1- There are no listed provider contexts
	 * 2- The context matches one of the provider contexts 
	 * 
	 * @param editPart the editPart
	 * @return boolean <code>true</code> if it supports; <code>false</code> otherwise
	 */
	public boolean supports(EditPart editPart) {
		if (contextDescriptors.isEmpty())
			return true;

		Iterator iter = contextDescriptors.iterator();
		while (iter.hasNext()) {
			ContextDescriptor descriptor = (ContextDescriptor) iter.next();
			if (descriptor.matches(editPart))
				return true;
		}
		return false;
	}

	/**
	 * A descriptor for a provider context
	 */
	private static class ContextDescriptor {

		/** the editparts */
		private final List editParts;

		/**
		 * creates a new context descriptor from some context info
		 * 
		 * @param editParts the list of editParts
		 */
		public ContextDescriptor(List editParts) {
			this.editParts = editParts;
		}

		/**
		 * Determines if the context descriptor matches the given context
		 * 
		 * @param editPart the editPart
		 * @return boolean
		 */
		public boolean matches(EditPart editPart) {
			if (editParts != null) {
				if (!objectMatches(editPart, editParts))
					return false;
			}
			return true;
		}
	}

}
