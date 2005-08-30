/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.index;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;

/**
 * This class is used to visit the referencers of an EObject.
 * 
 * @author rafikj
 *  
 */
public abstract class MSLReferenceVisitor {

	protected MSLEditingDomain domain = null;

	protected EObject referencedObject = null;

	private boolean resolve = true;

	/**
	 * Constructor.
	 */
	public MSLReferenceVisitor(MSLEditingDomain domain, EObject eObject) {
		this(domain, eObject, true);
	}

	/**
	 * Constructor.
	 */
	public MSLReferenceVisitor(MSLEditingDomain domain, EObject eObject,
			boolean resolve) {

		this.domain = domain;
		this.referencedObject = eObject;
		this.resolve = resolve;
	}

	/**
	 * Visit all the referencers.
	 */
	public void visitReferencers() {

		Map featureMap = domain.getObjectIndexer().getGroupedReferencers(
			referencedObject, resolve);

		// operate on a clone to prevent concurrent access exceptions.
		Object[] references = featureMap.keySet().toArray();

		for (int i = 0; i < references.length; i++) {

			EReference reference = (EReference) references[i];

			Object value = featureMap.get(reference);

			if (value instanceof List) {

				List referencerList = (List) value;

				// operate on a clone to prevent concurrent access exceptions.
				Object[] referencers = referencerList.toArray();

				for (int j = 0; j < referencers.length; j++) {

					WeakReference r = (WeakReference) referencerList.get(j);

					if (r != null) {

						EObject referencer = (EObject) r.get();

						if (referencer != null)
							visitedReferencer(reference, referencer);
					}
				}

			} else if (value instanceof WeakReference) {

				WeakReference r = (WeakReference) value;

				if (r != null) {

					EObject referencer = (EObject) r.get();

					if (referencer != null)
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
}