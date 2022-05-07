/******************************************************************************
 * Copyright (c) 2006, 2015 IBM Corporation, Christian W. Damus, and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 *    Christian W. Damus - bug 457888
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.internal.impl;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.type.core.ClientContext;
import org.eclipse.gmf.runtime.emf.type.core.IElementMatcher;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypeDebugOptions;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePlugin;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePluginStatusCodes;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.ElementTypeXmlConfig;
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.EMFTypeCoreMessages;

/**
 * The implementation of the client context that is created using a
 * configuration element.
 * 
 * @author ldamus
 */
public class XMLClientContext extends ClientContext {

	private static final String E_ENABLEMENT = "enablement"; //$NON-NLS-1$

	private static final String E_MATCHER = "matcher"; //$NON-NLS-1$

	/**
	 * Initializes me with my XML configuration.
	 * 
	 * @param config
	 *            my XML configuration element
	 * @throws CoreException
	 *             on any problem in accessing the <code>config</code>uration
	 *             or if anything is missing or incorrect
	 */
	public XMLClientContext(IConfigurationElement config) throws CoreException {
		super(initializeId(config), initializeMatcher(config, config
				.getAttribute(ElementTypeXmlConfig.A_ID)));
	}
	
	@Override
	protected boolean computeDynamic() {
		return false;
	}

	/**
	 * Gets my ID from the specified XML <code>config</code>.
	 * 
	 * @param config
	 *            my XML configuration
	 * @return my ID (never <code>null</code>)
	 * @throws CoreException
	 *             if my ID is not specified
	 */
	private static String initializeId(IConfigurationElement config)
			throws CoreException {
		String result = config.getAttribute(ElementTypeXmlConfig.A_ID);

		if (result == null) {
			CoreException ce = EMFTypePluginStatusCodes
					.getContextInitException(config.getContributor().getName(),
							EMFTypeCoreMessages.context_no_id_ERROR_);

			Trace.throwing(EMFTypePlugin.getPlugin(),
					EMFTypeDebugOptions.EXCEPTIONS_THROWING,
					XMLClientContext.class, "initializeId", ce); //$NON-NLS-1$

			throw ce;
		}

		return result;
	}

	/**
	 * Gets my matcher from the specified XML <code>config</code>.
	 * 
	 * @param config
	 *            my XML configuration
	 * @param id
	 *            the client context ID
	 * @return my matcher (never <code>null</code>)
	 * @throws CoreException
	 *             if my matcher is not specified or something went wrong in
	 *             initializing it
	 */
	private static IElementMatcher initializeMatcher(
			IConfigurationElement config, String id) throws CoreException {
		IElementMatcher result = null;

		IConfigurationElement[] enablement = config.getChildren(E_ENABLEMENT);
		if (enablement.length > 0) {
			result = initializeExpressionMatcher(enablement[0], id);
		} else {
			IConfigurationElement[] custom = config.getChildren(E_MATCHER);
			if (custom.length > 0) {
				result = initializeCustomMatcher(custom[0], id);
			}
		}

		if (result == null) {
			CoreException ce = EMFTypePluginStatusCodes
					.getContextInitException(id,
							EMFTypeCoreMessages.context_no_matcher_ERROR_);

			Trace.throwing(EMFTypePlugin.getPlugin(),
					EMFTypeDebugOptions.EXCEPTIONS_THROWING,
					XMLClientContext.class, "initializeMatcher", ce); //$NON-NLS-1$

			throw ce;
		}

		return result;
	}

	/**
	 * Creates an expression-based matcher from the specified XML
	 * <code>enablement</code> expression.
	 * 
	 * @param enablement
	 *            my XML expression
	 * @param id
	 *            the client context ID
	 * @return the matcher (never <code>null</code>)
	 * @throws CoreException
	 *             if something is malformed in the expression
	 */
	private static IElementMatcher initializeExpressionMatcher(
			IConfigurationElement enablement, String id) throws CoreException {

		return new XMLExpressionMatcher(enablement, id);
	}

	/**
	 * Instantiates a custom matcher class specified in the XML.
	 * 
	 * @param config
	 *            a matcher configuration element
	 * @param id
	 *            the client context ID
	 * @return the matcher (never <code>null</code>)
	 * @throws CoreException
	 *             if something is malformed in the expression
	 */
	private static IElementMatcher initializeCustomMatcher(
			IConfigurationElement config, String id) throws CoreException {
		Object result = config
				.createExecutableExtension(ElementTypeXmlConfig.A_CLASS);

		if (!(result instanceof IElementMatcher)) {

			CoreException ce = EMFTypePluginStatusCodes
					.getContextInitException(
							id,
							EMFTypeCoreMessages.context_matcher_wrong_class_ERROR_);

			Trace.throwing(EMFTypePlugin.getPlugin(),
					EMFTypeDebugOptions.EXCEPTIONS_THROWING,
					XMLClientContext.class, "initializeMatcher", ce); //$NON-NLS-1$

			throw ce;
		}

		return (IElementMatcher) result;
	}
}
