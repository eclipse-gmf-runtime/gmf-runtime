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
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.XMLSave;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ChildEntry;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMap;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapFactory;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLUtil;
import org.eclipse.gmf.runtime.emf.core.internal.util.Trace;
import org.eclipse.gmf.runtime.emf.core.resources.ILogicalResource;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;

/**
 * Implementation of a physical unit of a {@link LogicalResource}.  Units are
 * serialized as XMI.  It is recommended (though not required) that subclasses
 * of the logical resource provide companion unit classes extending this one.
 * See the <code>LogicalResource</code> documentation for details.
 * 
 * @see LogicalResource
 *
 * @author Christian W. Damus (cdamus)
 */
public class LogicalResourceUnit
	extends XMIResourceImpl {

	private final LogicalResource logical;
	private boolean dirty = false;
	
	private boolean autoLoadEnabled = true;
	
	// this list has no inverse references
	private EList roots = new ContentsList();
	
	private ResourceMap resourceIndex;
	
	/**
	 * Initializes me with as a child of the specified <code>logical</code>
	 * resource.
	 * 
	 * @param uri my URI
	 * @param logical my logical resource
	 */
	protected LogicalResourceUnit(URI uri, LogicalResource logical) {
		super(uri);
		
		this.logical = logical;
	}

	protected boolean useUUIDs() {
		return true;
	}
	
	/**
	 * Gets the logical resource that owns me.
	 * 
	 * @return my logical resource
	 */
	LogicalResource getLogicalResource() {
		return logical;
	}
	
	/**
	 * Queries whether I am the root unit.
	 * 
	 * @return whether I am the root unit
	 */
	boolean isRoot() {
		ResourceMap index = getResourceIndex();
		
		// don't resolve the proxy, because we want to avoid loading this
		//    resource if possible (in case of loading as physical)
		return (index == null) ||
			(index.eGet(ResourceMapPackage.eINSTANCE.getResourceMap_RootMap(), false) == null);
	}
	
	public void doLoad(InputStream inputStream, Map options)
		throws IOException {
		
		try {
			disableAutoLoad();
			Map idMap = LogicalResourceUtil.getUnloadedElements(this);
			
			super.doLoad(inputStream, options);
			
			// must reconcile my roots with any unloaded "proxies" in parent
			//   units before loading my sub-units, otherwise I will
			//     - try to splice sub-units into the wrong indices in empty
			//       collections in the unloaded elements
			//     - if the previous worked, I will lose the sub-units when
			//       replacing the proxy's collections with my own
			LogicalResourceUtil.reconcileUnloadedUnits(this, idMap);
			
			if (getResourceIndex() != null) {
				loadParentUnits(getResourceIndex(), options);
				loadSubunits(getResourceIndex(), options);
			}
		} finally {
			// We will finally propagate any errors or warnings to
			//  the parent logical resource so that it obeys the regular
			//  EMF resource contract by providing all errors and warnings
			//  that occurred during a load.
			getLogicalResource().getErrors().addAll(getErrors());
			getLogicalResource().getWarnings().addAll(getWarnings());
			setClean();
			enableAutoLoad();
		}
		
		if (Trace.isEnabled(MSLDebugOptions.RESOURCES)) {
			URI base = getLogicalResource().getURI();
			String relative = getURI().deresolve(base).toString();
			
			if (relative.length() == 0) {
				relative = "(root)"; //$NON-NLS-1$
			}
			
			Trace.trace(
				MSLDebugOptions.RESOURCES,
				"Loaded unit: " + relative + " of: " + base); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	public void doSave(OutputStream outputStream, Map options)
		throws IOException {
		
		try {
			if (isDirty() || (outputStream != null)) {
				// don't try to write if I don't need it, unless somebody has
				//   already created an output stream, in which case we would
				//   erase the contents of our file if we didn't proceed
				super.doSave(outputStream, options);
				
				if (Trace.isEnabled(MSLDebugOptions.RESOURCES)) {
					URI base = getLogicalResource().getURI();
					String relative = getURI().deresolve(base).toString();
					
					if (relative.length() == 0) {
						relative = "(root)"; //$NON-NLS-1$
					}
					
					Trace.trace(
						MSLDebugOptions.RESOURCES,
						"Saved unit: " + relative + " of: " + base); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		} finally {
			setClean();
		}
	}
	
	/**
	 * Queries whether auto-loading of sub-units is enabled.
	 * 
	 * @return <code>true</code> if auto-loading of sub-units is enabled;
	 *     <code>false</code>, otherwise
	 */
	boolean isAutoLoadEnabled() {
		return autoLoadEnabled && getLogicalResource().isAutoLoadEnabled();
	}
	
	/**
	 * Turns off auto-loading of unloaded sub-units.
	 */
	void disableAutoLoad() {
		autoLoadEnabled = false;
	}
	
	/**
	 * Turns on auto-loading of unloaded sub-units.  After the resource has
	 * finished loading (this distinguishes this flag from the
	 * <code>isLoaded</code> state).
	 */
	void enableAutoLoad() {
		autoLoadEnabled = true;
	}
	
	/**
	 * If we are loading as a logical resource, ensures that the parent units
	 * of my roots are loaded.
	 * 
	 * @param index my physical resource map
	 * @param options my load options
	 */
	private void loadParentUnits(ResourceMap index, Map options) {
		final boolean loadUnits = Boolean.TRUE.equals(
			options.get(ILogicalResource.OPTION_LOAD_AS_LOGICAL));
		
		for (Iterator iter = index.getChildEntries().iterator(); iter.hasNext();) {
			ChildEntry childEntry = (ChildEntry) iter.next();
			
			// be sure to resolve this reference
			childEntry.getChildObject();
			
			if (loadUnits) {
				// dereference parent resource map to force parent unit to load,
				//    if that load option is enabled
				childEntry.getParentMap();
			}
		}
	}
	
	/**
	 * Loads separate elements from sub-units according to the load options.
	 * At a minimum, unloaded "proxies" are created that will be filled in
	 * later.
	 * <p>
	 * <b>Note</b> that, as a special case, logical roots that are separate
	 * elements are always loaded because we must have all logical roots
	 * (at least) accounted for.
	 * </p>
	 * 
	 * @param index my physical resource map
	 * @param options my load options
	 */
	private void loadSubunits(ResourceMap index, Map options) {
		final boolean loadUnits = Boolean.TRUE.equals(
			options.get(ILogicalResource.OPTION_LOAD_ALL_UNITS));
		
		for (Iterator iter = index.getParentEntries().iterator(); iter.hasNext();) {
			ParentEntry parentEntry = (ParentEntry) iter.next();
			
			EObject parent = parentEntry.getParentObject();
			EReference slot = parentEntry.getChildSlot();
			
			if ((parent != null) && parent.eIsProxy()) {
				// this should not happen, because the parent must be in me
				parent = EcoreUtil.resolve(parent, getResourceSet());
			}
			
			for (Iterator iter2 = parentEntry.getResourceEntries().iterator(); iter2.hasNext();) {
				ResourceEntry subunit = (ResourceEntry) iter2.next();
				
				int pos = subunit.getChildPosition();
				
				EObject child;
				if (loadUnits || (parent == null)) {
					// may result in proxy if unit not found
					child = subunit.getChildObject();
				} else {
					// just get the proxy without resolving it
					child = (EObject) subunit.eGet(
						ResourceMapPackage.eINSTANCE.getResourceEntry_ChildObject(),
						false);
				}
				
				if (child.eIsProxy()) {
					// we may have loaded this unit already, by reaching
					//   another of its roots
					URI physUri = EcoreUtil.getURI(child).trimFragment();
					Resource res = getResourceSet().getResource(
						physUri, false);
					if (res == null) {
						res = getResourceSet().createResource(physUri);
					}
					subunit.setResource(res);
					
					if (res.isLoaded()) {
						// try to resolve the reference to the sub-unit in the
						//   dedicated resource set
						child = EcoreUtil.resolve(child, getResourceSet());
					} else {
						// create a stand-in for the child element
						//   but do not load its resource
						getLogicalResource().addMappedResource(child, res);
						
						// grab the object's ID while it is still a proxy
						String id = MSLUtil.getProxyID(child);
						String name = MSLUtil.getProxyName(child);
						
						// this object will do nicely for the time being, so
						// de-proxyize it now that we know the resource
						((InternalEObject) child).eSetProxyURI(null);
						addToParent(parent, slot, pos, child);
						
						// remove the dirty adapter because we don't care about
						//    changes in the "proxy" object
						getLogicalResource().removeContentAdapters(child);

						if (id != null) {
							setID(child, id);
						}
						if (name != null) {
							EObjectUtil.setName(child, name);
						}
					}
				}
				
				if (!child.eIsProxy() && (child.eContainer() == null)) {
					// we resolved to the root of a sub-unit resource (and
					//    didn't create an unloaded element, because it has
					//    a container).
					// Don't ask for eResource() because the child doesn't
					//    know it
					LogicalResourceUnit res = getLogicalResource().getUnit(child);
					
					subunit.setResource(res);
					addToParent(parent, slot, pos, child);
					
					if (res.getContents().get(0) instanceof ResourceMap) {
						// load its sub-units, recursively
						res.loadSubunits(
							(ResourceMap) res.getContents().get(0),
							options);
					}
					
					res.setClean();
				}
			}
		}
	}
	
	/**
	 * Inserts the specified <code>child</code> object (loaded from a sub-unit)
	 * into the correct <code>pos</code>ition in the correct <code>slot</code>
	 * of its <code>parent</code> element.
	 * 
	 * @param parent the parent element to insert the <code>child</code> into,
	 *    or <code>null</code> to insert as a logical root
	 * @param slot the containment reference to hold the <code>child</code>,
	 *    or <code>null</code> to insert into the logical resource contents
	 * @param pos the position in the reference, if it is a list
	 * @param child the child object to insert into the content tree
	 */
	private void addToParent(EObject parent, EReference slot, int pos, EObject child) {
		LogicalResource res = getLogicalResource();
		
		try {
			if (parent != null) {
				parent.eSetDeliver(false);
			} else {
				res.eSetDeliver(false);
			}
			child.eSetDeliver(false);
			
			if (slot == null) {
				// this is a logical root element
				res.getContents().add(pos, child);
			} else if (slot.isMany()) {
				EList values = (EList) parent.eGet(slot);
				
				if (!values.contains(child)) {
					values.add(pos, child);
				}
			} else {
				parent.eSet(slot, child);
			}
		} finally {
			child.eSetDeliver(true);
			if (parent != null) {
				parent.eSetDeliver(true);
			} else {
				res.eSetDeliver(true);
			}
			
			// manually propagate content adapters because we didn't deliver
			//    notifications
			res.propagateContentAdapters(child);
		}
	}
	
	/**
	 * Retrieves the index map of my physical resource structure.  This is
	 * either the map that I deserialized when I was loaded, or it is lazily
	 * created (only if there are multiple physical resources in the logical
	 * resource).  The resource map is an index of the locations in each
	 * parent where separate children belong.
	 * 
	 * @return my physical resource map
	 */
	ResourceMap getResourceIndex() {
		if (resourceIndex == null) {
			if (!getContents().isEmpty() && (getContents().get(0) instanceof ResourceMap)) {
				resourceIndex = (ResourceMap) getContents().get(0);
			} else if (!getLogicalResource().isMonolithic()) { // don't create one for a monolithic resource
				resourceIndex = ResourceMapFactory.eINSTANCE.createResourceMap();
				getContents().add(0, resourceIndex); // add as first root
				ResourceMap rootIndex = getLogicalResource().getRootUnit().getResourceIndex();
				if (rootIndex != resourceIndex) {
					resourceIndex.setRootMap(rootIndex);
				}
			}
		}
		
		return resourceIndex;
	}
	
	/**
	 * Cleans out of my resource index any separate elements that 
	 * are no longer attached to the logical content tree.  Note that we should
	 * only do this when we save, because otherwise, some operation might be
	 * about to reattach a temporarily detached sub-unit.
	 */
	void cleanupResourceIndex() {
		ResourceMap index = getResourceIndex();
		
		if (index != null) {
			// take this opportunity to hook the index up to its root resource
			//   index, if any
			ResourceMap rootIndex = getLogicalResource().getRootUnit()
				.getResourceIndex();
			
			if ((rootIndex != null) && (rootIndex != getResourceIndex())) {
				index.setRootMap(rootIndex);
			}
			
			// clean up the parent entries
			for (Iterator iter = index.getParentEntries().iterator(); iter.hasNext();) {
				ParentEntry parentEntry = (ParentEntry) iter.next();
				
				for (Iterator jter = parentEntry.getResourceEntries().iterator(); jter.hasNext();) {
					ResourceEntry subunitEntry = (ResourceEntry) jter.next();
					
					EObject child = subunitEntry.getChildObject();
					if (child.eResource() != getLogicalResource()) {
						// child has been disconnected.  Forget about its sub-unit
						jter.remove();  // remove the sub-unit resource entry
						
						LogicalResourceUnit subunit = getLogicalResource().getUnit(child);
						if ((subunit != null) && !subunit.isDirty()) {
							// ensure that it is cleaned up
							subunit.cleanupResourceIndex();
						}
					}
				}
				
				if (parentEntry.getResourceEntries().isEmpty()) {
					iter.remove();  // no need for a parent entry without sub-units
				}
			}
			
			// clean up the child entries.  This works in reverse:  we look for
			//    roots that have no corresponding child entries, and delete them
			if (this != getLogicalResource().getRootUnit()) {
				for (Iterator iter = getContents().iterator(); iter.hasNext();) {
					EObject next = (EObject) iter.next();
					
					if (next != index) {
						ChildEntry childEntry = index.getChildEntry(next);
						
						if (childEntry == null) {
							// child has been disconnected.  Remove it
							iter.remove();
						}
					}
				}
			}
		}
	}
	
	/**
	 * Queries whether I need to be saved.
	 * 
	 * @return <code>true</code> if I have changes that need to be saved;
	 *    <code>false</code>, otherwise
	 */
	boolean isDirty() {
		return dirty && isLoaded();  // unloaded resources cannot be dirty
	}
	
	/**
	 * Sets me as needing to be saved.
	 */
	void setDirty() {
		super.setModified(true); // do not propagate to logical resource
		dirty = true;
	}
	
	/**
	 * Sets me as not needing to be saved; I have no pending changes.
	 */
	void setClean() {
		super.setModified(false); // do not propagate to logical resource
		dirty = false;
	}
	
	/**
	 * Ensures that I know (and that adapters listening know) that I am loaded.
	 */
	void loaded() {
		if (!isLoaded()) {
			Notification notification = setLoaded(true);
			if (notification != null) {
				eNotify(notification);
			}
		}
	}
	
	public void setModified(boolean isModified) {
		super.setModified(isModified);
		
		if (isModified && getLogicalResource().isTrackingModification()) {
			// propagate modified state to the root
			getLogicalResource().setModified(isModified);
		}
	}
	
	protected XMLHelper createXMLHelper() {
		return new LogicalHelper(this);
	}
	
	protected XMLLoad createXMLLoad() {
		return new LogicalLoad(createXMLHelper());
	}
	
	protected XMLSave createXMLSave() {
		return new LogicalSave(createXMLHelper());
	}
	
	public EObject getEObject(String uriFragment) {
		return getLogicalResource().getEObject(uriFragment);
	}
	
	public String getID(EObject eObject) {
		return getLogicalResource().getID(eObject);
	}
	
	public void setID(EObject eObject, String id) {
		getLogicalResource().setID(eObject, id);
	}
	
	public String getEncoding() {
		return getLogicalResource().getEncoding();
	}
	
	public Map getIDToEObjectMap() {
		return getLogicalResource().getIDToEObjectMap();
	}
	
	public Map getEObjectToIDMap() {
		return getLogicalResource().getEObjectToIDMap();
	}
	
	public EList getContents() {
		return roots;
	}
	
	/**
	 * <p>
	 * A contents list for the physical resources in a logical resource.
	 * This list does not maintain an inverse relationship:  the roots of a
	 * physical resource do not know what physical resource they are in, but
	 * only the logical resource.
	 * </p><p>
	 * This contents list is also responsible for synchronizing the logical
	 * resource's {@linkplain LogicalResource#getResourceMap() resource map}.
	 * </p>
	 *
	 * @author Christian W. Damus (cdamus)
	 */
	private class ContentsList extends BasicEList implements InternalEList {
		private static final long serialVersionUID = 1L;
		
		protected boolean isUnique() {
			return true;
		}
		
		protected void didAdd(int index, Object object) {
			super.didAdd(index, object);
			didAddImpl(index, object);
		}

		private void didAddImpl(int index, Object object) {
			loaded();  // ensure that we know that we're loaded
			
			if (object instanceof ResourceMap) {
				InternalEObject eObject = (InternalEObject) object;
				
				// the resource map is properly contained by me
				eObject.eSetResource(LogicalResourceUnit.this, null);
				attached(eObject);
			} else {
				EObject eObject = (EObject) object;
				
				getLogicalResource().addMappedResource(
					eObject,
					LogicalResourceUnit.this);
				
				// don't create sub-unit entries in the root unit, and
				//   also don't do it while we are deserializing ourselves
				if (!isRoot() && isAutoLoadEnabled()) {
					EObject parent = eObject.eContainer();
					
					LogicalResourceUnit parentUnit =
						getLogicalResource().getUnit(parent);
					
					LogicalResourceUtil.createSubunitEntry(
						eObject, parentUnit, LogicalResourceUnit.this);
				} else if (isRoot() && !getLogicalResource().getContents().contains(eObject)) {
					// this is a logical root element.  Add it
					getLogicalResource().getContents().add(eObject);
				}
			}
		}
		
		protected void didRemove(int index, Object object) {
			super.didRemove(index, object);
			didRemoveImpl(index, object);
		}
		
		private void didRemoveImpl(int index, Object object) {
			if (object instanceof ResourceMap) {
				InternalEObject eObject = (InternalEObject) object;
				
				// the resource map was properly contained by me
				eObject.eSetResource(null, null);
				detached(eObject);
			} else {
				EObject eObject = (EObject) object;
				
				getLogicalResource().removeMappedResource(eObject);
				
				// don't remove child entries in the root unit, and
				//   also don't do it while we are deserializing ourselves
				if (!isRoot() && isAutoLoadEnabled()) {
					LogicalResourceUtil.removeChildEntry(
						LogicalResourceUnit.this,
						eObject);
				}
			}
		}
		
		protected void didSet(int index, Object newObject, Object oldObject) {
			super.didSet(index, newObject, oldObject);
			didRemoveImpl(index, oldObject);
			didAddImpl(index, newObject);
		}

		//
		// Delegate the internal interface to the inherited implementations.
		//
		
		public NotificationChain basicRemove(Object object, NotificationChain notifications) {
			remove(object);
			return notifications;
		}

		public NotificationChain basicAdd(Object object, NotificationChain notifications) {
			add(object);
			return notifications;
		}
		
		public List basicList() {
			return super.basicList();
		}
		
		public Iterator basicIterator() {
			return super.basicIterator();
		}
		
		public ListIterator basicListIterator() {
			return super.basicListIterator();
		}
		
		public ListIterator basicListIterator(int index) {
			return super.basicListIterator(index);
		}
	}
}
