/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.internal.descriptors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePluginStatusCodes;

/**
 * Describes a metamodel.
 * 
 * @author ldamus
 */
public class MetamodelDescriptor {

	/**
	 * The metamodel package.
	 */
	private final EPackage ePackage;

	/**
	 * Creates a new metamodel descriptor from a configuration element.
	 * 
	 * @param configElement
	 *            the configuration element
	 * @throws CoreException
	 *             when the EPackage can't be found for the namespace URI
	 *             specified in the configuration element
	 */
	public MetamodelDescriptor(IConfigurationElement configElement)
		throws CoreException {

		String nsURI = configElement.getAttribute(ElementTypeXmlConfig.A_NSURI);
		if (nsURI == null) {
			throw EMFTypePluginStatusCodes.getMetamodelInitException(
				StringStatics.BLANK, EMFTypePluginStatusCodes.METAMODEL_NO_NSURI_KEY);
		}
		ePackage = (nsURI != null) ? EPackage.Registry.INSTANCE
			.getEPackage(nsURI)
			: null;

		if (ePackage == null) {
			throw EMFTypePluginStatusCodes.getMetamodelInitException(
				nsURI, EMFTypePluginStatusCodes.METAMODEL_NSURI_NOT_FOUND_KEY);
		}
	}

	/**
	 * Gets the metamodel package.
	 * 
	 * @return the metamodel package
	 */
	public EPackage getEPackage() {
		return ePackage;
	}
}