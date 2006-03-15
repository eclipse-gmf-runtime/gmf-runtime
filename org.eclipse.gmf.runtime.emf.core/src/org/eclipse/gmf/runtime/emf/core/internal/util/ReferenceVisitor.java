/******************************************************************************
 * Copyright (c) 2004-2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.gmf.runtime.emf.core.util.CrossReferenceAdapter;

/**
 * This class is used to visit the referencers of an EObject.
 * 
 * @author rafikj
 * @author Christian W. Damus (cdamus)
 */
public abstract class ReferenceVisitor {

	protected CrossReferenceAdapter crossReferencer = null;

	protected EObject referencedObject = null;

	/**
	 * Constructor.
	 */
	public ReferenceVisitor(EObject eObject) {
		this.crossReferencer = CrossReferenceAdapter.getExistingCrossReferenceAdapter(
			eObject);
		this.referencedObject = eObject;
	}


	/**
	 * Visit all the referencers.
	 */
	public void visitReferencers() {

		if (crossReferencer != null) {
			Map featureMap = getGroupedReferencers(referencedObject);
	
			// operate on a clone to prevent concurrent access exceptions.
			Object[] references = featureMap.keySet().toArray();
	
			for (int i = 0; i < references.length; i++) {
	
				EReference reference = (EReference) references[i];
	
				List referencerList = (List)featureMap.get(reference);
	
				// operate on a clone to prevent concurrent access exceptions.
				Object[] referencers = referencerList.toArray();
	
				for (int j = 0; j < referencers.length; j++) {
					EObject referencer = (EObject) referencerList.get(j);
	
					visitedReferencer(reference, referencer);
				}
			}
		}
	}

	/**
	 * Override to implement processing the visit.
	 */
	protected abstract void visitedReferencer(EReference reference,
			EObject referencer);

	/**
	 * For the given referenced EObject, returns a Map whose keys are EReferences
	 * and values are EObjects that reference the referenced EObject with the key
	 * EReference.
	 * 
	 * @param referenced the referenced EObject
	 * @return a Map of referencers
	 */
	private Map getGroupedReferencers(EObject referenced) {

		Map newMap = new HashMap();

		// first group all the inverse referencers
		Collection nonNavigableInverseReferences = 
			crossReferencer.getNonNavigableInverseReferences(referenced);

		if (nonNavigableInverseReferences != null &&
				!nonNavigableInverseReferences.isEmpty()) {
			for (Iterator iter = nonNavigableInverseReferences.iterator(); iter
					.hasNext();) {
				Setting setting = (Setting) iter.next();
				List list = (List)newMap.get(setting.getEStructuralFeature());
				if (list == null) {
					list = new ArrayList();
					list.add(setting.getEObject());
					newMap.put(setting.getEStructuralFeature(), list);
				} else {
					list.add(setting.getEObject());
				}
			}
		}

		// next loop through all the EReferences to find referencers
		// for those EReferences with opposites
		List features = referenced.eClass().getEAllReferences();

		for (Iterator i = features.iterator(); i.hasNext();) {

			EReference reference = (EReference) i.next();

			EReference opposite = reference.getEOpposite();

			if (opposite != null && reference.isChangeable()
					&& !reference.isContainer() && !reference.isContainment()) {

				Set referencers = crossReferencer.getInverseReferencers(
					referenced, opposite, null);

				if (!referencers.isEmpty()) {

					newMap.put(opposite, new ArrayList(referencers));
				}
			}
		}

		if (newMap != null) {
			return Collections.unmodifiableMap(newMap);
		} else {
			return Collections.EMPTY_MAP;
		}
	}
}