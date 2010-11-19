/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gmf.runtime.common.core.service.AbstractProviderConfiguration;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author melaasar
 *
 * A provider configuration for the EditPartService. It helps in filtering out
 * and delay loading unrelated providers.
 */
public class EditPartProviderConfiguration extends AbstractProviderConfiguration {

	/** constants corresponding to different symbols in the extention schema */
	private static final String CONTEXT = "context"; //$NON-NLS-1$
	private static final String VIEWS = "views"; //$NON-NLS-1$
	private static final String PROVIDES_ROOT_EDITPART = "providesRootEditPart"; //$NON-NLS-1$

	/** a map of requests supported by this provider to their target contexts*/
	private List requests = new ArrayList();

	/**
	 * Builds a new provider contribution descriptor by parsing its configuration element
	 * 
	 * @param configElement A provider configuration element
	 * @return A provider XML contribution descriptor
	 */
	public static EditPartProviderConfiguration parse(IConfigurationElement configElement) {
		Assert.isNotNull(configElement, "null provider configuration element"); //$NON-NLS-1$
		return new EditPartProviderConfiguration(configElement);
	}

	/**
	 * Creates a new <code>ProviderContributionDescriptor</code> instance
	 * given a provider configuration element
	 * 
	 * @param configElement The provider XML configuration element
	 */
	private EditPartProviderConfiguration(IConfigurationElement configElement) {
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
			List views =
				getObjectList(
					configChildren[i].getAttribute(VIEWS),
					objects, configElement);
			if (views != null)
				requests.add(
					new ContextDescriptor(views));
			
			String s = configChildren[i].getAttribute(PROVIDES_ROOT_EDITPART);
			if (s != null) {
				requests.add(new ContextDescriptor(Boolean.valueOf(s)
					.booleanValue()));
			}
		}
	}

	/**
	 * Determines if the provider understands the given context
	 * The provider understands the request if:
	 * 1- There are no listed provider contexts
	 * 2- The context matches one of the provider contexts 
	 * 
	 * @param view The view
	 * @return boolean <code>true</code> if it supports; <code>false</code> otherwise
	 */
	public boolean supports(View view) {
		if (requests.isEmpty())
			return true;

		Iterator iter = requests.iterator();
		while (iter.hasNext()) {
			ContextDescriptor request = (ContextDescriptor) iter.next();
			if (request.matches(view))
				return true;
		}
		return false;
	}

	/**
	 * Determines if the provider supports creation of a <code>RootEditPart</code>
	 * The provider understands the request if:
	 * 1- There are no listed provider contexts
	 * 2- The context has <code>providesRootEditPart</code> set to true 
	 * 
	 * @return boolean <code>true</code> if it supports; <code>false</code> otherwise
	 */
	public boolean supportsRootEditPart() {
		if (requests.isEmpty())
			return true;

		Iterator iter = requests.iterator();
		while (iter.hasNext()) {
			ContextDescriptor request = (ContextDescriptor) iter.next();
			if (request.providesRootEditPart())
				return true;
		}
		return false;
	}
	/**
	 * A descriptor for a provider context
	 */
	private static class ContextDescriptor {
		/** the views */
		private List views;
		
		/** boolean flag for <code>RootEditPart</code> creation */
		private boolean providesRootEditPart;

		/**
		 * creates a new context descriptor from its context info
		 * 
		 * @param views The views
		 */
		public ContextDescriptor(List views) {
			this.views = views;
		}
		
		/**
		 * creates a new context descriptor from its context info
		 * 
		 * @param boolean
		 *            flag for <code>RootEditPart</code> creation
		 */
		public ContextDescriptor(boolean providesRootEditPart) {
			this.providesRootEditPart = providesRootEditPart;
		}
		
		/**
		 * Determines if the context descriptor matches the given context
		 * 
		 * @param view The view context
		 * @return boolean
		 */
		public boolean matches(View view) {
			return views != null && objectMatches(view, views);
		}
		
		/**
		 * Returns true if this context provides for
		 * <code>RootEditPart</code> creation.
		 * 
		 * @return boolean
		 */
		public boolean providesRootEditPart() {
			return providesRootEditPart;
		}	
	}
}
