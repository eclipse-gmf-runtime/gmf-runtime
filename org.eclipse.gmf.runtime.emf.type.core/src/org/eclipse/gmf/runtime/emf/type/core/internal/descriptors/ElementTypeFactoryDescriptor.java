/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.internal.descriptors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.emf.type.core.IElementTypeFactory;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePlugin;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePluginStatusCodes;
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.EMFTypeCoreMessages;

import com.ibm.icu.util.StringTokenizer;

/**
 * Descriptor for an element type factory. Used to delay loading of the plugin
 * in which the factory class is defined.
 * 
 * @author ldamus
 */
public class ElementTypeFactoryDescriptor {

	/**
	 * The configuration element.
	 */
	private final IConfigurationElement configElement;

	/**
	 * The element type factory.
	 */
	private IElementTypeFactory elementTypeFactory;

	/**
	 * The factory name.
	 */
	private String factoryName;

	/**
	 * The name of the element factory kind. Element types use the kind name to
	 * declare that they should be created by this factory.
	 */
	private final String kindName;

	/**
	 * The custom parameters that are supported by this factory.
	 */
	private final List params;

	/**
	 * Creates a new element type factory descriptor.
	 * 
	 * @param configElement
	 *            the configuration element.
	 * @throws CoreException
	 *             when the type ID or display name have not been correctly
	 *             specified in the configuration element
	 */
	public ElementTypeFactoryDescriptor(IConfigurationElement configElement)
		throws CoreException {

		this.configElement = configElement;

		// FACTORY NAME
		this.factoryName = configElement
			.getAttribute(ElementTypeXmlConfig.A_FACTORY);

		if (factoryName == null) {
			throw EMFTypePluginStatusCodes.getFactoryInitException(
				StringStatics.BLANK,
				EMFTypeCoreMessages.elementTypeFactory_reason_no_factory_WARN_);
		}

		// KIND NAME
		this.kindName = configElement.getAttribute(ElementTypeXmlConfig.A_KIND);
		if (kindName == null) {
			throw EMFTypePluginStatusCodes.getFactoryInitException(
				factoryName,
				EMFTypeCoreMessages.elementTypeFactory_reason_no_kind_WARN_);
		}

		// CUSTOM PARAMETERS
		String paramString = configElement
			.getAttribute(ElementTypeXmlConfig.A_PARAMS);
		params = new ArrayList();

		if (paramString != null) {
			StringTokenizer t = new StringTokenizer(paramString,
				StringStatics.COMMA);

			while (t.hasMoreTokens()) {
				String nextParam = t.nextToken().trim();
				params.add(nextParam);
			}
		}
	}

	/**
	 * Gets the element type factory. Will cause the factory to be created if
	 * this is the first time it is called.
	 * <P>
	 * May cause plugin containing the element type factory and element type
	 * class to be loaded.
	 * 
	 * @return the element type factory
	 */
	public IElementTypeFactory getElementTypeFactory() {
		
		if (elementTypeFactory == null) {
			
			if (factoryName != null) {
				try {
					elementTypeFactory = (IElementTypeFactory) configElement
						.createExecutableExtension(ElementTypeXmlConfig.A_FACTORY);

				} catch (CoreException e) {
					Log
							.error(
									EMFTypePlugin.getPlugin(),
									EMFTypePluginStatusCodes.ELEMENT_TYPE_FACTORY_CLASS_NOT_FOUND,
									EMFTypeCoreMessages
											.bind(
													EMFTypeCoreMessages.elementTypeFactory_class_not_found_ERROR_,
													factoryName), e);
					// Don't recompute the factory if is has failed once.
					factoryName = null;
				}
			}
		}
		return elementTypeFactory;
	}

	/**
	 * Gets the factory kind name.
	 * 
	 * @return the factory kind name
	 */
	public String getKindName() {
		return kindName;
	}

	/**
	 * Gets the list of custom parameter names.
	 * 
	 * @return the list of custom parameter names
	 */
	public List getParams() {
		return params;
	}
}