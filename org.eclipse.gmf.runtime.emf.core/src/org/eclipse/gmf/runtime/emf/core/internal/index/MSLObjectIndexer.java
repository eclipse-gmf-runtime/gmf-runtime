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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.resources.MResourceFactory;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLUtil;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;

/**
 * This class manages the reverse reference map feature at the object level.
 * When resources are loaded the reverse map gets updated without causing more
 * resources to be loaded.
 * 
 * @author rafikj
 */
public class MSLObjectIndexer {

	private MSLEditingDomain domain = null;

	/**
	 * Constructor.
	 */
	public MSLObjectIndexer(MSLEditingDomain domain) {

		super();

		this.domain = domain;
	}

	/**
	 * Registers a reference in the reverse reference map.
	 */
	public boolean registerReference(EObject referencer, EObject referenced,
			EReference reference) {

		// ignore non changeable and container/containment features.
		if ((!reference.isChangeable()) || (reference.isContainer())
			|| (reference.isContainment()))
			return false;

		if (referencer.eIsProxy())
			return false;

		// register resource reference.
		Resource referencerResource = referencer.eResource();

		Resource referencedResource = MSLUtil.getResource(domain, referenced);

		if (referenced.eIsProxy()) {

			URI uri = EcoreUtil.getURI(referenced);

			Resource.Factory factory = Resource.Factory.Registry.INSTANCE
				.getFactory(uri);

			if (!(factory instanceof MResourceFactory)) {

				if ((referencerResource == referencedResource)
					|| ((referencedResource != null) && (referencedResource
						.isLoaded())))
					referenced = EcoreUtil.resolve(referenced, domain
						.getResourceSet());
			}
		}

		domain.getResourceIndexer().registerReference(referencerResource,
			referencedResource);

		return doRegisterReference(referencer, referenced, reference);
	}

	/**
	 * Registers a reference in the reverse reference map.
	 */
	private boolean doRegisterReference(EObject referencer, EObject referenced,
			EReference reference) {

		// ignore features that have opposites.
		if (reference.getEOpposite() != null)
			return false;

		// objects must be resolved.
		if (referenced.eIsProxy())
			return false;

		Map featureMap = getFeatureMap(referenced);

		if (featureMap == null)
			featureMap = createFeatureMap(referenced);

		WeakReference ref = new WeakReference(referencer);

		Object value = featureMap.get(reference);

		// value is a list.
		if (value instanceof List) {

			int index = -1;

			List referencerList = (List) value;

			for (int i = 0, count = referencerList.size(); i < count; i++) {

				WeakReference r = (WeakReference) referencerList.get(i);

				if (r == null) {

					// found empty slot.
					if (index == -1)
						index = i;

				} else {

					Object current = r.get();

					// object already there.
					if (current == referencer)
						ref = null;

					else if (current == null) {

						referencerList.set(i, null);

						// found empty slot.
						if (index == -1)
							index = i;
					}
				}
			}

			// if not already there add.
			if (ref != null) {

				// resuse slot if any.
				if (index != -1)
					referencerList.set(index, ref);
				else
					referencerList.add(ref);
			}

		} else if (value instanceof WeakReference) {

			// value is a weak reference.
			WeakReference r = (WeakReference) value;

			Object current = r.get();

			if (current == referencer) {

				// already there.
				ref = null;

			} else if (current == null) {

				// put in empty slot.
				featureMap.put(reference, ref);

			} else {

				// create a new list if more than one.
				List referencerList = new ArrayList(2);

				referencerList.add(r);
				referencerList.add(ref);

				featureMap.put(reference, referencerList);
			}

		} else if (value == null) {

			// found empty slot.
			featureMap.put(reference, ref);
		}

		return true;
	}

	/**
	 * Deregisters a reference in the reverse reference map.
	 */
	public void deregisterReference(EObject referencer, EObject referenced,
			EReference reference) {

		// ignore non changeable and container/containment features.
		if ((!reference.isChangeable()) || (reference.isContainer())
			|| (reference.isContainment()))
			return;

		if (referencer.eIsProxy())
			return;

		// deregister resource reference.
		Resource referencerResource = referencer.eResource();

		Resource referencedResource = MSLUtil.getResource(domain, referenced);

		domain.getResourceIndexer().deregisterReference(referencerResource,
			referencedResource);

		doDeregisterReference(referencer, referenced, reference);
	}

	/**
	 * Deregisters a reference in the reverse reference map.
	 */
	private void doDeregisterReference(EObject referencer, EObject referenced,
			EReference reference) {

		Map featureMap = getFeatureMap(referenced);

		if (featureMap == null)
			return;

		Object value = featureMap.get(reference);

		if (value instanceof List) {

			// value is a list.
			List referencerList = (List) value;

			boolean empty = true;

			for (int i = 0, count = referencerList.size(); i < count; i++) {

				WeakReference r = (WeakReference) referencerList.get(i);

				if (r != null) {

					Object current = r.get();

					if (current == referencer)
						referencerList.set(i, null);

					else if (current == null)
						referencerList.set(i, null);

					else if (empty)
						empty = false;
				}
			}

			if (empty)
				featureMap.remove(reference);

		} else if (value instanceof WeakReference) {

			// value is a weak reference.
			WeakReference r = (WeakReference) value;

			if (r.get() == referencer)
				featureMap.remove(reference);

		} else
			return;

		if (featureMap.isEmpty())
			removeFeatureMap(referenced);
	}

	/**
	 * Registers references by traversing the referencer object. Referencer is a
	 * non proxy object.
	 */
	public void registerReferences(EObject container, EObject eObject) {

		if (eObject.eIsProxy())
			return;

		EList features = eObject.eClass().getEAllReferences();

		Iterator i = features.iterator();

		while (i.hasNext()) {

			EReference reference = (EReference) i.next();

			if ((reference.isChangeable()) && (!reference.isContainer())
				&& (eObject.eIsSet(reference))) {

				if (reference.isMany()) {

					if (reference.isContainment()) {

						EList objects = (EList) eObject.eGet(reference);

						for (Iterator j = objects.iterator(); j.hasNext();) {

							EObject contained = (EObject) j.next();

							if (contained != null)
								registerReferences(container, contained);
						}

					} else {

						EList objects = (EList) eObject.eGet(reference, false);

						// ensure references are not resolved.
						boolean resolve = true;

						for (Iterator j = ((InternalEList) objects)
							.basicIterator(); j.hasNext();) {

							EObject referenced = (EObject) j.next();

							if (referenced != null) {

								if (!registerReference(eObject, referenced,
									reference))
									resolve = false;
							}
						}

						// if all references were resolveable resolve the whole
						// list.
						if (resolve) {

							for (Iterator k = objects.iterator(); k.hasNext();)
								k.next();
						}
					}

				} else {

					if (reference.isContainment()) {

						EObject contained = (EObject) eObject.eGet(reference);

						if (contained != null)
							registerReferences(container, contained);

					} else {

						// ensure reference is not resolved.
						EObject referenced = (EObject) eObject.eGet(reference,
							false);

						if (referenced != null) {

							boolean resolve = registerReference(eObject,
								referenced, reference);

							// if reference was resolveable resolve it.
							if (resolve)
								eObject.eGet(reference);
						}
					}
				}
			}
		}

		// register referencer resources.
		final Resource containerResource = container.eResource();

		MSLReferenceVisitor visitor = new MSLReferenceVisitor(domain, eObject,
			false) {

			protected void visitedReferencer(EReference reference,
					EObject referencer) {

				Resource referencerResource = referencer.eResource();

				domain.getResourceIndexer().registerReference(
					referencerResource, containerResource);
			}
		};

		visitor.visitReferencers();
	}

	/**
	 * Deregisters references by traversing the referencer object.
	 */
	public void deregisterReferences(EObject container, EObject eObject) {
		deregisterReferences(container, eObject, eObject);
	}

	/**
	 * Deregisters references by traversing the referencer object.
	 */
	public void deregisterReferences(EObject container, final EObject detached,
			EObject eObject) {

		if (eObject.eIsProxy())
			return;

		final Resource containerResource = container.eResource();

		EList features = eObject.eClass().getEAllReferences();

		Iterator i = features.iterator();

		while (i.hasNext()) {

			EReference reference = (EReference) i.next();

			if ((reference.isChangeable()) && (!reference.isContainer())
				&& (eObject.eIsSet(reference))) {

				if (reference.isMany()) {

					if (reference.isContainment()) {

						EList objects = (EList) eObject.eGet(reference);

						for (Iterator j = objects.iterator(); j.hasNext();) {

							EObject contained = (EObject) j.next();

							if (contained != null)
								deregisterReferences(container, detached,
									contained);
						}

					} else {

						EList objects = (EList) eObject.eGet(reference, false);

						// ensure references are not resolved.
						for (Iterator j = ((InternalEList) objects)
							.basicIterator(); j.hasNext();) {

							EObject referenced = (EObject) j.next();

							if ((referenced != null)
								&& (!EObjectUtil.contains(detached, referenced))) {

								Resource referencedResource = MSLUtil
									.getResource(domain, referenced);

								domain.getResourceIndexer()
									.deregisterReference(containerResource,
										referencedResource);
							}
						}
					}

				} else {

					if (reference.isContainment()) {

						EObject contained = (EObject) eObject.eGet(reference);

						if (contained != null)
							deregisterReferences(container, detached, contained);

					} else {

						// ensure reference is not resolved.
						EObject referenced = (EObject) eObject.eGet(reference,
							false);

						if ((referenced != null)
							&& (!EObjectUtil.contains(detached, referenced))) {

							Resource referencedResource = MSLUtil.getResource(
								domain, referenced);

							domain.getResourceIndexer().deregisterReference(
								containerResource, referencedResource);
						}
					}
				}
			}
		}

		// deregister referencer resources.
		MSLReferenceVisitor visitor = new MSLReferenceVisitor(domain, eObject) {

			protected void visitedReferencer(EReference reference,
					EObject referencer) {

				if (!EObjectUtil.contains(detached, referencer)) {

					Resource referencerResource = referencer.eResource();

					domain.getResourceIndexer().deregisterReference(
						referencerResource, containerResource);
				}
			}
		};

		visitor.visitReferencers();
	}

	/**
	 * Resolves references by traversing the referencer object. Referencer is a
	 * non proxy object.
	 */
	public void resolveReferences(EObject container, EObject eObject) {

		if (eObject.eIsProxy())
			return;

		EList features = eObject.eClass().getEAllReferences();

		Iterator i = features.iterator();

		while (i.hasNext()) {

			EReference reference = (EReference) i.next();

			if ((reference.isChangeable()) && (!reference.isContainer())
				&& (eObject.eIsSet(reference))) {

				if (reference.isMany()) {

					if (reference.isContainment()) {

						EList objects = (EList) eObject.eGet(reference);

						for (Iterator j = objects.iterator(); j.hasNext();) {

							EObject contained = (EObject) j.next();

							if (contained != null)
								resolveReferences(container, contained);
						}

					} else {

						EList objects = (EList) eObject.eGet(reference, false);

						// ensure references are not resolved.
						boolean resolve = true;

						for (Iterator j = ((InternalEList) objects)
							.basicIterator(); j.hasNext();) {

							EObject referenced = (EObject) j.next();

							if (referenced != null) {

								if (!resolveReference(eObject, referenced,
									reference))
									resolve = false;
							}
						}

						// if all references were resolveable resolve the whole
						// list.
						if (resolve) {

							for (Iterator k = objects.iterator(); k.hasNext();)
								k.next();
						}
					}

				} else {

					if (reference.isContainment()) {

						EObject contained = (EObject) eObject.eGet(reference);

						if (contained != null)
							resolveReferences(container, contained);

					} else {

						// ensure reference is not resolved.
						EObject referenced = (EObject) eObject.eGet(reference,
							false);

						if (referenced != null) {

							boolean resolve = resolveReference(eObject,
								referenced, reference);

							// if reference was resolveable resolve it.
							if (resolve)
								eObject.eGet(reference);
						}
					}
				}
			}
		}
	}

	/**
	 * Resolves a reference.
	 */
	public boolean resolveReference(EObject referencer, EObject referenced,
			EReference reference) {

		// ignore non changeable and container/containment features.
		if ((!reference.isChangeable()) || (reference.isContainer())
			|| (reference.isContainment()))
			return false;

		if (referencer.eIsProxy())
			return false;

		if (referenced.eIsProxy()) {

			// register resource reference.
			Resource referencerResource = referencer.eResource();

			Resource referencedResource = MSLUtil.getResource(domain,
				referenced);

			URI uri = EcoreUtil.getURI(referenced);

			Resource.Factory factory = Resource.Factory.Registry.INSTANCE
				.getFactory(uri);

			if (!(factory instanceof MResourceFactory)) {

				if ((referencerResource == referencedResource)
					|| ((referencedResource != null) && (referencedResource
						.isLoaded())))
					referenced = EcoreUtil.resolve(referenced, domain
						.getResourceSet());
			}

			if (referenced.eIsProxy())
				return false;
			else
				doRegisterReference(referencer, referenced, reference);
		}

		return true;
	}

	/**
	 * Retrieves reverse references.
	 */
	public Map getGroupedReferencers(EObject referenced) {
		return getGroupedReferencers(referenced, true);
	}

	/**
	 * Retrieves reverse references.
	 */
	public Map getGroupedReferencers(EObject referenced, boolean resolve) {

		Map map = getFeatureMap(referenced);

		Map newMap = null;

		List features = referenced.eClass().getEAllReferences();

		for (Iterator i = features.iterator(); i.hasNext();) {

			EReference reference = (EReference) i.next();

			EReference opposite = reference.getEOpposite();

			if (opposite != null) {

				Set referencers = getReferencers(referenced, opposite, resolve);

				if (!referencers.isEmpty()) {

					if (newMap == null)
						newMap = (map == null) ? (new HashMap())
							: (new HashMap(map));

					List newReferencers = new ArrayList(referencers.size());

					for (Iterator j = referencers.iterator(); j.hasNext();)
						newReferencers.add(new WeakReference(j.next()));

					newMap.put(opposite, newReferencers);
				}
			}
		}

		if (newMap != null)
			return Collections.unmodifiableMap(newMap);

		else if (map != null)
			return Collections.unmodifiableMap(map);

		else
			return Collections.EMPTY_MAP;
	}

	/**
	 * Retrieves reverse references.
	 */
	public Set getAllReferencers(EObject referenced) {
		return getAllReferencers(referenced, true);
	}

	/**
	 * Retrieves reverse references.
	 */
	public Set getAllReferencers(EObject referenced, boolean resolve) {

		if (referenced.eIsProxy())
			return Collections.EMPTY_SET;

		final Set set = new HashSet();

		MSLReferenceVisitor visitor = new MSLReferenceVisitor(domain,
			referenced, resolve) {

			protected void visitedReferencer(EReference reference,
					EObject referencer) {

				if (!referencer.eIsProxy())
					set.add(referencer);
			}
		};

		visitor.visitReferencers();

		return Collections.unmodifiableSet(set);
	}

	/**
	 * Retrieves reverse references.
	 */
	public Set getReferencers(EObject referenced, EReference reference) {
		return getReferencers(referenced, reference, true);
	}

	/**
	 * Retrieves reverse references.
	 */
	public Set getReferencers(EObject referenced, EReference reference,
			boolean resolve) {

		if (referenced.eIsProxy())
			return Collections.EMPTY_SET;

		if ((reference.isChangeable()) && (!reference.isContainer())
			&& (!reference.isContainment())) {

			EReference opposite = reference.getEOpposite();

			if (opposite == null) {

				Map featureMap = getFeatureMap(referenced);

				if (featureMap != null) {

					Object value = featureMap.get(reference);

					if (value instanceof List) {

						// value is a list.
						List referencerList = (List) value;

						Set set = new HashSet();

						for (int i = 0, count = referencerList.size(); i < count; i++) {

							WeakReference r = (WeakReference) referencerList
								.get(i);

							if (r != null) {

								EObject referencer = (EObject) r.get();

								if ((referencer != null)
									&& (!referencer.eIsProxy()))
									set.add(referencer);
							}
						}

						return Collections.unmodifiableSet(set);

					} else if (value instanceof WeakReference) {

						// value is a weak reference.
						WeakReference r = (WeakReference) value;

						EObject referencer = (EObject) r.get();

						if (referencer != null)
							return Collections.singleton(referencer);
					}
				}

			} else {

				if ((opposite.isChangeable()) && (!opposite.isContainer())
					&& (!opposite.isContainment())
					&& (referenced.eIsSet(opposite))) {

					if (opposite.isMany()) {

						List referencers = (List) referenced.eGet(opposite,
							resolve);

						if (!referencers.isEmpty()) {

							// try not to resolve if asked to.
							if (resolve)
								return Collections.unmodifiableSet(new HashSet(
									referencers));
							else {

								Set nonProxyReferencers = new HashSet();

								for (Iterator j = ((InternalEList) referencers)
									.basicIterator(); j.hasNext();) {

									EObject referencer = (EObject) j.next();

									if ((referencer != null)
										&& (!referencer.eIsProxy()))
										nonProxyReferencers.add(referencer);
								}

								return Collections
									.unmodifiableSet(nonProxyReferencers);
							}
						}

					} else {

						// try not to resolve if asked to.
						EObject referencer = (EObject) referenced.eGet(
							opposite, resolve);

						if ((referencer != null) && (!referencer.eIsProxy()))
							return Collections.singleton(referencer);
					}
				}
			}
		}

		return Collections.EMPTY_SET;
	}

	/**
	 * Gets feature map of a given object.
	 */
	private Map getFeatureMap(EObject referenced) {

		for (int i = 0, count = referenced.eAdapters().size(); i < count; i++) {

			Adapter adapter = (Adapter) referenced.eAdapters().get(i);

			if (adapter instanceof MSLReferenceAdapter)
				return (Map) adapter;
		}

		return null;
	}

	/**
	 * Creates feature map of a given object.
	 */
	private Map createFeatureMap(EObject referenced) {

		Map map = new MSLReferenceAdapter();

		referenced.eAdapters().add(map);

		return map;
	}

	/**
	 * Removes feature map of a given object.
	 */
	private Map removeFeatureMap(EObject referenced) {

		for (int i = 0, count = referenced.eAdapters().size(); i < count; i++) {

			Adapter adapter = (Adapter) referenced.eAdapters().get(i);

			if (adapter instanceof MSLReferenceAdapter) {

				referenced.eAdapters().remove(i);

				return (Map) adapter;
			}
		}

		return null;
	}
}

