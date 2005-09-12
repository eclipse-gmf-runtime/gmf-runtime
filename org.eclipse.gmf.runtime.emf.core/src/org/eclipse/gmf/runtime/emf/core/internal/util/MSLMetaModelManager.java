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

package org.eclipse.gmf.runtime.emf.core.internal.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLPlugin;

/**
 * This class manages meta-models and provide localization of meta-class names.
 * 
 * @author rafikj
 */
public class MSLMetaModelManager {

	private final static Map METAMODEL_MAP = new HashMap();

	private final static Map REVERSE_METAMODEL_MAP = new HashMap();

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

		if (resourceLocator != null) {

			if (element instanceof EClass)
				displayName = resourceLocator.getString("_UI_" + name //$NON-NLS-1$
					+ "_type"); //$NON-NLS-1$

			else if (element instanceof EStructuralFeature) {

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
	 * Get the ID of a meta-model object.
	 */
	public static String getID(ENamedElement element) {

		if (element instanceof EOperation) {

			RuntimeException e = new IllegalArgumentException(
				"EOperation does not support IDs"); //$NON-NLS-1$

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, MSLMetaModelManager.class,
				"getID", e); //$NON-NLS-1$

			throw e;
		}

		if (element instanceof EParameter) {

			RuntimeException e = new IllegalArgumentException(
				"EParameter does not support IDs"); //$NON-NLS-1$

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, MSLMetaModelManager.class,
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
		return (ENamedElement) REVERSE_METAMODEL_MAP.get(id);
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

			if ((container != null) && (container instanceof ENamedElement)) {

				current = (ENamedElement) container;

				id.insert(0, '.');
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