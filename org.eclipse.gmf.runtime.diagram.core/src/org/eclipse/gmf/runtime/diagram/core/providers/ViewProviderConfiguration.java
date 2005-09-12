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

package org.eclipse.gmf.runtime.diagram.core.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;

import org.eclipse.gmf.runtime.common.core.service.AbstractProviderConfiguration;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author melaasar
 *
 * A provider configuration for the ViewService. It helps in filtering out
 * and delay loading unrelated providers.
 */
public class ViewProviderConfiguration extends AbstractProviderConfiguration {

	/** constants corresponding to different symbols in the extention schema */
	private static final String CONTEXT = "context"; //$NON-NLS-1$
	private static final String VIEW_CLASS = "viewClass"; //$NON-NLS-1$
	private static final String ELEMENTS = "elements"; //$NON-NLS-1$
	private static final String CONTAINERVIEWS = "containerViews"; //$NON-NLS-1$
	private static final String SEMANTICHINTS = "semanticHints"; //$NON-NLS-1$

	/** a map of requests supported by this provider to their target contexts*/
	private List requests = new ArrayList();

	/**
	 * Builds a new provider contribution descriptor by parsing its configuration element
	 * 
	 * @param configElement A provider configuration element
	 * @return A provider XML contribution descriptor
	 */
	public static ViewProviderConfiguration parse(IConfigurationElement configElement) {		
		assert null != configElement : "Null provider configuration elemen in ViewProviderConfiguration";//$NON-NLS-1$
		return new ViewProviderConfiguration(configElement);
	}

	/**
	 * Creates a new <code>ProviderContributionDescriptor</code> instance
	 * given a provider configuration element
	 * 
	 * @param configElement The provider XML configuration element
	 */
	private ViewProviderConfiguration(IConfigurationElement configElement) {
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
			String viewClassName = configChildren[i].getAttribute(VIEW_CLASS);
			List elements =
				getObjectList(
					configChildren[i].getAttribute(ELEMENTS),
					objects, configElement);
			List parentViews =
				getObjectList(
					configChildren[i].getAttribute(CONTAINERVIEWS),
					objects, configElement);
			Set semanticHints =
				getStrings(configChildren[i].getAttribute(SEMANTICHINTS));
			if (viewClassName != null
				|| elements != null
				|| parentViews != null
				|| semanticHints != null)
				requests.add(
					new ContextDescriptor(
						viewClassName,
						elements,
						parentViews,
						semanticHints));
		}
	}

	/**
	 * Determines if the provider understands the given context
	 * The provider understands the request if:
	 * 1- There are no listed provider contexts
	 * 2- The context matches one of the provider contexts 
	 * 
	 * @param viewKind The view kind
	 * @param semanticAdapter The semantic adapter
	 * @param containerView The container view
	 * @param semanticHint the factory hint
	 * @return boolean <code>true</code> if it supports; <code>false</code> otherwise
	 */
	public boolean supports(
		Class viewKind,
		IAdaptable semanticAdapter,
		View containerView,
		String semanticHint) {
		if (requests.isEmpty())
			return true;

		for (Iterator iter = requests.iterator();iter.hasNext();) {
			ContextDescriptor request = (ContextDescriptor) iter.next();
			if (request
				.matches(
					viewKind,
					semanticAdapter,
					containerView,
					semanticHint))
				return true;
		}
		return false;
	}

	/**
	 * A descriptor for a provider context
	 */
	private static class ContextDescriptor {
		/** the view's kind class name */
		private String viewClassName;
		/** the request's elements */
		private final List elements;
		/** the request's parent views */
		private final List parentViews;
		/** the request's semantic hints */
		private final Set semanticHints;

		/**
		 * creates a new context descriptor from some context
		 * 
		 * @param viewKind The view kind
		 * @param semanticAdapter The semantic adapter
		 * @param containerView The container view
		 * @param semanticHint the factory hint
		 */
		public ContextDescriptor(
			String viewClassName,
			List elements,
			List parentViews,
			Set semanticHints) {
			this.viewClassName = viewClassName;
			this.elements = elements;
			this.parentViews = parentViews;
			this.semanticHints = semanticHints;
		}

		/**
		 * Determines if the context descriptor matches the given context
		 * 
		 * @param viewKind The view kind
		 * @param semanticAdapter The semantic adapter
		 * @param containerView The container view
		 * @param semanticHint the semantic hint
		 * @return boolean
		 */
		public boolean matches(
			Class viewKind,
			IAdaptable semanticAdapter,
			View containerView,
			String semanticHint) {
			if (viewClassName != null) {
				if (!viewClassName.equals(viewKind.getName()))
					return false;
			}
			if (semanticHints != null) {
				if (!semanticHints.contains(semanticHint))
					return false;
			}
			if (elements != null) {
				if (!objectMatches(semanticAdapter, elements))
					return false;
			}
			if (parentViews != null) {
				if (!objectMatches(containerView, parentViews))
					return false;
			}
			return true;
		}
	}

}
