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
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.emf.core.internal.l10n.EMFCoreMessages;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLPlugin;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLStatusCodes;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMap;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage;
import org.eclipse.gmf.runtime.emf.core.internal.util.Trace;
import org.eclipse.gmf.runtime.emf.core.resources.AbstractLogicalResource;
import org.eclipse.gmf.runtime.emf.core.resources.CannotAbsorbException;
import org.eclipse.gmf.runtime.emf.core.resources.CannotSeparateException;
import org.eclipse.gmf.runtime.emf.core.resources.ILogicalResource;
import org.eclipse.gmf.runtime.emf.core.resources.MResourceFactory;
import org.eclipse.osgi.util.NLS;

/**
 * Implementation of the {@linkplain ILogicalResource logical resource} API.
 * <p>
 * <b>Note</b> that subclasses are encouraged to define also a companion unit
 * class extending {@link LogicalResourceUnit}, in case they have any need to
 * customize the serialization of their physical resources.  If a custom unit
 * class is implemented, then the logical resource class must override the
 * {@link #createUnit(URI)} method to create instances of the appropriate unit
 * class.
 * </p>
 * <p>
 * For example:
 * </p><pre>
 *     public class MyResource extends LogicalResource {
 *         // ... fields and stuff ...
 *         
 *         public MyResource(URI uri) {
 *             super(root, uri);
 *             
 *             // ... other initialization ...
 *         }
 *         
 *         protected LogicalResourceUnit createUnit(URI uri) {
 *             return new MyResourceUnit(uri, this);
 *         }
 *         
 *         // ... other methods and stuff ...
 *     }
 *     
 *     public class MyResourceUnit extends LogicalResourceUnit {
 *         public MyResourceUnit(URI uri, MyResource logical) {
 *             super(uri, logical);
 *         }
 *         
 *         // ... other stuff here ...
 *     }
 * </pre>
 *
 * @see LogicalResourceUnit
 * @see #createUnit(URI)
 * 
 * @author Christian W. Damus (cdamus)
 */
public class LogicalResource
	extends AbstractLogicalResource
	implements ILogicalResource {

	private DirtyAdapter dirtyAdapter;
	
	// stores the individual sub-unit resources.  All resources share one copy
	//    so that they can resolve x-refs to the same physical resource
	private final ResourceSet subunitResourceSet;
	
	private boolean autoLoadEnabled = true;
	
	/**
	 * Initializes me with my URI.
	 * 
	 * @param uri my URI
	 */
	protected LogicalResource(URI uri) {
		super(uri);
		
		dirtyAdapter = new DirtyAdapter();
		eAdapters().add(dirtyAdapter);
		subunitResourceSet = new SubunitResourceSet();
		contents = new LogicalContentsList();
	}

	protected boolean useUUIDs() {
		return true;
	}
	
	/**
	 * Creates a new unit of this logical resource.  Subclasses must override
	 * to create an instance of their companion unit class.
	 * 
	 * @param uri the URI of the new unit
	 * @return the new unit
	 */
	protected LogicalResourceUnit createUnit(URI unitUri) {
		return new LogicalResourceUnit(unitUri, this);
	}
	
	public boolean isSeparate(EObject eObject) {
		return LogicalResourceUtil.isSeparate(this, eObject);
	}
	
	protected void doSeparate(EObject eObject, URI physUri) throws CannotSeparateException {
		LogicalResourceUtil.separate(this, eObject, physUri);
	}
	
	protected void doAbsorb(EObject eObject) throws CannotAbsorbException {
		LogicalResourceUtil.absorb(this, eObject);
	}
	
	public boolean isLoaded(EObject eObject) {
		return LogicalResourceUtil.isLoaded(this, eObject);
	}
	
	protected void doLoad(EObject eObject) throws IOException {
		LogicalResourceUtil.load(this, eObject);
		
		LogicalResourceUnit unit = getUnit(eObject);
		
		// Propagate content adapters to the now-loaded object.
		//    Make sure that the adapters' propagation, if they encounter any
		//    more unloaded objects, doesn't cause the rest of the tree
		//    to load prematurely
		try {
			unit.disableAutoLoad();
			eObject.eAdapters().addAll(
				0,
				getContentAdapters(eObject.eContainer()));
		} finally {
			unit.enableAutoLoad();
		}
	}
	
	// overridden to make it visible in this package
	protected void addMappedResource(EObject object, Resource subunit) {
		super.addMappedResource(object, subunit);
	}

	// overridden to make it visible in this package
	protected void removeMappedResource(EObject object) {
		super.removeMappedResource(object);
	}
	
	// overridden to make it visible in this package
	protected Map getResourceMap() {
		return super.getResourceMap();
	}
	
	protected Resource getPhysicalResource(URI physUri) {
		Resource result = getSubunitResourceSet().getResource(physUri, false);
		
		if (result != null) {
			result = UnmodifiableResourceView.get(result);
		}
		
		return result;
	}
	
	/**
	 * Extends the inherited method to revert the logical resource to a single,
	 * monolithic logical resource on the new URI.  This ensures that saving the
	 * resource under a new URI does not leave the former root resource
	 * disconnected from its sub-units (as they would be subordinate to the
	 * new URI).  It also ensures that there will be no need to check out all of
	 * the sub-units in an SCM environment.
	 */
	public void setURI(URI uri) {
		super.setURI(uri);
		
		// load any as-yet-unloaded sub-units
		try {
			LogicalResourceUtil.loadAllUnloadedUnits(this);
		} catch (IOException e) {
			Trace.catching(getClass(), "setURI(URI)", e); //$NON-NLS-1$
			
			Log.error(
				MSLPlugin.getDefault(),
				MSLStatusCodes.LOGICAL_SETURI_UNLOADED_FAILED,					
				NLS.bind(EMFCoreMessages.setUri_unloaded_failed_EXC_, uri),
				e);
		}
		
		// forget all of my sub-units
		clearMappedResources();
		getSubunitResourceSet().getResources().clear();
		
		// create my new physical root
		LogicalResourceUnit phys = getRootUnit();
		phys.setDirty();
		
		// all of my roots are in the new physical root
		phys.getContents().addAll(getContents());
	}
	
	public Object getAdapter(Class adapter) {
		if (adapter == ResourceSet.class) {
			// I do represent a resource set, in a sense
			return getSubunitResourceSet();
		} else {
			return Platform.getAdapterManager().getAdapter(this, adapter);
		}
	}
	
	/**
	 * Finds and, if necessary, creates the physical resource corresponding
	 * to the logical resource URI.  This is the "root" of the physical resource
	 * structure.
	 * 
	 * @return the root physical resource
	 */
	LogicalResourceUnit getRootUnit() {
		LogicalResourceUnit result =
			(LogicalResourceUnit) getSubunitResourceSet().getResource(
				getURI(),
				false);
		
		if (result == null) {
			result = (LogicalResourceUnit) getSubunitResourceSet().createResource(
				getURI());
		}
		
		return result;
	}
	
	/**
	 * Obtains the resource set that maintains my physical resources.
	 * 
	 * @return my physical resource set
	 */
	ResourceSet getSubunitResourceSet() {
		return subunitResourceSet;
	}
	
	/**
	 * Ensures that the specified element has all of the content adapters
	 * attached to it that its container has.
	 * 
	 * @param eObject the element
	 */
	void propagateContentAdapters(EObject eObject) {
		eObject.eAdapters().addAll(getContentAdapters(eObject.eContainer()));
	}
	
	/**
	 * Ensures that the specified element does not have my dirty adapter
	 * attached to it.
	 * 
	 * @param eObject the element
	 */
	void removeContentAdapters(EObject eObject) {
		eObject.eAdapters().removeAll(getContentAdapters(eObject));
	}
	
	/**
	 * Gets those of the adapters attached to an element that are content
	 * adapters.  If the element is <code>null</code>, then the adapters on the
	 * logical resource are returned, for convenience when an element's
	 * container (which normally would propagate) is <code>null</code>.
	 * 
	 * @param eObject an element, or <code>null</code> to get the adapters on
	 *      the logical resource
	 * @return the content adapters (and not other kinds of adapters)
	 */
	List getContentAdapters(EObject eObject) {
		Collection adapters;
		if (eObject == null) {
			adapters = eAdapters();
		} else {
			adapters = eObject.eAdapters();
		}
		
		List result = new java.util.ArrayList(adapters.size());
		for (Iterator iter = adapters.iterator(); iter.hasNext();) {
			Object next = iter.next();
			
			if (next instanceof EContentAdapter) {
				result.add(next);
			}
		}
		
		return result;
	}
	
	private void initCustomLoadOptions(Map options) {
		if (!options.containsKey(OPTION_LOAD_AS_LOGICAL)) {
			options.put(OPTION_LOAD_AS_LOGICAL, Boolean.TRUE);
		}
		
		if (!Boolean.TRUE.equals(options.get(OPTION_LOAD_AS_LOGICAL))) {
			// don't load any sub-units
			options.put(OPTION_LOAD_ALL_UNITS, Boolean.FALSE);
			options.put(OPTION_AUTO_LOAD_UNITS, Boolean.FALSE);
		} else {
			if (!options.containsKey(OPTION_LOAD_ALL_UNITS)) {
				options.put(OPTION_LOAD_ALL_UNITS, Boolean.FALSE);
			}
			
			if (!options.containsKey(OPTION_AUTO_LOAD_UNITS)) {
				options.put(OPTION_AUTO_LOAD_UNITS, Boolean.TRUE);
			}
		}
		
		// propagate to demand-loaded sub-units
		((SubunitResourceSet) getSubunitResourceSet()).clearLoadOptions();
		getSubunitResourceSet().getLoadOptions().putAll(options);
	}
	
	public void doLoad(InputStream inputStream, Map options)
		throws IOException {
		
		try {
			disableAutoLoad();
			dirtyAdapter.disable();
			
			initCustomLoadOptions(options);
			
			final boolean loadAsLogical = Boolean.TRUE.equals(
				options.get(OPTION_LOAD_AS_LOGICAL));
			
			// do the loading
			
			LogicalResourceUnit physicalRoot = getRootUnit();
			
			LogicalResourceUtil.loadUnit(physicalRoot, inputStream);
			
			if (loadAsLogical && (physicalRoot.getResourceIndex() != null)) {
				// if we're loading ourselves as a logical resource, then
				//    check whether we are opening the root resource
				ResourceMap rootIndex = physicalRoot.getResourceIndex().getRootMap();
				
				if ((rootIndex != null) && (rootIndex != physicalRoot.getResourceIndex())) {
					
					// I am not the root of the logical resource.  I need to
					//   load it and become it.  Be sure to load the whole
					//   containment chain up to the root
					
					// This handles the case where rootIndex is a proxy
					URI rootUri = EcoreUtil.getURI(rootIndex).trimFragment();
					
					// Be careful not to set the URI of the physical resource
					//    that I have already loaded
					super.setURI(rootUri);
					
					physicalRoot = getRootUnit();
					
					if (!physicalRoot.isLoaded()) {
						// this shouldn't happen, because we should have
						//    resolved the rootIndex reference by loading
						//    the root resource already!
						LogicalResourceUtil.loadUnit(physicalRoot);
					}
				}
			} else if (!loadAsLogical && getContents().isEmpty()) {
				// we didn't get any of the logical roots, yet, from the true
				//    root unit, so we should fake it
				EList actualRoots = physicalRoot.getContents();
				int offset = 1 + actualRoots.indexOf(
					physicalRoot.getResourceIndex());
				
				getContents().addAll(actualRoots.subList(
					offset, actualRoots.size()));
			}
		} finally {
			dirtyAdapter.enable();
		}
		
		if (Trace.isEnabled(MSLDebugOptions.RESOURCES)) {
			Trace.trace(
				MSLDebugOptions.RESOURCES,
				"Loaded logical resource: " + getURI()); //$NON-NLS-1$
		}
	}
	
	public void save(Map options)
		throws IOException {
		
		if (getRootUnit().isDirty() || isMonolithic()) {
			// for compatibility with the defaultXMI resource implementation,
			//   we will force the save even when we are not dirty, if we
			//   are monolithic
			super.save(options);
		} else {
			// we're not dirty, so we don't need an output stream to write to,
			//    but we probably have sub-units that need saving
			doSave(null, options);
			setModified(false);
		}
	}
	
	public void doSave(OutputStream outputStream, Map options)
		throws IOException {
		
		try {
			dirtyAdapter.disable();
			
			cleanupResourceIndices();
			
			LogicalResourceUnit phys = getRootUnit();
			
			if (phys.isDirty() ||(outputStream != null)) {
				// use this output stream to save my physical root resource
				phys.save(outputStream, options);
			}
			
			saveSubunits(options, getSubunitResourceSet());
			
			if (Trace.isEnabled(MSLDebugOptions.RESOURCES)) {
				Trace.trace(
					MSLDebugOptions.RESOURCES,
					"Saved logical resource: " + getURI()); //$NON-NLS-1$
			}
		} finally {
			dirtyAdapter.enable();
			
			if (errors != null) {
				// look for an IOException that indicates a save problem, and
				//    throw it
				for (Iterator iter = errors.iterator(); iter.hasNext();) {
					Object next = iter.next();
					
					if (next instanceof IOException) {
						IOException e = (IOException) next;
						Trace.throwing(getClass(), "doSave(OutputStream, Map)", e); //$NON-NLS-1$
						throw e;
					}
				}
			}
		}
	}
	
	/**
	 * Queries whether auto-loading of sub-units is enabled.
	 * 
	 * @return <code>true</code> if auto-loading of sub-units is enabled;
	 *     <code>false</code>, otherwise
	 */
	boolean isAutoLoadEnabled() {
		return autoLoadEnabled;
	}
	
	/**
	 * Turns off auto-loading of unloaded sub-units.
	 */
	void disableAutoLoad() {
		autoLoadEnabled = false;
	}
	
	/**
	 * Turns on auto-loading of unloaded sub-units.  This should only be
	 * invoked either by the root resource when it first becomes unmodified
	 * after loading.
	 */
	void enableAutoLoad() {
		autoLoadEnabled = true;
	}
	
	/**
	 * Used by the root of the logical resource to save all of its sub-unit
	 * resources that are dirty.
	 * 
	 * @param options the current save options (all units saved with the same
	 *    options
	 * @param rset the resource set containing the sub-unit resources
	 * @throws IOException if anything goes wrong in saving a resource
	 */
	private void saveSubunits(Map options, ResourceSet rset) {
		for (Iterator iter = rset.getResources().iterator(); iter.hasNext();) {
			LogicalResourceUnit next = (LogicalResourceUnit) iter.next();
			
			if (next.isDirty()) {
				try {
					next.save(options);
				} catch (IOException e) {
					Trace.catching(getClass(), "saveSubunits(Map, ResourceSet)", e); //$NON-NLS-1$
					error(e);
				}
			}
		}
	}
	
	/**
	 * Adds a throwable to my list of error-severity exceptions.
	 * 
	 * @param t a throwable to add to my errors
	 */
	protected void error(Throwable t) {
		getErrors().add(t);
	}
	
	protected void doUnload() {
		super.doUnload();
		
		clearMappedResources();
		
		ResourceSet rset = getSubunitResourceSet();
		List toUnload = new java.util.ArrayList(rset.getResources());
		
		for (Iterator iter = toUnload.iterator(); iter.hasNext();) {
			((Resource) iter.next()).unload();
		}
		
		rset.getResources().clear();
	}
	
	/**
	 * Queries whether I am a monolithic logical resource, comprising only a
	 * single physical resource.
	 * 
	 * @return whether I do not comprise more than one physical resource
	 */
	boolean isMonolithic() {
		return getSubunitResourceSet().getResources().size() < 2;
	}
	
	/**
	 * Cleans up my resource indices to ensure that they are up-to-date when we
	 * save.  This should only be invoked on the root resource.
	 */
	private void cleanupResourceIndices() {
		Set resources = new java.util.HashSet(
			getSubunitResourceSet().getResources());
		
		// let each resource recompute its resource index if it is dirty.
		//   This may even involve moving sub-units between parent units
		for (Iterator iter = resources.iterator(); iter.hasNext();) {
			LogicalResourceUnit subunit = (LogicalResourceUnit) iter.next();
		
			if (subunit.isDirty()) {
				subunit.cleanupResourceIndex();
			} else {
				iter.remove(); // don't process in the next loop
			}
		}
		
		// now, we must sort the sub-unit entries for each parent object
		//    by position, so that we will not try to insert them out of
		//    sequence on next loading (which would access invalid indices).
		//    Do this in a separate loop once all resources know their
		//    sub-units
		Comparator comp = new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((ResourceEntry) o1).getChildPosition()
					- ((ResourceEntry) o2).getChildPosition();
			}};
			
		for (Iterator iter = resources.iterator(); iter.hasNext();) {
			LogicalResourceUnit subunit = (LogicalResourceUnit) iter.next();
			ResourceMap index = subunit.getResourceIndex();
			
			if (index != null) {
				List toSort = index.getParentEntries();
				
				for (Iterator jter = toSort.iterator(); jter.hasNext();) {
					ParentEntry next = (ParentEntry) jter.next();
					ECollections.sort(next.getResourceEntries(), comp);
				}
			}
		}
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
		
		if (!isModified) {
			// NOTE: Must do this only after firing the notification (in the
			// super method) because that is when the MSL's indexer traverses
			// the content tree, and it would force all of my sub-units to load
			enableAutoLoad();
		}
	}
	
	/**
	 * Finds and returns the physical resource that stores the specified
	 * element.
	 * 
	 * @param eObject an element in the logical resource
	 * @return the corresponding physical resource
	 * 
	 * @throws IllegalArgumentException if the <code>eObject</code> is not
	 *     in the logical resource
	 */
	public LogicalResourceUnit getUnit(EObject eObject) {
		return LogicalResourceUtil.getUnitFor(this, eObject);
	}
	
	/**
	 * Listens for changes in the contents of the logical resource, to set the
	 * dirty flag on the appropriate physical resource and to keep the physical
	 * mapping metadata up to date.
	 *
	 * @author Christian W. Damus (cdamus)
	 */
	private class DirtyAdapter extends EContentAdapter {
		private boolean enabled;
		
		public boolean isAdapterForType(Object type) {
			return type == DirtyAdapter.class;
		}
		
		/**
		 * Extends the inherited method to mark sub-units (physical resources)
		 * as dirty when their logical contents change, but only when dirty
		 * tracking is enabled.
		 */
		public void notifyChanged(Notification notification) {
			super.notifyChanged(notification);
			
			if (isEnabled() && !notification.isTouch()) {
				Object feature = notification.getFeature();
				if (feature instanceof EStructuralFeature &&
						((EStructuralFeature) feature).isTransient()) {
					// don't respond to transient features
					return;
				}
				
				Object notifier = notification.getNotifier();
				
				if (notifier instanceof EObject) {
					EObject eObject = (EObject) notifier;
					
					if ((eObject.eResource() != null)
							&& (eObject.eClass().getEPackage() != ResourceMapPackage.eINSTANCE)) {
						LogicalResourceUnit dirtyUnit = getUnit((EObject) notifier);
						dirtyUnit.setDirty();
					}
				} else if (notifier instanceof LogicalResource) {
					if (notification.getFeatureID(Resource.class) == Resource.RESOURCE__CONTENTS) {
						// any change in the contents list means that either the
						//    root unit's contents list is changed or its resource
						//    map is changed
						getRootUnit().setDirty();
					}
				}
			}
		}

		/**
		 * Extends the inherited method to update the resource index metadata
		 * whenever a separate element is removed from or added to the logical
		 * content tree.
		 */
		protected void handleContainment(Notification notification) {
			super.handleContainment(notification);
			
			if (isEnabled()) {
				Object notifier = notification.getNotifier();
				Object feature = notification.getFeature();
				
				switch (notification.getEventType()) {
					case Notification.SET:
					case Notification.UNSET: {
						Object value = notification.getOldValue();
						if (value != null) {
							removeFromResourceIndex(notifier, feature, value);
						}
						value = notification.getNewValue();
						if (value != null) {
							addToResourceIndex(value);
						}
						break;
					}
					case Notification.ADD: {
						addToResourceIndex(notification.getNewValue());
						break;
					}
					case Notification.MOVE: {
						updateResourceIndex(
							notification.getNewValue(),
							notification.getPosition());
						break;
					}
					case Notification.ADD_MANY: {
						Collection c = (Collection) notification.getNewValue();
						for (Iterator iter = c.iterator(); iter.hasNext(); ) {
							Object next = iter.next();
							addToResourceIndex(next);
						}
						break;
					}
					case Notification.REMOVE: {
						removeFromResourceIndex(
							notifier, feature, notification.getOldValue());
						break;
					}
					case Notification.REMOVE_MANY: {
						Collection c = (Collection) notification.getOldValue();
						for (Iterator iter = c.iterator(); iter.hasNext(); ) {
							Object next = iter.next();
							removeFromResourceIndex(notifier, feature, next);
						}
						break;
					}
				}
			}
		}
		
		/**
		 * Updates the resource index metadata when an element that was
		 * separate is added back into the content tree.  This recomputes the
		 * index in reparenting scenarios.
		 * 
		 * @param object an object added to the logical content tree
		 */
		private void addToResourceIndex(Object object) {
			if (object instanceof EObject) {
				EObject eObject = (EObject) object;
				EObject parent = eObject.eContainer();
				
				// is this a separate element that we are being added to?  If so,
				//    then we need to update the resource map for it
				addMappedResource(eObject, null);
				
				LogicalResourceUnit subunit =
					(LogicalResourceUnit) getResourceMap().get(eObject);
				
				if ((subunit != null) && !subunit.isRoot()) {
					subunit.getContents().add(eObject);
					
					LogicalResourceUnit parentUnit = getUnit(parent);
					if (parentUnit != null) {
						parentUnit.setDirty();
						updateChildPositions(parentUnit, parent, eObject.eContainmentFeature());
					}
				}
			}
		}
		
		/**
		 * Updates the resource index metadata when an element that was
		 * separate is removed from the content tree.  This recomputes the
		 * index in reparenting scenarios.
		 * 
		 * @param notifier the notifier from which an object was removed
		 * @param feature the containment feature from which an object was removed
		 * @param object an object removed from the logical content tree
		 */
		private void removeFromResourceIndex(Object notifier, Object feature, Object object) {
			if (object instanceof EObject) {
				EObject eObject = (EObject) object;
				EObject parent = null;
				EReference containment = null;
				
				if (notifier instanceof EObject) {
					parent = (EObject) notifier;
					containment = (EReference) feature;
				} // else the notifier is a resource, so the parent is null
				 
				// is this a separate element that we are being removed from?  If so,
				//    then we need to update the resource map for it
				LogicalResourceUnit subunit =
					(LogicalResourceUnit) getResourceMap().get(eObject);
				
				if ((subunit != null) && !subunit.isRoot()) {
					subunit.getContents().remove(eObject);
					
					// the adapter will afterwards find the new unit and dirty it
					subunit.setDirty();
					
					removeMappedResource(eObject);
					
					LogicalResourceUnit parentUnit = getUnit(parent);
					
					if (parentUnit != null) {
						parentUnit.setDirty();
						updateChildPositions(parentUnit, parent, containment);
					}
				}
			}
		}
		
		/**
		 * Updates the resource index metadata when an element is moved within
		 * a containment collection.  This recomputes the child position.
		 * 
		 * @param object an object moved in a containment collection
		 * @param position the new child position
		 */
		private void updateResourceIndex(Object object, int position) {
			if (object instanceof EObject) {
				EObject eObject = (EObject) object;
					
				// is this a separate element that we are being added to?  If so,
				//    then we need to update the resource map for it
				LogicalResourceUnit subunit =
					(LogicalResourceUnit) getResourceMap().get(eObject);
				
				if ((subunit != null) && !subunit.isRoot()) {
					LogicalResourceUnit parentUnit = getUnit(eObject.eContainer());
					ResourceEntry entry = parentUnit.getResourceIndex().getResourceEntry(
						eObject);
					
					if (entry != null) {
						entry.setChildPosition(position);
					}
				}
			}
		}
		
		/**
		 * Updates the positions in the physical resource map of the separate
		 * children in a containment reference in which an element has been
		 * added or removed.
		 * 
		 * @param unit the unit that owns <code>parent</code> element
		 * @param parent an element that had a child added or removed
		 * @param containment the containment reference
		 */
		private void updateChildPositions(
				LogicalResourceUnit unit,
				EObject parent,
				EReference containment) {
			
			if (unit != null) {
				// must update the positions of all other separate
				//    elements in the same container and feature, because
				//    they have been moved (though no MOVE event occurs)
				ResourceMap index = unit.getResourceIndex();
				if (index != null) {
					ParentEntry entry = index.getParentEntry(
						parent,
						containment);
					
					if (entry != null) {
						LogicalResourceUtil.updateChildPositions(unit, entry);
					}
				}
			}
		}
		
		final boolean isEnabled() {
			return enabled;
		}
		
		void enable() {
			enabled = true;
		}
		
		void disable() {
			enabled = false;
		}
	}
	
	/**
	 * Special resource set implementation to manage the physical resources
	 * comprising a logical resource.  This resource set delegates several
	 * features to the logical resource's containing resource set:
	 * <ul>
	 *   <li>URI converter:  to get the uri mappings (such as pathmaps)</li>
	 *   <li>load options:  for consistent deserialization characteristics</li>
	 *   <li>package registry:  for consistent resolution of demand-loaded
	 *       packages (such as for XML namespaces)</li>
	 *   <li>resource factory registry:  just in case, though a sub-unit
	 *       resource set will always create resources of the same actual type
	 *       as the logical resource</li>
	 * </ul>
	 *
	 * @author Christian W. Damus (cdamus)
	 */
	private class SubunitResourceSet extends ResourceSetImpl {
		private Map options;
		
		SubunitResourceSet() {
			super();
		}
		
		public URIConverter getURIConverter() {
			// we need to use the same URI converter as the logical resource's
			//    resource set in order to access path maps
			return LogicalResource.this.getURIConverter();
		}
		
		public Map getLoadOptions() {
			if (options == null) {
				options = new java.util.HashMap();
				
				// get default load options from the outer resource set
				ResourceSet rset = LogicalResource.this.getResourceSet();
				
				if (rset != null) {
					options.putAll(rset.getLoadOptions());
				} else {
					options.putAll(MResourceFactory.getDefaultLoadOptions());
				}
			}
			
			return options;
		}
		
		void clearLoadOptions() {
			options = null;
		}
		
		public EPackage.Registry getPackageRegistry() {
			EPackage.Registry result;
			
			// delegate package registry to the outer resource set
			ResourceSet rset = LogicalResource.this.getResourceSet();
			
			if (rset != null) {
				result = rset.getPackageRegistry();
			} else {
				result = super.getPackageRegistry();
			}
			
			return result;
		}
		
		public Resource.Factory.Registry getResourceFactoryRegistry() {
			Resource.Factory.Registry result;
			
			// delegate resource factory registry to the outer resource set
			ResourceSet rset = LogicalResource.this.getResourceSet();
			
			if (rset != null) {
				result = rset.getResourceFactoryRegistry();
			} else {
				result = Resource.Factory.Registry.INSTANCE;
			}
			
			return result;
		}
		
		protected void demandLoad(Resource resource)
			throws IOException {
			
			if (resource instanceof LogicalResourceUnit) {
				// we always want to load sub-units using the method that
				//   correctly links their roots into the content tree (where
				//   the parent unit is already loaded)
				LogicalResourceUtil.loadUnit((LogicalResourceUnit) resource);
			} else {
				// shouldn't happen, but just in case ...
				super.demandLoad(resource);
			}
		}
		
		public Resource createResource(URI newUri) {
			LogicalResourceUnit result = createUnit(newUri);
			
			getResources().add(result);
			
			return result;
		}
	}
	
	/**
	 * Contents list for the logical resource.  This list does have an inverse,
	 * according to the usual EMF resource contents.  Thus, the contents of
	 * a logical resource really do consider the logical resource to be their
	 * ultimate container.
	 *
	 * @author Christian W. Damus (cdamus)
	 */
	private class LogicalContentsList extends ContentsEList {
		private static final long serialVersionUID = 1L;
		
		protected void didAdd(int index, Object object) {
			super.didAdd(index, object);
			didAddImpl(index, object);
		}
		
		private void didAddImpl(int index, Object object) {
			if (!getResourceMap().containsKey(object)) {
				// do not put a separate logical root into the root unit
				
				LogicalResourceUnit phys = getRootUnit();
				EList actualRoots = phys.getContents();
				
				if (!phys.getContents().contains(object)) {
					int offset = 1 + actualRoots.indexOf(
						phys.getResourceIndex());
					
					phys.getContents().add(offset + index, object);
				}
			}
		}
		
		protected void didRemove(int index, Object object) {
			super.didRemove(index, object);
			didRemoveImpl(index, object);
		}
		
		private void didRemoveImpl(int index, Object object) {
			getRootUnit().getContents().remove(object);
		}
		
		protected void didSet(int index, Object newObject, Object oldObject) {
			super.didSet(index, newObject, oldObject);
			didRemoveImpl(index, oldObject);
			didAddImpl(index, newObject);
		}
	}
}
