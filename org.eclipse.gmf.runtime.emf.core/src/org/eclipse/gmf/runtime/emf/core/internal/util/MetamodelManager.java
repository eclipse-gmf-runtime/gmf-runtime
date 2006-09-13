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

package org.eclipse.gmf.runtime.emf.core.internal.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.EMFCoreDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.EMFCorePlugin;

/**
 * This class manages meta-models and provide localization of meta-class names.
 * 
 * @author rafikj
 * @author Christian W. Damus (cdamus)
 */
public class MetamodelManager {

	// used to get resource locators when none are provided to us
	private static final ComposedAdapterFactory adapterFactory =
		new ComposedAdapterFactory(
			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
	
	private final static Map METAMODEL_MAP = new HashMap();

	private final static Map REVERSE_METAMODEL_MAP = new HashMap();

	/**
	 * Register meta-model object.
	 */
	public static void register(ENamedElement element) {
		register(element, null);
	}
	
	/**
	 * Register meta-model object.
	 */
	public static void register(ENamedElement element,
			ResourceLocator resourceLocator) {

		if (element instanceof EOperation)
			return;

		if (element instanceof EParameter)
			return;

		String id = getNonCachedID(element);

		String name = element.getName();

		String displayName = null;

		if ((resourceLocator == null) && (element instanceof EPackage)) {
			// get a resource locator from the adapter factory registered
			//     against the IItemLabelProvider adapter type
			resourceLocator = findResourceLocator((EPackage) element);
		}
		
		if (resourceLocator != null) {

			if (element instanceof EClass) {
				displayName = resourceLocator.getString("_UI_" + name //$NON-NLS-1$
					+ "_type"); //$NON-NLS-1$

			} else if (element instanceof EStructuralFeature) {

				EClass eClass = ((EStructuralFeature) element)
					.getEContainingClass();

				if (eClass != null)
					displayName = resourceLocator.getString("_UI_" //$NON-NLS-1$
						+ eClass.getName() + "_" + name + "_feature"); //$NON-NLS-1$//$NON-NLS-2$

			} else if (element instanceof EEnumLiteral) {

				EEnum eEnum = ((EEnumLiteral) element).getEEnum();

				if (eEnum != null)
					displayName = resourceLocator.getString("_UI_" //$NON-NLS-1$
						+ eEnum.getName() + "_" + name + "_literal"); //$NON-NLS-1$//$NON-NLS-2$
			}
		}

		if (displayName == null)
			displayName = name;

		METAMODEL_MAP.put(element, new MetaModelDescriptor(id, displayName));

		REVERSE_METAMODEL_MAP.put(id, element);

		for (Iterator i = element.eContents().iterator(); i.hasNext();) {

			Object child = i.next();

			if (child instanceof ENamedElement)
				register((ENamedElement) child, resourceLocator);
		}
	}
	
	/**
	 * Attempts to find a resource locator for the specified metamodel package,
	 * using a heuristic that assumes that item-provider adapters implement
	 * the ResourceLocator interface (which is the default code generation).
	 * 
	 * @param pkg a package for which we need a resource locator
	 * 
	 * @return the resource locator if we could find one; <code>null</code> otherwise
	 */
	private static ResourceLocator findResourceLocator(EPackage pkg) {
		ResourceLocator result = null;
		
		// the compased adapter factory has a registry of pairs by EPackage
		//    and adapter class
		List types = new java.util.ArrayList(2);
		types.add(pkg);
		types.add(IItemLabelProvider.class);
		
		AdapterFactory factory = adapterFactory.getFactoryForTypes(types);
		
		if (factory != null) {
			// find some EClass to instantiate to get an item provider for it
			EObject instance = null;
			
			for (Iterator iter = pkg.getEClassifiers().iterator(); iter.hasNext();) {
				Object next = iter.next();
				
				if ((next instanceof EClass) && !((EClass) next).isAbstract()) {
					instance = pkg.getEFactoryInstance().create((EClass) next);
					break;
				}
			}
			
			if (instance != null) {
				Object adapter = factory.adapt(instance, IItemLabelProvider.class);
				
				if (adapter instanceof ResourceLocator) {
					result = (ResourceLocator) adapter;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Get the ID of a meta-model object.
	 */
	public static String getID(ENamedElement element) {

		if (element instanceof EOperation) {

			RuntimeException e = new IllegalArgumentException(
				"EOperation does not support IDs"); //$NON-NLS-1$

			Trace.throwing(EMFCorePlugin.getDefault(),
				EMFCoreDebugOptions.EXCEPTIONS_THROWING, MetamodelManager.class,
				"getID", e); //$NON-NLS-1$

			throw e;
		}

		if (element instanceof EParameter) {

			RuntimeException e = new IllegalArgumentException(
				"EParameter does not support IDs"); //$NON-NLS-1$

			Trace.throwing(EMFCorePlugin.getDefault(),
				EMFCoreDebugOptions.EXCEPTIONS_THROWING, MetamodelManager.class,
				"getID", e); //$NON-NLS-1$

			throw e;
		}

		MetaModelDescriptor descriptor = (MetaModelDescriptor) METAMODEL_MAP
			.get(element);

		if (descriptor != null)
			return descriptor.id;

		return getNonCachedID(element);
	}

	/**
	 * Get the localized name of a meta-model object. Name does not contain
	 * spaces.
	 */
	public static String getLocalName(ENamedElement element) {

		MetaModelDescriptor descriptor = (MetaModelDescriptor) METAMODEL_MAP
			.get(element);

		if (descriptor != null)
			return descriptor.localName;

		return element.getName();
	}

	/**
	 * Get the localized name of a meta-model object. Name may contain spaces.
	 */
	public static String getDisplayName(ENamedElement element) {

		MetaModelDescriptor descriptor = (MetaModelDescriptor) METAMODEL_MAP
			.get(element);

		if (descriptor != null)
			return descriptor.displayName;

		return element.getName();
	}

	/**
	 * Find meta-model object given its ID.
	 */
	public static ENamedElement getElement(String id) {
		ENamedElement result = (ENamedElement) REVERSE_METAMODEL_MAP.get(id);
		
		if ((result == null) && (id != null)) {
			// not registered, yet.  Look it up in the registry
			result = findInPackageRegistry(id);
		}
		
		return result;
	}

	/**
	 * Find the specified named element by ID in the global package registry.
	 * <b>Side-effect:</b>  registers the package when found so that we don't
	 * need to do this again.
	 * 
	 * @param id the ID to find.  Must not be <code>null</code>
	 * @return the named element, or <code>null</code> if not found
	 */
	private static ENamedElement findInPackageRegistry(String id) {
		ENamedElement result = null;
		
		int dot = id.indexOf('.');
		
		String pkgName = (dot >= 0)? id.substring(0, dot) : id;
		for (Iterator iter = EPackage.Registry.INSTANCE.values().iterator(); iter.hasNext();) {
			Object next = iter.next();
			
			if (next instanceof EPackage) {
				// skip descriptors because if the EPackage hasn't been
				//    instantiated then it cannot be in use by the client
				EPackage pkg = (EPackage) next;
				
				if (pkgName.equals(pkg.getName())) {
					result = findElement(pkg, id);
					if (result != null) {
						// register the package for subsequent look-ups
						register(pkg, null);
						break;
					}
				}
			}
		}
		
		return result;
	}
	
	private static ENamedElement findElement(ENamedElement element, String id) {
		ENamedElement result = null;
		int dot = id.indexOf('.');
		
		if (dot < 0) {
			if (id.equals(element.getName())) {
				// got the final result
				result = element;
			} // else the element is not found here
		} else {
			String name = id.substring(0, dot);
			
			if (name.equals(element.getName())) {
				// search recursively in the sub-tree
				id = id.substring(dot + 1);
				
				for (Iterator iter = element.eContents().iterator();
						(result == null) && iter.hasNext();) {
					
					Object next = iter.next();
					
					if (next instanceof ENamedElement) {
						result = findElement((ENamedElement) next, id);
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Get the non-cached ID of a meta-model object.
	 */
	private static String getNonCachedID(ENamedElement element) {

		StringBuffer id = new StringBuffer();
		ENamedElement current = element;

		while (current != null) {

			id.insert(0, current.getName());
			EObject container = current.eContainer();

			current = null;

			if (container != null) {
				if ((container instanceof ENamedElement)) {
	
					current = (ENamedElement) container;
	
					id.insert(0, '.');
				} else {
					// ENamedElements not contained by named elements (e.g.,
					//     contained in annotations) are not supported
					return null;
				}
			}
		}

		return id.toString();
	}

	/**
	 * This class describes a meta-model object.
	 */
	private static class MetaModelDescriptor {

		public String id = null;

		public String localName = null;

		public String displayName = null;

		public MetaModelDescriptor(String id, String displayName) {

			super();

			this.id = id.intern();
			this.localName = displayName.replaceAll(" ", "").intern(); //$NON-NLS-1$//$NON-NLS-2$
			this.displayName = displayName.intern();
		}
	}
}