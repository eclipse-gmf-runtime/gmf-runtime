/******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.internal.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;

/**
 * Defines utility methods for <code>EClass</code>es.
 * 
 * @author ldamus
 */
public class EClassUtil {

	/**
	 * Gets the list of super types for <code>eClass</code>. Unlike
	 * {@link EClass#getEAllSuperTypes()}, this method assumes that all models
	 * implicitly extend <code>EObject</code>.
	 * 
	 * @param eClass
	 *            the EClass
	 * @return the list of super types for the <code>eClass</code>, including
	 *         <code>EObject</code>
	 */
	public static List<EClass> getEAllSuperTypes(EClass eClass) {
		List<EClass> superTypes = new ArrayList<EClass>(eClass
				.getEAllSuperTypes());

		// Bugzilla 298661: assume all models implicitly extend EObject
		if (!superTypes.contains(EcorePackage.Literals.EOBJECT)) {
			superTypes.add(0, EcorePackage.Literals.EOBJECT);
		}
		return superTypes;
	}
}
