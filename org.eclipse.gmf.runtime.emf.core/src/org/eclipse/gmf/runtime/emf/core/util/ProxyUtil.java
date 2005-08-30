/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.resources.MResourceFactory;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLConstants;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLUtil;

/**
 * This class contains a set of utility methods that control the use of Proxy
 * objects. The MSL stores extra information with Proxy URI (the qualified name
 * of the refernced object incase of cross-resource references) using a
 * proprietary format. The following methods provide access to the data.
 * 
 * @author rafikj
 * @author sgutz
 */
public class ProxyUtil {

	/**
	 * Gets the proxy name by parsing the proxy URI.
	 * 
	 * @param eObject
	 *            The proxy object.
	 * @return The name.
	 */
	public static String getProxyName(EObject eObject) {

		if (eObject == null)
			return MSLConstants.EMPTY_STRING;

		if (!eObject.eIsProxy())
			return EObjectUtil.getName(eObject);

		URI uri = EcoreUtil.getURI(eObject);

		Resource.Factory factory = Resource.Factory.Registry.INSTANCE
			.getFactory(uri);

		if (factory instanceof MResourceFactory)
			return ((MResourceFactory) factory).getHelper().getProxyName(
				eObject);
		else
			return MSLUtil.getProxyName(eObject);
	}

	/**
	 * Gets the proxy qualified name by parsing the proxy URI.
	 * 
	 * @param eObject
	 *            The proxy object.
	 * @return The qualified name.
	 */
	public static String getProxyQName(EObject eObject) {

		if (eObject == null)
			return MSLConstants.EMPTY_STRING;

		if (!eObject.eIsProxy())
			return EObjectUtil.getQName(eObject, true);

		URI uri = EcoreUtil.getURI(eObject);

		Resource.Factory factory = Resource.Factory.Registry.INSTANCE
			.getFactory(uri);

		if (factory instanceof MResourceFactory)
			return ((MResourceFactory) factory).getHelper().getProxyQName(
				eObject);
		else
			return MSLUtil.getProxyQName(eObject);
	}

	/**
	 * Gets the proxy ID by parsing the proxy URI.
	 * 
	 * @param eObject
	 *            The proxy object.
	 * @return The ID.
	 */
	public static String getProxyID(EObject eObject) {

		if (eObject == null)
			return MSLConstants.EMPTY_STRING;

		if (!eObject.eIsProxy())
			return EObjectUtil.getID(eObject);

		URI uri = EcoreUtil.getURI(eObject);

		Resource.Factory factory = Resource.Factory.Registry.INSTANCE
			.getFactory(uri);

		if (factory instanceof MResourceFactory)
			return ((MResourceFactory) factory).getHelper().getProxyID(eObject);
		else
			return MSLUtil.getProxyID(eObject);
	}

	/**
	 * Gets the proxy class ID name by parsing the proxy URI.
	 * 
	 * @param eObject
	 *            The proxy object.
	 * @return The class ID.
	 */
	public static String getProxyClassID(EObject eObject) {

		if (eObject == null)
			return MSLConstants.EMPTY_STRING;

		if (!eObject.eIsProxy())
			return MetaModelUtil.getID(eObject.eClass());

		URI uri = EcoreUtil.getURI(eObject);

		Resource.Factory factory = Resource.Factory.Registry.INSTANCE
			.getFactory(uri);

		if (factory instanceof MResourceFactory)
			return ((MResourceFactory) factory).getHelper().getProxyClassID(
				eObject);
		else
			return MSLUtil.getProxyClassID(eObject);
	}

	/**
	 * Gets the proxy class by parsing the proxy URI.
	 * 
	 * @param eObject
	 *            The proxy object.
	 * @return The class.
	 */
	public static EClass getProxyClass(EObject eObject) {

		String id = getProxyClassID(eObject);

		if (id != null) {

			ENamedElement element = MetaModelUtil.getElement(id);

			if ((element != null) && (element instanceof EClass))
				return (EClass) element;
		}

		return null;
	}

	/**
	 * Creates a proxy of the specified type with the specified proxy URI.
	 * 
	 * @param classID
	 *            The type of proxy to create.
	 * @param uri
	 *            The URI for the new proxy.
	 * @return The new proxy.
	 */
	public static EObject createProxy(String classID, URI uri) {
		InternalEObject proxy = null;

		if (null != classID) {

			ENamedElement element = MetaModelUtil.getElement(classID);

			if (EClass.class.isInstance(element)) {

				EClass eClass = (EClass) element;

				proxy = (InternalEObject) eClass.getEPackage()
					.getEFactoryInstance().create(eClass);
				proxy.eSetProxyURI(uri);
			}
		}

		return proxy;
	}

	/**
	 * This method returns <code>null</code> if the proxy is not resolved
	 * otherwise returns the object itself.
	 * 
	 * @param eObject
	 *            The proxy object.
	 * @return The resolved object.
	 */
	public static EObject resolve(EObject eObject) {
		return resolve(MEditingDomain.INSTANCE, eObject, false);
	}

	/**
	 * This method returns <code>null</code> if the proxy is not resolved
	 * otherwise returns the object itself.
	 * 
	 * @param domain
	 *            Th editing domain.
	 * @param eObject
	 *            The proxy object.
	 * @return The resolved object.
	 */
	public static EObject resolve(MEditingDomain domain, EObject eObject) {
		return resolve(domain, eObject, false);
	}

	/**
	 * This method resolves the proxy if the <code>resolve</code> is set to
	 * TRUE. And returns null if the proxy is not resolved otherwise returns the
	 * object itself.
	 * 
	 * @param domain
	 *            Th editing domain.
	 * @param eObject
	 *            The proxy object.
	 * @param resolve
	 *            Force resolve.
	 * @return The resolved object.
	 */
	public static EObject resolve(MEditingDomain domain,
			EObject eObject, boolean resolve) {

		if (eObject == null)
			return null;

		if (!eObject.eIsProxy())
			return eObject;

		URI uri = EcoreUtil.getURI(eObject);

		Resource.Factory factory = Resource.Factory.Registry.INSTANCE
			.getFactory(uri);

		if (factory instanceof MResourceFactory)
			return ((MResourceFactory) factory).getHelper().resolve(domain,
				eObject, resolve);

		else
			return MSLUtil.resolve((MSLEditingDomain) domain, eObject, resolve);
	}

	private ProxyUtil() {
		// private
	}
}