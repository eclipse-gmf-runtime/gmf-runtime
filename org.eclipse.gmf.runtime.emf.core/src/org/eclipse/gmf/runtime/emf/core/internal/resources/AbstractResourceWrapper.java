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
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.notify.impl.NotifierImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;


/**
 * An implementation of a wrapper for EMF resources, useful for subclassing
 * to provide alternative views of resources.  This class is its own notifier,
 * forwarding notifications from the wrapped resource as though they originated
 * in itself, so that adapters don't see an inconsistency between the notifier
 * they are attached to and the notifier reported by the notifications (useful
 * especially when the same adapter instance is used on multiple resources).
 * <p>
 * Subclasses should be instance-controlled, with a static instance map
 * implemented by a subclass of the {@link AbstractResourceWrapper.CanonicalMap}
 * inner class.  The resource wrapper subclass's subclass of this map must
 * implement the {@link AbstractResourceWrapper.CanonicalMap#createWrapper(Resource)}
 * method to create a wrapper of the correct type.  The map calls this whenever
 * it needs to construct a wrapper.
 * </p>
 * 
 * @author Christian W. Damus (cdamus)
 */
public abstract class AbstractResourceWrapper
	extends NotifierImpl
	implements Resource {

	private final Resource wrapped;
	
	/**
	 * Initializes me with the resource that I wrap.
	 * 
	 * @param resource the wrapped resource
	 */
	protected AbstractResourceWrapper(Resource resource) {
		wrapped = resource;
		wrapped.eAdapters().add(new NotificationForwarder());
	}
	
	/**
	 * Retrieves the wrapped resource if the specified <code>resource</code> is
	 * an unmodifiable view.
	 * 
	 * @param resource a resource
	 * @return the wrapped resource, or the original <code>resource</code> if
	 *    not an unmodifiable view
	 */
	public static Resource unwrap(Resource resource) {
		Resource result = resource;
		
		if (resource instanceof AbstractResourceWrapper) {
			result = ((AbstractResourceWrapper) resource).getWrappedResource();
		}
		
		return result;
	}
	
	/**
	 * Provides subclasses with access to the wrapped resource.
	 * 
	 * @return the wrapped resource
	 */
	protected final Resource getWrappedResource() {
		return wrapped;
	}
	
	/**
	 * I equal another object if it is a wrapper of the same kind as me on
	 * a resource equal to mine.
	 */
	public boolean equals(Object obj) {
		boolean result = false;
		
		if ((obj != null) && (obj.getClass() == getClass())) {
			AbstractResourceWrapper other = (AbstractResourceWrapper) obj;
			
			result = wrapped.equals(other.wrapped);
		}
		
		return result;
	}
	
	/**
	 * My hash code is my wrapped resource's hash code.
	 */
	public int hashCode() {
		return wrapped.hashCode();
	}
	
	//
	// Strict delegation methods
	//
	
	public ResourceSet getResourceSet() {
		return wrapped.getResourceSet();
	}

	public URI getURI() {
		return wrapped.getURI();
	}

	public void setURI(URI uri) {
		wrapped.setURI(uri);
	}

	public EList getContents() {
		return wrapped.getContents();
	}

	public TreeIterator getAllContents() {
		return wrapped.getAllContents();
	}

	public String getURIFragment(EObject eObject) {
		return wrapped.getURIFragment(eObject);
	}

	public EObject getEObject(String uriFragment) {
		return wrapped.getEObject(uriFragment);
	}

	public void save(Map options)
		throws IOException {
		wrapped.save(options);
	}

	public void load(Map options)
		throws IOException {
		wrapped.load(options);
	}

	public void save(OutputStream outputStream, Map options)
		throws IOException {
		wrapped.save(outputStream, options);
	}

	public void load(InputStream inputStream, Map options)
		throws IOException {
		wrapped.load(inputStream, options);
	}

	public boolean isTrackingModification() {
		return wrapped.isTrackingModification();
	}

	public void setTrackingModification(boolean isTrackingModification) {
		wrapped.setTrackingModification(isTrackingModification);
	}

	public boolean isModified() {
		return wrapped.isModified();
	}

	public void setModified(boolean isModified) {
		wrapped.setModified(isModified);
	}

	public boolean isLoaded() {
		return wrapped.isLoaded();
	}

	public void unload() {
		wrapped.unload();
	}

	public EList getErrors() {
		return wrapped.getErrors();
	}

	public EList getWarnings() {
		return wrapped.getWarnings();
	}
	
	public String toString() {
		return "Wrapper(" + wrapped.toString() + ')'; //$NON-NLS-1$
	}

	//
	// Notification forwarding support
	//
	
	/**
	 * An adapter that forwards notifications from the real resource, wrapped
	 * to look like the unmodifiable resource view is the notifier.
	 *
	 * @author Christian W. Damus (cdamus)
	 */
	private class NotificationForwarder extends AdapterImpl {
		public void notifyChanged(Notification msg) {
			if (AbstractResourceWrapper.this.eNotificationRequired()) {
				// don't waste time in creating the wrapper if we don't need to
				eNotify(new NotificationWrapper(msg));
			}
		}
		
		public boolean isAdapterForType(Object type) {
			return type == getClass();
		}
	}
	
	/**
	 * A wrapper around notifications from the real resource, which pretend that
	 * the unmodifiable resource view is the notifier.
	 *
	 * @author Christian W. Damus (cdamus)
	 */
	private class NotificationWrapper implements Notification {
		private final Notification delegate;
		
		/**
		 * Initializes me with the notification that I wrap.
		 * 
		 * @param wrapped the wrapped notification
		 */
		NotificationWrapper(Notification wrapped) {
			delegate = wrapped;
		}

		/**
		 * I pretend that the unmodifiable resource view is the notifier.
		 * 
		 * @return the unmodifiable resource view
		 */
		public Object getNotifier() {
			return AbstractResourceWrapper.this;
		}

		public int getEventType() {
			return delegate.getEventType();
		}

		public Object getFeature() {
			return delegate.getFeature();
		}

		public int getFeatureID(Class expectedClass) {
			return delegate.getFeatureID(expectedClass);
		}

		public boolean getNewBooleanValue() {
			return delegate.getNewBooleanValue();
		}

		public byte getNewByteValue() {
			return delegate.getNewByteValue();
		}

		public char getNewCharValue() {
			return delegate.getNewCharValue();
		}

		public double getNewDoubleValue() {
			return delegate.getNewDoubleValue();
		}

		public float getNewFloatValue() {
			return delegate.getNewFloatValue();
		}

		public int getNewIntValue() {
			return delegate.getNewIntValue();
		}

		public long getNewLongValue() {
			return delegate.getNewLongValue();
		}

		public short getNewShortValue() {
			return delegate.getNewShortValue();
		}

		public String getNewStringValue() {
			return delegate.getNewStringValue();
		}

		public Object getNewValue() {
			return delegate.getNewValue();
		}

		public boolean getOldBooleanValue() {
			return delegate.getOldBooleanValue();
		}

		public byte getOldByteValue() {
			return delegate.getOldByteValue();
		}

		public char getOldCharValue() {
			return delegate.getOldCharValue();
		}

		public double getOldDoubleValue() {
			return delegate.getOldDoubleValue();
		}

		public float getOldFloatValue() {
			return delegate.getOldFloatValue();
		}

		public int getOldIntValue() {
			return delegate.getOldIntValue();
		}

		public long getOldLongValue() {
			return delegate.getOldLongValue();
		}

		public short getOldShortValue() {
			return delegate.getOldShortValue();
		}

		public String getOldStringValue() {
			return delegate.getOldStringValue();
		}

		public Object getOldValue() {
			return delegate.getOldValue();
		}

		public int getPosition() {
			return delegate.getPosition();
		}

		public boolean isReset() {
			return delegate.isReset();
		}

		public boolean isTouch() {
			return delegate.isTouch();
		}

		public boolean merge(Notification notification) {
			return delegate.merge(notification);
		}

		public boolean wasSet() {
			return delegate.wasSet();
		}
	}

	//
	// Canonical mapping support
	//
	
	/**
	 * Implementation of a canonical (1-to-1) mapping of resources to the
	 * wrapper that wraps them.  This is a doubly weak map to help garbage
	 * collection, because these maps are intended to be stored in static
	 * fields and the wrapped resources (keys in the map) will reference their
	 * wrappers (values in the map) via the adapters attached by the wrappers.
	 *
	 * @author Christian W. Damus (cdamus)
	 */
	protected abstract static class CanonicalMap extends java.util.WeakHashMap {
		/**
		 * Gets the canonical wrapper for the specified resource.  If necessary,
		 * it invokes the {@link #createWrapper(Resource)} method to create a
		 * new wrapper of the correct type.
		 * 
		 * @param resource a resource to get the wrapper for
		 * @return the canonical wrapper
		 * 
		 * @see #createWrapper(Resource)
		 */
		public AbstractResourceWrapper get(Resource resource) {
			AbstractResourceWrapper result = null;
			Reference ref = (Reference) get((Object) resource);
			
			if (ref != null) {
				result = (AbstractResourceWrapper) ref.get();
			}
			
			if (result == null) {
				// need to map a new reference
				result = createWrapper(resource);
				ref = new WeakReference(result);
				put(resource, ref);
			}
			
			return result;
		}

		/**
		 * Implemented by subclasses to instantiate a wrapper of the correct
		 * wrapper type.
		 * 
		 * @param resource a resource to create a new wrapper for
		 * @return the new wrapper
		 */
		protected abstract AbstractResourceWrapper createWrapper(Resource resource);
	}
}
