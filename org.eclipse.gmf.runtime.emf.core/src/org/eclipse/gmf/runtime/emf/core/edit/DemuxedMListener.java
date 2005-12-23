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


package org.eclipse.gmf.runtime.emf.core.edit;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.gmf.runtime.emf.core.resources.ILogicalResource;



/**
 * Default implementation of the {@link IDemuxedMListener} interface, which is
 * convenient for subclassing to define only the essential event handlers.
 * Also, this class takes care of constructing a {@link DemuxingMListener}
 * to funnel events from the editing domain.
 * <p>
 * This class is designed to be extended by clients.
 * </p>
 * 
 * @author Christian W. Damus (cdamus)
 * 
 * @see DemuxingMListener
 */
public class DemuxedMListener
	implements IDemuxedMListener, IDemuxedMListener2 {

	private final DemuxingMListener listener;
	
	/**
	 * Initializes me for the default editing domain.
	 */
	public DemuxedMListener() {
		listener = new DemuxingMListener(this);
	}
	
	/**
	 * Initializes me for the specified editing domain.
	 * 
	 * @param domain an editing domain to listen to
	 */
	public DemuxedMListener(MEditingDomain domain) {
		listener = new DemuxingMListener(this, domain);
	}

	/**
	 * Starts or resumes listening until the next call to the {@link #stopListening()}
	 * method.
	 * 
	 * @see #stopListening()
	 */
	public void startListening() {
		listener.startListening();
	}
	
	/**
	 * Suspends listening until the next call to the {@link #startListening()}
	 * method.
	 * 
	 * @see #startListening()
	 */
	public void stopListening() {
		listener.stopListening();
	}
	
	/**
	 * By default, returns a filter that listens to all events.
	 * 
	 * @return the {@link MFilter#WILDCARD_FILTER}
	 */
	public MFilter getFilter() {
		return MFilter.WILDCARD_FILTER;
	}

	/**
	 * Does nothing.
	 */
	public void handleResourceUnloadedEvent(Notification notification,
			Resource resource, EObject modelRoot) {
		// nothing to do
	}

	/**
	 * Does nothing.
	 */
	public void handleResourceLoadedEvent(Notification notification,
			Resource resource) {
		// nothing to do
	}

	/**
	 * Does nothing.
	 */
	public void handleResourceDirtiedEvent(Notification notification,
			Resource resource) {
		// nothing to do
	}

	/**
	 * Does nothing.
	 */
	public void handleResourceSavedEvent(Notification notification,
			Resource resource) {
		// nothing to do
	}

	/**
	 * Does nothing.
	 */
	public void handleResourceImportedEvent(Notification notification,
			Resource resource) {
		// nothing to do
	}

	/**
	 * Does nothing.
	 */
	public void handleResourceExportedEvent(Notification notification,
			Resource resource) {
		// nothing to do
	}

	/**
	 * Does nothing.
	 */
	public void handleElementCreatedEvent(Notification notification,
			EObject owner, EObject newElement) {
		// nothing to do
	}

	/**
	 * Does nothing.
	 */
	public void handleElementDeletedEvent(Notification notification,
			EObject owner, EObject oldElement) {
		// nothing to do
	}

	/**
	 * Does nothing.
	 */
	public void handleElementModifiedEvent(Notification notification,
			EObject element) {
		// nothing to do
	}

	/**
	 * Does nothing.
	 */
	public void handleUndoIntervalClosedEvent(Notification notification,
			MUndoInterval undoInterval) {
		// nothing to do
	}

	/**
	 * Does nothing.
	 */
	public void handleUndoIntervalsFlushedEvent(Notification notification,
			MUndoInterval undoInterval) {
		// nothing to do
	}

	/**
	 * Does nothing.
	 * 
	 * @deprecated Use the cross-resource containment support provided by EMF,
	 *     instead, by defining containment features that are capable of storing
	 *     proxies.
	 */
	public void handleElementSeparatedEvent(Notification notification,
			ILogicalResource resource, EObject eObject, Resource newResource) {
		// nothing to do
	}
	
	/**
	 * Does nothing.
	 * 
	 * @deprecated Use the cross-resource containment support provided by EMF,
	 *     instead, by defining containment features that are capable of storing
	 *     proxies.
	 */
	public void handleElementAbsorbedEvent(Notification notification,
			ILogicalResource resource, EObject eObject, Resource oldResource) {
		// nothing to do
	}
	
	/**
	 * Does nothing.
	 * 
	 * @deprecated Use the cross-resource containment support provided by EMF,
	 *     instead, by defining containment features that are capable of storing
	 *     proxies.
	 */
	public void handleElementLoadedEvent(Notification notification,
			ILogicalResource resource, EObject eObject) {
		// nothing to do
	}

	/*
	 * @see org.eclipse.gmf.runtime.emf.core.edit.IDemuxedMListener2#handleRootAddedEvent(org.eclipse.emf.common.notify.Notification, org.eclipse.emf.ecore.resource.Resource, org.eclipse.emf.ecore.EObject)
	 */
	public void handleRootAddedEvent(Notification notification, Resource resource, EObject eObject) {
		// nothing to do
	}

	/*
	 * @see org.eclipse.gmf.runtime.emf.core.edit.IDemuxedMListener2#handleRootRemovedEvent(org.eclipse.emf.common.notify.Notification, org.eclipse.emf.ecore.resource.Resource, org.eclipse.emf.ecore.EObject)
	 */
	public void handleRootRemovedEvent(Notification notification, Resource resource, EObject eObject) {
		// nothing to do
	}
}
