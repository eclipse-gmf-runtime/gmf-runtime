/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.util;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.gmf.runtime.emf.core.internal.util.MSLMetaModelManager;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLUtil;
import org.eclipse.gmf.runtime.emf.core.services.metamodel.IMetamodelSupport;

/**
 * Some utility functions that work at the meta-class level.
 * 
 * @author rafikj
 */
public class MetaModelUtil {
	/**
	 * Mapping of {@link EClass} ==&gt; name {@link EAttribute}.  Use a weak
	 * map to allow reclamation of dynamically-generated Ecore models; in order
	 * for this to work, the keys are also weak refs.
	 */
	private static final Map NAME_ATTRIBUTES = new java.util.WeakHashMap();
	
	/**
	 * Mapping of {@link EClass} ==&gt; qualified name {@link EAttribute}.
	 * Use a weak map to allow reclamation of dynamically-generated Ecore
	 * models; in order for this to work, the keys are also weak refs.
	 */
	private static final Map QNAME_ATTRIBUTES = new java.util.WeakHashMap();
	
	/**
	 * Gets the ID of a meta-model element. The ID of a meta-model element is
	 * the fully qualified name of the element going up to the root EPackage.
	 * 
	 * @param element
	 *            The Meta-model element.
	 * @return The ID.
	 */
	public static String getID(ENamedElement element) {
		return MSLMetaModelManager.getID(element);
	}

	/**
	 * Gets meta-model element by ID.
	 * 
	 * @param id
	 *            The element ID.
	 * @return The meta-model element.
	 */
	public static ENamedElement getElement(String id) {
		return MSLMetaModelManager.getElement(id);
	}

	/**
	 * Gets the localized name of a meta-model element. The name will not
	 * contain spaces.
	 * 
	 * @param element
	 *            The meta-model element.
	 * @return The localized name of the meta-model element.
	 */
	public static String getLocalName(ENamedElement element) {
		return MSLMetaModelManager.getLocalName(element);
	}

	/**
	 * Gets the localized display name of a meta-model element. Display name may
	 * contain spaces.
	 * <p>
	 * NOTE: This should be used to make model changes (e.g. name an element).
	 * When displaying strings in the UI (menus, dialogs, etc.) get the string
	 * from one of our resource managers.
	 * </p>
	 * 
	 * @param element
	 *            The meta-model element.
	 * @return The localized name of the meta-model element.
	 */
	public static String getDisplayName(ENamedElement element) {
		return MSLMetaModelManager.getDisplayName(element);
	}

	/**
	 * Checks if a container <code>EClass</code> can contain another
	 * <code>EClass</code>. The check can be recursive.
	 * 
	 * @param class1
	 *            The container <code>EClass</code>.
	 * @param class2
	 *            The contained <code>EClass</code>.
	 * @param recursive
	 *            True if recursive.
	 * @return True if an object can contain other objects of a given type.
	 */
	public static boolean canContain(EClass class1, EClass class2,
			boolean recursive) {
		return MSLUtil.canContain(class1, class2, recursive ? (new HashSet())
			: null);
	}

	/**
	 * Checks if a container <code>EClass</code> can contain another
	 * <code>EClass</code> at a given containment reference. The check can be
	 * recursive.
	 * 
	 * @param class1
	 *            The container <code>EClass</code>.
	 * @param reference
	 *            The reference.
	 * @param class2
	 *            The contained <code>EClass</code>.
	 * @param recursive
	 *            True if recursive.
	 * @return True if an object can contain other objects of a given type.
	 */
	public static boolean canContain(EClass class1, EReference reference,
			EClass class2, boolean recursive) {

		IMetamodelSupport metaModel = MSLUtil.getMetaModel(class1);

		if ((reference.isContainment())
			&& (class1.getEAllReferences().contains(reference))) {

			EClass eType = (EClass) reference.getEType();

			if ((eType.equals(class2)) || (eType.isSuperTypeOf(class2))) {

				if (metaModel == null)
					return true;
				else
					return metaModel.canContain(class1, reference, class2);
			}
		}

		if (recursive) {

			if (reference.isContainment()) {

				EClass eType = (EClass) reference.getEType();

				if (canContain(eType, class2, true))
					return true;
			}
		}

		return false;
	}

	/**
	 * Checks if a container <code>EClass</code> can reference another
	 * <code>EClass</code>.
	 * 
	 * @param class1
	 *            The referencer <code>EClass</code>.
	 * @param class2
	 *            The referenced <code>EClass</code>.
	 * @return True if an object can contain other objects of a given type.
	 */
	public static boolean canReference(EClass class1, EClass class2) {

		IMetamodelSupport metaModel = MSLUtil.getMetaModel(class1);

		Iterator i = class1.getEAllReferences().iterator();

		while (i.hasNext()) {

			EReference reference = (EReference) i.next();

			if (!reference.isContainment()) {

				EClass eType = (EClass) reference.getEType();

				if ((eType.equals(class2)) || (eType.isSuperTypeOf(class2))) {

					if (metaModel == null)
						return true;

					else if (metaModel.canContain(class1, reference, class2))
						return true;
				}
			}
		}

		return false;
	}

	/**
	 * Checks if a container <code>EClass</code> can reference another
	 * <code>EClass</code> at a given non-containment reference.
	 * 
	 * @param class1
	 *            The referencer <code>EClass</code>.
	 * @param reference
	 *            The reference.
	 * @param class2
	 *            The referenced <code>EClass</code>.
	 * @return True if an object can contain other objects of a given type.
	 */
	public static boolean canReference(EClass class1, EReference reference,
			EClass class2) {

		IMetamodelSupport metaModel = MSLUtil.getMetaModel(class1);

		if ((!reference.isContainment())
			&& (class1.getEAllReferences().contains(reference))) {

			EClass eType = (EClass) reference.getEType();

			if ((eType.equals(class2)) || (eType.isSuperTypeOf(class2))) {

				if (metaModel == null)
					return true;
				else
					return metaModel.canContain(class1, reference, class2);
			}
		}

		return false;
	}

	/**
	 * Finds a feature that can contain an object of type eClass.
	 * 
	 * @param container
	 *            The container <code>EClass</code>.
	 * @param eClass
	 *            The contained <code>EClass</code>.
	 * @return The found feature.
	 */
	public static EReference findFeature(EClass container, EClass eClass) {

		Iterator i = container.getEAllReferences().iterator();

		while (i.hasNext()) {

			EReference reference = (EReference) i.next();

			if (canContain(container, reference, eClass, false))
				return reference;
		}

		return null;
	}

	/**
	 * Gets the name attribute of an <code>EClass</code>.
	 * 
	 * @param eClass
	 *            The <code>EClass</code>.
	 * @return The name attribute.
	 */
	public static EAttribute getNameAttribute(EClass eClass) {
		
		EAttribute nameAttribute = null;

		// first, try to get the cached attribute
		WeakReference ref = (WeakReference) NAME_ATTRIBUTES.get(eClass);
		if (ref != null) {
			
			nameAttribute = (EAttribute) ref.get();
		} else {

			EStructuralFeature feature = eClass.getEStructuralFeature("name"); //$NON-NLS-1$

			if (feature != null) {

				if (feature instanceof EAttribute) {
					EClassifier type = feature.getEType();

					if (type != null) {

						if (type.getInstanceClass() == String.class)
							nameAttribute = (EAttribute) feature;
					}
				}
			}
			
			// cache the result, whatever it is.  As long as the key (EClass)
			//    isn't GCed, then the value (WeakRef) will remain to indicate
			//    that we have at least cached a null
			NAME_ATTRIBUTES.put(eClass, new WeakReference(nameAttribute));
		}

		return nameAttribute;
	}

	/**
	 * Gets the qualified name attribute of an <code>EClass</code>.
	 * 
	 * @param eClass
	 *            The <code>EClass</code>.
	 * @return The qualified name attribute.
	 */
	public static EAttribute getQNameAttribute(EClass eClass) {

		EAttribute nameAttribute = null;

		// first, try to get the cached attribute
		WeakReference ref = (WeakReference) QNAME_ATTRIBUTES.get(eClass);
		if (ref != null) {
			
			nameAttribute = (EAttribute) ref.get();
		} else  {

			EStructuralFeature feature = eClass
				.getEStructuralFeature("qualifiedName"); //$NON-NLS-1$

			if (feature != null) {

				if ((feature instanceof EAttribute)
					&& (feature.getEType().getInstanceClass() == String.class))
					nameAttribute = (EAttribute) feature;
			}
			
			// cache the result, whatever it is.  As long as the key (EClass)
			//    isn't GCed, then the value (WeakRef) will remain to indicate
			//    that we have at least cached a null
			QNAME_ATTRIBUTES.put(eClass, new WeakReference(nameAttribute));
		}

		return nameAttribute;
	}

	private MetaModelUtil() {
		// private
	}
}