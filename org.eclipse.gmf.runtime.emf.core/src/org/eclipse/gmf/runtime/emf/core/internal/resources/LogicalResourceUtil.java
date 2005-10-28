/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.emf.core.internal.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.emf.core.EventTypes;
import org.eclipse.gmf.runtime.emf.core.internal.l10n.EMFCoreMessages;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLPlugin;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLStatusCodes;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ChildEntry;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMap;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapFactory;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage;
import org.eclipse.gmf.runtime.emf.core.internal.util.Trace;
import org.eclipse.gmf.runtime.emf.core.resources.CannotSeparateException;
import org.eclipse.gmf.runtime.emf.core.resources.ILogicalResource;
import org.eclipse.gmf.runtime.emf.core.util.ResourceUtil;

/**
 * <p>
 * Utilities for working with "logical resources."  Logical resources can
 * comprise multiple separate resources, primarily for the purpose of
 * decomposing files in source control.  Individual physical resources in a
 * logical resource can be separately version-controlled to assist a smooth
 * team development workflow.
 * </p><p>
 * When a logical resource is loaded, it automatically loads its constituent
 * physical resources and combines them into a single in-memory representation.
 * All of the objects in this logical resource are addressed by a single URI.
 * </p><p>
 * Particular EMF metamodels (<code>EPackage</code>s) can restrict which
 * elements are permitted to form the roots of separate resources.  The
 * {@link #canSeparate(EObject)} method is used to determine whether an element
 * is permitted to root a separate resource.
 * </p><p>
 * Given an element that may be separated, the {@link #separate(EObject)} method
 * will create a separate resource for it if none is defined already.  A
 * separate element can be re-incorporated into the resource of its container
 * by the {@link #absorb(EObject)} method.  To determine whether an element
 * currently is separated, use the {@link #isSeparate(EObject)} method.
 * </p>
 * 
 * @author Christian W. Damus (cdamus)
 */
public class LogicalResourceUtil {
	private LogicalResourceUtil() {
		super();
	}
	
	/**
	 * Queries whether the specified <code>eObject</code> is the root of a
	 * separate resource in a larger logical resource.
	 * 
	 * @param res the logical resource
	 * @param eObject a model element in the logical resource
	 * 
	 * @return whether it is the root of a separate resource
	 */
	static boolean isSeparate(LogicalResource res, EObject eObject) {
		assert eObject.eResource() == res : "eObject is not in res" ; //$NON-NLS-1$
		
		LogicalResourceUnit unit = (LogicalResourceUnit) res.getResourceMap().get(eObject);
		
		return (unit != null) && (unit != res.getRootUnit());
	}
	
	/**
	 * Ensures that the specified <code>eObject</code> is the root of a
	 * separate resource, if it is {@linkplain #canSeparate(EObject) allowed}
	 * to be such.
	 * 
	 * @param res the logical resource
	 * @param eObject a model element
	 * @param uri URI for the physical resource to store the
	 *    <code>eObject</code> in, or <code>null</code> for the default
	 * 
	 * @throws CannotSeparateException if something unforeseen goes wrong in
	 *     the separation of the element.  This may be failure to make a
	 *     version-controlled file writeable, failure to meet metamodel-specific
	 *     constraints on separation, etc.
	 * 
	 * @see #canSeparate(LogicalResource, EObject)
	 * @see #absorb(LogicalResource, EObject)
	 */
	static void separate(LogicalResource res, EObject eObject, URI uri) throws CannotSeparateException {
		LogicalResourceUnit oldUnit = getUnitFor(res, eObject);
		LogicalResourceUnit newUnit = initializeUnit(res, eObject, uri);
		
		if (newUnit == oldUnit) {
			// not allowed to separate into current storage unit
			CannotSeparateException e = new CannotSeparateException(
				EMFCoreMessages.separate_sameUnit_ERROR_);
			Trace.throwing(LogicalResourceUtil.class, "separate", e); //$NON-NLS-1$
			throw e;
		}
		
		if (eObject.eContainer() == null) {
			// this was a root of the root unit
			oldUnit.getContents().remove(eObject);
			
			// add back to the map because we just removed it
			res.addMappedResource(eObject, oldUnit);
		}
		newUnit.getContents().add(eObject);
		
		// ensure that both units are dirty
		oldUnit.setDirty();
		newUnit.setDirty();
		
		reparentChildUnits(oldUnit, newUnit, eObject);
	}
	
	/**
	 * Ensures that any separate elements in the containment tree of a
	 * recently-separated or -absorbed element that reference the element's old
	 * unit as their parent now reference the new sub-unit.
	 * 
	 * @param oldUnit a separated or absorbed <code>element</code>'s old unit
	 * @param newUnit a separated or absorbed <code>element</code>'s new unit
	 * @param element the recently-separated/absorbed element
	 */
	private static void reparentChildUnits(
			LogicalResourceUnit oldUnit, LogicalResourceUnit newUnit,
			EObject element) {
		
		// must make a defensive copy to protect against concurrent
		//   modifications in the absorption case
		List parentEntries = new java.util.ArrayList(
			oldUnit.getResourceIndex().getParentEntries());
		
		for (Iterator iter = parentEntries.iterator(); iter.hasNext();) {
			ParentEntry next = (ParentEntry) iter.next();
			EObject parent = next.getParentObject();
			
			if (EcoreUtil.isAncestor(element, parent)) {
				// this parent is in another unit, now
				newUnit.getResourceIndex().getParentEntries().add(next);
				
				// mark the child resources dirty so that they will save
				for (Iterator jter = next.getResourceEntries().iterator(); jter.hasNext();) {
					ResourceEntry re = (ResourceEntry) jter.next();
					
					LogicalResourceUnit child = (LogicalResourceUnit) re.getResource();
					
					// must load it in order to save it, unless we are in
					//    physical mode
					if (child.isLoaded() || !isPhysicalMode(child.getLogicalResource())) {
						// proxy resolution loads the child resource
						EObject childObj = re.getChildObject();
						
						// point the child unit to its new parent unit
						ChildEntry ce = child.getResourceIndex().getChildEntry(childObj);
						ce.setParentMap(newUnit.getResourceIndex());
						child.setDirty();
					}
				}
			}
		}
	}
	
	/**
	 * Queries whether the specified logical resource was loaded in physical
	 * mode.
	 * 
	 * @param res the logical resource
	 * @return <code>true</code> if it was loaded in physical mode;
	 *     <code>false</code>, otherwise
	 */
	static boolean isPhysicalMode(LogicalResource res) {
		return !Boolean.TRUE.equals(
			res.getSubunitResourceSet().getLoadOptions().get(
				ILogicalResource.OPTION_LOAD_AS_LOGICAL));
	}
	
	/**
	 * Queries whether the specified <code>eObject</code> is currently loaded.
	 * If it is not loaded, then it is just a stand-in for the absent sub-tree
	 * of the logical model.
	 * 
	 * @param res a logical resource
	 * @param eObject an element in the logical resource.  It may be a separate
	 *      element (in which case the question of loaded is interesting) or
	 *      it may not.  In the latter case, the element is necessarily
	 *      to be considered as loaded
	 * @return <code>true</code> if the <code>eObject</code> is fully loaded
	 *      from its physical resource; <code>false</code>, otherwise
	 * 
	 * @throws IllegalArgumentException if the <code>eObject</code> is not
	 *     contained within me
	 *     
	 * @see #load(LogicalResource, EObject)
	 */
	static boolean isLoaded(LogicalResource res, EObject eObject) {
		assert eObject.eResource() == res : "eObject is not in res" ; //$NON-NLS-1$
		
		boolean result;
		
		if (!isSeparate(res, eObject)) {
			result = true;
		} else {
			LogicalResourceUnit unit = getUnitFor(res, eObject);
			result = unit.isLoaded() && unit.getContents().contains(eObject);
		}
		
		return result;
	}
	
	/**
	 * Loads the specified eObject from its physical resource.
	 * 
	 * @param res the logical resource
	 * @param eObject a separate element in the logical resource
	 * 
	 * @throws IllegalArgumentException if the <code>eObject</code> is not
	 *     separate or if it is not contained within me
	 * @throws IOException if any exception occurs while loading the
	 *     <code>eObject</code>'s physical resource
	 *     
	 * @see #isLoaded(LogicalResource, EObject)
	 */
	static void load(LogicalResource res, EObject eObject) throws IOException {
		LogicalResourceUnit unit = getUnitFor(res, eObject);
		
		if (!unit.isLoaded()) {
			loadUnit(unit);
		}
	}
	
	/**
	 * Convenience method to load a sub-unit into the logical resource with
	 * the correct load options.
	 * 
	 * @param unit a sub-unit to be loaded
	 * @throws IOException if anything goes wrong in loading it
	 * 
	 * @see #loadUnit(LogicalResourceUnit, InputStream)
	 */
	static void loadUnit(LogicalResourceUnit unit) throws IOException {
		unit.load(unit.getResourceSet().getLoadOptions());
	}
	
	/**
	 * Convenience method to load a sub-unit into the logical resource with
	 * the correct load options.
	 * 
	 * @param unit a sub-unit to be loaded
	 * @param inputStream the input stream to load the unit from
	 * @throws IOException if anything goes wrong in loading it
	 */
	static void loadUnit(LogicalResourceUnit unit, InputStream inputStream) throws IOException {
		unit.load(inputStream, unit.getResourceSet().getLoadOptions());
	}
	
	/**
	 * Gets a mapping of objects as yet unloaded in the logical resource
	 * that resolve to roots of the specified <code>unit</code>.
	 * 
	 * @param unit a unit in a logical resource
	 * @return map of {@link String} ID ==&gt; unloaded {@link EObject}
	 */
	static Map getUnloadedElements(LogicalResourceUnit unit) {
		LogicalResource logical = unit.getLogicalResource();
		Map result = new java.util.HashMap();
		
		for (Iterator iter = logical.getResourceMap().entrySet().iterator();
				iter.hasNext();) {
			
			Map.Entry entry = (Map.Entry) iter.next();
			if (entry.getValue() == unit) {
				// this is an element whose ID we need
				EObject element = (EObject) entry.getKey();
				
				result.put(logical.getID(element), element);
			}
		}
		
		return result;
	}
	
	/**
	 * Reconciles existing unloaded units from the specified map with the roots
	 * of the just-loaded <code>unit</code>.  This merges the features of the
	 * <code>unit</code>'s roots into existing unloaded elements; this
	 * effectively loads these elements.  The original unloaded elements (now
	 * loaded) then replace the <code>unit</code>'s existing roots.
	 * <p>
	 * The ID mapping provided maps IDs of the roots of the <code>unit</code> to
	 * the IDs of the unloaded units that match them, to preserve their IDs
	 * (because loading the <code>unit</code> replaced the existing IDs in the
	 * XML resource's ID-to-element map).
	 * </p>
	 * 
	 * @param unit a logical resource unit that was just loaded
	 * @param idMap mapping of 
	 */
	static void reconcileUnloadedUnits(LogicalResourceUnit unit, Map idMap) {
		LogicalResource logical = unit.getLogicalResource();
		
		EObject[] roots = (EObject[]) unit.getContents().toArray(
			new EObject[unit.getContents().size()]);  // safe copy
		
		for (int i = 0; i < roots.length; i++) {
			// the logical resource now knows the actual sub-unit roots under
			//   the IDs previously retrieved
			String id = logical.getID(roots[i]);
			EObject oldObject = (EObject) idMap.get(id);
			
			// only do replacement if they are not the same object
			if ((oldObject != null) && (oldObject != roots[i])) {
				become(oldObject, roots[i], id, unit);
				fireLoadEvent(oldObject);
			}
		}	
	}

	/**
	 * Auto-loads the specified element of the specified resource, if it is
	 * an unloaded separate element.
	 * 
	 * @param res a resource, which may or may not be a logical resource
	 * @param eObject an element of the resource, which may or may not be
	 *     an unloaded separate element
	 */
	public static void autoload(Resource res, EObject eObject) {
		if (res instanceof ILogicalResource) {
			ILogicalResource logical = (ILogicalResource) res;
			Resource unit = AbstractResourceWrapper.unwrap(
				getPhysicalResource(logical, eObject.eContainer()));
			
			if (isAutoLoadable(unit) && !logical.isLoaded(eObject)) {
				try {
					if (Trace.isEnabled(MSLDebugOptions.RESOURCES)) {
						URI base = logical.getURI();
						URI relative = unit.getURI().deresolve(base);
						
						Trace.trace(
							MSLDebugOptions.RESOURCES,
							"Auto-loading unit: " + relative + " of: " + base); //$NON-NLS-1$ //$NON-NLS-2$
					}
					
					logical.load(eObject);
				} catch (IOException e) {
					Trace.catching(
						LogicalResourceUtil.class,
						"autoload(Resource, EObject)", e); //$NON-NLS-1$
					
					Log.error(
						MSLPlugin.getDefault(),
						MSLStatusCodes.LOGICAL_AUTOLOAD_FAILED,					
						EMFCoreMessages.autoload_failed_EXC_,
						e);
				}
			}
		}
	}
	
	/**
	 * Queries whether the specified <code>unit</code> can be auto-loaded.
	 * Instances of {@link LogicalResourceUnit} have configurable auto-loading
	 * support; other implementations are implicitly auto-loadable.
	 * 
	 * @param unit a unit
	 * @return <code>true</code> if it can be auto-loaded;
	 *     <code>false</code>, otherwise
	 */
	private static boolean isAutoLoadable(Resource unit) {
		boolean result = true;
		
		if (unit instanceof LogicalResourceUnit) {
			result = ((LogicalResourceUnit) unit).isAutoLoadEnabled();
		}
		
		return result;
	}
	
	/**
	 * Quietly loads the specified newly loaded object's features into the
	 * old object, and replaces the loaded object with the old one in the
	 * <code>subunit</code>.
	 * 
	 * @param oldObj the old object that we are loading
	 * @param newObj the newly loaded object that the <code>oldObj</code>
	 *      is to become
	 * @param id the ID object of the object that was loaded
	 * @param subunit the resource from which <code>newObj</code> was loaded
	 */
	private static void become(EObject oldObj, EObject newObj, String id,
			LogicalResourceUnit subunit) {
		// do this quietly
		oldObj.eSetDeliver(false);
		
		boolean wasClean = !subunit.isDirty();
		
		// transfer all features from the new object
		for (Iterator iter = newObj.eClass().getEAllStructuralFeatures().iterator();
				iter.hasNext();) {
			
			EStructuralFeature feature = (EStructuralFeature) iter.next();
			
			if (isTransferableFeature(feature)) {
				if (!feature.isMany()) {
					// simple case.  Just eSet without resolving the value
					oldObj.eSet(feature, newObj.eGet(feature, false));
				} else {
					// must transfer the EList contents without resolving anything
					//   or gathering any notifications.  Must copy the source
					//   list to avoid concurrent modification
					List oldValue = (List) oldObj.eGet(feature, false);
					List newValue = new java.util.ArrayList((List) newObj.eGet(feature, false));
					
					oldValue.clear();
					for (Iterator xfer = newValue.iterator(); xfer.hasNext();) {
						oldValue.add(xfer.next());
					}
				}
			}
		}
		
		// set the old object into the sub-unit resource.  This has the
		//    side-effect of updating the object-to-subunit map
		int pos = subunit.getContents().indexOf(newObj);
		subunit.getContents().set(pos, oldObj);
		
		// update the child entry to point to the right object.  If we can't
		//   find it, then it was already up-to-date
		ChildEntry childEntry = subunit.getResourceIndex().getChildEntry(newObj);
		if (childEntry != null) {
			childEntry.setChildObject(oldObj);
		}
		
		// the old object must re-assume the ID of the new one
		subunit.setID(oldObj, id);
		
		if (wasClean) {
			// the sub-unit should not be considered as dirty by this operation
			subunit.setClean();
		}
		
		oldObj.eSetDeliver(true);
	}
	
	/**
	 * Determines whether the specified feature is transferable from one object
	 * to another.  Transferable features are those that meet the following
	 * criteria:
	 * <ul>
	 *   <li>they are {@link EStructuralFeature#isChangeable() changeable}</li>
	 *   <li>they are not {@link EStructuralFeature#isDerived() derived}</li>
	 *   <li>they are not {@link EReference#isContainer() container} references</li>
	 * </ul>
	 * 
	 * @param feature a structural feature
	 * @return whether it is transferable, according to the criteria, above
	 */
	private static boolean isTransferableFeature(EStructuralFeature feature) {
		boolean result = feature.isChangeable() && !feature.isDerived();

		if (result && (feature instanceof EReference)) {
			result = !((EReference) feature).isContainer();
		}
		
		return result;
	}
	
	/**
	 * Loads all units of a logical resource that are not already loaded.
	 * 
	 * @param res a logical resource
	 * 
	 * @throws IOException if anything goes wrong in loading any unloaded
	 *     units
	 */
	public static void loadAllUnloadedUnits(ILogicalResource res) throws IOException {
		// must always iterate a separate collection to prevent concurrent mods
		Set newElements = new java.util.HashSet(
			res.getMappedResources().keySet());
		int lastCount = newElements.size();
		boolean foundNewElements = true;
		
		while (foundNewElements) {
			Set elements = newElements;
			
			for (Iterator iter = elements.iterator(); iter.hasNext();) {
				EObject next = (EObject) iter.next();
				
				if (!res.isLoaded(next)) {
					res.load(next);
				}
			}
			
			// found any new unloaded elements?
			int newCount = res.getMappedResources().size();
			foundNewElements = newCount > lastCount;
			
			if (foundNewElements) {
				// compute the new elements, and iterate again
				newElements = new java.util.HashSet(
					res.getMappedResources().keySet());
				newElements.removeAll(elements);
				lastCount = newCount;
			}
		}
	}

	/**
	 * Finds the unit within a logical resource that physically contains the
	 * specified element.
	 * 
	 * @param res a logical resource, or a unit in a logical resource
	 * @param eObject an element in the logical resource
	 * @return the unit of the logical resource that contains the element 
	 */
	static LogicalResourceUnit getUnitFor(LogicalResource res, EObject eObject) {
		LogicalResourceUnit result = null;
		
		if (eObject == null) {
			result = res.getRootUnit();
		} else {
			result = (LogicalResourceUnit) AbstractResourceWrapper.unwrap(
				getPhysicalResource(res, eObject));
		}
		
		return result;
	}

	/**
	 * A convenience method for retrieving the physical resource (as an
	 * unmodifiable view) that stores the specified element.
	 * 
	 * @param eObject an element in the logical resource.  It needs not be
	 *     a separate element (i.e., the root of a physical resource)
	 * @return an unmodifiable view of the resource that stores the element
	 */
	public static Resource getPhysicalResource(ILogicalResource res, EObject eObject) {
		Resource result = null;
		Map map = res.getMappedResources();
		
		while ((eObject != null) && (result == null)) {
			if (map.containsKey(eObject)) {
				result = (Resource) map.get(eObject);
			}
			
			eObject = eObject.eContainer();
		}
		
		return result;
	}
	
	/**
	 * Ensures that the specified <code>eObject</code> is not the root of a
	 * separate resource.
	 * 
	 * @param res the logical resource
	 * @param eObject a model element
	 * 
	 * @see #separate(LogicalResource, EObject, URI)
	 */
	static void absorb(LogicalResource res, EObject eObject) {
		LogicalResourceUnit oldUnit = getUnitFor(res, eObject);
		
		// dirty the former unit
		oldUnit.getContents().remove(eObject);
		oldUnit.setDirty();
		
		// must also dirty the new unit
		LogicalResourceUnit newUnit = getUnitFor(res, eObject.eContainer());
		if (eObject.eContainer() == null) {
			// this is now a root of the root unit
			newUnit.getContents().add(eObject);
		}
		newUnit.setDirty();
		
		reparentChildUnits(oldUnit, newUnit, eObject);
	}
	
	/**
	 * Removes the specified element's child entry from the old unit's resource
	 * index.
	 * 
	 * @param oldUnit the element's old unit
	 * @param eObject the element
	 */
	static void removeChildEntry(LogicalResourceUnit oldUnit, EObject eObject) {
		ResourceMap index = oldUnit.getResourceIndex();
		
		if (index != null) {
			ChildEntry childEntry = index.getChildEntry(eObject);
			
			if (childEntry != null) {
				index.getChildEntries().remove(childEntry);
				
				ResourceMap parentIndex = childEntry.getParentMap();
				if (parentIndex != null) {
					ResourceEntry resourceEntry =
						parentIndex.getResourceEntry(eObject);
					
					if (resourceEntry != null) {
						ParentEntry parentEntry = resourceEntry.getParentEntry();
						
						parentEntry.getResourceEntries().remove(
							resourceEntry);
						
						if (parentEntry.getResourceEntries().isEmpty()) {
							// don't need this parent entry any more if it
							//    has no sub-units remaining
							parentEntry.getResourceMap().getParentEntries().remove(
								parentEntry);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Creates a resource entry for the sub-unit storing the specified
	 * <code>eObject</code> in the parent unit's resource <code>index</code>.
	 * This has the side-effect of creating the corresponding child entry
	 * in the sub-unit's own resource index.
	 * 
	 * @param eObject the sub-unit element
	 * @param parentUnit the <code>eObject</code>'s old physical unit
	 * @param subunit the <code>eObject</code>'s new physical unit
	 */
	static void createSubunitEntry(
			EObject eObject,
			LogicalResourceUnit parentUnit,
			LogicalResourceUnit subunit) {
		
		ResourceMap parentIndex = parentUnit.getResourceIndex();
		
		EObject parent = eObject.eContainer();
		EReference containment = eObject.eContainmentFeature();
		
		ParentEntry parentEntry = createParentEntry(parentIndex, parent, containment);
		
		ResourceEntry entry = parentEntry.getResourceEntry(eObject);
		
		if (entry == null) {
			entry = ResourceMapFactory.eINSTANCE.createResourceEntry();
			parentEntry.getResourceEntries().add(entry);
			entry.setChildObject(eObject);
		}
		
		updateChildPosition(subunit, entry);
		
		ResourceMap childIndex = subunit.getResourceIndex();
		if (childIndex != null) {
			// really shouldn't be null!
			ChildEntry childEntry = childIndex.getChildEntry(eObject);
			
			if (childEntry == null) {
				childEntry = ResourceMapFactory.eINSTANCE.createChildEntry();
				childIndex.getChildEntries().add(childEntry);
				childEntry.setChildObject(eObject);
			}
			
			childEntry.setParentMap(parentIndex);
		}
	}
	
	/**
	 * Creates the parent entry in a resource <code>index</code> for the
	 * specified container and reference.  If the <code>parent</code> and
	 * <code>containment</code> reference are both <code>null</code>, then
	 * we are creating an entry for the resource, itself (to separate a root).
	 * 
	 * @param index the resource index to create the entry in
	 * @param parent the container element (may be <code>null</code>)
	 * @param containment the containment reference (may be <code>null</code>)
	 * 
	 * @return the new parent entry, added to the <code>index</code>
	 */
	private static ParentEntry createParentEntry(ResourceMap index, EObject parent, EReference containment) {
		ParentEntry result = index.getParentEntry(parent, containment);
		
		if (result == null) {
			result = ResourceMapFactory.eINSTANCE.createParentEntry();
			result.setParentObject(parent);
			result.setChildSlot(containment);
			index.getParentEntries().add(result);
		}
		
		return result;
	}
	
	/**
	 * Initializes a sub-unit resource to prepare it for adding a new root
	 * element to it.
	 * 
	 * @param res the logical resource
	 * @param eObject the physical resource's new root
	 * @param uri the the physical resource's URI
	 * 
	 * @return the physical resource, ready to receive its new root
	 */
	private static LogicalResourceUnit initializeUnit(LogicalResource res,
			EObject eObject, URI uri) {
		
		ResourceSet rset = res.getSubunitResourceSet();
		LogicalResourceUnit result = (LogicalResourceUnit) rset.getResource(uri, false); 
		if (result == null) {
			result = (LogicalResourceUnit) rset.createResource(uri);
		}
		
		if (!result.isLoaded()) {
			IFile file = ResourceUtil.getFile(result);
			
			if ((file != null) && file.exists()) {
				try {
					loadUnit(result);
				} catch (IOException e) {
					Trace.catching(
						LogicalResourceUtil.class,
						"initResource(LogicalResource, EObject, URI)", e); //$NON-NLS-1$
					
					Log.warning(
						MSLPlugin.getDefault(),
						MSLStatusCodes.LOGICAL_LOAD_FAILED,				
						EMFCoreMessages.load_failed_EXC_,
						e);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Set the child position of the specified resource <code>entry</code>
	 * from its index in the containment feature (if it is a list).
	 * 
	 * @param subunit the sub-unit that the <code>entry</code> references
	 * @param entry the entry whose child position is to be set
	 */
	static void updateChildPosition(LogicalResourceUnit subunit, ResourceEntry entry) {
		EObject child = entry.getChildObject();
		EReference containment = entry.getParentEntry().getChildSlot();
		
		// ensure that the resource is set
		entry.setResource(subunit);
		
		if (containment == null) {
			Resource res = subunit.getLogicalResource();
			entry.setChildPosition(res.getContents().indexOf(child));
		} else if (containment.isMany()) {
			EList values = (EList) child.eContainer().eGet(containment);
			entry.setChildPosition(values.indexOf(child));
		} else {
			entry.eUnset(ResourceMapPackage.eINSTANCE.getResourceEntry_ChildPosition());
		}
	}
	
	/**
	 * Sets the child positions of the resources in a parent <code>entry</code>
	 * from their indices in the containment feature (if it is a list).  This is
	 * useful when all existing entries need to be recomputed because of the
	 * addition or removal of an element from the list
	 * 
	 * @param unit the unit that owns the parent <code>entry</code>
	 * @param entry the parent entry whose child positions are to be set
	 */
	static void updateChildPositions(LogicalResourceUnit unit, ParentEntry entry) {
		EObject parent = entry.getParentObject();
		EReference containment = entry.getChildSlot();
		
		for (Iterator iter = entry.getResourceEntries().iterator(); iter.hasNext();) {
			ResourceEntry resEntry = (ResourceEntry) iter.next();
			EObject child = resEntry.getChildObject();
			
			if (containment == null) {
				Resource res = unit.getLogicalResource();
				resEntry.setChildPosition(res.getContents().indexOf(child));
			} else if (containment.isMany()) {
				EList values = (EList) parent.eGet(containment);
				resEntry.setChildPosition(values.indexOf(child));
			} else {
				resEntry.eUnset(
					ResourceMapPackage.eINSTANCE.getResourceEntry_ChildPosition());
			}
		}
	}
	
	/**
	 * Computes the index of a <code>child</code> object in its
	 * <code>parent</code>'s <code>containment</code> reference.
	 * 
	 * @param child an element
	 * @param parent its container
	 * @param containment the containment feature of the <code>child</code>
	 * 
	 * @return the index, or -1 if the <code>containment</code> reference is not
	 *     a collection
	 */
	private static int indexOf(EObject child, EObject parent, EReference containment) {
		int result = -1;
		
		if (containment.isMany()) {
			// it's a containment reference, so no worries about proxy resolution
			EList value = (EList) parent.eGet(containment);
			result = value.indexOf(child);
		}
		
		return result;
	}
	
	/**
	 * Fires an artificial {@link EventTypes#SEPARATE} notification for the
	 * separation of the specified object.
	 * 
	 * @param eObject the newly separated object
	 * @param oldResource the object's old resource
	 * @param newResource its new resource
	 */
	public static void fireSeparationEvent(EObject eObject,
			Resource oldResource, Resource newResource) {
		Notification notification = createNotification(
			eObject,
			EventTypes.SEPARATE,
			oldResource,
			newResource);
		
		dispatch(notification);
	}
	
	/**
	 * Fires an artificial {@link EventTypes#ABSORB} notification for the
	 * absorption of the specified object.
	 * 
	 * @param eObject the newly separated object
	 * @param oldResource the object's old resource
	 * @param newResource its new resource
	 */
	public static void fireAbsorptionEvent(EObject eObject,
			Resource oldResource, Resource newResource) {
		Notification notification = createNotification(
			eObject,
			EventTypes.ABSORB,
			oldResource,
			newResource);
		
		dispatch(notification);
	}
	
	/**
	 * Fires an artificial {@link EventTypes#LOADED} notification for the
	 * loading of the specified object.
	 * 
	 * @param eObject the newly loaded object
	 */
	public static void fireLoadEvent(EObject eObject) {
		Notification notification = createNotification(
			eObject,
			EventTypes.LOAD,
			eObject,
			eObject);
		
		dispatch(notification);
	}
	
	/**
	 * Creates a notification of the specified type for the specified element,
	 * with old and new values.
	 * 
	 * @param eObject the element to create the notification for
	 * @param type the notification type
	 * @param oldValue the old value
	 * @param newValue the new value
	 * 
	 * @return the new notification
	 */
	private static Notification createNotification(
			EObject eObject, int type, Object oldValue, Object newValue) {
		
		EObject container = eObject.eContainer();
		EReference containment = eObject.eContainmentFeature();
		
		Notification result;
		int index;
		
		if (containment != null) {
			index = indexOf(eObject, container, containment);
			result = new NotificationImpl(
				(InternalEObject) container,
				type,
				containment,
				oldValue, newValue,
				index);
		} else {
			// it's a logical root
			Resource res = eObject.eResource();
			index = res.getContents().indexOf(eObject);
			result = new ResourceNotificationImpl(
				res,
				type,
				oldValue, newValue,
				index);
		}
		
		return result;
	}
	
	/**
	 * Dispatches the specified <code>notification</code> to adapters.
	 * 
	 * @param notification the notification to dispatch
	 */
	private static void dispatch(Notification notification) {
		Notifier notifier = (Notifier) notification.getNotifier();
		notifier.eNotify(notification);
	}
	
	/**
	 * Notification implementation for notification of changes to the physical
	 * structure of a logical resource.
	 *
	 * @author Christian W. Damus (cdamus)
	 */
	private static class NotificationImpl extends ENotificationImpl {
		NotificationImpl(InternalEObject notifier, int eventType,
			EStructuralFeature feature, Object oldValue, Object newValue,
			int index) {
			
			super(notifier, eventType, feature, oldValue, newValue, index);
		}
		
		public boolean isTouch() {
			// changes in the physical resource structure do not affect the
			//   resource's logical contents
			return true;
		}
	}
	
	private static class ResourceNotificationImpl
		extends org.eclipse.emf.common.notify.impl.NotificationImpl {

		private final Resource notifier;
		
		public ResourceNotificationImpl(Resource notifier,
				int eventType, Object oldValue, Object newValue, int position) {
			// consider the resource contents to be always set
			super(eventType, oldValue, newValue, position, true);
			this.notifier = notifier;
		}
		
		public Object getNotifier() {
			return notifier;
		}
		
		public int getFeatureID(Class expectedClass) {
			return Resource.RESOURCE__CONTENTS;
		}
		
		public boolean isTouch() {
			// changes in the physical resource structure do not affect the
			//   resource's logical contents
			return true;
		}
	}
}
