/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.resources;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.xmi.XMIException;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.SAXXMIHandler;
import org.eclipse.emf.ecore.xml.type.AnyType;

/**
 * The SAX handler for MSL resources. Updates demand-created packages with their
 * namespace prefixes and schema locations.
 * 
 * @author khussey
 */
public class GMFHandler
	extends SAXXMIHandler {

	protected final Map urisToProxies;
	
	protected boolean abortOnError;

	/**
	 * Constructs a new MSL handler for the specified resource with the
	 * specified helper and options.
	 * 
	 * @param xmiResource
	 *            The resource for the new handler.
	 * @param helper
	 *            The helper for the new handler.
	 * @param options
	 *            The load options for the new handler.
	 */
	public GMFHandler(XMLResource xmiResource, XMLHelper helper, Map options) {
		super(xmiResource, helper, options);

		urisToProxies = new HashMap();
		if (Boolean.TRUE.equals(options.get(GMFResource.OPTION_ABORT_ON_ERROR))) {
			abortOnError = true;
		}
	}

	/**
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#endDocument()
	 */
	public void endDocument() {
		super.endDocument();

		if (null != extendedMetaData) {

			for (Iterator demandedPackages = extendedMetaData
				.demandedPackages().iterator(); demandedPackages.hasNext();) {

				EPackage ePackage = (EPackage) demandedPackages.next();
				String nsURI = ePackage.getNsURI();

				if (null != nsURI) {

					if (null != urisToLocations) {
						URI locationURI = (URI) urisToLocations.get(nsURI);

						if (null != locationURI) {
							// set the schema location
							Resource resource = new ResourceImpl();
							resource.setURI(locationURI);
							resource.getContents().add(ePackage);
						}
					}

					for (Iterator entries = helper.getPrefixToNamespaceMap()
						.iterator(); entries.hasNext();) {

						Map.Entry entry = (Map.Entry) entries.next();

						if (nsURI.equals(entry.getValue())) {
							// set the namespace prefix
							ePackage.setNsPrefix((String) entry.getKey());
						}
					}
				}
			}
		}
	}

	/**
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#validateCreateObjectFromFactory(org.eclipse.emf.ecore.EFactory,
	 *      java.lang.String, org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EStructuralFeature)
	 */
	protected EObject validateCreateObjectFromFactory(EFactory factory,
			String typeName, EObject newObject, EStructuralFeature feature) {

		if (!(objects.peek() instanceof AnyType) && null != newObject
			&& newObject.eIsProxy() && !sameDocumentProxies.contains(newObject)) {

			URI proxyURI = ((InternalEObject) newObject).eProxyURI();

			Map typeNamesToProxies = (Map) urisToProxies.get(proxyURI);

			if (null == typeNamesToProxies) {
				urisToProxies.put(proxyURI, typeNamesToProxies = new HashMap());
			}

			EObject proxy = (EObject) typeNamesToProxies.get(typeName);

			if (null == proxy) {
				typeNamesToProxies.put(typeName, proxy = newObject);
			}

			// canonicalize proxies
			newObject = proxy;
		}

		return super.validateCreateObjectFromFactory(factory, typeName,
			newObject, feature);
	}

	/**
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#error(org.eclipse.emf.ecore.xmi.XMIException)
	 */
	public void error(XMIException e) {
		super.error(e);
		if (abortOnError) {
			if (e.getCause() != null) {
				throw new AbortResourceLoadException(e.getCause());
			}
			throw new AbortResourceLoadException(e);
		}
	}
}
