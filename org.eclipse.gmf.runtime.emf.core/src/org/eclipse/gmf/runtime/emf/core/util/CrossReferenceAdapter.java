/******************************************************************************
 * Copyright (c) 2006, 2010 IBM Corporation and others.
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentsEList;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.util.ECrossReferenceEList;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * An adapter that maintains itself as an adapter for all contained objects. It
 * can be installed for an {@link EObject}, a {@link Resource}, or a
 * {@link ResourceSet}.
 * <p>
 * This adapter maintain information on inverse references, resource imports,
 * and resource exports.
 * 
 * @author Christian Vogt (cvogt)
 * @author Christian W. Damus (cdamus)
 */
public class CrossReferenceAdapter extends ECrossReferenceAdapter {

	private Map imports = new HashMap();

	private Map exports = new HashMap();

	private boolean resolve = true;
	
    private Map eClassToChangeableFeatures = new HashMap();

    private static List nullList = new ArrayList(1);

	/**
	 * Initializes me.
	 */
	public CrossReferenceAdapter() {
		this(true);
	}

	/**
	 * Initializes me.
	 * 
	 * @param resolve
	 *            flag to determine if the proxies need to be resolved
	 */
	public CrossReferenceAdapter(boolean resolve) {
		super();

		this.resolve = resolve;
	}

	/**
	 * Updates imports and exports maps.
	 * 
	 * @param notification
	 *            the event notification
	 */
	public void selfAdapt(Notification notification) {
		super.selfAdapt(notification);
		Object notifier = notification.getNotifier();
		Object feature = notification.getFeature();

		// update import / export information when a resource
		// is unloaded or loaded
		if (notifier instanceof Resource) {
			if (notification.getFeatureID(Resource.class) == Resource.RESOURCE__IS_LOADED) {
				if (!notification.getNewBooleanValue()) {
					deregisterReferences((Resource) notifier);
				} else {
					for (Iterator i = ((Resource) notifier).getContents()
							.iterator(); i.hasNext();) {
						EObject child = (EObject) i.next();
						if (child != null) {
							updateImportsAndExports((Resource) notifier, child,
									true);
						}
					}
				}
			}

			return;
		}

		// interested in maintaining import / export information
		// when the notifier is an EObject and the feature is a
		// non-containment EReference
		if (!(notifier instanceof EObject) || !(feature instanceof EReference)) {
			return;
		}

		EReference reference = (EReference) feature;
		if (!isImportExportCapable(reference, (EObject) notifier)) {
			return;
		}

		switch (notification.getEventType()) {
		case Notification.RESOLVE:
		case Notification.SET:
		case Notification.UNSET: {
			if (notification.getPosition() != Notification.NO_INDEX) {
				EObject oldValue = (EObject) notification.getOldValue();
				if (oldValue != null) {
					deregisterReference(((EObject) notification.getNotifier())
							.eResource(), oldValue.eResource());
				}
				EObject newValue = (EObject) notification.getNewValue();
				if (newValue != null) {
					registerReference(((EObject) notification.getNotifier())
							.eResource(), newValue.eResource());
				}
			}
			break;
		}
		case Notification.ADD: {
			EObject newValue = (EObject) notification.getNewValue();
			if (newValue != null) {
				registerReference(((EObject) notification.getNotifier())
						.eResource(), newValue.eResource());
			}
			break;
		}
		case Notification.ADD_MANY: {
			Collection newValues = (Collection) notification.getNewValue();
			for (Iterator i = newValues.iterator(); i.hasNext();) {
				EObject newValue = (EObject) i.next();
				registerReference(((EObject) notification.getNotifier())
						.eResource(), newValue.eResource());
			}
			break;
		}
		case Notification.REMOVE: {
			EObject oldValue = (EObject) notification.getOldValue();
			if (oldValue != null) {
				deregisterReference(((EObject) notification.getNotifier())
						.eResource(), oldValue.eResource());
			}
			break;
		}
		case Notification.REMOVE_MANY: {
			Collection oldValues = (Collection) notification.getOldValue();
			for (Iterator i = oldValues.iterator(); i.hasNext();) {
				EObject oldValue = (EObject) i.next();
				deregisterReference(((EObject) notification.getNotifier())
						.eResource(), oldValue.eResource());
			}
			break;
		}
		}
	}

	/**
	 * Extends the superclass method to handle the removal cases of containment,
	 * to tear down aggregate (resource-level) cross-references.
	 */
	protected void handleContainment(Notification notification) {
		super.handleContainment(notification);

		Object notifier = notification.getNotifier();
		if (notifier instanceof ResourceSet) {
			// not interested in removal of resources from the resource set
			return;
		}

		switch (notification.getEventType()) {
		case Notification.ADD: {
			EObject newValue = (EObject) notification.getNewValue();

			if (newValue != null) {
				Resource resource;
				if (notifier instanceof Resource) {
					resource = (Resource) notifier;
				} else {
					resource = ((EObject) notification.getNotifier())
							.eResource();
				}

				// handle processing of the new value that has been added
				updateImportsAndExports(resource, newValue, true);
			}

			break;
		}
		case Notification.ADD_MANY: {
			Resource resource;
			if (notifier instanceof Resource) {
				resource = (Resource) notifier;
			} else {
				resource = ((EObject) notification.getNotifier()).eResource();
			}

			Collection newValues = (Collection) notification.getNewValue();

			for (Iterator iter = newValues.iterator(); iter.hasNext();) {
				EObject next = (EObject) iter.next();

				if (next != null) {
					// handle processing of the new value that has been added
					updateImportsAndExports(resource, next, true);
				}
			}
			break;
		}

		case Notification.REMOVE: {
			EObject oldValue = (EObject) notification.getOldValue();

			if (oldValue != null) {
				Resource resource;
				if (notifier instanceof Resource) {
					resource = (Resource) notifier;
				} else {
					resource = ((EObject) notification.getNotifier())
							.eResource();
				}

				// handle processing of the old value that has been removed
				updateImportsAndExports(resource, oldValue, false);
			}
			break;
		}
		case Notification.REMOVE_MANY: {
			Resource resource;
			if (notifier instanceof Resource) {
				resource = (Resource) notifier;
                
                if (!resource.isLoaded()) {
                    // purge the resource from the imports/exports map
                    deregisterReferences(resource);
                    return;
                }
			} else {
				resource = ((EObject) notification.getNotifier()).eResource();
			}

			Collection oldValues = (Collection) notification.getOldValue();

			for (Iterator iter = oldValues.iterator(); iter.hasNext();) {
				EObject next = (EObject) iter.next();

				if (next != null) {
					// handle processing of the old value that has been removed
					updateImportsAndExports(resource, next, false);
				}
			}
			break;
		}
		}
	}

	/**
	 * Updates the imports and exports map for the specified eObject
	 * 
	 * @param resource
	 *            a resource
	 * @param eObject
	 *            the specified eObject
	 * @param register
	 *            boolean flag to indicate whether to register imports or
	 *            unregister imports
	 */
	public void updateImportsAndExports(Resource resource, EObject value,
			boolean register) {
		CrossReferenceAdapter adapter = getExistingCrossReferenceAdapter(value);

		if (register) {
			if (adapter != null) {
				// now, register incoming unidirectional references and
				// opposites
				for (Iterator iter = adapter.getInverseReferences(value).iterator();
                        iter.hasNext();) {
                    EStructuralFeature.Setting next = (EStructuralFeature.Setting) iter.next();
                    EReference ref = (EReference) next.getEStructuralFeature();
                    EObject owner = next.getEObject();
                    
                    if (isImportExportCapable(ref, owner)) {
                        registerReference(owner.eResource(), resource);
                    }
				}
			}
		} else {
			// deregister the outgoing references and incoming bidirectionals
            EContentsEList.FeatureIterator crossReferences = getOptimizedCrossReferenceIterator(
            		value);
            
			while (crossReferences.hasNext()) {
				EObject referent = (EObject) crossReferences.next();

				if (referent != null) {
					EReference eReference = (EReference) crossReferences
							.feature();

					if (isImportExportCapable(eReference, referent)) {
						Resource referencedResource = referent.eResource();
						deregisterReference(resource, referencedResource);
					}
				}
			}

			// now, deregister incoming unidirectional references and opposites
			if (adapter != null) {
                for (Iterator iter = adapter.getInverseReferences(value).iterator();
                        iter.hasNext();) {
                    EStructuralFeature.Setting next = (EStructuralFeature.Setting) iter.next();
                    EReference ref = (EReference) next.getEStructuralFeature();
                    EObject owner = next.getEObject();
                    
                    if (isImportExportCapable(ref, owner)) {
                        deregisterReference(owner.eResource(), resource);
                    }
                }
			}
		}

		// process contents
		if (adapter != null) {
			adapter.updateImportsAndExportsForContents(resource, value,
					register);
		}
	}

	/**
	 * Updates the imports and exports map for the contents of the specified
	 * eObject
	 * 
	 * @param resource
	 *            a resource
	 * @param eObject
	 *            the specified eObject
	 * @param register
	 *            boolean flag to indicate whether to register imports or
	 *            unregister imports
	 */
	public void updateImportsAndExportsForContents(Resource resource,
			EObject value, boolean register) {
		// go through the children of the eObject
		for (Iterator i = resolve() ? value.eContents().iterator()
				: ((InternalEList) value.eContents()).basicIterator(); i
				.hasNext();) {
			updateImportsAndExports(resource, (EObject) i.next(), register);
		}
	}

	/**
	 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#setTarget(org.eclipse.emf.common.notify.Notifier)
	 */
	public void setTarget(Notifier target) {
		super.setTarget(target);

		if (target instanceof EObject) {
			EObject eObject = (EObject) target;
			Resource resource = eObject.eResource();

			// register the outgoing references and incoming bidirectionals
			EContentsEList.FeatureIterator crossReferences = getOptimizedCrossReferenceIterator(
					eObject);
			
			while (crossReferences.hasNext()) {
				EObject referent = (EObject) crossReferences.next();

				if (referent != null) {
					EReference eReference = (EReference) crossReferences
							.feature();

					if (isImportExportCapable(eReference, referent)) {
						Resource referencedResource = referent.eResource();
						registerReference(resource, referencedResource);
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
			deregisterReferences((Resource) notifier);
		}
	}

	/**
	 * Gets the imports of a resource.
	 * 
	 * @param referencer
	 *            the resource to retrieve imports for
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
	 * @param referenced
	 *            the resource to retrieve exports for
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
	 * Registers a reference updating the imports and exports maps accordingly.
	 * 
	 * @param referencer
	 *            the referencing resource
	 * @param referenced
	 *            the referenced resouce
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

			Counter importsCount = (Counter) importsMap.get(referenced);

			if (importsCount == null) {

				importsCount = new Counter();
				importsMap.put(referenced, importsCount);

				importAdded(referencer, referenced);
			} else {
				importsCount.inc();
			}

			Map exportsMap = getExportsMap(referenced);

			if (exportsMap == null) {
				exportsMap = new HashMap();
				exports.put(referenced, exportsMap);
			}

			Counter exportsCount = (Counter) exportsMap.get(referencer);

			if (exportsCount == null) {

				exportsCount = new Counter();
				exportsMap.put(referencer, exportsCount);

				exportAdded(referenced, referencer);
			} else {
				exportsCount.inc();
			}
		}
	}

	/**
	 * Hook to be implemented by subclasses upon the establishment of a new
	 * import of the <code>referenced</code> resource by the
	 * <code>referencer</code>. This implementation does nothing; subclasses
	 * need not call <code>super</code>.
	 * 
	 * @param referencer
	 *            the referencing resource (doing the importing)
	 * @param referenced
	 *            the resource that it references
	 */
	protected void importAdded(Resource referencer, Resource referenced) {
		// subclass hook
	}

	/**
	 * Hook to be implemented by subclasses upon the elimination of an import of
	 * the <code>referenced</code> resource by the <code>referencer</code>.
	 * This implementation does nothing; subclasses need not call
	 * <code>super</code>.
	 * 
	 * @param referencer
	 *            the formerly referencing resource (doing the importing)
	 * @param referenced
	 *            the resource that it had referenced
	 */
	protected void importRemoved(Resource referencer, Resource referenced) {
		// subclass hook
	}

	/**
	 * Hook to be implemented by subclasses upon the establishment of a new
	 * export of the <code>referenced</code> resource to the
	 * <code>referencer</code>. This implementation does nothing; subclasses
	 * need not call <code>super</code>.
	 * 
	 * @param referenced
	 *            the resource being referenced (doing the exporting)
	 * @param referencer
	 *            the referencing resource
	 */
	protected void exportAdded(Resource referenced, Resource referencer) {
		// subclass hook
	}

	/**
	 * Hook to be implemented by subclasses upon the elimination of an export of
	 * the <code>referenced</code> resource to the <code>referencer</code>.
	 * This implementation does nothing; subclasses need not call
	 * <code>super</code>.
	 * 
	 * @param referenced
	 *            the resource formerly being referenced (doing the exporting)
	 * @param referencer
	 *            the formerly referencing resource
	 */
	protected void exportRemoved(Resource referenced, Resource referencer) {
		// subclass hook
	}

	/**
	 * Deregisters a reference updating the imports and exports maps
	 * accordingly.
	 * 
	 * @param referencer
	 *            the referencing resource
	 * @param referenced
	 *            the referenced resource
	 */
	private void deregisterReference(final Resource referencer,
			final Resource referenced) {

		if ((referencer != null) && (referenced != null)
				&& (referencer != referenced)) {

			Map importsMap = getImportsMap(referencer);

			if (importsMap != null) {

				Counter importsCount = (Counter) importsMap.get(referenced);

				if ((importsCount != null) && importsCount.dec()) {

					importsMap.remove(referenced);

					importRemoved(referencer, referenced);

					if (importsMap.isEmpty()) {
						imports.remove(referencer);
					}
				}
			}

			Map exportsMap = getExportsMap(referenced);

			if (exportsMap != null) {

				Counter exportsCount = (Counter) exportsMap.get(referencer);

				if ((exportsCount != null) && exportsCount.dec()) {

					exportsMap.remove(referencer);

					exportRemoved(referenced, referencer);

					if (exportsMap.isEmpty()) {
						exports.remove(referenced);
					}
				}
			}
		}
	}

	/**
	 * Cleans up a resource from the imports and exports maps.
	 * 
	 * @param referencer
	 *            the referencing resource
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
	 * Returns a Set of EObjects that reference the given EObject. If an
	 * EReference is specified, the scope of the search is limited only to that
	 * EReference. To include all references specify a value of null. If an
	 * EClass type is specified, the returned Set will only include those
	 * referencers that match the given type. To include all types specify a
	 * value of null.
	 * 
	 * @param referenced
	 *            the referenced EObject
	 * @param reference
	 *            the reference to find referencers on, null for any reference
	 * @param type
	 *            the type of the referencers, use null for any type
	 * @return a Set of referencers
	 */
	public Set getInverseReferencers(EObject referenced, EReference reference,
			EClass type) {
		return getReferencers(getInverseReferences(referenced), reference, type);
	}

	/**
     * Like the {@link #getInverseReferencers(EObject, EReference, EClass)} method,
     * obtains referencing objects (optionally filtered by reference and type),
     * except that it additionally only considers references that are
     * {@linkplain EStructuralFeature#isChangeable() changeable} and can
     * {@linkplain EReference#isResolveProxies() reference other resources}.
     * 
     * @param referenced
     *            the referenced EObject
     * @param reference
     *            the reference to find referencers on, null for any reference
     * @param type
     *            the type of the referencers, use null for any type
     * @return a Set of referencers on potentially cross-resource references
     */
    public Set getInverseReferencersCrossResource(EObject referenced, EReference reference,
            EClass type) {
        return getReferencers(getInverseReferencesCrossResource(referenced), reference, type);
    }
    

	/**
     * Like the {@link #getInverseReferences(EObject)} method,
     * obtains settings implementing references to the specified object,
     * except that it only considers references that are
     * {@linkplain EStructuralFeature#isChangeable() changeable} and can
     * {@linkplain EReference#isResolveProxies() reference other resources}.
     * 
     * @param eObject the referenced EObject
     * 
     * @return a collection of {@link EStructuralFeature.Setting}s on
     *     potentially cross-resource references
     */
    public Collection getInverseReferencesCrossResource(EObject eObject) {
        return getInverseReferencesCrossResource(eObject, !resolve());
    }

	/**
	 * Returns a Set of EObjects that reference the given EObject through a uni
	 * directional EReferences. If an EReference is specified, the scope of the
	 * search is limited only to that EReference. To include all references
	 * specify a value of null. If an EClass type is specified, the returned Set
	 * will only include those referencers that match the given type. To include
	 * all types specify a value of null.
	 * 
	 * @param referenced
	 *            the referenced EObject
	 * @param reference
	 *            the reference to find referencers on, null for any reference
	 * @param type
	 *            the type of the referencers, use null for any type
	 * @return a Set of referencers
	 */
	public Set getNonNavigableInverseReferencers(EObject referenced,
			EReference reference, EClass type) {
		return getReferencers(getNonNavigableInverseReferences(referenced),
				reference, type);
	}

	/**
	 * Extracts the EObjects from the EStructuralFeature.Setting references and
	 * returns a filtered Set based on the given reference and type.
	 * 
	 * @param references
	 *            a collection of EStructuralFeature.Setting
	 * @param reference
	 *            the reference to find referencers on, null for any reference
	 * @param type
	 *            the type of the referencers, use null for any type
	 * @return a Set of referencers
	 */
	private Set getReferencers(Collection references, EReference reference,
			EClass type) {
		Set set = new HashSet();
		if (!references.isEmpty()) {
			for (Iterator iter = references.iterator(); iter.hasNext();) {
				Setting setting = (Setting) iter.next();
				if (reference == null
						|| reference == setting.getEStructuralFeature()) {
					EObject referencer = setting.getEObject();
					if (referencer != null
							&& (type == null || type.isInstance(referencer))) {
						set.add(referencer);
					}
				}
			}
		}
		return set;
	}

	/**
	 * Searches the adapter list of the given Notifier for a
	 * CrossReferenceAdapter. If not found, returns null.
	 * 
	 * @param notifier
	 *            the notifier to search
	 * @return the CrossReferenceAdapter if found, otherwise null
	 */
	public static CrossReferenceAdapter getExistingCrossReferenceAdapter(
			Notifier notifier) {
		if (notifier == null) {
			return null;
		}

		List adapters = notifier.eAdapters();

		for (int i = 0, size = adapters.size(); i < size; ++i) {
			Adapter adapter = (Adapter) adapters.get(i);
			if (adapter instanceof CrossReferenceAdapter) {
				return (CrossReferenceAdapter) adapter;
			}
		}
		return null;
	}

	/**
	 * Obtains the cross-reference adapter for the specified resource set, if
	 * necessary creating it and attaching it.
	 * 
	 * @param resourceSet
	 *            the resource set
	 * 
	 * @return the resourceSet's cross-reference adapter
	 */
	public static CrossReferenceAdapter getCrossReferenceAdapter(
			ResourceSet resourceSet) {
		if (resourceSet == null) {
			return null;
		}

		CrossReferenceAdapter result = getExistingCrossReferenceAdapter(resourceSet);

		if (result == null) {
			result = new CrossReferenceAdapter();
			resourceSet.eAdapters().add(result);
		}

		return result;
	}

	/**
	 * A mutable integer used to count number of object-level references between
	 * two resources.
	 * 
	 * @author Christian W. Damus (cdamus)
	 */
	private static final class Counter {
		private int value = 1;

		Counter() {
			super();
		}

		/**
		 * Increments me.
		 */
		void inc() {
			value++;
		}

		/**
		 * Decrements me.
		 * 
		 * @return <code>true</code> if I am now zero; <code>false</code>,
		 *         otherwise
		 */
		boolean dec() {
			return --value <= 0;
		}
	}

	protected boolean resolve() {
		return this.resolve;
	}

	public Collection getInverseReferences(EObject eObject, boolean _resolve) {
		Collection result = new ArrayList();

		if (_resolve) {
			resolveAll(eObject);
		}

		EObject eContainer = eObject.eContainer();
		if (eContainer != null) {
			result.add(((InternalEObject) eContainer).eSetting(eObject
					.eContainmentFeature()));
		}

		Collection nonNavigableInverseReferences = (Collection) inverseCrossReferencer
				.get(eObject);
		if (nonNavigableInverseReferences != null) {
			result.addAll(nonNavigableInverseReferences);
		}

		for (Iterator i = eObject.eClass().getEAllReferences().iterator(); i
				.hasNext();) {
			EReference eReference = (EReference) i.next();
			EReference eOpposite = eReference.getEOpposite();
			if (eOpposite != null && !eReference.isContainer()
					&& !eReference.isContainment()
					&& eObject.eIsSet(eReference)) {
				if (FeatureMapUtil.isMany(eObject, eReference)) {
					Object collection = eObject.eGet(eReference);
					for (Iterator j = resolve() ? ((Collection) collection)
							.iterator() : ((InternalEList) collection)
							.basicIterator(); j.hasNext();) {
						InternalEObject referencingEObject = (InternalEObject) j
								.next();
						result.add(referencingEObject.eSetting(eOpposite));
					}
				} else {
					// although the reference is set, the value could be null
					InternalEObject referencingEObject = ((InternalEObject) eObject
							.eGet(eReference, resolve()));
					if (referencingEObject != null) {
						result.add(referencingEObject.eSetting(eOpposite));
					}
				}
			}
		}

		return result;
	}

	/**
	 * Computes the references defined by the specified EClass that are
	 * {@linkplain EStructuralFeature#isChangeable() changeable}.
	 * 
	 * @param eCls an EClass
	 * @return a list of its {@link EReference}s that are changeable
	 */
    private List getCrossReferencesChangeableFeatures(EClass eCls) {
        List features = (List) eClassToChangeableFeatures.get(eCls);
        if (features == null) {
            features = nullList;
            EStructuralFeature[] crossReferenceFeatures =

            ((EClassImpl.FeatureSubsetSupplier) eCls
                .getEAllStructuralFeatures()).crossReferences();
            if (crossReferenceFeatures != null) {
                features = new ArrayList(crossReferenceFeatures.length);
                for (int i = 0; i < crossReferenceFeatures.length; i++) {
                    EStructuralFeature feature = crossReferenceFeatures[i];
                    if (isMutable(feature))
                        features.add(feature);
                }
            }
            eClassToChangeableFeatures.put(eCls, features);
        }
        return features != nullList ? features
            : null;
    }

    /**
     * Queries whether a feature is mutable.  A feature is considered
     * mutable if and only if it is changeable and it is either not derived
     * or it is a member of a feature map (though not itself a feature map).
     * 
     * @param feature the feature to test
     * 
     * @return <code>true</code> if the reference is mutable;
     *     <code>false</code>, otherwise
     */
    static boolean isMutable(EStructuralFeature feature) {
        boolean result = feature.isChangeable();
        
        if (result) {
            if (feature.isDerived()) {
                // check whether it is a feature-map member that is not, itself,
                //    a feature map
                EStructuralFeature group = ExtendedMetaData.INSTANCE.getGroup(feature);
                
                result = (group != null) && !FeatureMapUtil.isFeatureMap(feature);
            }
        }
        
        return result;
    }

	/**
	 * An iterator over the references defined by the specified EObject that
	 * are {@linkplain EStructuralFeature#isChangeable() changeable}.
	 * 
	 * @param eObj an EObject
	 * @return an iterator over its {@link EReference}s that are changeable
	 */
    private EContentsEList.FeatureIterator getOptimizedCrossReferenceIterator(
            EObject eObj) {
        List features = getCrossReferencesChangeableFeatures(eObj.eClass());
        if (features != null) {
            EContentsEList list = null;
            if (features.size() > 0) {
                list = new ECrossReferenceEList(eObj,
                    (EStructuralFeature[]) features
                        .toArray(new EStructuralFeature[features.size()])) {
                    // to get to the protected constructor
                };
            } else {
                list = ECrossReferenceEList.EMPTY_CROSS_REFERENCE_ELIST;
            }

            return (EContentsEList.FeatureIterator) (resolve() ? list
                .iterator()
                : ((InternalEList) list).basicIterator());
        }
        return (EContentsEList.FeatureIterator) ECrossReferenceEList.EMPTY_CROSS_REFERENCE_ELIST
            .iterator();
    }

	/**
     * Like the {@link #getInverseReferences(EObject, boolean)} method,
     * obtains settings implementing references to the specified object,
     * except that it only considers references that are
     * {@linkplain EStructuralFeature#isChangeable() changeable} and can
     * {@linkplain EReference#isResolveProxies() reference other resources}.
     * 
     * @param eObject the referenced EObject
     * @param resolve whether to resolve proxies or not
     * 
     * @return a collection of {@link EStructuralFeature.Setting}s on
     *     potentially cross-resource references
     */
    public Collection getInverseReferencesCrossResource(EObject eObject, boolean resolve) {
        Collection result = new ArrayList();

        if (resolve) {
            resolveAll(eObject);
        }

        EObject eContainer = eObject.eContainer();
        if (eContainer != null) {
            result.add(((InternalEObject) eContainer).eSetting(eObject
                    .eContainmentFeature()));
        }

        Collection nonNavigableInverseReferences = (Collection) inverseCrossReferencer
                .get(eObject);
        if (nonNavigableInverseReferences != null) {
            result.addAll(nonNavigableInverseReferences);
        }

        for (Iterator i = eObject.eClass().getEAllReferences().iterator(); i
                .hasNext();) {
            EReference eReference = (EReference) i.next();
            EReference eOpposite = eReference.getEOpposite();
            
            if (eOpposite != null
            		&& isImportExportCapable(eReference, eObject)
            		&& eObject.eIsSet(eReference)) {
                if (FeatureMapUtil.isMany(eObject, eReference)) {
                    Object collection = eObject.eGet(eReference);
                    for (Iterator j = resolve() ? ((Collection) collection)
                            .iterator() : ((InternalEList) collection)
                            .basicIterator(); j.hasNext();) {
                        InternalEObject referencingEObject = (InternalEObject) j
                                .next();
                        result.add(referencingEObject.eSetting(eOpposite));
                    }
                } else {
                    // although the reference is set, the value could be null
                    InternalEObject referencingEObject = ((InternalEObject) eObject
                            .eGet(eReference, resolve()));
                    if (referencingEObject != null) {
                        result.add(referencingEObject.eSetting(eOpposite));
                    }
                }
            }
        }

        return result;
    }
    
    /**
     * Queries whether the specified reference, applied to the given owner
     * object, is capable of establishing a resource import or export by
     * virtue of being a mutable cross-resource reference.
     * <p>
     * A reference is considered to support resource imports and exports if
     * all of the following apply:
     * </p>
     * <ul>
     *   <li>the reference is not a container or containment reference.  Note
     *       that this excludes cross-resource containment from registering
     *       as an import/export dependency</li>
     *   <li>the reference resolves proxies</li>
     *   <li>the reference is changeable</li>
     * </ul>
     * 
     * @param reference a reference feature
     * @param owner an object under consideration that defines this reference.
     *     Subclasses may need to introspect the object or its EClass to further
     *     refine their criteria
     * 
     * @return <code>true</code> if this reference in the context of this
     *     owner should be counted for resource imports and exports;
     *     false, otherwise
     */
   protected boolean isImportExportCapable(EReference reference, EObject owner) {
    	return !reference.isContainer()
        	&& !reference.isContainment()
        	&& reference.isResolveProxies() // can be cross-resource
        	&& reference.isChangeable();    // not computed
    }
}