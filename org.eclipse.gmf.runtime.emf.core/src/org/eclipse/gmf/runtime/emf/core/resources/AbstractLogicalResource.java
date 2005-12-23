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

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;


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
 * 
 * @deprecated Use the cross-resource containment support provided by EMF,
 *     instead, by defining containment features that are capable of storing
 *     proxies.
 */
public abstract class AbstractLogicalResource
	extends XMIResourceImpl
	implements ILogicalResource {

	private final Map resourceMap = new java.util.HashMap();
	
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
	 * I cannot separate any elements.
	 * 
	 * @return <code>false</code>, always
	 */
	public boolean canSeparate(EObject eObject) {
		return false;
	}

	/**
	 * I cannot separate any elements.
	 * 
	 * @return <code>false</code>, always
	 */
	public boolean isSeparate(EObject eObject) {
		return false;
	}

	public void separate(EObject eObject, URI uri) {
		throw new IllegalArgumentException("cannot separate eObject"); //$NON-NLS-1$
	}

	public void absorb(EObject eObject) {
		throw new IllegalArgumentException("eObject is not separate"); //$NON-NLS-1$
	}
	
	public boolean isLoaded(EObject eObject) {
		return true;  // no object in a non-logical-resource can be unloaded
	}
	
	public void load(EObject eObject)
		throws IOException {
		
		throw new IllegalArgumentException("eObject is not separate"); //$NON-NLS-1$
	}

	public Map getMappedResources() {
		return Collections.EMPTY_MAP;
	}

	public Object getAdapter(Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
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
	 * Implemented by the subclass to perform the absorption operation.
	 * 
	 * @param eObject the element to be absorbed
	 * @throws CannotAbsorbException if the absorption could not be performed
	 */
	protected abstract void doAbsorb(EObject eObject) throws CannotAbsorbException;
	
	/**
	 * Implemented by the subclass to perform the load operation.  This method
	 * will only be invoked if the element is not already loaded.
	 * 
	 * @param eObject the element to be loaded
	 * @throws IOException if the load failed on an I/O problem
	 */
	protected abstract void doLoad(EObject eObject) throws IOException;
	
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
		// no need to do anything any longer
	}
	
	/**
	 * Removes the specified <code>object</code>'s subunit resource from the map.
	 * Subclasses implementing this method must invoke <code>super</code>.
	 * 
	 * @param object the (formerly) separate element
	 */
	protected void removeMappedResource(EObject object) {
		// no need to do anything any longer
	}
	
	/**
	 * Clears my mapping of elements to units.
	 * Subclasses implementing this method must invoke <code>super</code>.
	 */
	protected void clearMappedResources() {
		// no need to do anything any longer
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
		
		return eObject.eResource();
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
