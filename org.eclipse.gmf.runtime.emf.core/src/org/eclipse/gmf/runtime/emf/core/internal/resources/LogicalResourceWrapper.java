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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.gmf.runtime.emf.core.resources.ILogicalResource;


/**
 * A wrapper for {@link Resource}s that implements the {@link ILogicalResource}
 * API but does not allow the separation of any elements.
 *
 * @author Christian W. Damus (cdamus)
 * 
 * @see #canSeparate(EObject)
 */
public class LogicalResourceWrapper
	extends AbstractResourceWrapper
	implements ILogicalResource {
	
	private static final CanonicalMap instances = new CanonicalMap() {
		protected AbstractResourceWrapper createWrapper(Resource resource) {
			return new LogicalResourceWrapper(resource);
		}};
	
	private Map resourceMap;
	
	/**
	 * Initializes me with the delegate that I wrap.
	 * 
	 * @param res my delegate
	 */
	private LogicalResourceWrapper(Resource res) {
		super(res);
	}
	
	/**
	 * Gets the canonical logical view of the specified
	 * <code>resource</code>.  Note that, if the <code>resource</code>
	 * argument is already a logical resource, then it is returned as is.
	 * 
	 * @param resource the resource to get an unmodifiable view of
	 * @return its unmodifiable view
	 */
	public static ILogicalResource get(Resource resource) {
		ILogicalResource result = null;
		
		if (resource instanceof LogicalResourceWrapper) {
			result = (ILogicalResource) resource;
		} else {
			result = (ILogicalResource) instances.get(resource);
		}
		
		return result;
	}

	//
	// ILogicalResource methods
	//
	
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
		if (resourceMap == null) {
			resourceMap = new java.util.HashMap() {
				{
					eAdapters().add(new AdapterImpl() {
						public void notifyChanged(Notification msg) {
							if (msg.getFeatureID(null) == RESOURCE__CONTENTS) {
								switch (msg.getEventType()) {
								case Notification.ADD:
									add(msg.getNewValue());
									break;
								case Notification.ADD_MANY:
									addAll((Collection) msg.getNewValue());
									break;
								case Notification.REMOVE:
									remove(msg.getOldValue());
									break;
								case Notification.REMOVE_MANY:
									removeAll((Collection) msg.getOldValue());
									break;
								case Notification.SET:
									remove(msg.getOldValue());
									add(msg.getNewValue());
								}
							}
						}});
					
					addAll(getContents());
				}
				
				void add(Object o) {
					put(o, UnmodifiableResourceView.get(getWrappedResource()));
				}
				
				void addAll(Collection c) {
					for (Iterator iter = c.iterator(); iter.hasNext();) {
						add(iter.next());
					}
				}
				
				void removeAll(Collection c) {
					for (Iterator iter = c.iterator(); iter.hasNext();) {
						remove(c);
					}
				}};
			
			resourceMap = Collections.unmodifiableMap(resourceMap);
		}
		
		return resourceMap;
	}

	public Object getAdapter(Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}
}
