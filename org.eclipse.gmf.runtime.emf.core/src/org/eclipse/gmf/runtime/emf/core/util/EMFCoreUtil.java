/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.core.internal.index.CrossReferenceAdapter;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.EMFCoreDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.EMFCorePlugin;
import org.eclipse.gmf.runtime.emf.core.internal.util.EMFCoreConstants;
import org.eclipse.gmf.runtime.emf.core.internal.util.Util;
import org.eclipse.gmf.runtime.emf.core.resources.IExtendedResourceFactory;
import org.eclipse.gmf.runtime.emf.core.resources.IResourceHelper;


/**
 * Static utilities for working with EMF objects and resources in the GMF
 * environment.
 *
 * @author Christian W. Damus (cdamus)
 */
public class EMFCoreUtil {

	/**
	 * Creates an object at a given containment reference whose
	 * <code>EClass</code> is eClass.
	 * 
	 * @param container
	 *            The container of the new object.
	 * @param reference
	 *            The feature containing the object.
	 * @param eClass
	 *            The <code>EClass</code>.
	 * @return The new <code>EObject</code>.
	 */
	public static EObject create(EObject container, EReference reference,
			EClass eClass) {

		EObject result = null;

		IResourceHelper helper = Util.getHelper(container.eResource());
		
		if (helper != null) {

			result = helper.create(eClass);

		} else {
			result = eClass.getEPackage().getEFactoryInstance().create(eClass);
		}
		
		if (reference.isMany()) {
			((Collection) container.eGet(reference)).add(result);
		} else {
			container.eSet(reference, result);
		}
		
		return result;
	}

	/**
	 * Gets the first container with the specified EClass.
	 * 
	 * @param eObject
	 *            The <code>EObject</code>.
	 * @param eClass
	 *            The eClass.
	 * @return The container.
	 */
	public static EObject getContainer(EObject eObject, EClass eClass) {

		for (EObject parent = eObject; parent != null; parent = parent
			.eContainer())
			if (eClass.isInstance(parent))
				return parent;

		return null;
	}

	/**
	 * Finds the first common container of a collection of objects.
	 * 
	 * @param objects
	 *            The <code>EObject</code>s.
	 * @param desiredContainerType
	 *            The desired <code>EClass</code> of the container.
	 * @return The least common container.
	 */
	public static EObject getLeastCommonContainer(Collection objects,
			EClass desiredContainerType) {

		EObject commonContainer = null;

		List prevContainers = new ArrayList();

		Iterator i = objects.iterator();

		while (i.hasNext()) {

			EObject element = (EObject) i.next();
			List containers = new ArrayList();

			boolean found = false;

			EObject container = element;

			while (container != null) {

				EClass containerClass = container.eClass();

				if ((desiredContainerType == containerClass)
					|| (desiredContainerType.isSuperTypeOf(containerClass))) {

					containers.add(container);

					if (!found) {

						if ((prevContainers.isEmpty())
							|| (commonContainer == null)) {
							commonContainer = container;
							found = true;

						} else if ((prevContainers.contains(container))
							&& (EcoreUtil.isAncestor(container, commonContainer))) {

							commonContainer = container;
							found = true;
						}
					}
				}

				container = container.eContainer();
			}

			if (!found)
				return null;

			prevContainers = containers;
		}

		return commonContainer;
	}

	/**
	 * Uses a reverse reference map that is maintained by the MSL service to
	 * find all referencers of a particular element. The search can be narrowed
	 * down by passing the list of Reference features to match.
	 * <code>features</code> can be null.
	 * 
	 * @param eObject
	 *            The referenced object.
	 * @param features
	 *            The reference features.
	 * @return The collection of referencers.
	 */
	public static Collection getReferencers(EObject eObject,
			EReference[] features) {

		CrossReferenceAdapter crossReferenceAdapter =
				CrossReferenceAdapter.getExistingCrossReferenceAdapter(eObject);

		if (crossReferenceAdapter == null) {
			TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(
				eObject);
			
			if (domain != null) {
				crossReferenceAdapter = CrossReferenceAdapter.getCrossReferenceAdapter(
					domain);
			}
			
			if (crossReferenceAdapter == null) {
				// still null?  Give up
				return Collections.EMPTY_LIST;
			}
		}
		
		if ((features != null) && (features.length != 0)) {

			Collection referencers = new ArrayList();

			for (int i = 0, count = features.length; i < count; i++) {

				EReference feature = features[i];

				Iterator j = crossReferenceAdapter.getInverseReferencers(eObject,
						feature, null).iterator();

				while (j.hasNext()) {

					EObject referencer = (EObject) j.next();

					referencers.add(referencer);
				}
			}

			return referencers;

		} else
			return crossReferenceAdapter.getInverseReferencers(eObject, null, null);
	}
	
	/**
	 * Gets the imports of a resource.  These are the resources to which this
	 * <code>resource</code> has references.
	 * 
	 * @param resource a resource.
	 * @return the imports of the resource
	 */
	public static Collection getImports(Resource resource) {
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(resource);
		
		if (domain != null) {
			return CrossReferenceAdapter.getCrossReferenceAdapter(domain).getImports(resource);
		}
		
		return Collections.EMPTY_SET;
	}

	/**
	 * Gets the exports of a resource.  These are the resources that have
	 * references to the <code>resource</code>.
	 * 
	 * @param resource a resource.
	 * @return the exports of the resource
	 */
	public static Collection getExports(Resource resource) {
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(resource);
		
		if (domain != null) {
			return CrossReferenceAdapter.getCrossReferenceAdapter(domain).getExports(resource);
		}
		
		return Collections.EMPTY_SET;
	}

	/**
	 * Gets the transitive closure of the imports of a resource.  This obtains
	 * those resources that import the <code>resource</code>, the resources
	 * that import those, etc.
	 * 
	 * @param resource a resource.
	 * @return the transitive imports of the resource
	 */
	public static Collection getTransitiveImports(Resource resource) {
		Collection result = new HashSet();
		Collection unload = new HashSet();

		getTransitiveImports(resource, result, unload);

		for (Iterator i = unload.iterator(); i.hasNext();) {
			((Resource) i.next()).unload();
		}
		
		return result;
	}

	/**
	 * Helper method to recursively compute transitive imports of a resource.
	 */
	private static void getTransitiveImports(Resource resource, Collection imports,
			Collection unload) {

		if (!resource.isLoaded()) {

			try {
				resource.load(Collections.EMPTY_MAP);
			} catch (Exception e) {
				// ignore resources that fail to load.
			}

			unload.add(resource);
		}

		Collection directImports = getImports(resource);

		for (Iterator i = directImports.iterator(); i.hasNext();) {

			Resource directImport = (Resource) i.next();

			if (!imports.contains(directImport)) {

				imports.add(directImport);

				getTransitiveImports(directImport, imports, unload);
			}
		}
	}

	/**
	 * Gets the transitive closure of the exports of a resource.  This obtains
	 * those resources that export the <code>resource</code>, the resources
	 * that export those, etc.
	 * 
	 * @param resource a resource.
	 * @return the transitive exports of the resource
	 */
	public static Collection getTransitiveExports(Resource resource) {
		Collection result = new HashSet();
		Collection unload = new HashSet();

		getTransitiveExports(resource, result, unload);

		for (Iterator i = unload.iterator(); i.hasNext();) {
			((Resource) i.next()).unload();
		}
		
		return result;
	}

	/**
	 * Helper method to recursively compute transitive imports of a resource.
	 */
	private static void getTransitiveExports(Resource resource, Collection exports,
			Collection unload) {

		if (!resource.isLoaded()) {

			try {
				resource.load(Collections.EMPTY_MAP);
			} catch (Exception e) {
				// ignore resources that fail to load.
			}

			unload.add(resource);
		}

		Collection directExports = getExports(resource);

		for (Iterator i = directExports.iterator(); i.hasNext();) {

			Resource directExport = (Resource) i.next();

			if (!exports.contains(directExport)) {

				directExports.add(directExport);

				getTransitiveExports(directExport, exports, unload);
			}
		}
	}

	/**
	 * Gets the name of an object if the object has name, returns an empty
	 * string otherwise.
	 * 
	 * @param eObject
	 *            The object.
	 * @return The object's name.
	 */
	public static String getName(EObject eObject) {

		if (eObject.eIsProxy())
			return getProxyName(eObject);

		EAttribute nameAttribute = PackageUtil.getNameAttribute(eObject
			.eClass());

		if (nameAttribute != null) {

			String name = (String) eObject.eGet(nameAttribute);

			if (name != null)
				return name;
		}

		return EMFCoreConstants.EMPTY_STRING;
	}
	
	private static String getProxyName(EObject proxy) {

		URI uri = EcoreUtil.getURI(proxy);

		Resource.Factory factory = Resource.Factory.Registry.INSTANCE
			.getFactory(uri);

		String result = null;
		
		if (factory instanceof IExtendedResourceFactory) {
			result = ((IExtendedResourceFactory) factory).getProxyName(proxy);
		}
		
		if (result == null) {
			// default algorithm
			
			result = EMFCoreConstants.EMPTY_STRING;
		}
		
		return result;
	}

	/**
	 * Sets the name of an object if the object can be assigned a name.
	 * 
	 * @param eObject
	 *            The object.
	 * @param name
	 *            The object's new name.
	 *            
	 * @throws IllegalArgumentException if the <code>eObject</code> does not
	 *     have a name attribute
	 */
	public static void setName(EObject eObject, String name) {

		EAttribute nameAttribute = PackageUtil.getNameAttribute(eObject
			.eClass());

		if (nameAttribute != null) {

			eObject.eSet(nameAttribute, name);

		} else {

			RuntimeException e = new IllegalArgumentException(
				"object has no name attribute"); //$NON-NLS-1$

			Trace.throwing(EMFCorePlugin.getDefault(),
				EMFCoreDebugOptions.EXCEPTIONS_THROWING, EMFCoreUtil.class,
				"setName", e); //$NON-NLS-1$

			throw e;
		}
	}

	/**
	 * Gets the fully qualified name of an object.
	 * 
	 * @param eObject
	 *            The object.
	 * @param formatted
	 *            if True, unnamed parents will be listed using their meta-class
	 *            name.
	 * @return The object's qualified name.
	 */
	public static String getQualifiedName(EObject eObject, boolean formatted) {

		if (eObject.eIsProxy())
			return getProxyQualifiedName(eObject);

		if (!formatted) {

			EAttribute qNameAttribute = PackageUtil.getQualifiedNameAttribute(
				eObject.eClass());

			if (qNameAttribute != null) {

				String qualifiedName = (String) eObject.eGet(qNameAttribute);

				if (qualifiedName != null)
					return qualifiedName;
				else
					return EMFCoreConstants.EMPTY_STRING;
			}
		}

		String prefix = EMFCoreConstants.EMPTY_STRING;

		EObject eContainer = eObject.eContainer();

		while ((eContainer != null) && (eContainer instanceof EAnnotation))
			eContainer = eContainer.eContainer();

		if (eContainer != null)
			prefix = getQualifiedName(eContainer, formatted);

		String name = getName(eObject);

		if ((formatted) && (name.equals(EMFCoreConstants.EMPTY_STRING)))
			name = EMFCoreConstants.META_CLASS_BEGIN
				+ PackageUtil.getLocalizedName(eObject.eClass())
				+ EMFCoreConstants.META_CLASS_END;

		return (prefix.length() == 0) ? name
			: (prefix + ((name.equals(EMFCoreConstants.EMPTY_STRING)) ? EMFCoreConstants.EMPTY_STRING
				: (EMFCoreConstants.QUALIFIED_NAME_SEPARATOR + name)));
	}

	private static String getProxyQualifiedName(EObject proxy) {

		URI uri = EcoreUtil.getURI(proxy);

		Resource.Factory factory = Resource.Factory.Registry.INSTANCE
			.getFactory(uri);

		String result = null;
		
		if (factory instanceof IExtendedResourceFactory) {
			result = ((IExtendedResourceFactory) factory).getProxyQualifiedName(proxy);
		}
		
		if (result == null) {
			// default algorithm
			
			result = EMFCoreConstants.EMPTY_STRING;
		}
		
		return result;
	}
	
	/**
	 * Gets the proxy ID by parsing the proxy URI.
	 * 
	 * @param proxy
	 *            The proxy object.
	 * @return The ID.
	 */
	public static String getProxyID(EObject proxy) {

		if (proxy == null) {
			return EMFCoreConstants.EMPTY_STRING;
		}
		
		if (!proxy.eIsProxy()) {
			XMLResource res = (XMLResource) proxy.eResource();
			
			if (res == null) {
				return EMFCoreConstants.EMPTY_STRING;
			} else {
				return res.getID(proxy);
			}
		}
		
		URI uri = EcoreUtil.getURI(proxy);

		Resource.Factory factory = Resource.Factory.Registry.INSTANCE
			.getFactory(uri);

		String result = null;
		
		if (factory instanceof IExtendedResourceFactory) {
			result = ((IExtendedResourceFactory) factory).getProxyID(proxy);
		}

		if (result == null) {
			// default implementation
			
			result = Util.getProxyID(proxy);
		}
		
		return result;
	}

	/**
	 * Gets the proxy class by parsing the proxy URI.
	 * 
	 * @param proxy
	 *            The proxy object.
	 * @return The class.
	 */
	public static EClass getProxyClass(EObject proxy) {

		if (proxy == null) {
			return null;
		}
		
		if (!proxy.eIsProxy()) {
			return proxy.eClass();
		}
		
		URI uri = EcoreUtil.getURI(proxy);

		Resource.Factory factory = Resource.Factory.Registry.INSTANCE
			.getFactory(uri);

		String id = null;
		
		if (factory instanceof IExtendedResourceFactory) {
			id = ((IExtendedResourceFactory) factory).getProxyClassID(proxy);
		}
		
		if (id == null) {
			// default algorithm
			
			id = PackageUtil.getID(proxy.eClass());
		}

		if (id != null) {

			ENamedElement element = PackageUtil.getElement(id);

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
	public static EObject createProxy(EClass eClass, URI uri) {
		InternalEObject proxy = (InternalEObject) eClass.getEPackage()
			.getEFactoryInstance().create(eClass);
		proxy.eSetProxyURI(uri);

		return proxy;
	}

	/**
	 * Attempts to resolve the specified <code>proxy</code> object, returning
	 * <code>null</code> (rather than the original proxy) if it is unresolvable.
	 * 
	 * @param domain
	 *            Th editing domain.
	 * @param proxy
	 *            The proxy object.
	 * @return The resolved object, or <code>null</code> if not resolved
	 */
	public static EObject resolve(TransactionalEditingDomain domain,
			EObject proxy) {

		if (proxy == null)
			return null;

		if (!proxy.eIsProxy())
			return proxy;

		URI uri = EcoreUtil.getURI(proxy);

		Resource.Factory factory = Resource.Factory.Registry.INSTANCE
			.getFactory(uri);

		if (factory instanceof IExtendedResourceFactory) {
			return ((IExtendedResourceFactory) factory).resolve(domain, proxy);
		}
		
		// the default algorithm
		
		return Util.resolve(domain, proxy);
	}
}
