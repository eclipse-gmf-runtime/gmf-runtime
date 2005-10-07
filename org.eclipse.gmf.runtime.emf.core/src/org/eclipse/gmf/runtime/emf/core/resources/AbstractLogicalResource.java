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


package org.eclipse.gmf.runtime.emf.core.resources;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import org.eclipse.gmf.runtime.emf.core.EventTypes;
import org.eclipse.gmf.runtime.emf.core.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.resources.AbstractResourceWrapper;
import org.eclipse.gmf.runtime.emf.core.internal.resources.LogicalResourcePolicyManager;
import org.eclipse.gmf.runtime.emf.core.internal.resources.LogicalResourceUtil;
import org.eclipse.gmf.runtime.emf.core.internal.resources.UnmodifiableResourceView;
import org.eclipse.gmf.runtime.emf.core.internal.util.Trace;


/**
 * Partial implementation of the {@link ILogicalResource} API, extending the
 * default XMI resource implementation.
 * <p>
 * It is recommended to
 * extend this class rather than implement the interface because it implements
 * the interaction with registered
 * {@linkplain ILogicalResourcePolicy resource policies} where appropriate
 * and fires {@link org.eclipse.emf.common.notify.Notification}s for separate,
 * absorb, and load events.
 * </p>
 * <p>
 * Subclasses may use any available resource implementation (or even a custom
 * implementation) for the physical resources (units) that are actually
 * persisted.  It does not matter that this logical implementation is an XML
 * resource, which is only used to provide support for ID-based URI fragments.
 * This makes it easier for management of element IDs or fragments across the
 * entire logical resource, by having the physical resources delegate ID
 * management to the logical resource.
 * </p>
 * <p>
 * Whenever separation or loading creates such a resource, it must
 * be {@linkplain #addMappedResource(Object, Resource) added} to the resource
 * map.  Likewise, absorption must
 * {@linkplain #removeMappedResource(Object) remove} entries from the map.
 * </p>
 * 
 * @see ILogicalResourcePolicy
 * 
 * @author Christian W. Damus (cdamus)
 */
public abstract class AbstractLogicalResource
	extends XMIResourceImpl
	implements ILogicalResource {

	private final Map resourceMap = new java.util.HashMap();
	private final Map unmodifiableResourceMap = new java.util.HashMap();
	
	/**
	 * A map remembering the resource that used to store a separate object
	 * after that object has been detached (in the hope that it will be
	 * re-attached soon).
	 * Maps {@link String} element ID to resource {@link URI}
	 */
	private final Map detachedIdToResourceMap = new java.util.HashMap(); 
	
	/**
	 * Initializes me.
	 */
	public AbstractLogicalResource() {
		super();
	}

	/**
	 * Initializes me with my URI.
	 * 
	 * @param uri my URI
	 */
	public AbstractLogicalResource(URI uri) {
		super(uri);
	}
	
	/**
	 * Implements the interface method by delegation to the registered logical
	 * resource policies, with the additional constraint that the
	 * <code>eObject</code> must not already be
	 * {@linkplain ILogicalResource#isSeparate(EObject) separate}.
	 * 
	 * @see ILogicalResource#isSeparate(EObject)
	 */
	public final boolean canSeparate(EObject eObject) {
		assert eObject.eResource() == this : "eObject is not in receiver" ; //$NON-NLS-1$
		
		// the logical resource roots are not considered as separate or
		//   as separable
		return !isSeparate(eObject) && customCanSeparate(eObject)
			&& LogicalResourcePolicyManager.getInstance().canSeparate(this, eObject);
	}
	
	/**
	 * Applies additional criteria to the question of whether an element
	 * can be separated.  May be overridden by subclasses to provide custom
	 * behaviour.
	 * <p>
	 * This default implementation just returns <code>true</code>.
	 * </p>
	 * 
	 * @param eObject the element that is proposed for separation
	 * @return whether it meets the resource implementation's additional criteria
	 */
	protected boolean customCanSeparate(EObject eObject) {
		return true;
	}
	
	/**
	 * Implementation of the element separation operation, which does the
	 * following:
	 * <ul>
	 *   <li>enforces the preconditions declared by the interface</li>
	 *   <li>consults the registered {@linkplain ILogicalResourcePolicy}
	 *       policies before and after separation</li>
	 *   <li>fires the {@link org.eclipse.gmf.runtime.emf.core.EventTypes#SEPARATE}
	 *       notification when (and if) separation succeeds</li>
	 *   <li>delegates the actual separation implementation to the
	 *       {@link #doSeparate(EObject, URI)} method implemented by the
	 *       subclass</li>
	 * </ul> 
	 * 
	 * @see #doSeparate(EObject, URI)
	 * @see ILogicalResourcePolicy
	 */
	public final void separate(EObject eObject, URI physUri) throws CannotSeparateException {
		assert eObject.eResource() == this : "eObject is not in receiver" ; //$NON-NLS-1$
		
		if (isSeparate(eObject)) {
			throw new IllegalArgumentException("eObject is already separate"); //$NON-NLS-1$
		}
		
		physUri = LogicalResourcePolicyManager.getInstance().preSeparate(this, eObject, physUri);
		
		Resource oldUnit = getPhysicalResource(eObject);
		
		if (Trace.isEnabled(MSLDebugOptions.RESOURCES)) {
			Trace.trace(
				MSLDebugOptions.RESOURCES,
				"Separating: " + oldUnit.getURI() + " ==> " + physUri + ": " + eObject);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		}
		
		if (physUri == null) {
			// not provided by user and no policy had a suggestion
			CannotSeparateException e = new CannotSeparateException(
				ResourceManager.getI18NString("separate.noUri_ERROR_")); //$NON-NLS-1$
			Trace.throwing(LogicalResourceUtil.class, "separate", e); //$NON-NLS-1$
			throw e;
		}
		
		if (physUri.equals(oldUnit.getURI())) {
			// not allowed to separate into current storage unit
			CannotSeparateException e = new CannotSeparateException(
				ResourceManager.getI18NString("separate.sameUnit_ERROR_")); //$NON-NLS-1$
			Trace.throwing(LogicalResourceUtil.class, "separate", e); //$NON-NLS-1$
			throw e;
		}
		
		doSeparate(eObject, physUri);
		
		Resource newUnit = getPhysicalResource(eObject);
		
		LogicalResourcePolicyManager.getInstance().postSeparate(this, eObject, physUri);
		
		// notify listeners
		LogicalResourceUtil.fireSeparationEvent(eObject, oldUnit, newUnit);
		
		setModified(true);
	}
	
	/**
	 * Implemented by the subclass to perform the separation operation.
	 * 
	 * @param eObject the element to be separated
	 * @param physUri the destination URI.  This URI has already been validated
	 *     for appropriateness.  It will not be <code>null</code>, and should
	 *     not be overridden by this method
	 * @throws CannotSeparateException if the separation could not be
	 *     performed
	 */
	protected abstract void doSeparate(EObject eObject, URI physUri) throws CannotSeparateException;
	
	/**
	 * Implementation of the element absorption operation, which does the
	 * following:
	 * <ul>
	 *   <li>enforces the preconditions declared by the interface</li>
	 *   <li>consults the registered {@linkplain ILogicalResourcePolicy}
	 *       policies before and after absorption</li>
	 *   <li>fires the {@link org.eclipse.gmf.runtime.emf.core.EventTypes#ABSORB}
	 *       notification when (and if) absorption succeeds</li>
	 *   <li>delegates the actual absorption implementation to the
	 *       {@link #doAbsorb(EObject)} method implemented by the subclass</li>
	 * </ul> 
	 * 
	 * @see #doAbsorb(EObject)
	 * @see ILogicalResourcePolicy
	 */
	public final void absorb(EObject eObject) throws CannotAbsorbException {
		assert eObject.eResource() == this : "eObject is not in receiver" ; //$NON-NLS-1$
		
		if (!isSeparate(eObject)) {
			throw new IllegalArgumentException("eObject is not separate"); //$NON-NLS-1$
		}
		
		if (!isLoaded(eObject)) {
			throw new IllegalArgumentException("eObject is not loaded"); //$NON-NLS-1$
		}
		
		Resource oldUnit = getPhysicalResource(eObject);
		EObject parent = eObject.eContainer();
		
		Resource newUnit;
		if (parent == null) {
			// the parent unit of a separate root must be to physical root unit
			newUnit = getPhysicalResource(getURI());
		} else {
			newUnit = getPhysicalResource(eObject.eContainer());
		}
		
		if (Trace.isEnabled(MSLDebugOptions.RESOURCES)) {
			Trace.trace(
				MSLDebugOptions.RESOURCES,
				"Absorbing: " + oldUnit.getURI() //$NON-NLS-1$
				+ " ==> " + newUnit.getURI() + ": " + eObject);  //$NON-NLS-1$//$NON-NLS-2$
		}
		
		LogicalResourcePolicyManager.getInstance().preAbsorb(this, eObject);
		
		doAbsorb(eObject);
		
		LogicalResourcePolicyManager.getInstance().postAbsorb(this, eObject);
		
		// notify listeners
		LogicalResourceUtil.fireAbsorptionEvent(eObject, oldUnit, newUnit);
		
		setModified(true);
	}
	
	/**
	 * Implemented by the subclass to perform the absorption operation.
	 * 
	 * @param eObject the element to be absorbed
	 * @throws CannotAbsorbException if the absorption could not be performed
	 */
	protected abstract void doAbsorb(EObject eObject) throws CannotAbsorbException;
	
	/**
	 * Implementation of the element load operation, which does the
	 * following:
	 * <ul>
	 *   <li>enforces the preconditions declared by the interface</li>
	 *   <li>delegates the actual load implementation to the
	 *       {@link #doLoad(EObject)} method implemented by the subclass, if
	 *       the element is not already loaded</li>
	 * </ul> 
	 * <p>
	 * <b>Note</b> that, because loading an object may also load other objects
	 * from the same sub-unit, this template method does not fire the
	 * {@link EventTypes#LOAD} notification, as it would not know how many
	 * to fire for which objects.  This is the responsibility of the subclass's
	 * {@link #doLoad(EObject)} implementation.
	 * </p>
	 * 
	 * @see #doLoad(EObject)
	 */
	public final void load(EObject eObject) throws IOException {
		assert eObject.eResource() == this : "eObject is not in receiver" ; //$NON-NLS-1$
		
		if (!isSeparate(eObject)) {
			throw new IllegalArgumentException("eObject is not separate"); //$NON-NLS-1$
		}
		
		if (!isLoaded(eObject)) {
			doLoad(eObject);
		}
	}
	
	/**
	 * Implemented by the subclass to perform the load operation.  This method
	 * will only be invoked if the element is not already loaded.
	 * 
	 * @param eObject the element to be loaded
	 * @throws IOException if the load failed on an I/O problem
	 */
	protected abstract void doLoad(EObject eObject) throws IOException;
	
	public final Map getMappedResources() {
		return Collections.unmodifiableMap(unmodifiableResourceMap);
	}
	
	/**
	 * Adds the specified <code>subunit</code> resource to the map.
	 * Subclasses implementing this method must invoke <code>super</code>.
	 * 
	 * @param object the separate element to map to the <code>unit</code>
	 * @param unit the sub-unit resource, or <code>null</code> to attempt to
	 *     re-map a re-attached element to the unit that it was in before it
	 *     was detached
	 */
	protected void addMappedResource(EObject object, Resource unit) {
		if (unit == null) {
			// look to see whether this object was previously a separate element
			//    that was detached from me
			URI unitUri = (URI) detachedIdToResourceMap.remove(getID(object));
			
			if ((unitUri != null) && !uri.equals(getURI())) {
				unit = getPhysicalResource(unitUri);
			}
		}
		
		if (unit != null) {
			// unwrap just in case
			resourceMap.put(object, AbstractResourceWrapper.unwrap(unit));
			
			// wrap just in case
			unmodifiableResourceMap.put(object, UnmodifiableResourceView.get(unit));
		}
	}
	
	/**
	 * Removes the specified <code>object</code>'s subunit resource from the map.
	 * Subclasses implementing this method must invoke <code>super</code>.
	 * 
	 * @param object the (formerly) separate element
	 */
	protected void removeMappedResource(EObject object) {
		// this element has been now detached.  Preserve the physical mapping
		//    in case it is re-attached later
		Resource unit = (Resource) getMappedResources().get(object);
		
		if (unit != null) {
			// the object's ID is retained by the detached object ID map
			detachedIdToResourceMap.put(getID(object), unit.getURI());
		}
		
		resourceMap.remove(object);
		unmodifiableResourceMap.remove(object);
	}
	
	/**
	 * Clears my mapping of elements to units.
	 * Subclasses implementing this method must invoke <code>super</code>.
	 */
	protected void clearMappedResources() {
		resourceMap.clear();
		unmodifiableResourceMap.clear();
		detachedIdToResourceMap.clear();
	}
	
	/**
	 * Obtains the writable form of my
	 * {@linkplain #getMappedResources() mapped resources}.
	 * 
	 * @return my mapped resources
	 * 
	 * @see #getMappedResources()
	 */
	protected Map getResourceMap() {
		return resourceMap;
	}

	/**
	 * A convenience method for retrieving the physical resource (as an
	 * unmodifiable view) that stores the specified element.
	 * 
	 * @param eObject an element in the logical resource.  It needs not be
	 *     a separate element (i.e., the root of a physical resource)
	 * @return an unmodifiable view of the resource that stores the element
	 */
	protected final Resource getPhysicalResource(EObject eObject) {
		assert eObject.eResource() == this : "eObject is not in receiver" ; //$NON-NLS-1$
		
		return LogicalResourceUtil.getPhysicalResource(this, eObject);
	}

	/**
	 * Implemented by subclasses to access the physical unit identified by the
	 * specified URI.
	 * <p>
	 * <b>Note</b> that the result should be an unmodifiable view of the
	 * resource in question.
	 * </p>
	 * 
	 * @param physUri the physical URI
	 * @return the corresponding unit, or <code>null</code> if not found.
	 *      No resource should be demand-created by this operation
	 */
	protected abstract Resource getPhysicalResource(URI physUri);
}
