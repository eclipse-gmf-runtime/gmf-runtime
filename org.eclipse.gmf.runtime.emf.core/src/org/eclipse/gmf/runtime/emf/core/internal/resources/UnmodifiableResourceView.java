/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.emf.core.internal.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMap;


/**
 * An unmodifiable view of a resource, used for presenting sub-units safely
 * to clients of the logical resource API.  This class maintains a canonical
 * mapping of unmodifiable views to resources, accessed by the
 * {@link #get(Resource)} method.
 *
 * @author Christian W. Damus (cdamus)
 */
public class UnmodifiableResourceView
	extends AbstractResourceWrapper
	implements Resource {

	private static final CanonicalMap instances = new CanonicalMap() {
		protected AbstractResourceWrapper createWrapper(Resource resource) {
			return new UnmodifiableResourceView(resource);
		}};
	
	/**
	 * Initializes me with the resource that I wrap.
	 * 
	 * @param resource the wrapped resource
	 */
	private UnmodifiableResourceView(Resource resource) {
		super(resource);
	}
	
	/**
	 * Gets the canonical unmodifiable view of the specified
	 * <code>resource</code>.  Note that, if the <code>resource</code>
	 * argument is already an unmodifiable resource, then it is returned as is.
	 * 
	 * @param resource the resource to get an unmodifiable view of
	 * @return its unmodifiable view
	 */
	public static Resource get(Resource resource) {
		Resource result = null;
		
		if (resource instanceof UnmodifiableResourceView) {
			result = resource;
		} else {
			result = instances.get(resource);
		}
		
		return result;
	}
	
	/**
	 * No access to the resource set is provided.
	 * 
	 * @return <code>null</code>, always
	 */
	public ResourceSet getResourceSet() {
		return null;
	}

	/**
	 * Not allowed.
	 * 
	 * @throws UnsupportedOperationException, always
	 */
	public void setURI(URI uri) {
		throw new UnsupportedOperationException("setURI(URI)"); //$NON-NLS-1$
	}

	/**
	 * Returns an unmodifiable view of the wrapped resource's contents, excluding
	 * the first root (which is the resource map).
	 */
	public EList getContents() {
		EList contents = super.getContents();
		EList result;
		
		if (!contents.isEmpty() && (contents.get(0) instanceof ResourceMap)) {
			// too bad ELists don't support sub-lists as ELists
			result = new BasicEList(contents.size() - 1);
			ECollections.setEList(result, contents.subList(1, contents.size()));
		} else {
			result = contents;
		}
		
		return ECollections.unmodifiableEList(result);
	}

	public TreeIterator getAllContents() {
		return EcoreUtil.getAllContents(getContents());
	}

	/**
	 * Not allowed.
	 * 
	 * @throws UnsupportedOperationException, always
	 */
	public void save(Map options)
		throws IOException {
		throw new UnsupportedOperationException("save(Map)"); //$NON-NLS-1$
	}

	/**
	 * Not allowed.
	 * 
	 * @throws UnsupportedOperationException, always
	 */
	public void load(Map options)
		throws IOException {
		throw new UnsupportedOperationException("load(Map)"); //$NON-NLS-1$
	}

	/**
	 * Not allowed.
	 * 
	 * @throws UnsupportedOperationException, always
	 */
	public void save(OutputStream outputStream, Map options)
		throws IOException {
		throw new UnsupportedOperationException("save(OutputStream, Map)"); //$NON-NLS-1$
	}

	/**
	 * Not allowed.
	 * 
	 * @throws UnsupportedOperationException, always
	 */
	public void load(InputStream inputStream, Map options)
		throws IOException {
		throw new UnsupportedOperationException("load(InputStream, Map)"); //$NON-NLS-1$
	}

	/**
	 * Not allowed.
	 * 
	 * @throws UnsupportedOperationException, always
	 */
	public void setTrackingModification(boolean isTrackingModification) {
		throw new UnsupportedOperationException("setTrackingModification(boolean)"); //$NON-NLS-1$
	}

	/**
	 * Not allowed.
	 * 
	 * @throws UnsupportedOperationException, always
	 */
	public void setModified(boolean isModified) {
		throw new UnsupportedOperationException("setModified(boolean)"); //$NON-NLS-1$
	}

	/**
	 * Not allowed.
	 * 
	 * @throws UnsupportedOperationException, always
	 */
	public void unload() {
		throw new UnsupportedOperationException("unload()"); //$NON-NLS-1$
	}

	/**
	 * Returns an unmodifiable view of the wrapped resource's errors.
	 */
	public EList getErrors() {
		return ECollections.unmodifiableEList(super.getErrors());
	}

	/**
	 * Returns an unmodifiable view of the wrapped resource's warnings.
	 */
	public EList getWarnings() {
		return ECollections.unmodifiableEList(super.getWarnings());
	}
	
	public String toString() {
		return "Unmodifiable(" + getWrappedResource().toString() + ')'; //$NON-NLS-1$
	}
}
