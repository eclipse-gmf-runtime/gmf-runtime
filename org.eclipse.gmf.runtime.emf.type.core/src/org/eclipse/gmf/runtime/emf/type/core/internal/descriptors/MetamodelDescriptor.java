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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePlugin;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePluginStatusCodes;
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.EMFTypeCoreMessages;

/**
 * Describes a metamodel. Loading the metamodel is delayed until access is made
 * to {{@link #getEPackage()}.
 * 
 * @author ldamus
 */
public class MetamodelDescriptor {

	/**
	 * The metamodel package.
	 */
	private EPackage ePackage;
	
	/**
	 * The namespace URI of the metamodel package.
	 */
	private final String nsURI;
	
	/**
	 * Flag indicating that a metamodel could not be found with my namespace
	 * URI.
	 */
	private boolean metamodelNotFound = false;

	/**
	 * Creates a new metamodel descriptor from a configuration element.
	 * 
	 * @param configElement
	 *            the configuration element
	 * @throws CoreException
	 *             when the EPackage namespace URI is not
	 *             specified in the configuration element
	 */
	public MetamodelDescriptor(IConfigurationElement configElement)
		throws CoreException {

		nsURI = configElement.getAttribute(ElementTypeXmlConfig.A_NSURI);
		if (nsURI == null) {
			throw EMFTypePluginStatusCodes.getMetamodelInitException(
				StringStatics.BLANK, EMFTypeCoreMessages.metamodel_reason_no_nsURI_WARN_);
		}
	}

	/**
	 * Gets the metamodel package.
	 * 
	 * @return the metamodel package
	 */
	public EPackage getEPackage() {
		
		if (ePackage == null & !metamodelNotFound) {
			ePackage = (getNsURI() != null) ? EPackage.Registry.INSTANCE
					.getEPackage(getNsURI()) : null;

			if (ePackage == null) {
				metamodelNotFound = true;
				Log
						.error(
								EMFTypePlugin.getPlugin(),
								EMFTypePluginStatusCodes.METAMODEL_NOT_INITED,
								EMFTypeCoreMessages
										.bind(
												EMFTypeCoreMessages.metamodel_not_init_WARN_,
												nsURI,
												EMFTypeCoreMessages.metamodel_reason_nsURI_not_found_WARN_));
			}
		}
		return ePackage;
	}
	
	/**
	 * Gets the namespace URI for the metamodel.
	 * 
	 * @return the namespace URI
	 */
	public String getNsURI() {
		return nsURI;
	}
}