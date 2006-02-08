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

package org.eclipse.gmf.runtime.emf.core.internal.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

/**
 * An adapter that maintains itself as an adapter for all contained objects.
 * It can be installed for an {@link EObject}, a {@link Resource}, or a {@link ResourceSet}.
 * <p>
 * This adapter maintain information on inverse references, resource imports, and resource
 * exports.
 * 
 * @author Christian Vogt (cvogt)
 * @author Christian W. Damus (cdamus)
 */
public class CrossReferenceAdapter extends ECrossReferenceAdapter {

	private Map imports = new HashMap();

	private Map exports = new HashMap();

	/**
	 * Initializes me.
	 */
	public CrossReferenceAdapter() {
		super();
	}

	/**
	 * Updates imports and exports maps.
	 * 
	 * @param notification the event notification
	 */
	public void selfAdapt(Notification notification) {
		super.selfAdapt(notification);
		Object notifier = notification.getNotifier();
		Object feature = notification.getFeature();

		// update import / export information when a resource
		// is unloaded
		if (notifier instanceof Resource) {
			if (notification.getFeatureID(Resource.class) == Resource.RESOURCE__IS_LOADED
					&& !notification.getNewBooleanValue()) {
				deregisterReferences((Resource)notifier);
			}
			return;
		}

		// interested in maintaining import / export information
		// when the notifier is an EObject and the feature is a
		// non-containment EReference
		if (!(notifier instanceof EObject)
				|| !(feature instanceof EReference)) {
			return;
		}

		EReference reference = (EReference)feature;
		if (reference.isContainment()) {
			return;
		}

		switch (notification.getEventType()) {
			case Notification.RESOLVE: 
			case Notification.SET:
			case Notification.UNSET: {
				EObject oldValue = (EObject) notification.getOldValue();
				if (oldValue != null) {
					deregisterReference(
							((EObject)notification.getNotifier()).eResource(),
							oldValue.eResource());
				}
				EObject newValue = (EObject) notification.getNewValue();
				if (newValue != null) {
					registerReference(
							((EObject)notification.getNotifier()).eResource(),
							newValue.eResource());
				}
				break;
			}
			case Notification.ADD: {
				EObject newValue = (EObject) notification.getNewValue();
				if (newValue != null) {
					registerReference(
							((EObject)notification.getNotifier()).eResource(),
							newValue.eResource());
				}
				break;
			}
			case Notification.ADD_MANY: {
				Collection newValues = (Collection) notification.getNewValue();
				for (Iterator i = newValues.iterator(); i.hasNext();) {
					EObject newValue = (EObject) i.next();
					registerReference(
							((EObject)notification.getNotifier()).eResource(),
							newValue.eResource());
				}
				break;
			}
			case Notification.REMOVE: {
				EObject oldValue = (EObject) notification.getOldValue();
				if (oldValue != null) {
					deregisterReference(
							((EObject)notification.getNotifier()).eResource(),
							oldValue.eResource());
				}
				break;
			}
			case Notification.REMOVE_MANY: {
				Collection oldValues = (Collection) notification.getOldValue();
				for (Iterator i = oldValues.iterator(); i.hasNext();) {
					EObject oldValue = (EObject) i.next();
					deregisterReference(
							((EObject)notification.getNotifier()).eResource(),
							oldValue.eResource());
				}
				break;
			}
		}
	}
	
	/**
	 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#setTarget(org.eclipse.emf.common.notify.Notifier)
	 */
	public void setTarget(Notifier target) {
		super.setTarget(target);
		if (target instanceof Resource) {
			Resource resource = (Resource)target;
			for (TreeIterator conents = resource.getAllContents(); conents.hasNext(); ) {
				EObject eObject = (EObject)conents.next();
				List allRefs = eObject.eClass().getEAllReferences();
				for (int i = 0; i < allRefs.size(); ++i) {
					EReference eReference = (EReference)allRefs.get(i);
					if (!eReference.isContainer() && !eReference.isContainment() && eObject.eIsSet(eReference)) {
						if (eReference.isMany()) {
							for (Iterator iter = ((Collection)eObject.eGet(eReference)).iterator(); iter.hasNext(); ) {
								registerReference(resource, ((EObject)iter.next()).eResource());
							}
						} else {
							registerReference(resource, ((EObject)eObject.eGet(eReference)).eResource());
						}
					}
				}
			}
	    }
	}

	/**
	 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#unsetTarget(org.eclipse.emf.common.notify.Notifier)
	 */
	public void unsetTarget(Notifier notifier) {
		super.unsetTarget(notifier);
		if (notifier instanceof Resource) {
			deregisterReferences((Resource)notifier);
	    }
	}

	/**
	 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#isIncluded(org.eclipse.emf.ecore.EReference)
	 */
	protected boolean isIncluded(EReference eReference) {
		return super.isIncluded(eReference) && eReference.isChangeable()
			&& !eReference.isContainer() && !eReference.isContainment();
	}

	/**
	 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#getInverseReferences(org.eclipse.emf.ecore.EObject)
	 */
	public Collection getInverseReferences(EObject eObject) {
		Collection result = new ArrayList();

		// removed the addition of eContainer from default behavior
		
		Collection nonNavigableInverseReferences = (Collection)inverseCrossReferencer.get(eObject);
		if (nonNavigableInverseReferences != null) {
			result.addAll(nonNavigableInverseReferences);
		}
		
		for (Iterator i = eObject.eClass().getEAllReferences().iterator(); i.hasNext(); ) {
			EReference eReference = (EReference)i.next();
			EReference eOpposite = eReference.getEOpposite();
			// added eReference.isChangeable() from default behavior
			if (eOpposite != null && eReference.isChangeable() && !eReference.isContainer() && !eReference.isContainment() && eObject.eIsSet(eReference)) {
				if (eReference.isMany()) {
					for (Iterator j = ((Collection)eObject.eGet(eReference)).iterator(); j.hasNext(); ) {
						InternalEObject referencingEObject = (InternalEObject)j.next();
						result.add(referencingEObject.eSetting(eOpposite));
					}
				} else {
					result.add(((InternalEObject)eObject.eGet(eReference)).eSetting(eOpposite));
				}
			}
		}
		
		return result;
	}

	/**
	 * Gets the imports of a resource.
	 * 
	 * @param referencer the resource to retrieve imports for
	 * @return a Set of resource imports
	 */
	public Set getImports(Resource referencer) {

		Map importsMap = getImportsMap(referencer);

		if (importsMap != null) {
			return Collections.unmodifiableSet(importsMap.keySet());
		} else {
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * Gets the exports of a resource.
	 * 
	 * @param referenced the resource to retrieve exports for
	 * @return a Set of resource exports
	 */
	public Set getExports(Resource referenced) {

		Map exportsMap = getExportsMap(referenced);

		if (exportsMap != null) {
			return Collections.unmodifiableSet(exportsMap.keySet());
		} else {
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * Returns the imports map of the given resource.
	 * 
	 * @param resource
	 * @return imports map of the given resource
	 */
	private Map getImportsMap(Resource resource) {
		return (Map) imports.get(resource);
	}

	/**
	 * Returns the exports map of the given resource.
	 * 
	 * @param resource
	 * @return exports map of the given resource
	 */
	private Map getExportsMap(Resource resource) {
		return (Map) exports.get(resource);
	}

	/**
	 * Registers a reference updating the imports and exports maps
	 * accordingly.
	 *
	 * @param referencer the referencing resource
	 * @param referenced the referenced resouce
	 */
	private void registerReference(final Resource referencer,
			final Resource referenced) {

		if ((referencer != null) && (referenced != null)
			&& (referencer != referenced)) {

			Map importsMap = getImportsMap(referencer);

			if (importsMap == null) {
				importsMap = new HashMap();
				imports.put(referencer, importsMap);
			}

			Integer importsCount = (Integer) importsMap.get(referenced);

			if (importsCount == null) {

				importAdded(referencer, referenced);

				importsCount = new Integer(1);

			} else {
				importsCount = new Integer(importsCount.intValue() + 1);
			}

			importsMap.put(referenced, importsCount);

			Map exportsMap = getExportsMap(referenced);

			if (exportsMap == null) {
				exportsMap = new HashMap();
				exports.put(referenced, exportsMap);
			}

			Integer exportsCount = (Integer) exportsMap.get(referencer);

			if (exportsCount == null) {

				exportAdded(referenced, referencer);

				exportsCount = new Integer(1);

			} else {
				exportsCount = new Integer(exportsCount.intValue() + 1);
			}

			exportsMap.put(referencer, exportsCount);
		}
	}
	
	/**
	 * Hook to be implemented by subclasses upon the establishment of a new
	 * import of the <code>referenced</code> resource by the <code>referencer</code>.
	 * This implementation does nothing; subclasses need not call <code>super</code>.
	 * 
	 * @param referencer the referencing resource (doing the importing)
	 * @param referenced the resource that it references
	 */
	protected void importAdded(Resource referencer, Resource referenced) {
		// subclass hook
	}

	/**
	 * Hook to be implemented by subclasses upon the elimination of an
	 * import of the <code>referenced</code> resource by the <code>referencer</code>.
	 * This implementation does nothing; subclasses need not call <code>super</code>.
	 * 
	 * @param referencer the formerly referencing resource (doing the importing)
	 * @param referenced the resource that it had referenced
	 */
	protected void importRemoved(Resource referencer, Resource referenced) {
		// subclass hook
	}
	
	/**
	 * Hook to be implemented by subclasses upon the establishment of a new
	 * export of the <code>referenced</code> resource to the <code>referencer</code>.
	 * This implementation does nothing; subclasses need not call <code>super</code>.
	 * 
	 * @param referenced the resource being referenced (doing the exporting)
	 * @param referencer the referencing resource
	 */
	protected void exportAdded(Resource referenced, Resource referencer) {
		// subclass hook
	}

	/**
	 * Hook to be implemented by subclasses upon the elimination of an
	 * export of the <code>referenced</code> resource to the <code>referencer</code>.
	 * This implementation does nothing; subclasses need not call <code>super</code>.
	 * 
	 * @param referenced the resource formerly being referenced (doing the exporting)
	 * @param referencer the formerly referencing resource
	 */
	protected void exportRemoved(Resource referenced, Resource referencer) {
		// subclass hook
	}

	/**
	 * Deregisters a reference updating the imports and exports maps
	 * accordingly.
	 * 
	 * @param referencer the referencing resource
	 * @param referenced the referenced resource
	 */
	private void deregisterReference(final Resource referencer,
			final Resource referenced) {

		if ((referencer != null) && (referenced != null)
			&& (referencer != referenced)) {

			Map importsMap = getImportsMap(referencer);

			if (importsMap != null) {

				Integer importsCount = (Integer) importsMap.get(referenced);

				if (importsCount != null) {

					if (importsCount.intValue() < 2) {

						importsMap.remove(referenced);

						importRemoved(referencer, referenced);
					} else {
						importsMap.put(referenced, new Integer(importsCount
							.intValue() - 1));
					}
				}

				if (importsMap.isEmpty()) {
					imports.remove(referencer);
				}
			}

			Map exportsMap = getExportsMap(referenced);

			if (exportsMap != null) {

				Integer exportsCount = (Integer) exportsMap.get(referencer);

				if (exportsCount != null) {

					if (exportsCount.intValue() < 2) {

						exportsMap.remove(referencer);

						exportRemoved(referenced, referencer);
					} else {
						exportsMap.put(referencer, new Integer(exportsCount
							.intValue() - 1));
					}
				}

				if (exportsMap.isEmpty()) {
					exports.remove(referenced);
				}
			}
		}
	}

	/**
	 * Cleans up a resource from the imports and exports maps.
	 * 
	 * @param referencer the referencing resource
	 */
	private void deregisterReferences(final Resource referencer) {

		Object[] resImports = getImports(referencer).toArray();

		for (int i = 0; i < resImports.length; i++) {

			final Resource referenced = (Resource) resImports[i];

			Map importsMap = getImportsMap(referencer);

			if (importsMap != null) {

				importsMap.remove(referenced);

				importRemoved(referencer, referenced);

				if (importsMap.isEmpty()) {
					imports.remove(referencer);
				}
			}

			Map exportsMap = getExportsMap(referenced);

			if (exportsMap != null) {

				exportsMap.remove(referencer);

				exportRemoved(referenced, referencer);

				if (exportsMap.isEmpty()) {
					exports.remove(referenced);
				}
			}
		}
	}
	

	/**
	 * Returns a Set of EObjects that reference the given EObject.
	 * If an EReference is specified, the scope of the search is limited
	 * only to that EReference. To include all references specify a value of null.
	 * If an EClass type is specified, the returned Set will only include those
	 * referencers that match the given type. To include all types specify a value of null.
	 * 
	 * @param referenced the referenced EObject
	 * @param reference the reference to find referencers on, null for any reference
	 * @param type the type of the referencers, use null for any type
	 * @return a Set of referencers
	 */
	public Set getInverseReferencers(EObject referenced, EReference reference, EClass type) {
		return getReferencers(getInverseReferences(referenced), reference, type);
	}

	/**
	 * Returns a Set of EObjects that reference the given EObject through a uni
	 * directional EReferences. If an EReference is specified, the scope of the
	 * search is limited only to that EReference. To include all references specify
	 * a value of null. If an EClass type is specified, the returned Set will only
	 * include those referencers that match the given type. To include all types
	 * specify a value of null.
	 * 
	 * @param referenced the referenced EObject
	 * @param reference the reference to find referencers on, null for any reference
	 * @param type the type of the referencers, use null for any type
	 * @return a Set of referencers
	 */
	public Set getNonNavigableInverseReferencers(EObject referenced, EReference reference, EClass type) {
		return getReferencers(getNonNavigableInverseReferences(referenced), reference, type);
	}

	/**
	 * Extracts the EObjects from the EStructuralFeature.Setting references
	 * and returns a filtered Set based on the given reference and type.
	 * 
	 * @param references a collection of EStructuralFeature.Setting
	 * @param reference the reference to find referencers on, null for any reference
	 * @param type the type of the referencers, use null for any type
	 * @return a Set of referencers
	 */
	private Set getReferencers(Collection references, EReference reference, EClass type) {
		Set set = new HashSet();
		if (!references.isEmpty()) {
			for (Iterator iter = references.iterator(); iter.hasNext(); ) {
				Setting setting = (Setting) iter.next();
				if (reference == null || reference == setting.getEStructuralFeature()) {
					EObject referencer = setting.getEObject();
					if (referencer != null && (type == null || type.isInstance(referencer))) {
						set.add(referencer);
					}
				}
			}
		}
		return set;
	}

	/**
	 * Searches the adapter list of the given Notifier for a CrossReferenceAdapter.
	 * If not found, returns null.
	 * 
	 * @param notifier the notifier to search
	 * @return the CrossReferenceAdapter if found, otherwise null
	 */
	public static CrossReferenceAdapter getExistingCrossReferenceAdapter(Notifier notifier) {
		List adapters = notifier.eAdapters();
		
		for (int i = 0, size = adapters.size(); i < size; ++i) {
			Adapter adapter = (Adapter)adapters.get(i);
			if (adapter instanceof CrossReferenceAdapter) {
				return (CrossReferenceAdapter)adapter;
			}
		}
		return null;
	}

	/**
	 * Obtains the cross-reference adapter for the specified editing domain's
	 * resource set, if necessary creating it and attaching it.
	 * 
	 * @param domain an editing domain
	 * 
	 * @return the editing domain's cross-reference adapter
	 */
	public static CrossReferenceAdapter getCrossReferenceAdapter(TransactionalEditingDomain domain) {
		CrossReferenceAdapter result = getExistingCrossReferenceAdapter(
			domain.getResourceSet());
		
		if (result == null) {
			result = new CrossReferenceAdapter();
			domain.getResourceSet().eAdapters().add(result);
		}
		
		return result;
	}

}
