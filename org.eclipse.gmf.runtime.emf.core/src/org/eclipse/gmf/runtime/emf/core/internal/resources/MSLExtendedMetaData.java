/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.resources;

import java.util.Iterator;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLInfoImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLMapImpl;

/**
 * Extended metadata for MSL resources. Provides forward-compatibility support
 * by matching namespace URIs.
 * 
 * @author khussey
 *  
 */
public class MSLExtendedMetaData
	extends BasicExtendedMetaData {

	/**
	 * The registry of 'compatible' packages.
	 */
	private final EPackage.Registry compatibilityRegistry = new EPackageRegistryImpl();

	/**
	 * The map used to map compatible packages to their original namespaces.
	 */
	private final XMLResource.XMLMap xmlMap = new XMLMapImpl();

	/**
	 * Constructs a new MSL extended metadata.
	 */
	public MSLExtendedMetaData() {
		// NOTE: (cmcgee) The way that the EMF package registries work will not allow us
		//  to iterate over the key set and get access to all of the registered
		//  EPackages unless we use the global registry. This is a requirement for our
		//  loose matching of namespace URIs, which is what we do in this class. Unless
		//  this problem is resolved, we can only use the global registry.
		super(EPackage.Registry.INSTANCE);
	}

	/**
	 * Retrieves a pattern for the specified namespace URI by replacing version
	 * information (i.e. digits) with wildcards.
	 * 
	 * @param nsURI
	 *            The namespace URI for which to retrieve a pattern.
	 * @return The namespace URI pattern.
	 */
	protected String getNsURIPattern(String nsURI) {
		StringBuffer nsURIPattern = new StringBuffer();

		for (int i = 0; i < nsURI.length(); i++) {

			if (Character.isDigit(nsURI.charAt(i))) {
				nsURIPattern.append("\\d");//$NON-NLS-1$
			} else {
				nsURIPattern.append(nsURI.charAt(i));
			}
		}

		return nsURIPattern.toString();
	}

	/**
	 * @see org.eclipse.emf.ecore.util.ExtendedMetaData#getPackage(java.lang.String)
	 */
	public EPackage getPackage(String namespace) {
		EPackage ePackage = super.getPackage(namespace);

		if (null == ePackage && null != namespace) {
			ePackage = compatibilityRegistry.getEPackage(namespace);

			if (null != ePackage) {
				return ePackage;
			}

			String nsURIPattern = getNsURIPattern(namespace);

			// try to find a 'compatible' package with a matching namespace URI
			for (Iterator nsURIs = registry.keySet().iterator(); nsURIs
				.hasNext();) {

				String nsURI = (String) nsURIs.next();

				if (nsURI.matches(nsURIPattern)) {
					ePackage = registry.getEPackage(nsURI);

					compatibilityRegistry.put(namespace, ePackage);

					XMLResource.XMLInfo xmlInfo = new XMLInfoImpl();
					xmlInfo.setTargetNamespace(namespace);
					xmlMap.add(ePackage, xmlInfo);

					break;
				}
			}
		}

		return ePackage;
	}

	/**
	 * @see org.eclipse.emf.ecore.util.ExtendedMetaData#getNamespace(org.eclipse.emf.ecore.EPackage)
	 */
	public String getNamespace(EPackage ePackage) {
		XMLResource.XMLInfo xmlInfo = xmlMap.getInfo(ePackage);

		if (null != xmlInfo) {
			String namespace = xmlInfo.getTargetNamespace();

			if (null != namespace) {
				return namespace;
			}
		}

		return super.getNamespace(ePackage);
	}
}